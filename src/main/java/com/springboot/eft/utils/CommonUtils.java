package com.springboot.eft.utils;

import com.springboot.eft.additional_utils.constant.ValueConstants;
import com.springboot.eft.modules.constant.DefaultValues;

public class CommonUtils {

	private CommonUtils() {
	}

	/**
	 * 将相对路径转换成绝对路径
	 *
	 * @param path 文件路径
	 * @return {@link String}
	 */
	public static String checkPath(String path) {
		String prefix = DefaultValues.COLON + ValueConstants.SEPARATOR;
		return path.startsWith(ValueConstants.SEPARATOR) || path.startsWith(prefix, ValueConstants.ONE_INT) ? path :
				DefaultValues.STORAGE_PATH + path;
	}

}
