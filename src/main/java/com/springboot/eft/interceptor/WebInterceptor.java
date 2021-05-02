package com.springboot.eft.interceptor;

import com.springboot.eft.EftApplication;
import com.springboot.eft.additional_utils.Checker;
import com.springboot.eft.additional_utils.NetUtils;
import com.springboot.eft.additional_utils.constant.ValueConstants;
import com.springboot.eft.annotation.AuthInterceptor;
import com.springboot.eft.entity.User;
import com.springboot.eft.enums.InterceptorLevel;
import com.springboot.eft.modules.constant.DefaultValues;
import com.springboot.eft.service.impl.UserServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebInterceptor implements HandlerInterceptor {

	@Autowired
	UserServiceImpl userService;

	@Override
	public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws
			Exception {
		String url = request.getServletPath();
		InterceptorLevel level = InterceptorLevel.NONE;
		if (handler instanceof HandlerMethod) {
			AuthInterceptor interceptor = ((HandlerMethod) handler).getMethodAnnotation(AuthInterceptor.class);
			//注解到类上面的注解，无法直接获取，只能通过扫描
			if (Checker.isNull(interceptor)) {
				for (Class<?> type : EftApplication.controllers) {
					RequestMapping mapping = type.getAnnotation(RequestMapping.class);
					if (Checker.isNotNull(mapping)) {
						for (String path : mapping.value()) {
							if (url.startsWith(path)) {
								interceptor = type.getAnnotation(AuthInterceptor.class);
								break;
							}
						}
						break;
					}
				}
			}
			if (Checker.isNotNull(interceptor)) {
				level = interceptor.value();
			}
		}
		User user = (User) request.getSession().getAttribute(ValueConstants.USER_STRING);
		if (Checker.isNull(user)) {
			//读取token，自动登录
			Cookie cookie = NetUtils.getCookie(ValueConstants.TOKEN_STRING, request.getCookies());
			if (Checker.isNotNull(cookie)) {
				user = userService.login(ValueConstants.EMPTY_STRING, ValueConstants.EMPTY_STRING, cookie.getValue(),
						response);
				if (Checker.isNotNull(user)) {
					request.getSession().setAttribute(ValueConstants.USER_STRING, user);
				}
			}
		}
		if (level != InterceptorLevel.NONE) {
			boolean isRedirect = Checker.isNull(user) || (level == InterceptorLevel.ADMIN && user.getPermission() <
					2) || (level == InterceptorLevel.SYSTEM && user.getPermission() < 3);
			if (isRedirect) {
				response.sendRedirect(DefaultValues.SIGNIN_PAGE);
				return false;
			}
		}
		return true;
	}

}
