package com.br.mom.ms.controller.system;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.mom.ms.model.Zookeeper;
import com.br.mom.ms.service.ZookeeperService;
import com.br.mom.ms.util.DateUtil;
import com.br.mom.ms.util.DisconfPush;
import com.br.mom.ms.util.JsonProcessUtil;
import com.br.mom.ms.util.PropertiesUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author qiang.wang@100credit.com
 */
@Controller
@RequestMapping("/system")
public class ZookpeerController {
	@Autowired
	private ZookeeperService zookeeperService;

	/**
	 * zookpeer配置
	 *
	 * @param httpServletRequest
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/zookpeer")
	public String zookpeerAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
		// DatabaseServiceClient databaseService =
		// DatabaseServiceClient.getInstance();
		String action = httpServletRequest.getParameter("action");
		model.addAttribute("DateUtil", DateUtil.getInstance());
		if (action == null || "".equals(action)) {
			Zookeeper zookeeper = new Zookeeper();
			// List<Zookeeper> zookeepers =
			// databaseService.selectAllZookeeperSelective(zookeeper);
			List<Zookeeper> zookeepers = zookeeperService.selectAllSelective(zookeeper);
			model.addAttribute("zookpeers", zookeepers);
			return "system/zookpeer";
		} else if ("save".equals(action)) {
			String[] ids = httpServletRequest.getParameterValues("ids[]");
			if (ids != null) {
				for (String id : ids) {
					Zookeeper zookeeper = new Zookeeper();
					String ip = httpServletRequest.getParameter(id + ":ip");
					String port = httpServletRequest.getParameter(id + ":port");
					zookeeper.setId(Integer.valueOf(id));
					if (ip == null || ip.isEmpty()) {
						continue;
					}
					if (port == null || port.isEmpty()) {
						continue;
					}
					zookeeper.setIp(ip);
					zookeeper.setPort(port);
					// databaseService.updateZookeeperByPrimaryKeySelective(zookeeper);
					zookeeperService.updateByPrimaryKeySelective(zookeeper);
				}
			}
			String[] newIp = httpServletRequest.getParameterValues("new:ip[]");
			String[] newPort = httpServletRequest.getParameterValues("new:port[]");
			String[] newJq = httpServletRequest.getParameterValues("new:jq[]");
			if (newIp != null) {
				int len = newIp.length;
				for (int i = 0; i < len; i++) {
					Zookeeper zookeeper = new Zookeeper();
					if (newIp[i] == null || newIp[i].isEmpty()) {
						continue;
					}
					if (newPort[i] == null || newPort[i].isEmpty()) {
						continue;
					}
					if (newJq[i] == null || newJq[i].isEmpty()) {
						continue;
					}
					zookeeper.setIp(newIp[i]);
					zookeeper.setPort(newPort[i]);
					zookeeper.setJq(newJq[i]);
					// databaseService.insertZookeeperSelective(zookeeper);
					zookeeperService.insertSelective(zookeeper);
				}
			}
		} else if ("delete".equals(action)) {
			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
			if (selectedIds != null) {
				for (String selectedId : selectedIds) {
					// databaseService.deleteZookeeperByPrimaryKey(Integer.valueOf(selectedId));
					zookeeperService.deleteByPrimaryKey(Integer.valueOf(selectedId));
				}
			}
		}
		// 针对save和delete的推送
		ObjectMapper mapper = JsonProcessUtil.getMapperInstance();
		List<Zookeeper> zookeeperList = zookeeperService.selectAllSelective(null);
		ArrayNode arrayNode = mapper.createArrayNode();
		for (int i = 0; i < zookeeperList.size(); i++) {
			arrayNode.add(mapper.convertValue(zookeeperList.get(i), ObjectNode.class));
		}
		DisconfPush.push(arrayNode, PropertiesUtil.getStringValue("zookeeperConfigId"));
		
		return "redirect:/system/zookpeer";
	}

}
