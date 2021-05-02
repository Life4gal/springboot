package com.springboot.eft.additional_utils;

import com.springboot.eft.additional_utils.annotation.AopLog;

import java.lang.reflect.Method;

public class AopLogUtils {

	private AopLogUtils() {
	}

	/**
	 * 获取 {@link AopLog}注解中的描述
	 *
	 * @param className  类名：joinPoint#getTarget#getClass#getName
	 * @param methodName 方法名：joinPoint#getSignature#getName
	 * @param args       参数数组：joinPoint#getArgs
	 * @return 描述
	 * @throws ClassNotFoundException 异常
	 */
	public static String getDescription(String className, String methodName, Object[] args) throws ClassNotFoundException {
		Class<?> targetClass = Class.forName(className);
		Method[] methods = targetClass.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class<?>[] clazz = method.getParameterTypes();
				if (clazz.length == args.length) {
					return method.getAnnotation(AopLog.class).value();
				}
			}
		}
		return "";
	}

	/**
	 * 获取 {@link AopLog}注解中的描述
	 *
	 * @param className  类名：joinPoint#getTarget#getClass#getName
	 * @param methodName 方法名：joinPoint#getSignature#getName
	 * @param args       参数数组：joinPoint#getArgs
	 * @return 描述
	 */
	public static String getDescriptionNoThrow(String className, String methodName, Object[] args) {
		try {
			return getDescription(className, methodName, args);
		} catch (ClassNotFoundException e) {
			LoggerUtils.error("get description from {}#{} error: {}", className, methodName, e.getMessage());
			return "";
		}
	}

}
