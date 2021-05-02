package com.springboot.eft.additional_utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.springboot.eft.additional_utils.annotation.ToJsonString;
import com.springboot.eft.additional_utils.constant.ValueConstants;
import com.springboot.eft.additional_utils.enums.FieldModifier;
import com.springboot.eft.additional_utils.enums.JsonMethod;
import com.springboot.eft.additional_utils.enums.JsonType;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BeanUtils {

	private static final JsonMethod[] METHODS = new JsonMethod[]{JsonMethod.MANUAL, JsonMethod.HANDLE};

	private BeanUtils() {
	}

	/**
	 * 将一个Bean的数据装换到另外一个（需实现setter和getter）
	 *
	 * @param object  一个Bean
	 * @param another 另外一个Bean对象
	 * @param <T>     另外Bean类型
	 * @return {@link T}
	 * @throws IllegalAccessException    异常
	 * @throws InvocationTargetException 异常
	 */
	public static <T> T bean2Another(Object object, T another) throws InvocationTargetException,
			IllegalAccessException {
		return bean2Another(object, another.getClass(), another);
	}

	/**
	 * 将一个Bean的数据装换到另外一个（需实现setter和getter，以及一个默认无参构造函数）
	 *
	 * @param object 一个Bean
	 * @param clazz  另外一个Bean
	 * @param <T>    另外Bean类型
	 * @return {@link T}
	 * @throws InstantiationException    异常
	 * @throws IllegalAccessException    异常
	 * @throws InvocationTargetException 异常
	 * @throws NoSuchMethodException     异常
	 */
	public static <T> T bean2Another(Object object, Class<T> clazz) throws IllegalAccessException,
			InstantiationException, InvocationTargetException, NoSuchMethodException {
		return bean2Another(object, clazz, clazz.getDeclaredConstructor().newInstance());
	}

	/**
	 * 将一个Bean的数据装换到另外一个（需实现setter和getter）
	 *
	 * @param object  一个Bean
	 * @param clazz   另外一个Bean类
	 * @param another 另一个Bean对象
	 * @param <T>     另外Bean类型
	 * @return {@link T}
	 * @throws IllegalAccessException    异常
	 * @throws InvocationTargetException 异常
	 * @throws InstantiationException    异常
	 * @throws NoSuchMethodException     异常
	 */
	public static <T> T bean2Another(Object object, T another, Class<T> clazz) throws IllegalAccessException,
			InstantiationException, InvocationTargetException, NoSuchMethodException {
		if (Checker.isNull(another)) {
			another = clazz.getDeclaredConstructor().newInstance();
		}
		return bean2Another(object, clazz, another);
	}

	/**
	 * 将一个Bean的数据装换到另外一个（需实现setter和getter）
	 *
	 * @param object  一个Bean
	 * @param clazz   另外一个Bean类
	 * @param another 另一个Bean对象
	 * @param <T>     另外Bean类型
	 * @return {@link T}
	 * @throws IllegalAccessException    异常
	 * @throws InvocationTargetException 异常
	 */
	private static <T> T bean2Another(Object object, Class<?> clazz, T another) throws InvocationTargetException,
			IllegalAccessException {
		Method[] methods = object.getClass().getMethods();
		Map<String, Method> clazzMethods = ReflectUtils.getMethodMap(clazz, "set");
		for (Method method : methods) {
			String name = method.getName();
			if (name.startsWith("get") && !"getClass".equals(name)) {
				String clazzMethod = "s" + name.substring(1);
				if (clazzMethods.containsKey(clazzMethod)) {
					Object value = method.invoke(object);
					if (Checker.isNotNull(value)) {
						clazzMethods.get(clazzMethod).invoke(another, value);
					}
				}
			}
		}
		return another;

	}

	/**
	 * 反序列化对象
	 *
	 * @param file  对象文件
	 * @param clazz 类
	 * @param <T>   类型
	 * @return 反序列化后的对象
	 * @throws IOException            异常
	 * @throws ClassNotFoundException 异常
	 */
	public static <T> T deserialize(String file, Class<T> clazz) throws IOException, ClassNotFoundException {
		return TypeUtils.castToJavaBean(deserialize(file), clazz);
	}

	/**
	 * 反序列化对象
	 *
	 * @param file 对象文件
	 * @return 反序列化后的对象
	 * @throws IOException            异常
	 * @throws ClassNotFoundException 异常
	 */
	public static Object deserialize(String file) throws IOException, ClassNotFoundException {
		Object object;
		try (FileInputStream fileIn = new FileInputStream(file); ObjectInputStream in = new ObjectInputStream(fileIn)) {
			object = in.readObject();
		}
		return object;
	}

	/**
	 * 序列化对象
	 *
	 * @param object 实现了 {@link Serializable} 接口的对象
	 * @param file   保存到指定文件
	 * @throws Exception 异常
	 */
	public static void serialize(Object object, String file) throws Exception {
		if (object instanceof Serializable) {
			try (FileOutputStream fileOut = new FileOutputStream(file); ObjectOutputStream out =
					new ObjectOutputStream(fileOut)) {
				out.writeObject(object);
			}
		} else {
			throw new Exception(object.getClass().getName() + " doesn't implements serializable interface");
		}
	}

	/**
	 * 将 Java Bean 类转换成 {@link JSONObject}
	 *
	 * @param object 类对象
	 * @return {@link JSONObject}
	 */
	public static JSONObject toJsonObject(Object object) {
		return (JSONObject) JSONObject.toJSON(object);
	}

	/**
	 * 将类属性装换成JSON（只能转换有get和is方法的）
	 *
	 * @param object 转换的对象
	 * @return {@link JSONObject}
	 * @throws IllegalAccessException    异常
	 * @throws InvocationTargetException 异常
	 */
	public static JSONObject beanToJson(Object object) throws IllegalAccessException, InvocationTargetException {
		Method[] methods = object.getClass().getMethods();
		JSONObject jsonObject = new JSONObject();
		for (Method method : methods) {
			String name = method.getName();
			if (name.startsWith("get") && !"getClass".equals(name)) {
				name = name.substring(3);
				jsonObject.put(name.substring(0, 1).toLowerCase() + name.substring(1), method.invoke(object));
			} else if (name.startsWith("is")) {
				jsonObject.put(name, method.invoke(object));
			}
		}
		return jsonObject;
	}

	/**
	 * 将JOSNObject转换为Bean
	 *
	 * @param jsonObject {@link JSONObject}
	 * @param object     {@link Object}
	 * @throws IllegalAccessException 异常
	 * @deprecated 请使用 {@link JSONObject#toJavaObject(JSON, Class)}
	 */
	public static void jsonPutIn(JSONObject jsonObject, Object object) throws IllegalAccessException {
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			field.set(object, TypeUtils.castToJavaBean(jsonObject.get(field.getName()), field.getType()));
		}
	}

	/**
	 * 将Bean类的全部属性转换成JSON字符串
	 *
	 * @param object Bean对象
	 * @return 格式化的JSON字符串
	 * @throws IllegalAccessException 异常
	 */
	public static String toPrettyJson(Object object) throws IllegalAccessException {
		return toPrettyJson(object, FieldModifier.ALL);
	}

	/**
	 * 将Bean类指定修饰符的属性转换成JSON字符串
	 *
	 * @param object   Bean对象
	 * @param modifier 属性的权限修饰符
	 * @return 格式化的JSON字符串
	 * @throws IllegalAccessException 异常
	 */
	public static String toPrettyJson(Object object, FieldModifier modifier) throws IllegalAccessException {
		return Formatter.formatJson(toJsonString(object, modifier));
	}

	/**
	 * 将Bean类的全部属性转换成JSON字符串
	 *
	 * @param object Bean对象
	 * @return 没有格式化的JSON字符串
	 * @throws IllegalAccessException 异常
	 */
	public static String toJsonString(Object object) throws IllegalAccessException {
		return toJsonString(object, FieldModifier.ALL);
	}

	/**
	 * 将Bean类指定修饰符的属性转换成JSON字符串
	 *
	 * @param object   Bean对象
	 * @param modifier 属性的权限修饰符
	 * @return 没有格式化的JSON字符串
	 * @throws IllegalAccessException 异常
	 */
	public static String toJsonString(Object object, FieldModifier modifier) throws IllegalAccessException {
		return toJsonString(object, modifier, JsonMethod.AUTO);
	}

	/**
	 * 将Bean类指定修饰符的属性转换成JSON字符串
	 *
	 * @param object   Bean对象
	 * @param modifier 属性的权限修饰符
	 * @param method   {@link JsonMethod}
	 * @return 没有格式化的JSON字符串
	 * @throws IllegalAccessException 异常
	 */
	public static String toJsonString(Object object, FieldModifier modifier, JsonMethod method) throws IllegalAccessException {
		JSONObject jsonObject = new JSONObject();
		StringBuilder builder = new StringBuilder("{");
		boolean isManual = false;
		if (Checker.isNotNull(object)) {
			Class<?> bean = object.getClass();
			Field[] fields = bean.getDeclaredFields();
			for (Field field : fields) {
				int mod = field.getModifiers();
				boolean addable =
						modifier == FieldModifier.ALL || (modifier == FieldModifier.PRIVATE && Modifier.isPrivate(mod)) || (modifier == FieldModifier.PUBLIC && Modifier.isPublic(mod));
				if (addable) {
					field.setAccessible(true);
					isManual = Checker.isIn(method, METHODS);
					if (isManual) {
						Object f = field.get(object);
						if (Checker.isNotNull(f)) {
							builder.append(converter(field.getName(), f));
						}
					} else {
						jsonObject.put(field.getName(), field.get(object));
					}
				}
			}
		}
		return isManual ? builder.substring(0, builder.length() - 1) + "}" : jsonObject.toString();
	}

	/**
	 * 通过注解将Bean转换为JSON
	 *
	 * @param object Bean对象
	 * @return {@link String}
	 * @throws IllegalAccessException 异常
	 */
	public static String toJsonStringByAnnotation(Object object) throws IllegalAccessException {
		JsonType jsonType = JsonType.CONDENSED;
		FieldModifier modifier = FieldModifier.ALL;
		JsonMethod method = JsonMethod.AUTO;
		if (Checker.isNotNull(object)) {
			Class<?> bean = object.getClass();
			if (bean.isAnnotationPresent(ToJsonString.class)) {
				ToJsonString annotation = bean.getAnnotation(ToJsonString.class);
				jsonType = annotation.type();
				modifier = annotation.modifier();
				method = annotation.method();
			}
		}
		String json = toJsonString(object, modifier, method);
		return jsonType == JsonType.PRETTY ? Formatter.formatJson(json) : json;
	}

	/**
	 * 手动转换 {@link List} 和 {@link Map}
	 *
	 * @param fieldName 字段名
	 * @param object    对象
	 * @return json对象
	 */
	private static String converter(String fieldName, Object object) {
		StringBuilder builder = new StringBuilder();
		if (Checker.isNotEmpty(fieldName)) {
			builder.append("\"").append(fieldName).append("\":");
		}
		if (object instanceof Collection<?> list) {
			builder.append("[");
			list.forEach(obj -> builder.append(converter(ValueConstants.EMPTY_STRING, obj)));
			return builder.substring(0, builder.length() - 1) + "],";
		} else if (object instanceof Map<?, ?> map) {
			builder.append("{");
			map.forEach((k, v) -> builder.append(converter(k.toString(), v)));
			return builder.substring(0, builder.length() - 1) + "},";
		} else if (Checker.isEmpty(fieldName)) {
			builder.append("\"").append(object).append("\",");
		} else {
			builder.append("\"").append(object).append("\",");
		}
		return builder.toString();
	}
}

