package com.br.mom.ms.controller.configure;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.br.mom.ms.common.enums.PersistentType;
import com.br.mom.ms.model.Consumer;
import com.br.mom.ms.service.ConsumerService;
import com.br.mom.ms.util.MySqlConnection;

@Controller
@RequestMapping("/configure")
public class UtilController {
@Autowired
private ConsumerService consumerService;
    /**
     * 工具模块配置
     *
     * @param httpServletRequest
     * @param model
     * @return
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    @RequestMapping(value = "/util")
    public String utilAction(HttpServletRequest httpServletRequest, Model model) throws IOException, SQLException, ClassNotFoundException {
//        DatabaseServiceClient databaseService = DatabaseServiceClient.getInstance();
//        List<Consumer> consumers = databaseService.selectAllConsumerSelective(null);
    	List<Consumer> consumers = consumerService.selectAllSelective(null);
        List<Consumer> supprtConsumers = new ArrayList<Consumer>();
        for (Consumer c : consumers) {
            if (PersistentType.fromInt(c.persistentType) != PersistentType.MYSQL) {
                continue;
            }
            String params = c.getParams();
            JSONObject configure = JSONObject.parseObject(params, Feature.DisableCircularReferenceDetect);
            if (!configure.containsKey("is_sub_database")) {
                continue;
            }
            boolean isSubDatabase = configure.getBoolean("is_sub_database");
            JSONObject persistent = configure.getJSONObject("persistent");
            boolean isSubTable = persistent.getBoolean("is_sub_table");
            if (!isSubDatabase && !isSubTable) { // 不分库也不分表
                supprtConsumers.add(c);
            }
        }
        model.addAttribute("consumers", supprtConsumers);
        String consumerId = httpServletRequest.getParameter("consumer_id");
        if (consumerId != null && !consumerId.isEmpty()) {
            model.addAttribute("consumerId", consumerId);
            for (Consumer c : supprtConsumers) {
                if (c.getId() == Integer.valueOf(consumerId)) {
                    String params = c.getParams();
                    JSONObject configure = JSONObject.parseObject(params, Feature.DisableCircularReferenceDetect);
                    JSONObject persistent = configure.getJSONObject("persistent");
                    String host = persistent.getString("host");
                    String port = persistent.getString("port");
                    String user = persistent.getString("user");
                    String passwd = persistent.getString("passwd");
                    String database = persistent.getString("database");
                    String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
                    String command = persistent.getString("command");
                    String[] words = command.split("\\s");
                    MySqlConnection connection = new MySqlConnection(url, user, passwd);
                    Connection conn = connection.getConnection();
                    DatabaseMetaData dbMeta = conn.getMetaData();
                    ResultSet rs = dbMeta.getPrimaryKeys(null, null, words[2]);
                    String primaryKey = null;
                    while (rs.next()) {
                        primaryKey = rs.getString("COLUMN_NAME");
                    }
                    if (primaryKey != null && !primaryKey.isEmpty()) {
                        String sql = String.format("select * from %s order by %s desc limit 50", words[2], primaryKey);
                        List<Map<String, Object>> rows = connection.query(sql, null);
                        for (Map<String, Object> kv : rows) {
                            for (Entry<String, Object> e : kv.entrySet()) {
                            	e.getValue();//没用
                            }
                        }
                        model.addAttribute("rows", rows);
                    }
                }
            }
        }

        return "configure/util";
    }
}
