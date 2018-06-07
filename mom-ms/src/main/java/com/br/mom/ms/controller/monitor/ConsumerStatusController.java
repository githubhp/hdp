package com.br.mom.ms.controller.monitor;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.mom.ms.bean.Page;
import com.br.mom.ms.common.enums.MOMType;
import com.br.mom.ms.common.enums.PersistentType;
import com.br.mom.ms.model.Consumer;
import com.br.mom.ms.model.ConsumerStatus;
import com.br.mom.ms.model.Queue;
import com.br.mom.ms.service.ConsumerService;
import com.br.mom.ms.service.ConsumerStatusService;
import com.br.mom.ms.service.QueueService;
import com.br.mom.ms.util.DateUtil;

/**
 * @author xin.cao@100credit.com
 */
@Controller
@RequestMapping("/monitor")
public class ConsumerStatusController {
	@Autowired
	private QueueService queueService;
	@Autowired
	private ConsumerService consumerService;
	@Autowired
	private ConsumerStatusService consumerStatusService;

	/**
	 * 消费者状态配置
	 *
	 * @param httpServletRequest
	 * @param model
	 * @return
	 * @throws java.io.IOException
	 */
	@RequestMapping(value = "/consumer_status")
	public String consumerStatusAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
		// 绑定id和consumer
		List<Consumer> consumers = consumerService.selectAllSelective(null);
		Map<Integer, Consumer> idAndConsumer = new HashMap<Integer, Consumer>();
		for (Consumer p : consumers) {
			idAndConsumer.put(p.getId(), p);
		}
		model.addAttribute("idAndConsumer", idAndConsumer);
		// 绑定id和queue
		List<Queue> queues = queueService.selectAllSelective(null);
		Map<Integer, Queue> idAndQueue = new HashMap<Integer, Queue>();
		for (Queue q : queues) {
			idAndQueue.put(q.getId(), q);
		}
		model.addAttribute("idAndQueue", idAndQueue);

		// String action = httpServletRequest.getParameter("action");
		// 从前台获取当前页和每页展示条数,如果没有传入使用默认值
		String pageNow = httpServletRequest.getParameter("pageNumber");
		String pageSize = httpServletRequest.getParameter("customPageSize");
		// 分页查询
		Page<ConsumerStatus> page = new Page<ConsumerStatus>();
		if (pageNow != null) {
			page.setPageNow(Integer.valueOf(pageNow));
		}
		if (pageSize != null) {
			page.setPageSize(Integer.valueOf(pageSize));
		}
		List<ConsumerStatus> consumerStatuss = consumerStatusService.selectByPage(page.getStartIndex(),
				page.getPageSize());
		//过滤consumer已经删除的记录
		Iterator<ConsumerStatus> iterator = consumerStatuss.iterator();
		while (iterator.hasNext()) {
			try{
				idAndConsumer.get(iterator.next().getConsumerId()).getName();
			} catch (ConcurrentModificationException | NullPointerException e) {
				iterator.remove();
			}
		}
		//过滤consumer的中间件为activemq的记录,如果中间件类型除了activemq外还有其他类型则保留
		Iterator<ConsumerStatus> iterator_2 = consumerStatuss.iterator();
		while(iterator_2.hasNext()){
			if((idAndConsumer.get(iterator_2.next().getConsumerId()).getMomType())==1){
				iterator_2.remove();
			}
		}
		page.setList(consumerStatuss);
		// 设置记录总条数
		int totalRecords = consumerStatusService.selectTotalRecords();
		page.setTotalRecords(totalRecords);

		model.addAttribute("page", page);
		// model.addAttribute("consumerStatuss", consumerStatuss);
		model.addAttribute("MOMType", MOMType.KAFKA);
		model.addAttribute("PersistentType", PersistentType.CUSTOME);
		model.addAttribute("DateUtil", DateUtil.getInstance());
		return "monitor/consumer_status";
	}
}