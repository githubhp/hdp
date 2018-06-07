package com.br.mom.ms.controller.system;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.mom.ms.model.Redis;
import com.br.mom.ms.service.RedisService;
import com.br.mom.ms.util.DateUtil;

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
public class RedisController {
	@Autowired
	private RedisService redisService;

	/**
	 * redis配置
	 *
	 * @param httpServletRequest
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/redis")
	public String redisAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
		// DatabaseServiceClient databaseService =
		// DatabaseServiceClient.getInstance();
		String action = httpServletRequest.getParameter("action");
		model.addAttribute("DateUtil", DateUtil.getInstance());
		if (action == null || "".equals(action)) {
			Redis redis = new Redis();
			// List<Redis> rediss =
			// databaseService.selectAllRedisSelective(redis);
			List<Redis> rediss = redisService.selectAllSelective(redis);
			model.addAttribute("rediss", rediss);
			return "system/redis";
		} else if ("save".equals(action)) {
			String[] ids = httpServletRequest.getParameterValues("ids[]");
			if (ids != null) {
				for (String id : ids) {
					Redis redis = new Redis();
					String ip = httpServletRequest.getParameter(id + ":ip");
					String port = httpServletRequest.getParameter(id + ":port");
					redis.setId(Integer.valueOf(id));
					if (ip == null || ip.isEmpty()) {
						continue;
					}
					if (port == null || port.isEmpty()) {
						continue;
					}
					redis.setIp(ip);
					redis.setPort(port);
					// databaseService.updateRedisByPrimaryKeySelective(redis);
					redisService.updateByPrimaryKeySelective(redis);
				}
			}
			String[] newIp = httpServletRequest.getParameterValues("new:ip[]");
			String[] newPort = httpServletRequest.getParameterValues("new:port[]");
			if (newIp != null) {
				int len = newIp.length;
				for (int i = 0; i < len; i++) {
					Redis redis = new Redis();
					if (newIp[i] == null || newIp[i].isEmpty()) {
						continue;
					}
					if (newPort[i] == null || newPort[i].isEmpty()) {
						continue;
					}
					redis.setIp(newIp[i]);
					redis.setPort(newPort[i]);
					// databaseService.insertRedisSelective(redis);
					redisService.insertSelective(redis);
				}
			}
		} else if ("delete".equals(action)) {
			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
			if (selectedIds != null) {
				for (String selectedId : selectedIds) {
					// databaseService.deleteRedisByPrimaryKey(Integer.valueOf(selectedId));
					redisService.deleteByPrimaryKey(Integer.valueOf(selectedId));
				}
			}
		}
		return "redirect:/system/redis";
	}
}
