package com.br.mom.ms.disconf;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
/**
 * 
* @ClassName: APIReadClient 
* @Description: 配置中心获取对应的配置信息
* @author chao.zhang 
* @date 2017年5月9日 下午3:50:36 
*
 */
public class APIReadClient {
	private static Logger logger = LoggerFactory.getLogger(APIReadClient.class);
	private static String host = "disconf-web.100credit.cn";
	private static String username="admin";
	private static String passwd="bWzOBB5DaoGOKj";
	
	
	
	public APIReadClient() {
		super();
	}

	public static APIReadClient getInstance(){
		return APIReadClientHolder.apiReadClient;
	}
	
	static class APIReadClientHolder{
		public static APIReadClient apiReadClient=new APIReadClient();
	}
	
	public static void main(String[] args) {
		System.out.println(APIReadClient.getInstance().getConfigData("195"));

	}

	public  String getConfigData(String configId) {
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			URI uri = new URIBuilder().setScheme("http").setHost(host)
					.setPath("/api/account/signin").build();
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
			uri = new URIBuilder().setScheme("http").setHost(host)
					.setPath("/api/web/config/" + configId).build();
			HttpGet getData = new HttpGet(uri);
			response = client.execute(getData);
			entity = response.getEntity();
			data = JSONObject.parseObject(EntityUtils.toString(entity, Consts.UTF_8));
			if (!data.getBoolean("success")) {
				client.close();
				logger.error(Result.CHANGE_FAILURE.getMessage());
			} else {
				logger.warn(Result.SUCCESS.getMessage());
				return data.getJSONObject("result").getString("value");
			}
			client.close();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public String toString() {
		return "APIReadClient [host=" + host + ", username=" + username + ", passwd=" + passwd
				+ "]";
	}

}
