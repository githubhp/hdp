package com.br.mom.ms.controller.monitor;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.br.mom.ms.bean.Page;
import com.br.mom.ms.model.ConsumerThreshold;
import com.br.mom.ms.service.ConsumerThresholdService;
import com.br.mom.ms.util.DateUtil;
import com.br.mom.ms.zk.BrCuratorClient;

@Controller
@RequestMapping("/monitor")
public class ConsumerThresholdController {
	private final static Logger logger = LoggerFactory.getLogger(ConsumerThresholdController.class);
	@Autowired
	private ConsumerThresholdService consumerThresholdService;
	@Autowired
	private BrCuratorClient brCuratorClient;

	/**
	 * 消费端监控
	 *
	 * @param httpServletRequest
	 * @param model
	 * @return
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	@RequestMapping(value = "/consumer_threshold")
	public String consumerThresholdAction(HttpServletRequest httpServletRequest, Model model)
			throws IOException, KeeperException, InterruptedException {
		//注册监听,为所有监听的消费端注册监听,如果状态变化则会根据状态变化触发不同事件
		List<ConsumerThreshold> consumerThresholdpath = consumerThresholdService.selectAllSelective(null);
		for (ConsumerThreshold consumerThreshold : consumerThresholdpath) {
			String nodepath = "/consumers/" + consumerThreshold.getGroupName() + "/ids";
			try {
				brCuratorClient.addWatch(nodepath, false);
			} catch (Exception e) {
				logger.error(">>>Register monitor error:", e);
			}
		}
		String action = httpServletRequest.getParameter("action");
		String pageNow = httpServletRequest.getParameter("pageNumber");
		String pageSize = httpServletRequest.getParameter("customPageSize");

		model.addAttribute("DateUtil", DateUtil.getInstance());

		if (action == null || "".equals(action)) {
			//分页查询
			Page<ConsumerThreshold> page = new Page<ConsumerThreshold>();
			if (pageNow != null) {
				page.setPageNow(Integer.valueOf(pageNow));
			}
			if (pageSize != null) {
				page.setPageSize(Integer.valueOf(pageSize));
			}
			List<ConsumerThreshold> consumerThresholds = consumerThresholdService.selectByPage(page.getStartIndex(),
					page.getPageSize());
			int totalRecords = consumerThresholdService.selectTotalRecords();
			page.setTotalRecords(totalRecords);
			page.setList(consumerThresholds);
			model.addAttribute("page", page);

			return "monitor/consumer_threshold";
			// model.addAttribute("consumerThresholds", consumerThresholds);
		} else if ("save".equals(action)) {
			String[] ids = httpServletRequest.getParameterValues("ids[]");
			if (ids != null) {
				for (String id : ids) {
					ConsumerThreshold consumerThreshold = new ConsumerThreshold();
					String groupName = httpServletRequest.getParameter(id + ":group");
					String queueName = httpServletRequest.getParameter(id + ":topic");
					String port = httpServletRequest.getParameter(id + ":port");
					String threshold = httpServletRequest.getParameter(id + ":threshold");
					consumerThreshold.setId(Integer.valueOf(id));
					if (groupName == null || groupName.isEmpty()) {
						continue;
					}
					if (queueName == null || queueName.isEmpty()) {
						continue;
					}
					if (port == null || port.isEmpty()) {
						continue;
					}
					if (threshold == null || threshold.isEmpty()) {
						continue;
					}
					consumerThreshold.setGroupName(groupName);
					consumerThreshold.setQueueName(queueName);
					consumerThreshold.setThreshold(threshold);
					consumerThresholdService.updateByPrimaryKeySelective(consumerThreshold);
				}
			}
			String[] newgroupName = httpServletRequest.getParameterValues("new:group[]");
			String[] newqueueName = httpServletRequest.getParameterValues("new:topic[]");
			String[] newPort = httpServletRequest.getParameterValues("new:port[]");
			String[] newthreshold = httpServletRequest.getParameterValues("new:threshold[]");
			if (newgroupName != null) {
				int len = newgroupName.length;
				for (int i = 0; i < len; i++) {
					ConsumerThreshold consumerThreshold = new ConsumerThreshold();
					if (newgroupName[i] == null || newgroupName[i].isEmpty()) {
						continue;
					}
					//设置消费组
					String newGroupName = newgroupName[i];
					newGroupName = newGroupName.replaceAll("\\s*", "");
					consumerThreshold.setGroupName(newGroupName);
					//设置消费主题
					String newQueueName = newqueueName[i];
					newQueueName = newQueueName.replaceAll("\\s*", "");
					consumerThreshold.setQueueName(newQueueName);
					//设置端口号
					String newport = newPort[i];
					newport = newport.trim();
					if (newport.contains(";") | newport.contains(",") | newport.contains(" ")) {
						String[] s = newport.split("[;, ]");
						StringBuffer sbuffer = new StringBuffer();
						for (String s1 : s) {
							sbuffer.append(s1);
							sbuffer.append("\n\t");
						}
						String sk = sbuffer.substring(0, sbuffer.lastIndexOf("\n")).toString();
						consumerThreshold.setPort(sk);
					} else {
						consumerThreshold.setPort(newport);
					}
					//设置消费者数
					try {
						String path_consumer = "/consumers/" + newgroupName[i] + "/ids";
						path_consumer = path_consumer.replaceAll("\\s*", "");
						List<String> consumerList = brCuratorClient.getChild(path_consumer);
						StringBuffer consumerNumber = new StringBuffer();
						for (String s : consumerList) {
							consumerNumber.append(s);
							consumerNumber.append("\n");
						}
						consumerThreshold.setConsumerNumber(consumerNumber.toString());
						//设置状态
						Integer k = 100;
						if (consumerList != null && !consumerList.isEmpty()) {
							k = Integer.valueOf(newthreshold[i]) - consumerList.size();
						}
						String status = "0";
						if (k == 100) {
							status = "0";
						} else if (k == 0) {
							status = "1";
						} else {
							status = "0";
						}
						consumerThreshold.setStatus(status);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//设置阈值
					String newThreshold = newthreshold[i];
					newThreshold.replaceAll("\\s*", "");
					consumerThreshold.setThreshold(newThreshold);
					//设置更新时间和创建时间
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date newtime = new Date();
					String time = String.format(dateFormat.format(newtime));
					consumerThreshold.setCreateTime(time);
					consumerThreshold.setUpdateTime(time);
					consumerThresholdService.insertSelective(consumerThreshold);
				}
			}
		} else if ("delete".equals(action)) {
			String[] selectedIds = httpServletRequest.getParameterValues("selected_ids[]");
			if (selectedIds != null) {
				for (String selectedId : selectedIds) {
					consumerThresholdService.deleteByPrimaryKey(Integer.valueOf(selectedId));
				}
			}
		}
		return "redirect:/monitor/consumer_threshold";
	}
}
