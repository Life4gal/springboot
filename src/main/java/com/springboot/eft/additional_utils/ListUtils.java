package com.springboot.eft.additional_utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;

public class ListUtils {

	public static final int DEFAULT_LIST_CAPACITY = 10;

	private ListUtils() {
	}

	/**
	 * 生成一个 {@link ArrayList}
	 *
	 * @param capacity 初始化长度
	 * @param values   值数组
	 * @param <T>      值类型
	 * @return {@link ArrayList}
	 */
	@SafeVarargs
	public static <T> ArrayList<T> getArrayList(int capacity, T... values) {
		return new ArrayList<T>(capacity) {{
			addAll(Arrays.asList(values));
		}};
	}

	/**
	 * 生成一个 {@link ArrayList}
	 *
	 * @param values 值数组
	 * @param <T>    值类型
	 * @return {@link ArrayList}
	 */
	@SafeVarargs
	public static <T> ArrayList<T> getArrayList(T... values) {
		return getArrayList(DEFAULT_LIST_CAPACITY, values);
	}

	/**
	 * 生成一个 {@link LinkedList}
	 *
	 * @param values 值数组
	 * @param <T>    值类型
	 * @return {@link LinkedList}
	 */
	@SafeVarargs
	public static <T> LinkedList<T> getLinkedList(T... values) {
		return new LinkedList<T>() {{
			addAll(Arrays.asList(values));
		}};
	}

	/**
	 * 生成一个 {@link Vector}
	 *
	 * @param values   值数组
	 * @param capacity 初始化长度
	 * @param <T>      值类型
	 * @return {@link Vector}
	 */
	@SafeVarargs
	public static <T> Vector<T> getVector(int capacity, T... values) {
		return new Vector<T>() {{
			addAll(Arrays.asList(values));
		}};
	}

	/**
	 * 生成一个 {@link Vector}
	 *
	 * @param values 值数组
	 * @param <T>    值类型
	 * @return {@link Vector}
	 */
	@SafeVarargs
	public static <T> Vector<T> getVector(T... values) {
		return getVector(DEFAULT_LIST_CAPACITY, values);
	}
}
