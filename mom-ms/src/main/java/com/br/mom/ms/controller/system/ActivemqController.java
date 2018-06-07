package com.br.mom.ms.controller.system;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.mom.ms.model.Activemq;
import com.br.mom.ms.service.ActivemqService;
import com.br.mom.ms.util.DateUtil;
import com.br.mom.ms.util.DisconfPush;
import com.br.mom.ms.util.JsonProcessUtil;
import com.br.mom.ms.util.PropertiesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author xin.cao@100credit.com
 */
@Controller
@RequestMapping("/system")
public class ActivemqController {
	@Autowired
	private ActivemqService activemqService;

	/**
	 * 消费者配置
	 *
	 * @param httpServletRequest
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/activemq")
	public String activemqAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
		// DatabaseServiceClient databaseService =
		// DatabaseServiceClient.getInstance();
		String action = httpServletRequest.getParameter("action");
		model.addAttribute("DateUtil", DateUtil.getInstance());
		if (action == null || "".equals(action)) {
			Activemq activemq = new Activemq();
			// List<Activemq> activemqs =
			// databaseService.selectAllActivemqSelective(activemq);
			List<Activemq> activemqs = activemqService.selectAllSelective(activemq);
			model.addAttribute("activemqs", activemqs);
			return "system/activemq";
		} else if ("save".equals(action)) {
			String[] ids = httpServletRequest.getParameterValues("ids[]");
			if (ids != null) {
				for (String id : ids) {
					Activemq activemq = new Activemq();
					String serverList = httpServletRequest.getParameter(id + ":serverList");
					String username = httpServletRequest.getParameter(id + ":username");
					String password = httpServletRequest.getParameter(id + ":password");
					activemq.setId(Integer.valueOf(id));
					if (serverList == null || serverList.isEmpty()) {
						continue;
					}
					if (username == null || username.isEmpty()) {
						continue;
					}
					if (password == null || password.isEmpty()) {
						continue;
					}
					activemq.setServerList(serverList);
					activemq.setUsername(username);
					activemq.setPassword(password);
					// databaseService.updateActivemqByPrimaryKeySelective(activemq);
					activemqService.updateByPrimaryKeySelective(activemq);
				}
			}
			String[] newServerList = httpServletRequest.getParameterValues("new:serverList[]");
			String[] newUsername = httpServletRequest.getParameterValues("new:username[]");
			String[] newPassword = httpServletRequest.getParameterValues("new:password[]");
			if (newServerList != null) {
				int len = newServerList.length;
				for (int i = 0; i < len; i++) {
					Activemq activemq = new Activemq();
					if (newServerList[i] == null || newServerList[i].isEmpty()) {
						continue;
					}
					if (newUsername[i] == null || newUsername[i].isEmpty()) {
						continue;
					}
					if (newPassword[i] == null || newPassword[i].isEmpty()) {
						continue;
					}
					activemq.setServerList(newServerList[i]);
					activemq.setUsername(newUsername[i]);
					activemq.setPassword(newPassword[i]);
					// databaseService.insertActivemqSelective(activemq);
					activemqService.insertSelective(activemq);
				}
			}
		} else if ("delete".equals(action)) {
			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
			if (selectedIds != null) {
				for (String selectedId : selectedIds) {
					// databaseService.deleteActivemqByPrimaryKey(Integer.valueOf(selectedId));
					activemqService.deleteByPrimaryKey(Integer.valueOf(selectedId));
				}
			}
		}
		// 针对save和delete的推送
		ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
		List<Activemq> amqList = activemqService.selectAllSelective(null);
		ArrayNode arrayNode = mapper.createArrayNode();
		for (int i = 0; i < amqList.size(); i++) {
			arrayNode.add(mapper.convertValue(amqList.get(i), ObjectNode.class));
		}
		DisconfPush.push(arrayNode, PropertiesUtil.getStringValue("amqConfigId"));
		
		return "redirect:/system/activemq";
	}
}
