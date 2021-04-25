package com.springboot.eft.utils;

import com.springboot.eft.constant.ValueConstants;

/**
 * @deprecated 请使用 {@link NetUtils}
 */
public class WebUtils {

	private WebUtils() {
	}

	/**
	 * 脚本过滤
	 *
	 * @param string {@link String}
	 * @return 过滤后的字符串
	 */
	public static String scriptFilter(String string) {
		return Checker.checkNull(string).replaceAll(ValueConstants.SCRIPT_FILTER_PATTERN, "");
	}
}
