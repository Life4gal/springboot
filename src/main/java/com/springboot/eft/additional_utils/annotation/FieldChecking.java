package com.springboot.eft.additional_utils.annotation;

import com.springboot.eft.additional_utils.constant.ValueConstants;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface FieldChecking {

	/**
	 * 校验码，默认验证失败
	 *
	 * @return {@link Integer}
	 */
	int code() default 104;

	/**
	 * 提示消息
	 *
	 * @return {@link String}
	 */
	String message() default "{} is required or incorrect format";

	/**
	 * 验证状态
	 *
	 * @return {@link String}
	 */
	String status() default ValueConstants.ERROR_EN;

	/**
	 * 自定义验证表达式（用val代替字段值），支持正则匹配（使用英文冒号:开头），验证返回true时isPassed返回true，验证返回false时isPassed返回false
	 *
	 * @return {@link String}
	 */
	String expression() default "";

	/**
	 * 默认值（仅针对字段值为NULL时）
	 *
	 * @return {@link String}
	 */
	String defaultValue() default "";
}
