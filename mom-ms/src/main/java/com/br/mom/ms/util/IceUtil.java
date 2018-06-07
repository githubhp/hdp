package com.br.mom.ms.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.br.bsf.ext.app.util.BrBSFConsumerBean;
import com.br.common.util.DateUtils;
import com.br.ice.service.alarm.BrSendAlarmNewServicePrx;

public class IceUtil {

	public static BrSendAlarmNewServicePrx getBrSendAlarmNewServicePrx() {
		BrSendAlarmNewServicePrx service = (BrSendAlarmNewServicePrx) BrBSFConsumerBean
				.getServiceProxy(BrSendAlarmNewServicePrx.class);
		service.ice_invocationTimeout(1000);
		return service;

	}

	public static void sendAlarm(Exception e, String mailTitle, String message, String exceptionCode) {
//		String mailContent = "时间: " + DateUtils.format(new Date(), "HH:mm:ss") + "\n 异常内容：" + message + " \n 异常堆栈信息："
//				+ ExceptionMsgUtil.getExceptionMsg(e);
		StringBuffer mailContent = new StringBuffer();
		mailContent.append("时间:");
		mailContent.append(DateUtils.format(new Date(),"HH:mm:ss"));
		mailContent.append("\n 信息:");
		mailContent.append(message);
//		mailContent.append("\n 异常堆栈信息：");
//		mailContent.append(ExceptionMsgUtil.getExceptionMsg(e));
		Map<String, Object> alarmMap = new HashMap<String, Object>();
		alarmMap.put("exceptionCode", exceptionCode);
		alarmMap.put("mailContent", mailContent);
		alarmMap.put("mailTitle", "【mom_ms】" + mailTitle);
		alarmMap.put("msgContent", message);
		alarmMap.put("toUser", "lzq841819029");
		alarmMap.put("appType", "MOM_MS");
		String alarmString = JSONObject.toJSONString(alarmMap);
		String alarmResult = getBrSendAlarmNewServicePrx().sendAlarm(alarmString);
		System.out.println("alarmResult:" + alarmResult);

	}

	public static void main(String[] args) {
		try {
		} catch (Exception e) {
			IceUtil.sendAlarm(e, "报警测试", "messageContext", "45002");
		}
	}

}
