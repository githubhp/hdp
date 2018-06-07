package com.br.mom.ms.controller.system;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.mom.ms.model.ConsumerBroker;
import com.br.mom.ms.service.ConsumerBrokerService;
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
public class ConsumerBrokerController {
	@Autowired
	private ConsumerBrokerService consumerBrokerService;

	/**
	 * consumerBroker配置
	 *
	 * @param httpServletRequest
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/consumer_broker")
	public String consumerBrokerAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
		String action = httpServletRequest.getParameter("action");
		model.addAttribute("DateUtil", DateUtil.getInstance());
		if (action == null || "".equals(action)) {
			List<ConsumerBroker> consumerBrokers = consumerBrokerService.selectAllSelective(null);
			model.addAttribute("consumerBrokers", consumerBrokers);
			return "system/consumer_broker";
		} else if ("save".equals(action)) {
			String[] ids = httpServletRequest.getParameterValues("ids[]");
			if (ids != null) {
				for (String id : ids) {
					ConsumerBroker consumerBroker = new ConsumerBroker();
					String ip = httpServletRequest.getParameter(id + ":ip");
					String port = httpServletRequest.getParameter(id + ":port");
					consumerBroker.setId(Integer.valueOf(id));
					if (ip == null || ip.isEmpty()) {
						continue;
					}
					if (port == null || port.isEmpty()) {
						continue;
					}
					consumerBroker.setIp(ip);
					consumerBroker.setPort(port);
					consumerBrokerService.updateByPrimaryKeySelective(consumerBroker);
				}
			}
			String[] newIp = httpServletRequest.getParameterValues("new:ip[]");
			String[] newPort = httpServletRequest.getParameterValues("new:port[]");
			if (newIp != null) {
				int len = newIp.length;
				for (int i = 0; i < len; i++) {
					ConsumerBroker consumerBroker = new ConsumerBroker();
					if (newIp[i] == null || newIp[i].isEmpty()) {
						continue;
					}
					if (newPort[i] == null || newPort[i].isEmpty()) {
						continue;
					}
					// 设置更新时间和创建时间
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date newtime = new Date();
					String time = dateFormat.format(newtime);
					consumerBroker.setCreateTime(time);
					consumerBroker.setIp(newIp[i]);
					consumerBroker.setPort(newPort[i]);
					consumerBroker.setStatus(1);
					consumerBrokerService.insertSelective(consumerBroker);
				}
			}
		} else if ("delete".equals(action)) {
			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
			if (selectedIds != null) {
				for (String selectedId : selectedIds) {
					consumerBrokerService.deleteByPrimaryKey(Integer.valueOf(selectedId));
				}
			}
		}
		// 针对save和delete的推送
		ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
		List<ConsumerBroker> consumerBroker = consumerBrokerService.selectAllSelective(null);
		ArrayNode arrayNode = mapper.createArrayNode();
		for (int i = 0; i < consumerBroker.size(); i++) {
			arrayNode.add(mapper.convertValue(consumerBroker.get(i), ObjectNode.class));
		}
		DisconfPush.push(arrayNode, PropertiesUtil.getStringValue("consumerBrokerConfigId"));
		return "redirect:/system/consumer_broker";
	}
}
/*
 * DatabaseServiceClient databaseService = DatabaseServiceClient.getInstance();
 * String action = httpServletRequest.getParameter(
 * "action");model.addAttribute("DateUtil",DateUtil.getInstance());if(action==
 * null||"".equals(action)) { ConsumerBroker consumerBroker = new
 * ConsumerBroker(); List<ConsumerBroker> consumerBrokers =
 * databaseService.selectAllConsumerBrokerSelective(consumerBroker); for
 * (ConsumerBroker c : consumerBrokers) { NettyTransceiver client; try { client
 * = new NettyTransceiver(new InetSocketAddress(c.getIp(),
 * Integer.valueOf(c.getPort()))); TaskMonitorService monitorService =
 * (TaskMonitorService) SpecificRequestor .getClient(TaskMonitorService.class,
 * client); monitorService.wokerInfos(""); client.close();
 * c.setStatus(ResultStatus.YES.getNo()); } catch (IOException ex) {
 * c.setStatus(ResultStatus.NO.getNo()); logger.info(ex.getMessage()); } }
 * model.addAttribute("consumerBrokers", consumerBrokers); return
 * "/system/consumer_broker"; }else if("save".equals(action)) { String[] ids =
 * httpServletRequest.getParameterValues("ids[]"); if (ids != null) { for
 * (String id : ids) { ConsumerBroker consumerBroker = new ConsumerBroker();
 * String ip = httpServletRequest.getParameter(id + ":ip"); String port =
 * httpServletRequest.getParameter(id + ":port");
 * consumerBroker.setId(Integer.valueOf(id)); if (ip == null || ip.isEmpty()) {
 * continue; } if (port == null || port.isEmpty()) { continue; }
 * consumerBroker.setIp(ip); consumerBroker.setPort(port);
 * databaseService.updateConsumerBrokerByPrimaryKeySelective(consumerBroker); }
 * } String[] newIp = httpServletRequest.getParameterValues("new:ip[]");
 * String[] newPort = httpServletRequest.getParameterValues("new:port[]"); if
 * (newIp != null) { int len = newIp.length; for (int i = 0; i < len; i++) {
 * ConsumerBroker consumerBroker = new ConsumerBroker(); if (newIp[i] == null ||
 * newIp[i].isEmpty()) { continue; } if (newPort[i] == null ||
 * newPort[i].isEmpty()) { continue; } consumerBroker.setIp(newIp[i]);
 * consumerBroker.setPort(newPort[i]); consumerBroker.setStatus(1);
 * databaseService.insertConsumerBrokerSelective(consumerBroker); } } }else
 * if("delete".equals(action)) { String[] selectedIds =
 * httpServletRequest.getParameterValues("selected_ids[]"); if (selectedIds !=
 * null) { for (String selectedId : selectedIds) {
 * databaseService.deleteConsumerBrokerByPrimaryKey(Integer.valueOf(selectedId))
 * ; } } }else if("refresh".equals(action)) { String[] selectedIds =
 * httpServletRequest.getParameterValues("selected_ids[]"); if (selectedIds !=
 * null) { for (String selectedId : selectedIds) { ConsumerBroker c = new
 * ConsumerBroker(); c.setId(Integer.valueOf(selectedId)); c =
 * databaseService.selectConsumerBrokerSelective(c); NettyTransceiver client;
 * try { client = new NettyTransceiver(new InetSocketAddress(c.getIp(),
 * Integer.valueOf(c.getPort()))); TaskMonitorService monitorService =
 * (TaskMonitorService) SpecificRequestor .getClient(TaskMonitorService.class,
 * client); monitorService.refresh(); client.close(); } catch (IOException ex) {
 * logger.info(ex.getMessage()); } } }
 * }return"redirect:/system/consumer_broker"; }}
 */