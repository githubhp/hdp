package com.br.mom.ms.controller.configure;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

import net.sf.json.JSONObject;

/**
 * @author xin.cao@100credit.com
 */
@Controller
@RequestMapping("/configure")
public class ConsumerController {
	@Autowired
	private QueueService queueService;
	@Autowired
	private ConsumerService consumerService;
	@Autowired
	private ConsumerStatusService consumerStatusService;

	/**
	 * 消费者配置
	 *
	 * @param httpServletRequest
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/consumer")
	public String consumerAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
		String action = httpServletRequest.getParameter("action");
		String pageNow = httpServletRequest.getParameter("pageNumber");
		String pageSize = httpServletRequest.getParameter("customPageSize");
		model.addAttribute("DateUtil", DateUtil.getInstance());

		if (action == null || "".equals(action)) {
			// 分页查询
			Page<Consumer> page = new Page<Consumer>();
			if (pageNow != null) {
				page.setPageNow(Integer.valueOf(pageNow));
			}
			if (pageSize != null) {
				page.setPageSize(Integer.valueOf(pageSize));
			}
			// Consumer consumer = new Consumer();
			// List<Consumer> consumers =
			// consumerService.selectAllSelective(consumer);
			List<Consumer> consumers = consumerService.selectByPage(page.getStartIndex(), page.getPageSize());
			int totalRecords = consumerService.selectTotalRecords();
			page.setTotalRecords(totalRecords);
			page.setList(consumers);
			model.addAttribute("page", page);

			Queue queue = new Queue();
			List<Queue> queues = queueService.selectAllSelective(queue);
			// model.addAttribute("consumers", consumers);
			model.addAttribute("queues", queues);
			model.addAttribute("momTypes", MOMType.values());
			model.addAttribute("persistentTypes", PersistentType.values());
			return "configure/consumer";
		} else if ("save".equals(action)) {
			String[] ids = httpServletRequest.getParameterValues("ids[]");
			if (ids != null) {
				for (String id : ids) {
					Consumer consumer = new Consumer();
					String name = httpServletRequest.getParameter(id + ":name");
					String queueId = httpServletRequest.getParameter(id + ":queueId");
					String momType = httpServletRequest.getParameter(id + ":momType");
					String persistentType = httpServletRequest.getParameter(id + ":persistentType");
					consumer.setId(Integer.valueOf(id));
					if (name == null || name.isEmpty()) {
						continue;
					}
					consumer.setName(name);
					consumer.setQueueId(Integer.valueOf(queueId));
					consumer.setMomType(Integer.valueOf(momType));
					consumer.setPersistentType(Integer.valueOf(persistentType));
					consumerService.updateByPrimaryKeySelective(consumer);
				}
			}
			String[] newName = httpServletRequest.getParameterValues("new:name[]");
			String[] newQueueId = httpServletRequest.getParameterValues("new:queueId[]");
			String[] newMOMType = httpServletRequest.getParameterValues("new:momType[]");
			String[] newPersistentType = httpServletRequest.getParameterValues("new:persistentType[]");
			if (newName != null) {
				int len = newName.length;
				for (int i = 0; i < len; i++) {
					Consumer consumer = new Consumer();
					if (newName[i] == null || newName[i].isEmpty()) {
						continue;
					}
					consumer.setName(newName[i]);
					consumer.setQueueId(Integer.valueOf(newQueueId[i]));
					consumer.setMomType(Integer.valueOf(newMOMType[i]));
					consumer.setPersistentType(Integer.valueOf(newPersistentType[i]));
					consumer.setParams("{}");
					int id = consumerService.insertSelective(consumer);
					ConsumerStatus consumerStatus = new ConsumerStatus();
					consumerStatus.setConsumerId(id);
					consumerStatusService.insertSelective(consumerStatus);
				}
			}
		} else if ("singlesave".equals(action)) {
			String[] ids = httpServletRequest.getParameterValues("ids[]");
			if (ids != null) {
				for (String id : ids) {
					Consumer consumer = new Consumer();
					String name = httpServletRequest.getParameter(id + ":name");
					String queueId = httpServletRequest.getParameter(id + ":queueId");
					String momType = httpServletRequest.getParameter(id + ":momType");
					String persistentType = httpServletRequest.getParameter(id + ":persistentType");
					consumer.setId(Integer.valueOf(id));
					if (name == null || name.isEmpty()) {
						continue;
					}
					consumer.setName(name);
					consumer.setQueueId(Integer.valueOf(queueId));
					consumer.setMomType(Integer.valueOf(momType));
					consumer.setPersistentType(Integer.valueOf(persistentType));
					consumerService.updateByPrimaryKeySelective(consumer);
				}
			}
		} else if ("singledelete".equals(action)) {
			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
			if (selectedIds != null) {
				for (String selectedId : selectedIds) {
					Consumer consumer = consumerService.selectByPrimaryKey(Integer.valueOf(selectedId));
					consumer.setFlag(1);
					consumerService.logicalDelete(consumer);
				}
			}
		} else if ("delete".equals(action)) {
			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
			if (selectedIds != null) {
				for (String selectedId : selectedIds) {
					consumerService.deleteByPrimaryKey(Integer.valueOf(selectedId));
				}
			}
		}
		return "redirect:/configure/consumer";
	}

	@RequestMapping(value = "/consumer/params")
	public @ResponseBody String paramsAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
		String consumerId = httpServletRequest.getParameter("consumerId");
		String params = httpServletRequest.getParameter("params");
		System.out.println(String.format("consumerId = %s, params = %s", consumerId, params));

		if (consumerId == null || consumerId.isEmpty()) {
			return "";
		}
		if (params == null || params.isEmpty()) {
			Consumer consumer = new Consumer();
			consumer.setId(Integer.valueOf(consumerId));
			List<Consumer> consumers = consumerService.selectAllSelective(consumer);
			if (consumers == null || consumers.isEmpty()) {
				return "";
			}
			return JSONObject.fromObject(consumers.get(0).getParams()).toString(8);
		} else {
			Consumer consumer = new Consumer();
			consumer.setId(Integer.valueOf(consumerId));
			List<Consumer> consumers = consumerService.selectAllSelective(consumer);
			if (consumers == null || consumers.isEmpty()) {
				return "";
			} else {
				consumer = consumers.get(0);
			}
			consumer.setParams(JSONObject.fromObject(params).toString());
			consumerService.updateByPrimaryKeySelective(consumer);
			return JSONObject.fromObject(consumer.getParams()).toString(8);
		}
	}
}
