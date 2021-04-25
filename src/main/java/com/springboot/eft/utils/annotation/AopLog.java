package com.springboot.eft.utils.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AopLog {
	/**
	 * the description of method
	 *
	 * @return {@link String}
	 */
	String value() default "";
}
