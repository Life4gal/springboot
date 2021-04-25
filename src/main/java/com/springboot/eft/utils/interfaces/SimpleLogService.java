package com.springboot.eft.utils.interfaces;

import java.util.List;
import java.util.Map;

public interface SimpleLogService<T> {

	/**
	 * 保存日志
	 *
	 * @param log {@link T}
	 * @return {@link T}
	 */
	T save(T log);

	/**
	 * 保存异常信息
	 *
	 * @param log             {@link T}
	 * @param exceptionClass  异常类
	 * @param exceptionDetail 异常详情
	 * @return {@link T}
	 */
	T saveException(T log, String exceptionClass, String exceptionDetail);

	/**
	 * 清除过期的日志
	 */
	default void removeExpired() {
	}

	/**
	 * 查询日志
	 *
	 * @param criteria 查询日志
	 * @return 日志列表
	 */
	default List<T> listBy(Map<String, Object> criteria) {
		return null;
	}
}
