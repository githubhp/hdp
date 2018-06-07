package com.br.mom.ms.controller.monitor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.mom.ms.bean.Page;
import com.br.mom.ms.model.ConsumerGroups;
import com.br.mom.ms.service.ConsumerGroupsService;
import com.br.mom.ms.util.DateUtil;
import com.br.mom.ms.zk.BrCuratorClient;

/**
 * 监控kafka集群所有已经存在的消费组
 */
@Controller
@RequestMapping("/monitor")
public class ConsumerGroupsController {
	@Autowired
	private ConsumerGroupsService consumerGroupsService;
	@Autowired
	private BrCuratorClient brCuratorClient;

	@RequestMapping(value = "/consumer_groups")
	public String consumerThresholdAction(HttpServletRequest httpServletRequest, Model model) throws Exception {
		
		String action = httpServletRequest.getParameter("action");
		String pageNow = httpServletRequest.getParameter("pageNumber");
		String pageSize =  httpServletRequest.getParameter("customPageSize");
		
		model.addAttribute("DateUtil", DateUtil.getInstance());
		if (action == null || "".equals(action)) {
			//分页查询
			Page<ConsumerGroups> page = new Page<ConsumerGroups>();
			if(pageNow!=null){
				page.setPageNow(Integer.valueOf(pageNow));
			}
			if(pageSize!=null){
				page.setPageSize(Integer.valueOf(pageSize));
			}
			List<ConsumerGroups> consumerGroupss = consumerGroupsService.selectByPage(page.getStartIndex(), page.getPageSize());
			int totalRecords = consumerGroupsService.selectTotalRecords();
			page.setTotalRecords(totalRecords);
			page.setList(consumerGroupss);
			model.addAttribute("page",page);
			return "monitor/consumer_groups";
//			model.addAttribute("consumerGroupss", consumerGroupss);
		}
		return "redirect:/monitor/consumer_groups";
	}

	// 刷新功能,查询(当前)所有消费组及消费组中存活的消费者
	@RequestMapping(value = "/consumer_groups/flush")
	public String flushAction(HttpServletRequest httpServletRequest, Model model) throws Exception {
		// 获取数据库中的消费组
		List<ConsumerGroups> consumerGroupss = consumerGroupsService.selectAllSelective(null);
		List<String> list = new ArrayList<String>();
		for (ConsumerGroups consumerGroups : consumerGroupss) {
			list.add(consumerGroups.getConsumerGroup());
		}
		// 获取zookeeper上的消费组
		String path = "/consumers";
		List<String> childs = brCuratorClient.getChild(path);
		// 遍历zookeeper上的消费组
		for (String child : childs) {
			ConsumerGroups consumerGroups = new ConsumerGroups();
			// 设置该消费组名称
			consumerGroups.setConsumerGroup(child);
			// 设置该消费组中的运行的消费者
			String childpath = "/consumers/" + child + "/ids";
			List<String> childdata = brCuratorClient.getChild(childpath);
			StringBuffer buffer = new StringBuffer();
			for (String data : childdata) {
				buffer.append(data);
				buffer.append("\n");
			}
			consumerGroups.setConsumerLive(buffer.toString());
			// 设置当前时间为更新时间
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date newtime = new Date();
			consumerGroups.setUpdateTime(String.format(dateFormat.format(newtime)));
			// 如果数据库中有这个消费组，则更新数据库中的这个消费组
			if (list.contains(child)) {
				// 设置消费组的id
				for (ConsumerGroups consumergroups : consumerGroupss) {
					if (consumergroups.getConsumerGroup().equals(child)) {
						consumerGroups.setId(consumergroups.getId());
					}
				}
				// 更新数据
				consumerGroupsService.updateByPrimaryKeySelective(consumerGroups);
			} else {
				// 如果数据库中没有该条数据则插入该条数据到数据库中
				consumerGroupsService.insertSelective(consumerGroups);
			}
		}
		return "redirect:/monitor/consumer_groups";
	}
}
