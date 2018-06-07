package com.br.mom.ms.controller.monitor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.br.mom.ms.common.enums.PersistentType;
import com.br.mom.ms.model.Consumer;
import com.br.mom.ms.model.ConsumerOffset;
import com.br.mom.ms.model.ConsumerStatus;
import com.br.mom.ms.model.Queue;
import com.br.mom.ms.service.ConsumerOffsetService;
import com.br.mom.ms.service.ConsumerService;
import com.br.mom.ms.service.ConsumerStatusService;
import com.br.mom.ms.service.QueueService;
import com.br.mom.ms.util.DateUtil;

@Controller
@RequestMapping("/monitor")
public class ConsumerOffsetController {
	private final static Logger logger = LoggerFactory.getLogger(ConsumerOffsetController.class);
	@Autowired
	private ConsumerOffsetService consumerOffsetService;
	@Autowired
	private ConsumerStatusService consumerStatusService;
	@Autowired
	private ConsumerService consumerService;
	@Autowired
	private QueueService queueService;
	private String consumerId;
	private String startTime;
	private String stopTime;

	@RequestMapping(value = "/consumer_offset")
	public String producerStatusAction(HttpServletRequest httpServletRequest, HttpServletResponse response,
			Model model) {
		String action = httpServletRequest.getParameter("action");
		List<Queue> queues = queueService.selectAllSelective(null);
		Map<Integer, Queue> idAndQueue = new HashMap<Integer, Queue>();
		for (Queue q : queues) {
			idAndQueue.put(q.getId(), q);
		}
		model.addAttribute("idAndQueue", idAndQueue);
		List<Consumer> consumers = consumerService.selectAllSelective(null);
		//删除中间件是activemq的消费者
		Iterator<Consumer> iterator = consumers.iterator();
		while(iterator.hasNext()){
			if(iterator.next().getMomType()==1){
				iterator.remove();
			}
		}
		Map<Integer, Consumer> idAndConsumer = new HashMap<Integer, Consumer>();
		Map<Integer, String> idAndGroup = new HashMap<Integer, String>();
		for (Consumer p : consumers) {
			Queue queue = idAndQueue.get(p.getQueueId());
			if(queue!=null) {
				idAndConsumer.put(p.getId(), p);
				String group = queue.getName() + "_"
						+ PersistentType.fromInt(p.getPersistentType()).getName();
				idAndGroup.put(p.getId(), group);
			}
		}
		model.addAttribute("consumers", consumers);
		model.addAttribute("idAndConsumer", idAndConsumer);
		model.addAttribute("idAndGroup", idAndGroup);
		model.addAttribute("DateUtil", DateUtil.getInstance());
		model.addAttribute("PersistentType", PersistentType.CUSTOME);
		// 查询部分
		if ("select".equals(action)) {
			// 从前台获取Consumer_id参数,改参数为要查询的消费组的consumer_id
			consumerId = httpServletRequest.getParameter("consumer_id");
			startTime = httpServletRequest.getParameter("start_time");
			stopTime=httpServletRequest.getParameter("stop_time");
			if (consumerId != null) {
				Integer consumer_id = Integer.valueOf(consumerId);
				model.addAttribute("consumerId", consumerId);
				// 根据consumer_id获取consumerOffset表中该消费端的记录
				ConsumerOffset consumerOffset = new ConsumerOffset();
				consumerOffset.setConsumerId(consumer_id);
				consumerOffset.setCreateTime(startTime);
				consumerOffset.setUpdateTime(stopTime);
				List<ConsumerOffset> consumerOffsets = consumerOffsetService.selectByConsumerIdAndTime(consumerOffset);
				model.addAttribute("consumerOffsets", consumerOffsets);
			}
		}

		// 补录部分
		if ("add".equals(action)) {
			// 从前台获取consumer_group参数,该参数为要补录数据消费组的consumer_id
			String consumerGroup = httpServletRequest.getParameter("consumer_group");
			model.addAttribute("consumerGroup", consumerGroup);

			// 获取目前数据库中已经保存的该consumer_id消费端的consumerOffset数据,将已经统计的天数放入list中
			Integer consumerId = Integer.valueOf(consumerGroup);
			List<ConsumerOffset> selectConsumerOffsets = consumerOffsetService.selectByConsumerId(consumerId);
			List<String> consumerOffsetCreateTimeList = new ArrayList<String>();
			for (ConsumerOffset consumerOffset : selectConsumerOffsets) {
				consumerOffsetCreateTimeList.add(consumerOffset.getCreateTime());
			}
			// 根据获取的consumer的id,查询该消费端在consumerStatus中所有的每天出队数数据
			if (consumerGroup != null) {
				List<ConsumerStatus> selectConsumerStatuss = consumerStatusService.selectByConsumerId(consumerId);
				for (ConsumerStatus consumerStatus : selectConsumerStatuss) {
					// 如果已经统计的天数中不包含某条数据,则添加该条数据到consuemrOffset库中
					if (!consumerOffsetCreateTimeList.contains(consumerStatus.getUpdateTime())) {
						// 缺少哪天的数据就补录哪天的数据,最晚为昨天的数据,不会统计今天的数据
						ConsumerStatus cs = new ConsumerStatus();
						// 根据consumerId和时间,获取要补录哪天数据的最后一条记录和该天之前一天的最后一条记录
						cs.setConsumerId(consumerStatus.getConsumerId());

						String updateTime = consumerStatus.getUpdateTime();
						if (updateTime == null) {
							continue;
						}
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date = null;
						try {
							date = format.parse(updateTime);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						Calendar calendar = Calendar.getInstance();

						String formatForDate_1 = DateUtil.formatForDate(updateTime);
						String formatForDate_2 = DateUtil.formatForDate(format.format(calendar.getTime()));
						// 如果是今天的数据则跳过不补,只补今天以前的数据
						if (formatForDate_2.equals(formatForDate_1)) {
							continue;
						}

						calendar.setTime(date);
						// 设置要补录数据哪天的时间,获取该天0点到最后一条数据入库时间的最后一条记录
						// createTime为该天的最后一条记录时间,updateTime为该天0点
						cs.setCreateTime(format.format(calendar.getTime()));

						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND, 0);
						cs.setUpdateTime(format.format(calendar.getTime()));
						// 根据consumerId和补录当天的时间段获取该天的最后一条记录
						ConsumerStatus consumerStatus_1 = consumerStatusService.selectByConsumerIdAndTime(cs);
						// 设置补录当天的前一天的updateTime
						calendar.add(Calendar.DAY_OF_MONTH, -1);
						cs.setUpdateTime(format.format(calendar.getTime()));
						// 设置补录当天的前一天的createTime
						calendar.set(Calendar.HOUR_OF_DAY, 23);
						calendar.set(Calendar.MINUTE, 59);
						calendar.set(Calendar.SECOND, 59);
						cs.setCreateTime(format.format(calendar.getTime()));
						// 根据consumerId和补录当天前一天的最后一条记录
						ConsumerStatus consumerStatus_2 = consumerStatusService.selectByConsumerIdAndTime(cs);
						// 根据补录当天和补录当天前一天的两条记录,设置要补录的consumerOffset
						ConsumerOffset consumerOffset = new ConsumerOffset();
						// 设置消费消息数
						try {
							consumerOffset.setOffset(consumerStatus_1.getDequeue() - consumerStatus_2.getDequeue());
						} catch (NullPointerException e) {
							continue;
						}
						consumerOffset.setConsumerId(consumerStatus.getConsumerId());
						// 设置consumerOffset的createTime,该时间为consumerStatus中的updateTime
						consumerOffset.setCreateTime(consumerStatus.getUpdateTime());
						// 设置consumerOffset的updateTime,该时间为补录数据的时间
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date newtime = new Date();
						consumerOffset.setUpdateTime(String.format(dateFormat.format(newtime)));
						// 将设置好的consumerOffset数据插入数据库中
						consumerOffsetService.insert(consumerOffset);
						logger.info("补录消费组：" + consumerId + "_" + idAndGroup.get(consumerId) + "数据完成~");
					}
				}
			}
		}
		return "monitor/consumer_offset";
	}

