package com.springboot.eft.utils;

import javax.servlet.http.Cookie;

/**
 * @deprecated 请使用 {@link NetUtils}
 */
public class HttpUtils {

	private HttpUtils() {
	}

	/**
	 * 通过名称获取Cookie
	 *
	 * @param name    Cookie名
	 * @param cookies cookie数组
	 * @return {@link Cookie}
	 */
	public static Cookie getCookie(String name, Cookie[] cookies) {
		if (Checker.isNotNull(cookies)) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}
}
