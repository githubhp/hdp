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

import com.br.mom.ms.bean.Page;
import com.br.mom.ms.common.enums.ContentFormat;
import com.br.mom.ms.common.enums.MOMType;
import com.br.mom.ms.constent.ResultStatus;
import com.br.mom.ms.model.Queue;
import com.br.mom.ms.service.QueueService;
import com.br.mom.ms.util.DateUtil;
import com.br.mom.ms.util.DisconfPush;
import com.br.mom.ms.util.JsonProcessUtil;
import com.br.mom.ms.util.PropertiesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/configure")
public class QueueController {
	private static Logger logger = LoggerFactory.getLogger(QueueController.class);
	@Autowired
	private QueueService queueService;

	/**
	 * 队列配置
	 *
	 * @param httpServletRequest
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/queue")
	public String queueAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
		String action = httpServletRequest.getParameter("action");
		String pageNow = httpServletRequest.getParameter("pageNumber");
		String pageSize = httpServletRequest.getParameter("customPageSize");
		model.addAttribute("DateUtil", DateUtil.getInstance());

		if (action == null || "".equals(action)) {
			// Queue queue = new Queue();
			// List<Queue> queues = queueService.selectAllSelective(queue);
			// 分页查询
			Page<Queue> page = new Page<Queue>();
			if (pageNow != null) {
				page.setPageNow(Integer.valueOf(pageNow));
			}
			if (pageSize != null) {
				page.setPageSize(Integer.valueOf(pageSize));
			}
			List<Queue> queues = queueService.selectByPage(page.getStartIndex(), page.getPageSize());
			int totalRecords = queueService.selectTotalRecords();
			page.setTotalRecords(totalRecords);
			page.setList(queues);
			// model.addAttribute("queues", queues);
			model.addAttribute("page", page);
			model.addAttribute("contentFormats", ContentFormat.values());
			model.addAttribute("momTypes", MOMType.values());
			model.addAttribute("resultStatuss", ResultStatus.values());
			return "configure/queue";
		} else if ("save".equals(action)) {
			String[] ids = httpServletRequest.getParameterValues("ids[]");
			if (ids != null) {
				for (String id : ids) {
					String name = httpServletRequest.getParameter(id + ":name");
					String contentFormat = httpServletRequest.getParameter(id + ":contentFormat");
					String momType = httpServletRequest.getParameter(id + ":momType");
					String status = httpServletRequest.getParameter(id + ":status");
					if (name == null || name.isEmpty()) {
						continue;
					}
					Queue queue = new Queue();
					queue.setName(name);
					List<Queue> queues = queueService.selectAllSelective(queue);
					if (queues != null && !queues.isEmpty()) {
						queue = queues.get(0);
						if (Integer.valueOf(id) != queue.getId()) {
							continue;
						}
					}
					queue = new Queue();
					queue.setId(Integer.valueOf(id));
					queue.setName(name);
					queue.setContentFormat(Integer.valueOf(contentFormat));
					queue.setMomType(Integer.valueOf(momType));
					queue.setStatus(Integer.valueOf(status));
					queueService.updateByPrimaryKeySelective(queue);
				}
			}
			String[] newName = httpServletRequest.getParameterValues("new:name[]");
			String[] newContentFormat = httpServletRequest.getParameterValues("new:contentFormat[]");
			String[] newMOMType = httpServletRequest.getParameterValues("new:momType[]");
			String[] newStatus = httpServletRequest.getParameterValues("new:status[]");
			if (newName != null) {
				int len = newName.length;
				for (int i = 0; i < len; i++) {
					if (newName[i] == null || newName[i].isEmpty()) {
						continue;
					}
					Queue queue = new Queue();
					queue.setName(newName[i]);
					List<Queue> queues = queueService.selectAllSelective(queue);
					if (queues != null && !queues.isEmpty()) {
						continue;
					}
					queue = new Queue();
					queue.setName(newName[i]);
					queue.setContentFormat(Integer.valueOf(newContentFormat[i]));
					queue.setMomType(Integer.valueOf(newMOMType[i]));
					queue.setStatus(Integer.valueOf(newStatus[i]));
					queueService.insertSelective(queue);
				}
			}
		} else if ("delete".equals(action)) {
			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
			if (selectedIds != null) {
				for (String selectedId : selectedIds) {
					queueService.deleteByPrimaryKey(Integer.valueOf(selectedId));
				}
			}
		}else if ("singledelete".equals(action)) {
			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
			if (selectedIds != null) {
				for (String selectedId : selectedIds) {
					Queue queue = queueService.selectByPrimaryKey(Integer.valueOf(selectedId));
					queue.setFlag(1);
					queueService.logicalDelete(queue);
				}
			}
		}else if ("singlesave".equals(action)) {
			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
			if (selectedIds != null) {
				for (String id : selectedIds) {
					String name = httpServletRequest.getParameter(id + ":name");
					String contentFormat = httpServletRequest.getParameter(id + ":contentFormat");
					String momType = httpServletRequest.getParameter(id + ":momType");
					String status = httpServletRequest.getParameter(id + ":status");
					if (name == null || name.isEmpty()) {
						continue;
					}
					Queue queue = new Queue();
					queue.setName(name);
					List<Queue> queues = queueService.selectAllSelective(queue);
					if (queues != null && !queues.isEmpty()) {
						queue = queues.get(0);
						if (Integer.valueOf(id) != queue.getId()) {
							continue;
						}
					}
					queue = new Queue();
					queue.setId(Integer.valueOf(id));
					queue.setName(name);
					queue.setContentFormat(Integer.valueOf(contentFormat));
					queue.setMomType(Integer.valueOf(momType));
					queue.setStatus(Integer.valueOf(status));
					queueService.updateByPrimaryKeySelective(queue);
				}
			}
		}else if ("push".equals(action)){
			// 正对save和delete的推送
			ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
			List<Queue> queueList = queueService.selectAllSelective(null);
			ArrayNode arrayNode = mapper.createArrayNode();
			for (int i = 0; i < queueList.size(); i++) {
				arrayNode.add(mapper.convertValue(queueList.get(i), ObjectNode.class));
			}
			DisconfPush.push(arrayNode, PropertiesUtil.getStringValue("queueConfigId"));
			logger.info("queue推送配置中心完毕!");
		}
		return "redirect:/configure/queue";
	}
}