	@RequestMapping(value = "/consumer_offset/data")
	@ResponseBody
	public String producerStatusDataAction(HttpServletRequest httpServletRequest, HttpServletResponse response,
			Model model) {
		// 从前台获取Consumer_id参数,改参数为要查询的消费组的consumer_id
		JSONArray jsonArray = new JSONArray();
		if (consumerId != null) {
			Integer consumer_id = Integer.valueOf(consumerId);
			// 根据consumer_id获取consumerOffset表中该消费端的记录
			ConsumerOffset consumerOffset = new ConsumerOffset();
			consumerOffset.setConsumerId(consumer_id);
			consumerOffset.setCreateTime(startTime);
			consumerOffset.setUpdateTime(stopTime);
			List<ConsumerOffset> consumerOffsets = consumerOffsetService.selectByConsumerIdAndTime(consumerOffset);
			for (ConsumerOffset co : consumerOffsets) {
				JSONObject object = new JSONObject();
				object.put("Date", DateUtil.formatForDate(co.getCreateTime()));
				object.put("offset", co.getOffset());
				jsonArray.add(object);
			}
			consumerId = null;
			startTime=null;
			stopTime=null;
		}
		return jsonArray.toJSONString();
	}

	@RequestMapping(value = "/consumer_offset_min")
	public String ConsumerOffsetMinAction(HttpServletRequest httpServletRequest, HttpServletResponse response,
			Model model) {
		String action = httpServletRequest.getParameter("action");
		List<Queue> queues = queueService.selectAllSelective(null);
		Map<Integer, Queue> idAndQueue = new HashMap<Integer, Queue>();
		for (Queue q : queues) {
			idAndQueue.put(q.getId(), q);
		}
		model.addAttribute("idAndQueue", idAndQueue);
		List<Consumer> consumers = consumerService.selectAllSelective(null);
		Iterator<Consumer> iterator = consumers.iterator();
		while(iterator.hasNext()){
			if(iterator.next().getMomType()==1){
				iterator.remove();
			}
		}
		Map<Integer, Consumer> idAndConsumer = new HashMap<Integer, Consumer>();
		Map<Integer, String> idAndGroup = new HashMap<Integer, String>();
		for (Consumer p : consumers) {
			Queue queue = idAndQueue.get(p.getQueueId());
			if(queue!=null) {
				idAndConsumer.put(p.getId(), p);
				String group = queue.getName()+ "_"
						+ PersistentType.fromInt(p.getPersistentType()).getName();
				idAndGroup.put(p.getId(), group);
			}
		}
		model.addAttribute("consumers", consumers);
		model.addAttribute("idAndConsumer", idAndConsumer);
		model.addAttribute("idAndGroup", idAndGroup);
		model.addAttribute("DateUtil", DateUtil.getInstance());
		model.addAttribute("PersistentType", PersistentType.CUSTOME);
		// 查询部分
		if ("select".equals(action)) {
			// 从前台获取Consumer_id参数,改参数为要查询的消费组的consumer_id
			consumerId = httpServletRequest.getParameter("consumer_id");
			startTime = httpServletRequest.getParameter("start_time");
			stopTime = httpServletRequest.getParameter("stop_time");
			if (consumerId != null) {
				Integer consumer_id = Integer.valueOf(consumerId);
				ConsumerStatus consumerStatus = new ConsumerStatus();
				consumerStatus.setConsumerId(consumer_id);
				consumerStatus.setCreateTime(startTime);
				consumerStatus.setUpdateTime(stopTime);
				model.addAttribute("consumerId", consumerId);
				// 根据consumer_id获取consumerOffset表中该消费端的记录
				List<ConsumerStatus> consumerOffsets = consumerStatusService
						.selectByConsumerIdAndDateTime(consumerStatus);
				model.addAttribute("consumerOffsets", consumerOffsets);
			}
		}
		return "monitor/consumer_offset_min";
	}

