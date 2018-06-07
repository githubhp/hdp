package com.br.mom.ms.zk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.br.mom.ms.util.PropertiesUtil;

@Service
@Scope("singleton")
public class BrCuratorClient {

	private final static Logger logger = LoggerFactory.getLogger(BrCuratorClient.class);
	private CuratorFramework mZkClient = null;

	@Autowired
	private BrCuratorListener brCuratorListener;
	/*
	 * 初始化方法
	 */
	@PostConstruct
	private void init() {
		try {
			mZkClient = CuratorFrameworkFactory.newClient(PropertiesUtil.getStringValue("zookeeper.id"),
					new RetryNTimes(3, 60000));
			mZkClient.getCuratorListenable().addListener(brCuratorListener);
			mZkClient.start();
			logger.info("zk client start successfully!");
		} catch (Exception e) {
			logger.error(">>>init:{}", e.toString());
		}
	}
	/*
	 * 对某个节点或者子节点添加监听事件
	 */
	public void addWatch(String nodePath, boolean isSelf) throws Exception {
		if (isSelf) {
			mZkClient.getData().watched().forPath(nodePath);
		} else {
			mZkClient.getChildren().watched().forPath(nodePath);
		}
	}
	/*
	 * 获取子节点信息
	 */
	public List<String> getChild(String path) throws Exception {
		List<String> data = new ArrayList<String>();
		try{
			data = mZkClient.getChildren().forPath(path);
		}catch(NoNodeException e){
			return data;
		}
		catch(Exception e){
			logger.error(">>>get children data error："+e);
		}
		return data;
	}
	/*
	 * 获取某个路径下的所有分区的offset总和
	 */
	public Long getAllOffset(String path) throws Exception {
		Long num = 0L;
		List<String> partitionList = null;
		try {
			partitionList = mZkClient.getChildren().forPath(path);
			if (partitionList == null || partitionList.isEmpty()) {
				return 0L;
			}
			byte[] data = null;
			for (String s : partitionList) {
				data = mZkClient.getData().forPath(path + "/" + s);
				num += Long.valueOf(new String(data, "utf8"));
			}
		} catch (NoNodeException e) {
			return 0L;
		} catch (Exception e) {
			logger.error(">>>>>>error:", e);
		}
		return num;
	}
	/*
	 * 获取某个路径下的各个分区的offset
	 */
	public Map<String, String> getOffset(String path) {
		Map<String, String> map = new HashMap<String, String>();
		List<String> partitionList = null;
		try {
			partitionList = mZkClient.getChildren().forPath(path);
			if (partitionList == null || partitionList.isEmpty()) {
				return map;
			}
			byte[] data = null;
			for (String s : partitionList) {
				data = mZkClient.getData().forPath(path + "/" + s);
				String str = new String(data, "utf8");
				map.put(s, str);
			}
		} catch (NoNodeException e) {
			return map;
		} catch (Exception e) {
			logger.error(">>>>>>error:", e);
		}
		return map;
	}

	public static void main(String[] args) throws Exception {
		BrCuratorListener brlistener = new BrCuratorListener();
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
		CuratorFramework mZkClient = CuratorFrameworkFactory.newClient("192.168.23.51:2181", new RetryNTimes(3, 60000));
		mZkClient.getCuratorListenable().addListener(brlistener);
		mZkClient.start();
		String path = "/consumers/test_li_four_HDFS/offsets/test_li_four";
		List<String> partitionList = mZkClient.getChildren().forPath(path);
		byte[] data = null;
		// Long num = 0L;
		for (String s : partitionList) {
			data = mZkClient.getData().forPath(path + "/" + s);
			// num += Long.valueOf(new String(data, "utf8"));
			String str = new String(data, "utf8");
			map.put(s, str);
		}
		System.out.println(map.toString());
		mZkClient.close();
	}
}
