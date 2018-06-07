package com.br.mom.ms.config;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import javax.servlet.MultipartConfigElement;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.br.common.sys.common.concurr.LogThread;
import com.br.common.sys.common.context.SpringContextHolder;
import com.br.common.sys.common.interceptor.LogInterceptor;

/**
 * Created by wolfking(赵伟伟)
 * Created on 2017/1/15 20:21
 * Mail zww199009@163.com
 */
@Component
public class SpringBootConfigure extends WebMvcConfigurerAdapter{
	
    @Autowired
    private LogInterceptor logInterceptor;
    @Autowired
    private LogThread logThread;
    
    @Value("${adminPath}")
    protected String adminPath;
    
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor).addPathPatterns("/**");
//        											.excludePathPatterns(adminPath+"/login");
        /*registry.addInterceptor(new CommonRoleInterceptor()).addPathPatterns("/monitor/**");
		registry.addInterceptor(new AdminRoleInterceptor())*/
//				.addPathPatterns(new String[] { "/configure/**", "/user_center/**", "/system/**" });
        super.addInterceptors(registry);
        logThread.start();
    }
    
    /**
	 * 拦截器配置
	 */

	/**
	 * 静态资源管理
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/WEB-INF/resources/css/");
		registry.addResourceHandler("/img/**").addResourceLocations("classpath:/WEB-INF/resources/img/");
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/WEB-INF/resources/js/");
	}
	
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }

    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }

    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
    
//    @Bean
//    public FilterRegistrationBean xssFilterRegistration() {
//        XssFilter xssFilter = new XssFilter();
//        xssFilter.setUrlExclusion(Arrays.asList("/notice/update", "/notice/add"));
//        FilterRegistrationBean registration = new FilterRegistrationBean(xssFilter);
//        registration.addUrlPatterns("/*");
//        return registration;
//    }
//    
    @Bean
    @Override
    public Validator getValidator() {
    	LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(SpringContextHolder.getBean(MessageSource.class));
        validatorFactoryBean.setProviderClass(HibernateValidator.class);
        return validatorFactoryBean;
    }
    
    @Bean(name = "messageSource")
    public static MessageSource LocalMessageSource() {
    	ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    	messageSource.addBasenames("classpath:/messages/message-info");
    	messageSource.addBasenames("classpath:/messages/sign-controller-info");
    	messageSource.addBasenames("classpath:/messages/sign-service-info");
    	messageSource.addBasenames("classpath:/messages/config-controller-info");
    	messageSource.addBasenames("classpath:/messages/app-service-info");
    	messageSource.addBasenames("classpath:/messages/app-controller-info");
    	messageSource.setUseCodeAsDefaultMessage(false);
    	return messageSource;
    }
    
    @Bean  
    public MultipartConfigElement multipartConfigElement() {  
        MultipartConfigFactory factory = new MultipartConfigFactory();  
        factory.setMaxFileSize("100MB");   
        factory.setMaxRequestSize("100MB");  
        return factory.createMultipartConfig();  
    }  
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:"+adminPath+"/login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    } 
    
    public static void main(String[] args) {
//		Properties p = new Properties();
//		try {
//			p.load(new InputStreamReader(new FileInputStream("src/main/resources/messages/message-info.properties"),"UTF-8"));
//			String sys = p.getProperty("syserror.paramtype");
//			System.out.println("sys error is "+sys);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}  
//    	ReloadableResourceBundleMessageSource p2 = LocalMessageSource();
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    	messageSource.addBasenames("classpath:/messages/message-info");
    	messageSource.addBasenames("classpath:/messages/sign-controller-info");
    	messageSource.addBasenames("classpath:/messages/sign-service-info");
    	messageSource.addBasenames("classpath:/messages/config-controller-info");
    	messageSource.addBasenames("classpath:/messages/app-service-info");
    	messageSource.addBasenames("classpath:/messages/app-controller-info");
    	messageSource.setUseCodeAsDefaultMessage(false);
//    	messageSource.setDefaultEncoding("zh_CN");
    	String mess = messageSource.getMessage("auth.access.denied", null,Locale.SIMPLIFIED_CHINESE);
    	System.out.println("sys error is "+mess);
	}
}
