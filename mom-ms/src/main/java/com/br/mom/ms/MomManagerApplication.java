package com.br.mom.ms;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableTransactionManagement
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages={"com.br.mom.ms","com.br.common.sys"})
@EnableCaching
@EnableScheduling
@MapperScan(basePackages = {"com.br.common.sys.**.dao","com.br.mom.ms.**.dao"})
public class MomManagerApplication extends WebMvcConfigurerAdapter {

	private static Logger logger = LoggerFactory.getLogger(MomManagerApplication.class);

	public static void main(String[] args) {
		logger.warn("mom manager system is starting ");
		SpringApplication.run(MomManagerApplication.class, args);
		logger.warn("mom manager system had bean started");
	}
}
