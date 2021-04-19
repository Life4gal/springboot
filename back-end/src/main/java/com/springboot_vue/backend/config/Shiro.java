package com.springboot_vue.backend.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springboot_vue.backend.common.cache.RedisManager;
import com.springboot_vue.backend.oauth.Realm;
import com.springboot_vue.backend.oauth.SessionDAO;
import com.springboot_vue.backend.oauth.SessionManager;

@Configuration
public class Shiro {

	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

		filterChainDefinitionMap.put("/**", "anon");

		//返回json数据，由前端跳转
		shiroFilterFactoryBean.setLoginUrl("/handleLogin");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("md5");
		hashedCredentialsMatcher.setHashIterations(2);
		return hashedCredentialsMatcher;
	}

	@Bean
	public Realm oAuthRealm() {
		Realm myShiroRealm = new Realm();
		myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return myShiroRealm;
	}


	@Bean
	public SecurityManager securityManager(Realm oAuthRealm, SessionManager sessionManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(oAuthRealm);
		securityManager.setSessionManager(sessionManager);
		// 自定义缓存实现 使用redis
		//securityManager.setCacheManager(cacheManager());
		return securityManager;
	}

	@Bean
	public SessionManager sessionManager(SessionDAO authSessionDAO) {
		SessionManager oAuthSessionManager = new SessionManager();
		oAuthSessionManager.setSessionDAO(authSessionDAO);
		return oAuthSessionManager;
	}


	@Bean
	public SessionDAO authSessionDAO(RedisManager redisManager) {
		SessionDAO authSessionDAO = new SessionDAO();
		authSessionDAO.setRedisManager(redisManager);
		return authSessionDAO;
	}


	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

}
