package com.springboot.eft.additional_utils.enums;

/**
 * Bean类字段转换成JSON的方式
 */
public enum JsonMethod {

	/**
	 * 使用阿里巴巴的JSONObject
	 */
	AUTO,

	/**
	 * 使用手动的方式
	 *
	 * @deprecated 推荐使用 {@link JsonMethod#MANUAL}
	 */
	HANDLE,

	/**
	 * 手动的方式
	 */
	MANUAL
}