	@RequestMapping(value = "/consumer_offset_min/data")
	@ResponseBody
	public String ConsumerOffsetMinDataAction(HttpServletRequest httpServletRequest, HttpServletResponse response,
			Model model) {
		// 从前台获取Consumer_id参数,改参数为要查询的消费组的consumer_id
		JSONArray jsonArray = new JSONArray();
		if (consumerId != null && startTime != null && stopTime != null) {
			Integer consumer_id = Integer.valueOf(consumerId);
			ConsumerStatus consumerStatus = new ConsumerStatus();
			consumerStatus.setConsumerId(consumer_id);
			consumerStatus.setCreateTime(startTime);
			consumerStatus.setUpdateTime(stopTime);
			// 根据consumer_id获取consumerOffset表中该消费端的记录
			List<ConsumerStatus> consumerStatuss = consumerStatusService.selectByConsumerIdAndDateTime(consumerStatus);
			for (ConsumerStatus cs : consumerStatuss) {
				JSONObject object = new JSONObject();
				object.put("Date", DateUtil.formatForDateTime(cs.getUpdateTime()));
				object.put("dequeue", cs.getDequeue());
				jsonArray.add(object);
			}
			consumerId = null;
			startTime = null;
			stopTime = null;
		}
		return jsonArray.toJSONString();
	}
}
