package com.springboot_vue.backend.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.springboot_vue.backend.common.cache.RedisManager;
import com.springboot_vue.backend.oauth.SessionManager;

/**
 * Session超时，通知前端删除token
 */
public class ClearToken implements HandlerInterceptor {

	private static final String SESSION_TIME_OUT_K = "SESSION_TIME_OUT";
	private static final String SESSION_TIME_OUT_V = "timeout";

	@Autowired
	private RedisManager redisManager;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		String token = request.getHeader(SessionManager.OAUTH_TOKEN);

		if (null != token) {
			Session s = redisManager.get(token, Session.class);

			if (null == s || null == s.getId()) {
				response.setHeader(SESSION_TIME_OUT_K, SESSION_TIME_OUT_V);
			}
			return true;
		}

		return false;
	}

}
