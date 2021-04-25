package com.springboot.eft.utils.interfaces;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

public interface SimpleHutoolWatcher {

	/**
	 * 文件发生变化
	 *
	 * @param event       时间
	 * @param currentPath 当前路径
	 */
	default void onModify(WatchEvent<?> event, Path currentPath) {
	}

	/**
	 * 执行一些业务代码
	 */
	default void doSomething() {
	}
}
