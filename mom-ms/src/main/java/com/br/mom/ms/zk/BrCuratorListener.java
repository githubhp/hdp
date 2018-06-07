package com.br.mom.ms.zk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.mom.ms.controller.AlarmController;
import com.br.mom.ms.model.ConsumerThreshold;
import com.br.mom.ms.service.ConsumerThresholdService;
import com.br.mom.ms.util.IceUtil;

@Service
public class BrCuratorListener implements CuratorListener {
	@Autowired
	private ConsumerThresholdService consumerThresholdService;

	private final static Logger logger = LoggerFactory.getLogger(BrCuratorListener.class);

	@Override
	public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
		try {
			final WatchedEvent watchedEvent = event.getWatchedEvent();

			if (watchedEvent != null) {
				if (watchedEvent.getState() == KeeperState.SyncConnected) {
					switch (watchedEvent.getType()) {
					case NodeChildrenChanged:
						save(client, event, consumerThresholdService);
						break;
					case NodeDataChanged:
						byte[] data = client.getData().watched().forPath(event.getPath());
						logger.info("date:{}", new String(data));
					default:
						logger.info("default!");
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.error(">>>eventReceived:", e);
		}
	}

	public static void save(CuratorFramework client, CuratorEvent event,
			ConsumerThresholdService consumerThresholdService) throws KeeperException, InterruptedException {
		Thread.sleep(5000);
		List<String> children = new ArrayList<String>();
		try {
			children = client.getChildren().watched().forPath(event.getPath());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String[] groupName = event.getPath().split("/");
		String groupname = groupName[2];
		List<ConsumerThreshold> consumerThresholdpath = consumerThresholdService.selectAllSelective(null);
		for (ConsumerThreshold consumer : consumerThresholdpath) {
			if (groupname.equals(consumer.getGroupName())) {
				ConsumerThreshold consumerThreshold = new ConsumerThreshold();
				Integer id = consumer.getId();
				StringBuffer consumerNumber = new StringBuffer();
				if (children.size() != 0) {
					for (String s : children) {
						consumerNumber.append(s);
						consumerNumber.append("\n");
					}
					// consumerThreshold.setConsumerNumber(consumerNumber.toString());
				}
				logger.info(">>>consumer:" + children);
				Integer k = 100;
				String status = "0";
				try {
					k = Integer.valueOf(consumer.getThreshold()) - children.size();
				} catch (Exception e) {
					logger.error("get consumer Threshold error:", e);
				}
				if (k == 0) {
					status = "1";
					consumerThreshold.setConsumerLive(" ");
					consumerThreshold.setConsumerNumber(consumerNumber.toString());
				} else if (k > 0) {
					status = "0";
					consumerThreshold.setConsumerLive(consumerNumber.toString());

				} else {
					status = "2";
					consumerThreshold.setConsumerLive(consumerNumber.toString());
				}
				consumerThreshold.setId(Integer.valueOf(id));
				consumerThreshold.setStatus(status);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date newtime = new Date();
				String time = dateFormat.format(newtime).toString();
				consumerThreshold.setUpdateTime(time);
				consumerThresholdService.updateByPrimaryKeySelective(consumerThreshold);

				// mom后台是否发送停止或者启动消费端的报警信息
				if (AlarmController.flag == true) {
					List<String> consumer_old = new ArrayList<String>();
					String[] consumer_ = consumer.getConsumerNumber().split("\n");
					for (String s : consumer_) {
						consumer_old.add(s);
					}
					if (consumerThreshold.getStatus().equals("0")) {
						for (String consumer2 : consumer_old) {
							if (!children.contains(consumer2)) {
								IceUtil.sendAlarm(new Exception(), "停止消费端", consumer2 + "消费组已停止", "45005");
								logger.info(">>>>>停止" + consumer2 + "消费组");
							}
						}
					} else {
						for (String consumr1 : children) {
							if (!consumer_old.contains(consumr1)) {
								IceUtil.sendAlarm(new Exception(), "启动消费端", consumr1 + "消费组已启动", "45005");
								logger.info(">>>>>启动" + consumr1 + "消费组");
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		/*
		 * 需要得出结果： 1----启动 2----正常 3----启动 4----正常 5----启动 6----正常 7----停止
		 * 8----停止 9----停止
		 */
		String str_new = "1	2	3	4	5	6";
		String[] str1 = str_new.split("	");
		List<String> str_1 = new ArrayList<String>();
		for (String s : str1) {
			str_1.add(s);
		}
		List<String> str_old = new ArrayList<String>();
		str_old.add("2");
		str_old.add("4");
		str_old.add("7");
		str_old.add("6");
		str_old.add("9");
		str_old.add("8");
		for (String s : str_1) {
			if (!str_old.contains(s)) {
				System.out.println(s + "----启动");
			}
		}
		for (String s : str_old) {
			if (!str_1.contains(s)) {
				System.out.println(s + "----停止");
			}
		}
	}

}
