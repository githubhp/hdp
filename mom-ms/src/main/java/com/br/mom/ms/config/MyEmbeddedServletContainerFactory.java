package com.br.mom.ms.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

@Component()  
public class MyEmbeddedServletContainerFactory extends TomcatEmbeddedServletContainerFactory {  
    public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers)  {  
        return super.getEmbeddedServletContainer(initializers);  
    }  
      
    protected void customizeConnector(Connector connector)  
    {  
    	connector.setMaxPostSize(64*1024*1000);
        super.customizeConnector(connector);  
        Http11NioProtocol protocol = (Http11NioProtocol)connector.getProtocolHandler();  
        //设置最大连接数  
        protocol.setMaxConnections(50);  
        //设置最大线程数  
        protocol.setMaxThreads(50);  
        protocol.setConnectionTimeout(30000);  
    }  
}  