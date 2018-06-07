package com.br.mom.ms.controller.configure;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.br.mom.ms.bean.Page;
import com.br.mom.ms.common.enums.MOMType;
import com.br.mom.ms.common.enums.ResultStatus;
import com.br.mom.ms.model.Producer;
import com.br.mom.ms.model.ProducerStatus;
import com.br.mom.ms.model.Queue;
import com.br.mom.ms.service.ProducerService;
import com.br.mom.ms.service.ProducerStatusService;
import com.br.mom.ms.service.QueueService;
import com.br.mom.ms.util.DateUtil;
import com.br.mom.ms.util.DisconfPush;
import com.br.mom.ms.util.JsonProcessUtil;
import com.br.mom.ms.util.PropertiesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author xin.cao@100credit.com
 */
@Controller
@RequestMapping("/configure")
public class ProducerController {
	private static Logger logger = LoggerFactory.getLogger(ProducerController.class);
	@Autowired
	private ProducerService producerService;
	@Autowired
	private QueueService queueService;
	@Autowired
	private ProducerStatusService producerStatusService;

	/**
	 * 生产者配置
	 *
	 * @param httpServletRequest
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/producer")
	public String producerAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
		String action = httpServletRequest.getParameter("action");
		String pageNow = httpServletRequest.getParameter("pageNumber");
		String pageSize = httpServletRequest.getParameter("customPageSize");
		model.addAttribute("DateUtil", DateUtil.getInstance());

		if (action == null || "".equals(action)) {
			// Producer producer = new Producer();
			// List<Producer> producers =
			// producerService.selectAllSelective(producer);
			// 分页查询
			Page<Producer> page = new Page<Producer>();
			if (pageNow != null) {
				page.setPageNow(Integer.valueOf(pageNow));
			}
			if (pageSize != null) {
				page.setPageSize(Integer.valueOf(pageSize));
			}
			List<Producer> producers = producerService.selectByPage(page.getStartIndex(), page.getPageSize());
			int totalRecords = producerService.selectTotalRecords();
			page.setTotalRecords(totalRecords);
			page.setList(producers);
			model.addAttribute("page", page);
			Queue queue = new Queue();
			List<Queue> queues = queueService.selectAllSelective(queue);
			// model.addAttribute("producers", producers);
			model.addAttribute("queues", queues);
			model.addAttribute("momTypes", MOMType.values());
			return "configure/producer";
		} else if ("save".equals(action)) {
			String[] ids = httpServletRequest.getParameterValues("ids[]");
			if (ids != null) {
				for (String id : ids) {
					Producer producer = new Producer();
					String name = httpServletRequest.getParameter(id + ":name");
					String queueId = httpServletRequest.getParameter(id + ":queueId");
					String momType = httpServletRequest.getParameter(id + ":momType");
					producer.setId(Integer.valueOf(id));
					producer.setQueueId(Integer.valueOf(queueId));
					producer.setMomType(Integer.valueOf(momType));
					if (name == null || name.isEmpty()) {
						continue;
					}
					producer.setName(name);
					producerService.updateByPrimaryKeySelective(producer);
				}
			}
			String[] newName = httpServletRequest.getParameterValues("new:name[]");
			String[] newQueueId = httpServletRequest.getParameterValues("new:queueId[]");
			String[] newMOMType = httpServletRequest.getParameterValues("new:momType[]");
			if (newName != null) {
				int len = newName.length;
				for (int i = 0; i < len; i++) {
					Producer producer = new Producer();
					if (newName[i] == null || newName[i].isEmpty()) {
						continue;
					}
					producer.setName(newName[i]);
					producer.setQueueId(Integer.valueOf(newQueueId[i]));
					producer.setMomType(Integer.valueOf(newMOMType[i]));
					int id = producerService.insertSelective(producer);
					ProducerStatus producerStatus = new ProducerStatus();
					producerStatus.setProducerId(id);
					producerStatusService.insertSelective(producerStatus);
				}
			}
		} else if ("delete".equals(action)) {
			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
			if (selectedIds != null) {
				for (String selectedId : selectedIds) {
					producerService.deleteByPrimaryKey(Integer.valueOf(selectedId));
				}
			}
		} else if ("singledelete".equals(action)) {
			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
			if (selectedIds != null) {
				for (String selectedId : selectedIds) {
					Producer producer = producerService.selectByPrimaryKey(Integer.valueOf(selectedId));
					producer.setFlag(1);
					producerService.logicalDelete(producer);
				}
			}
		} else if ("singlesave".equals(action)) {
			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
			if (selectedIds != null) {
				for (String id : selectedIds) {
					Producer producer = new Producer();
					String name = httpServletRequest.getParameter(id + ":name");
					String queueId = httpServletRequest.getParameter(id + ":queueId");
					String momType = httpServletRequest.getParameter(id + ":momType");
					producer.setId(Integer.valueOf(id));
					producer.setQueueId(Integer.valueOf(queueId));
					producer.setMomType(Integer.valueOf(momType));
					if (name == null || name.isEmpty()) {
						continue;
					}
					producer.setName(name);
					producerService.updateByPrimaryKeySelective(producer);
				}
			}
		}else if ("push".equals(action)) {
			// 针对save和delete的推送
			ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
			List<Producer> producerList = producerService.selectAllSelective(null);
			ArrayNode arrayNode = mapper.createArrayNode();
			for (int i = 0; i < producerList.size(); i++) {
				arrayNode.add(mapper.convertValue(producerList.get(i), ObjectNode.class));
			}
			DisconfPush.push(arrayNode, PropertiesUtil.getStringValue("ProducerConfigId"));
			logger.info("producer推送配置中心完毕!");
		}
		return "redirect:/configure/producer";
	}

	@RequestMapping(value = "/producer/simulator")
	public @ResponseBody String simulatorAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
		// 模拟rpc心跳功能
		// String name = httpServletRequest.getParameter("name");
		// String destinationName =
		// httpServletRequest.getParameter("destinationName");
		// String content = httpServletRequest.getParameter("content");
		try {
			// BrokerLayerServicePrx brokeLayService=(BrokerLayerServicePrx)
			// BSFConsumerBean.getServiceProxy(BrokerLayerServicePrx.class);
			// brokeLayService.sender(name, destinationName, content);
		} catch (Exception e) {
			return String.valueOf(ResultStatus.NO.getNo());
		}
		return String.valueOf(ResultStatus.YES.getNo());
	}
}
