package com.br.mom.ms.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.br.common.sys.modules.sys.shiro.FormAuthenticationFilter;
import com.br.common.sys.modules.sys.shiro.SystemAuthorizingRealm;
import com.br.common.sys.modules.sys.shiro.session.CacheSessionDAO;
import com.br.common.sys.modules.sys.shiro.session.SessionManager;

//import org.apache.shiro.cache.CacheManager;
import net.sf.ehcache.CacheManager;

@Component
public class ShiroConfig {

	@Bean(name = "shiroFilterChainDefinitions")
	public String shiroFilterChainDefinitions(Environment environment, @Value("${adminPath}") String adminPath) {
		String string = "/static/** = anon\n";
		string += "/views/** = anon\n";
		string += adminPath + "/test/** = anon\n";
		string += adminPath + "/configure/** = anon\n";
		string += adminPath + "/basic = basic\n";
		string += adminPath + "/login = authc\n";
		string += adminPath + "/logout = logout\n";
		string += adminPath + "/** = user\n";
		string += "/ReportServer/** = user";
		return string;
	}

	@Bean(name = "basicHttpAuthenticationFilter")
	public BasicHttpAuthenticationFilter casFilter(@Value("${adminPath:/a}") String adminPath) {
		BasicHttpAuthenticationFilter basicHttpAuthenticationFilter = new BasicHttpAuthenticationFilter();
		basicHttpAuthenticationFilter.setLoginUrl(adminPath + "/login");
		return basicHttpAuthenticationFilter;
	}

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean(@Value("${adminPath:/a}") String adminPath,
			BasicHttpAuthenticationFilter basicHttpAuthenticationFilter,
			FormAuthenticationFilter formAuthenticationFilter, DefaultWebSecurityManager securityManager,
			@Qualifier("shiroFilterChainDefinitions") String shiroFilterChainDefinitions) {
		Map<String, Filter> filters = new HashMap<>();
		filters.put("basic", basicHttpAuthenticationFilter);
		filters.put("authc", formAuthenticationFilter);
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setFilters(filters);
		bean.setSecurityManager(securityManager);
		bean.setLoginUrl(adminPath + "/login");
		bean.setSuccessUrl(adminPath + "?login");
		bean.setFilterChainDefinitions(shiroFilterChainDefinitions);
		return bean;
	}

	@Bean
	public SimpleCookie rememberMeCookie() {
		// System.out.println("ShiroConfiguration.rememberMeCookie()");
		// 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		// <!-- 记住我cookie生效时间30天 ,单位秒;-->
		simpleCookie.setMaxAge(259200);
		return simpleCookie;
	}

	/**
	 * cookie管理对象;
	 * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
	 * 
	 * @return
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		// System.out.println("ShiroConfiguration.rememberMeManager()");
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		// rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
		cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
		return cookieRememberMeManager;
	}

	@Bean(name = "shiroCacheManager")
    public EhCacheManager shiroCacheManager(CacheManager manager) {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(manager);
        return ehCacheManager;
    }

    @Bean(name = "sessionManager")
    public SessionManager sessionManager(CacheSessionDAO dao) {
        SessionManager sessionManager = new SessionManager();
        sessionManager.setSessionDAO(dao);
        sessionManager.setGlobalSessionTimeout(86400000);
        sessionManager.setSessionValidationInterval(1800000);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookie(new SimpleCookie("speed.session.id"));
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(
            SystemAuthorizingRealm systemAuthorizingRealm,
            SessionManager sessionManager,
            EhCacheManager ehCacheManager,
            CookieRememberMeManager cookieRememberMeManager) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setSessionManager(sessionManager);
        defaultWebSecurityManager.setCacheManager(ehCacheManager);
        defaultWebSecurityManager.setRealm(systemAuthorizingRealm);
        defaultWebSecurityManager.setRememberMeManager(cookieRememberMeManager);
        return defaultWebSecurityManager;
    }

	// @Bean(name = "shiroCacheManager")
	// public CacheManager shiroCacheManager() {
	// JedisCacheManager jedisCacheManager = new JedisCacheManager();
	// return jedisCacheManager;
	// }
	//
	// @Bean(name = "sessionManager")
	// public SessionManager sessionManager(CacheSessionDAO dao) {
	// SessionManager sessionManager = new SessionManager();
	// sessionManager.setSessionDAO(dao);
	// sessionManager.setGlobalSessionTimeout(86400000);
	// sessionManager.setSessionValidationInterval(1800000);
	// sessionManager.setSessionValidationSchedulerEnabled(true);
	// sessionManager.setSessionIdCookie(new SimpleCookie("speed.session.id"));
	// sessionManager.setSessionIdCookieEnabled(true);
	// return sessionManager;
	// }
	//
	// @Bean(name = "securityManager")
	// public DefaultWebSecurityManager defaultWebSecurityManager(
	// SystemAuthorizingRealm systemAuthorizingRealm,
	// SessionManager sessionManager,
	// CacheManager cacheManager,
	// CookieRememberMeManager cookieRememberMeManager) {
	// DefaultWebSecurityManager defaultWebSecurityManager = new
	// DefaultWebSecurityManager();
	// defaultWebSecurityManager.setSessionManager(sessionManager);
	// defaultWebSecurityManager.setCacheManager(cacheManager);
	// defaultWebSecurityManager.setRealm(systemAuthorizingRealm);
	// defaultWebSecurityManager.setRememberMeManager(cookieRememberMeManager);
	// return defaultWebSecurityManager;
	// }

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			DefaultWebSecurityManager defaultWebSecurityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager);
		return authorizationAttributeSourceAdvisor;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		filterRegistration.addInitParameter("targetFilterLifecycle", "true");
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		return filterRegistration;
	}

	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

}
