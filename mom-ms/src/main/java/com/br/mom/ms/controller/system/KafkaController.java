package com.br.mom.ms.controller.system;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.mom.ms.model.Kafka;
import com.br.mom.ms.service.KafkaService;
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
/**
 *
 * @author xin.cao@100credit.com
 */
@Controller
@RequestMapping("/system")
public class KafkaController {
	@Autowired
	private KafkaService kafkaService;

	/**
	 * kafka配置
	 *
	 * @param httpServletRequest
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/kafka")
	public String kafkaAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
		// DatabaseServiceClient databaseService =
		// DatabaseServiceClient.getInstance();
		String action = httpServletRequest.getParameter("action");
		model.addAttribute("DateUtil", DateUtil.getInstance());
		if (action == null || "".equals(action)) {
			Kafka kafka = new Kafka();
			// List<Kafka> kafkas =
			// databaseService.selectAllKafkaSelective(kafka);
			List<Kafka> kafkas = kafkaService.selectAllSelective(kafka);
			model.addAttribute("kafkas", kafkas);
			return "system/kafka";
		} else if ("save".equals(action)) {
			String[] ids = httpServletRequest.getParameterValues("ids[]");
			if (ids != null) {
				for (String id : ids) {
					Kafka kafka = new Kafka();
					String ip = httpServletRequest.getParameter(id + ":ip");
					String port = httpServletRequest.getParameter(id + ":port");
					kafka.setId(Integer.valueOf(id));
					if (ip == null || ip.isEmpty()) {
						continue;
					}
					if (port == null || port.isEmpty()) {
						continue;
					}
					kafka.setIp(ip);
					kafka.setPort(port);
					// databaseService.updateKafkaByPrimaryKeySelective(kafka);
					kafkaService.updateByPrimaryKeySelective(kafka);
				}
			}
			String[] newIp = httpServletRequest.getParameterValues("new:ip[]");
			String[] newPort = httpServletRequest.getParameterValues("new:port[]");
			if (newIp != null) {
				int len = newIp.length;
				for (int i = 0; i < len; i++) {
					Kafka kafka = new Kafka();
					if (newIp[i] == null || newIp[i].isEmpty()) {
						continue;
					}
					if (newPort[i] == null || newPort[i].isEmpty()) {
						continue;
					}
					kafka.setIp(newIp[i]);
					kafka.setPort(newPort[i]);
					// databaseService.insertKafkaSelective(kafka);
					kafkaService.insertSelective(kafka);
				}
			}
		} else if ("delete".equals(action)) {
			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
			if (selectedIds != null) {
				for (String selectedId : selectedIds) {
					// databaseService.deleteKafkaByPrimaryKey(Integer.valueOf(selectedId));
					kafkaService.deleteByPrimaryKey(Integer.valueOf(selectedId));
				}
			}
		}
		// 针对save和delete的推送
		ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
		List<Kafka> kafkaList = kafkaService.selectAllSelective(null);
		ArrayNode arrayNode = mapper.createArrayNode();
		for (int i = 0; i < kafkaList.size(); i++) {
			arrayNode.add(mapper.convertValue(kafkaList.get(i), ObjectNode.class));
		}
		DisconfPush.push(arrayNode, PropertiesUtil.getStringValue("kafkaConfigId"));
		
		return "redirect:/system/kafka";
	}
}
