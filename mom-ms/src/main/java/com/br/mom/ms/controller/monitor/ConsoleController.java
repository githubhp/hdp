package com.br.mom.ms.controller.monitor;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.mom.ms.model.Console;
import com.br.mom.ms.model.Queue;
import com.br.mom.ms.service.ConsoleService;
import com.br.mom.ms.service.QueueService;

/**
 *
 * @author xin.cao@100credit.com
 */
@Controller
@RequestMapping("/monitor")
public class ConsoleController {
	@Autowired
	private QueueService queueService;
	@Autowired
	private ConsoleService consoleService;

	/**
	 * 消费者状态配置
	 *
	 * @param httpServletRequest
	 * @param model
	 * @return
	 * @throws java.io.IOException
	 */
	@RequestMapping(value = "/console")
	public String consumerStatusAction(HttpServletRequest httpServletRequest, Model model) throws IOException {
		List<Queue> queues = queueService.selectAllSelective(null);
		model.addAttribute("queues", queues);
		String queueId = httpServletRequest.getParameter("queue_id");
		if (queueId != null) {
			Console console = new Console();
			console.setQueueId(Integer.valueOf(queueId));
			List<Console> consoles = consoleService.selectAllSelective(console);
			model.addAttribute("consoles", consoles);
			model.addAttribute("queueId", queueId);
		}
		return "monitor/console";
	}

}
