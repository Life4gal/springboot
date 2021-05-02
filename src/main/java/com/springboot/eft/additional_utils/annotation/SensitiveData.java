package com.springboot.eft.additional_utils.annotation;

import java.lang.annotation.*;

/**
 * 此注解仅作用于 {@link String} 类型
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveData {
}
