package com.springboot.eft.additional_utils.annotation;

import com.springboot.eft.additional_utils.enums.FieldModifier;
import com.springboot.eft.additional_utils.enums.JsonMethod;
import com.springboot.eft.additional_utils.enums.JsonType;

import java.lang.annotation.*;

/**
 * 重写类的toString方法，返回一个JSON字符串
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ToJsonString {

	/**
	 * JSON字符串的格式
	 *
	 * @return {@link String}
	 */
	JsonType type() default JsonType.CONDENSED;

	/**
	 * 哪些修饰符的字段需要转换成JSON
	 *
	 * @return {@link FieldModifier}
	 */
	FieldModifier modifier() default FieldModifier.ALL;


	/**
	 * JSON转换的方式
	 *
	 * @return {@link JsonMethod}
	 */
	JsonMethod method() default JsonMethod.AUTO;
}
