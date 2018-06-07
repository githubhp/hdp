package com.br.mom.ms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.mom.ms.util.PropertiesUtil;

@RestController
@RequestMapping("/alarm")
public class AlarmController {
	private final static Logger logger = LoggerFactory.getLogger(AlarmController.class);
	public static volatile boolean flag = Boolean.parseBoolean(PropertiesUtil.getStringValue("mom.alarm.switch"));

	@RequestMapping("/stop")
	public String stopAlarm(HttpServletRequest request, HttpServletResponse response, String consumerName) {
		logger.info("begin stop alarm");
		AlarmController.flag = false;
		logger.info("alarm is stoped! flag = " + flag);
		return "alarm is stoped! flag = " + flag;
	}

	@RequestMapping("/start")
	public String openAlarm(HttpServletRequest request, HttpServletResponse response, String consumerName) {
		logger.info("begin start alarm");
		AlarmController.flag = true;
		logger.info("alarm is start! flag = " + flag);
		return "alarm is start! flag = " + flag;
	}
}
