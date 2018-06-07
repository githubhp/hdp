package com.br.mom.ms.util;

import java.util.HashMap;
import java.util.Map;

import com.br.mom.ms.disconf.APIClient;
import com.br.mom.ms.disconf.Configure;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
/**
 * 推送配置中心公共类
* @ClassName: DisconfPush 
* 
* @author chao.zhang 
* @date 2017年5月2日 上午11:44:40 
*
 */
public class DisconfPush {
//	private static Logger logger=LoggerFactory.getLogger(DisconfPush.class);
	/**
	 * 推送到配置中心
	 * @param arrayNode
	 * @param configId
	 * @throws JsonProcessingException
	 */
	public  static  void push(ArrayNode arrayNode, String configId) throws JsonProcessingException{
		Configure.setCD(1000);
		ObjectMapper mapper=JsonProcessUtil.getMapperInstance();
		APIClient apic = APIClient.getInstance("admin", "bWzOBB5DaoGOKj");
		Map<String, String> configure = new HashMap<String, String>();
		configure.put("data", mapper.writeValueAsString(arrayNode));
		apic.changeAppFileConfig(configId, configure);
	}
}
