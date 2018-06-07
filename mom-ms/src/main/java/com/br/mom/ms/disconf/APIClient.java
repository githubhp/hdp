package com.br.mom.ms.disconf;

import com.alibaba.fastjson.JSONObject;
import com.br.mom.ms.util.PropertiesUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @ClassName: APIClient
 * @Description: 配置中心推送数据
 * @author chao.zhang
 * @date 2017年5月9日 下午3:51:11
 *
 */
public class APIClient {

	private static final Logger logger = LoggerFactory.getLogger(APIClient.class);
	private static APIClient apiClient = null;
	private static final Object lock = new Object();
	private String host = PropertiesUtil.getStringValue("disconf.host");
	private String username = PropertiesUtil.getStringValue("disconf.username");;
	private String passwd = PropertiesUtil.getStringValue("disconf.passwd");;
	private static Map<String, List<ChangeAppConfigTask>> tableAndChangeAppConfigTask = new HashMap<String, List<ChangeAppConfigTask>>();

	private APIClient(String username, String passwd) {
		this.username = username;
		this.passwd = passwd;
	}

	private APIClient(String host, String username, String passwd) {
		this.host = host;
		this.username = username;
		this.passwd = passwd;
	}

	public static APIClient getInstance(String host, String username, String passwd) {
		if (APIClient.apiClient == null) {
			synchronized (lock) {
				if (APIClient.apiClient == null) {
					APIClient.apiClient = new APIClient(username, passwd);
					Timer timer = new Timer("定时任务线程");
					timer.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							for (Entry<String, List<ChangeAppConfigTask>> e : tableAndChangeAppConfigTask.entrySet()) {
								List<ChangeAppConfigTask> cacts = e.getValue();
								if (cacts != null && !cacts.isEmpty()) {
									tableAndChangeAppConfigTask.put(cacts.get(0).getConfigId(),
											new ArrayList<ChangeAppConfigTask>());
									ChangeAppConfigTask cact = cacts.get(cacts.size() - 1);
									cact.run();
								}
							}
						}
					}, 0, Configure.getCD());
				}
			}
		}
		return APIClient.apiClient;
	}

	public static APIClient getInstance(String username, String passwd) {
		if (APIClient.apiClient == null) {
			synchronized (lock) {
				if (APIClient.apiClient == null) {
					APIClient.apiClient = new APIClient(username, passwd);
					Timer timer = new Timer("定时任务线程");
					timer.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							for (Entry<String, List<ChangeAppConfigTask>> e : tableAndChangeAppConfigTask.entrySet()) {
								List<ChangeAppConfigTask> cacts = e.getValue();
								if (cacts != null && !cacts.isEmpty()) {
									tableAndChangeAppConfigTask.put(cacts.get(0).getConfigId(),
											new ArrayList<ChangeAppConfigTask>());
									ChangeAppConfigTask cact = cacts.get(cacts.size() - 1);
									cact.run();
								}
							}
						}
					}, 0, Configure.getCD());
				}
			}
		}
		return APIClient.apiClient;
	}

	/**
	 * 修改配置内容
	 *
	 * @param configId
	 *            唯一标识一个配置文件
	 * @param nameAndValue
	 */
	public void changeAppFileConfig(String configId, Map<String, String> nameAndValue) {
		if (!tableAndChangeAppConfigTask.containsKey(configId)) {
			tableAndChangeAppConfigTask.put(configId, new ArrayList<ChangeAppConfigTask>());
		}
		List<ChangeAppConfigTask> cacts = tableAndChangeAppConfigTask.get(configId);
		cacts.add(new ChangeAppFileConfigTask(host, username, passwd, configId, nameAndValue));
	}

	public static class ChangeAppFileConfigTask extends ChangeAppConfigTask {

		private String host;
		private String username;
		private String passwd;
		private Map<String, String> nameAndValue;
		private long createTime;

		public ChangeAppFileConfigTask(String host, String username, String passwd, String configId,
				Map<String, String> nameAndValue) {
			this.host = host;
			this.username = username;
			this.passwd = passwd;
			this.configId = configId;
			this.nameAndValue = nameAndValue;
			this.createTime = System.currentTimeMillis();
		}

		public void run() {
			logger.info(toString());
			try {
				CloseableHttpClient client = HttpClients.createDefault();
				URI uri = new URIBuilder().setScheme("http").setHost(host).setPath("/api/account/signin").build();
				HttpPost post = new HttpPost(uri);
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("name", username));
				nvps.add(new BasicNameValuePair("password", passwd));
				nvps.add(new BasicNameValuePair("remember", "1"));
				UrlEncodedFormEntity form = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
				post.setEntity(form);
				CloseableHttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				JSONObject data = JSONObject.parseObject(EntityUtils.toString(entity, Consts.UTF_8));
				if (!data.getBoolean("success")) {
					client.close();
					logger.error(Result.LOGIN_FAILURE.getMessage());
				}
				uri = new URIBuilder().setScheme("http").setHost(host).setPath("/api/web/config/filetext/" + configId)
						.build();
				HttpPut put = new HttpPut(uri);
				nvps = new ArrayList<NameValuePair>();
				StringBuilder sb = new StringBuilder();
				for (Entry<String, String> e : nameAndValue.entrySet()) {
					sb.append(e.getKey()).append("=").append(e.getValue()).append("\n");
				}
				nvps.add(new BasicNameValuePair("fileContent", sb.toString()));
				form = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
				put.setEntity(form);
				response = client.execute(put);
				entity = response.getEntity();
				data = JSONObject.parseObject(EntityUtils.toString(entity, Consts.UTF_8));
				if (!data.getBoolean("success")) {
					client.close();
					logger.error(Result.CHANGE_FAILURE.getMessage());
				} else {
					logger.warn(Result.SUCCESS.getMessage());
				}
				client.close();
			} catch (URISyntaxException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			} catch (ParseException e) {
				logger.error(e.getMessage());
			} catch (Throwable e) {
				logger.error(e.getMessage());
			}
		}

		@Override
		public String toString() {
			return "ChangeAppFileConfigTask{" + "host=" + host + ", username=" + username + ", passwd=" + passwd
					+ ", nameAndValue=" + nameAndValue + ", createTime=" + createTime + '}';
		}

	}

	/**
	 * 修改配置内容
	 *
	 * @param configId
	 *            唯一标识一个配置文件
	 * @param table
	 * @param cloumnNames
	 * @param nameAndValues
	 */
	public void changeAppItemConfig(String configId, String table, List<String> cloumnNames,
			List<Map<String, String>> nameAndValues) {
		if (!tableAndChangeAppConfigTask.containsKey(configId)) {
			tableAndChangeAppConfigTask.put(configId, new ArrayList<ChangeAppConfigTask>());
		}
		List<ChangeAppConfigTask> cacts = tableAndChangeAppConfigTask.get(configId);
		cacts.add(new ChangeAppItemConfigTask(host, username, passwd, configId, table, cloumnNames, nameAndValues));
	}

	public static class ChangeAppItemConfigTask extends ChangeAppConfigTask {

		private String host;
		private String username;
		private String passwd;
		private String table;
		private List<String> cloumnNames;
		private List<Map<String, String>> nameAndValues;
		private long createTime;

		public ChangeAppItemConfigTask(String host, String username, String passwd, String configId, String table,
				List<String> cloumnNames, List<Map<String, String>> nameAndValues) {
			this.host = host;
			this.username = username;
			this.passwd = passwd;
			this.configId = configId;
			this.table = table;
			this.cloumnNames = cloumnNames;
			this.nameAndValues = nameAndValues;
			this.createTime = System.currentTimeMillis();
		}

		public void run() {
			logger.info(toString());
			try {
				CloseableHttpClient client = HttpClients.createDefault();
				URI uri = new URIBuilder().setScheme("http").setHost(host).setPath("/api/account/signin").build();
				HttpPost post = new HttpPost(uri);
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("name", username));
				nvps.add(new BasicNameValuePair("password", passwd));
				nvps.add(new BasicNameValuePair("remember", "1"));
				UrlEncodedFormEntity form = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
				post.setEntity(form);
				CloseableHttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				JSONObject data = JSONObject.parseObject(EntityUtils.toString(entity, Consts.UTF_8));
				if (!data.getBoolean("success")) {
					client.close();
					logger.error(Result.LOGIN_FAILURE.getMessage());
				}
				uri = new URIBuilder().setScheme("http").setHost(host).setPath("/api/web/config/item/" + configId)
						.build();
				HttpPut put = new HttpPut(uri);
				nvps = new ArrayList<NameValuePair>();
				JSONObject value = new JSONObject();
				value.put("table", table);
				StringBuilder lines = new StringBuilder();
				for (Map<String, String> nameAndValue : nameAndValues) {
					StringBuilder line = new StringBuilder();
					for (String cloumnName : cloumnNames) {
						line.append(nameAndValue.get(cloumnName)).append("|");
					}
					String lineCheck = MdImplement.encodeMD5To32(line.toString());
					nameAndValue.put("line_check", lineCheck);
					lines.append(lineCheck).append("|");
				}
				value.put("data", nameAndValues);
				value.put("data_check", MdImplement.encodeMD5To32(lines.toString()));
				nvps.add(new BasicNameValuePair("value", value.toString()));
				form = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
				put.setEntity(form);
				response = client.execute(put);
				entity = response.getEntity();
				data = JSONObject.parseObject(EntityUtils.toString(entity, Consts.UTF_8));
				if (!data.getBoolean("success")) {
					client.close();
					logger.error(Result.CHANGE_FAILURE.getMessage());
				} else {
					logger.warn(Result.SUCCESS.getMessage());
				}
				client.close();
			} catch (URISyntaxException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			} catch (ParseException e) {
				logger.error(e.getMessage());
			} catch (Throwable e) {
				logger.error(e.getMessage());
			}
		}

		public long getCreateTime() {
			return this.createTime;
		}

		@Override
		public String toString() {
			return "ChangeAppItemConfigTask{" + "host=" + host + ", username=" + username + ", passwd=" + passwd
					+ ", configId=" + configId + ", table=" + table + ", cloumnNames=" + cloumnNames
					+ ", nameAndValues=" + nameAndValues + '}';
		}
	}

	public static abstract class ChangeAppConfigTask implements Runnable {

		protected String configId;

		public String getConfigId() {
			return configId;
		}
	}

	public static void main(String... args) {
		APIClient apic = APIClient.getInstance("admin", "bWzOBB5DaoGOKj");
		Map<String, String> configure = new HashMap<String, String>();
		configure.put("name", "曹鑫");
		apic.changeAppFileConfig("148", configure);
	}
}
