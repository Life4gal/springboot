package com.springboot.eft.additional_utils.common.interceptor;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;

public class ToStringMethodInterceptor implements MethodInterceptor {

	private static final Logger logger = Logger.getLogger(ToStringMethodInterceptor.class);

	/**
	 * 此方法实现了cglib的动态代理
	 *
	 * @param o           {@link Object}
	 * @param method      {@link Method}
	 * @param objects     {@link Object[]}
	 * @param methodProxy {@link MethodProxy}
	 * @return {@link Object}
	 * @throws Throwable 异常
	 */
	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		return methodProxy.invokeSuper(o, objects);
	}
}
