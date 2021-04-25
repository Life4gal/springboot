package com.springboot.eft.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.springboot.eft.constant.ValueConstants;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {

	/**
	 * 文件名匹配（不能包含非法字符）,忽略大小写
	 */
	public static final Pattern FILE_NAME_PATTERN = Pattern.compile("([^/\\\\:*\"<>|?]+\\.)*[^/\\\\:*\"<>|?]+(\\?.*)" +
			"?$", Pattern.CASE_INSENSITIVE);

	private static final char[] RMB_CHARACTERS = "零壹贰叁肆伍陆柒捌玖".toCharArray();

	private static final String[] RMB_INTEGER_UNIT = {"元", "万", "亿", "兆"};

	private static final String[] RMB_DECIMAL_UNIT = {"角", "分"};

	private static final String[] RMB_UNIT = {"", "拾", "佰", "仟"};

	/**
	 * 单位KB
	 */
	private static final String SIZE_KB = "KB";

	/**
	 * 单位MB
	 */
	private static final String SIZE_MB = "MB";

	/**
	 * 单位GB
	 */
	private static final String SIZE_GB = "GB";

	/**
	 * 单位TB
	 */
	private static final String SIZE_TB = "TB";

	/**
	 * yyyy-MM-dd HH:mm:ss格式
	 */
	private static final SimpleDateFormat DATE_WITH_LONG_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * yyyy-MM-dd格式
	 */
	private static final SimpleDateFormat DATE_WITHOUT_TIME = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * HH:mm:ss格式
	 */
	private static final SimpleDateFormat LONG_TIME = new SimpleDateFormat("HH:mm:ss");

	/**
	 * HH:mm格式
	 */
	private static final SimpleDateFormat SHORT_TIME = new SimpleDateFormat("HH:mm");

	private static final String STANDARD_FORMAT = "#0.00";

	private Formatter() {
	}

	/**
	 * 货币金额大写
	 *
	 * @param number 金额
	 * @return 大写字符串
	 */
	public static String toFinancialCharacter(double number) {
		final String zero = String.valueOf(ValueConstants.ZERO_INT);
		String[] array = formatDecimal(number).split("\\.");
		final int len = Math.min(array[0].length(), 16);
		StringBuilder leftUpper = new StringBuilder();
		StringBuilder rightUpper = new StringBuilder();
		int reverseIndex;
		for (int i = len - 1; i >= 0; i--) {
			reverseIndex = len - i - 1;
			int idx = array[0].charAt(i) - ValueConstants.FORTY_EIGHT;
			int remainder = reverseIndex % 4;
			int res = reverseIndex / 4;
			if (remainder == 0 && res > 0) {
				leftUpper.insert(ValueConstants.ZERO_INT, RMB_INTEGER_UNIT[res]);
			}
			leftUpper.insert(ValueConstants.ZERO_INT, idx > 0 ? RMB_CHARACTERS[idx] + RMB_UNIT[remainder] : idx);
		}
		array[1] = Utils.rightTrim(array[1], zero);
		String left = Utils.rightTrim(leftUpper.toString(), zero);
		if (Checker.isEmpty(array[1])) {
			rightUpper.append("整");
		} else {
			for (int i = 0; i < array[1].length(); i++) {
				int idx = array[1].charAt(i) - ValueConstants.FORTY_EIGHT;
				if (i == 0) {
					rightUpper.append(idx > 0 ? RMB_CHARACTERS[idx] + RMB_DECIMAL_UNIT[i] : zero);
				} else {
					rightUpper.append(RMB_CHARACTERS[idx]).append(RMB_DECIMAL_UNIT[i]);
				}
			}
		}
		String upper = Utils.leftTrim((Checker.isEmpty(left) ? (Checker.isEmpty(rightUpper.toString()) ? zero +
				RMB_INTEGER_UNIT[0] : "") : left + RMB_INTEGER_UNIT[0]) + rightUpper, zero).replaceAll("0+", "零");
		int unitLen = (len / RMB_INTEGER_UNIT.length) + 1;
		for (int i = 1; i < unitLen; i++) {
			for (int j = i + 1; j < unitLen; j++) {
				upper = upper.replace(RMB_INTEGER_UNIT[j] + RMB_CHARACTERS[0] + RMB_INTEGER_UNIT[i],
						RMB_INTEGER_UNIT[j]);
			}
			upper = upper.replace(RMB_CHARACTERS[0] + RMB_INTEGER_UNIT[i], RMB_INTEGER_UNIT[i] + RMB_CHARACTERS[0]);
			upper = upper.replace(RMB_INTEGER_UNIT[i] + RMB_CHARACTERS[0] + RMB_INTEGER_UNIT[0], RMB_INTEGER_UNIT[i]
					+ RMB_INTEGER_UNIT[0]);
		}
		return upper.replaceAll("零零", "零");
	}

	/**
	 * 格式化为货币字符串
	 *
	 * @param locale {@link Locale}，比如：{@link Locale#CHINA}
	 * @param number 数字
	 * @return 货币字符串
	 */
	public static String toCurrency(Locale locale, double number) {
		return NumberFormat.getCurrencyInstance(locale).format(number);
	}

	/**
	 * 格式化为货币字符串，默认 {@link Locale}为 {@link Locale#CHINA}
	 *
	 * @param number 数字
	 * @return 货币字符串
	 */
	public static String toCurrency(double number) {
		return toCurrency(Locale.CHINA, number);
	}

	/**
	 * 将字符串转换为INT型
	 *
	 * @param string 需要转换的字符串
	 * @return {@link Integer}
	 */
	public static int stringToInt(String string) {
		return stringToInteger(string);
	}

	/**
	 * 格式文件大小
	 *
	 * @param size 单位为B的文件大小
	 * @return 格式化为KB、MB、GB、TB的{@link String}
	 */
	public static String formatSize(long size) {
		if (size < ValueConstants.KB) {
			return size + " B";
		} else if (size < ValueConstants.MB) {
			return formatDecimal((double) size / ValueConstants.KB) + " KB";
		} else if (size < ValueConstants.GB) {
			return formatDecimal((double) size / ValueConstants.MB) + " MB";
		} else if (size < ValueConstants.TB) {
			return formatDecimal((double) size / ValueConstants.GB) + " GB";
		} else {
			return formatDecimal((double) size / ValueConstants.TB) + " TB";
		}
	}

	/**
	 * 将格式化后的大小转换成long型
	 *
	 * @param size 格式为 xx.xx B、xx.xx KB、xx.xx MB、xx.xx GB、xx.xx TB 的字符串
	 * @return 单位为B的{@link Long}
	 */
	public static long sizeToLong(String size) {
		size = size.trim();
		if (Checker.isNotEmpty(size)) {
			String num;
			if (size.contains(ValueConstants.SPACE)) {
				num = size.split(ValueConstants.SPACE)[0];
			} else {
				num = Utils.extractDigit(size);
			}
			double result;
			if (size.contains(SIZE_TB)) {
				result = stringToDouble(num) * ValueConstants.TB;
			} else if (size.contains(SIZE_GB)) {
				result = stringToDouble(num) * ValueConstants.GB;
			} else if (size.contains(SIZE_MB)) {
				result = stringToDouble(num) * ValueConstants.MB;
			} else if (size.contains(SIZE_KB)) {
				result = stringToDouble(num) * ValueConstants.KB;
			} else {
				result = stringToDouble(num);
			}
			return (long) result;
		}
		return -1;
	}

	/**
	 * 将String转换成Double
	 *
	 * @param string 需要转换为字符串
	 * @return 转换结果，如果数值非法，则返回-1
	 */
	public static double stringToDouble(String string) {
		string = string.trim();
		if (Checker.isDecimal(string)) {
			return Double.parseDouble(string);
		}
		return -1;
	}

	/**
	 * 将String转换成Long
	 *
	 * @param string 需要转换的字符串
	 * @return 转换结果，如果数值非法，则返回-1
	 */
	public static long stringToLong(String string) {
		string = string.trim();
		if (Checker.isNumber(string)) {
			return Long.parseLong(string);
		}
		return -1;
	}

	/**
	 * 自定义格式化数值
	 *
	 * @param number 需要格式化的数值
	 * @param format 格式，例如：#0.00
	 * @return {@link String}
	 */
	public static String customFormatDecimal(double number, String format) {
		return new DecimalFormat(format).format(number);
	}

	/**
	 * 使用默认方式格式化字符串，默认格式：#0.00
	 *
	 * @param number 需要格式化的数值
	 * @return {@link String}
	 */
	public static String formatDecimal(double number) {
		return formatDecimal(number, STANDARD_FORMAT);
	}

	/**
	 * 自定义格式化数值
	 * <br><br>主要格式参数说明：
	 * <p>
	 * <code>0</code> 数字前面补0
	 * <br><code>-</code> 左对齐
	 * <br><code>(</code> 将负数括在括号内
	 * <br><code>,</code> 添加分组 分隔符
	 * </p>
	 *
	 * @param number 需要格式化的数值
	 * @param format 格式，例如：#0.00
	 * @return {@link String}
	 */
	public static String formatDecimal(double number, String format) {
		return customFormatDecimal(number, format);
	}

	/**
	 * 将时间戳转换为字符串
	 *
	 * @param time 时间戳
	 * @return {@link String}
	 */
	public static synchronized String timeStampToString(long time) {
		return DATE_WITH_LONG_TIME.format(time);
	}

	/**
	 * 将Map转换成JSON，调用对象的toString方法
	 *
	 * @param map {@link Map}
	 * @return {@link String}
	 */
	public static String mapToJson(Map<?, ?> map) {
		return formatJson(JSONObject.toJSON(map).toString());
	}


	/**
	 * 将Map转换成JSON，调用对象的toString方法
	 *
	 * @param map {@link Map}
	 * @return {@link String}
	 */
	public static String mapToJsonArray(Map<?, ?> map) {
		JSONArray array = new JSONArray();
		if (Checker.isNotEmpty(map)) {
			map.forEach((key, value) -> {
				JSONObject object = new JSONObject();
				object.put("key", key);
				object.put("value", value);
				array.add(object);
			});
		}
		return formatJson(array.toString());
	}

	/**
	 * 将List转换成JSON，需要类重写toString方法，并返回一个JSON字符串
	 *
	 * @param list {@link List}
	 * @return {@link String}
	 */
	public static String listToJson(List<?> list) {
		JSONArray array = new JSONArray();
		if (Checker.isNotEmpty(list)) {
			array.addAll(list);
		}
		return formatJson(array.toString());
	}

	/**
	 * 格式化JSON
	 *
	 * @param string JSON格式的字符串
	 * @return {@link String}
	 */
	public static String formatJson(String string) {
		String json;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		json = gson.toJson(JsonParser.parseString(string));
		return json;
	}

	/**
	 * 转换为日期格式的字符串
	 *
	 * @param date 日期
	 * @return 只有日期的{@link String}
	 */
	public static synchronized String dateToString(Date date) {
		return DATE_WITHOUT_TIME.format(Checker.checkNull(date));
	}

	/**
	 * 转换为日期和长时间格式的字符串
	 *
	 * @param date 日期
	 * @return 日期+长时间的{@link String}
	 */
	public static synchronized String datetimeToString(Date date) {
		return DATE_WITH_LONG_TIME.format(Checker.checkNull(date));
	}

	/**
	 * 从文件路径中获取文件名
	 *
	 * @param string 文件路径
	 * @return {@link String}
	 * @throws UnsupportedEncodingException 异常
	 */
	public static String getFileName(String string) throws UnsupportedEncodingException {
		if (Checker.isNotEmpty(string)) {
			string = URLDecoder.decode(string, StandardCharsets.UTF_8);
			Matcher matcher = FILE_NAME_PATTERN.matcher(string);
			if (matcher.find() && Checker.isNotEmpty(matcher.group(0))) {
				String name = matcher.group(0).split("\\?")[0];
				if (Checker.isNotEmpty(name)) {
					return name;
				}
			}
		}
		return "undefined";
	}

	/**
	 * 将String转换成FLOAT
	 *
	 * @param string 需要转换的字符串
	 * @return 转换结果，如果数值非法，则返回-1
	 */
	public static float stringToFloat(String string) {
		string = string.trim();
		if (Checker.isDecimal(string)) {
			return Float.parseFloat(string);
		}
		return -1;
	}

	/**
	 * 将String转换成Integer
	 *
	 * @param string 需要转换的字符串
	 * @return 转换结果，如果数值非法，则返回-1
	 */
	public static int stringToInteger(String string) {
		string = string.trim();
		if (Checker.isNumber(string)) {
			return Integer.parseInt(string);
		}
		return -1;
	}

	/**
	 * 将String转换成Short
	 *
	 * @param string 需要转换的字符串
	 * @return 转换结果，如果数值非法，则返回-1
	 */
	public static short stringToShort(String string) {
		string = string.trim();
		if (Checker.isNumber(string)) {
			return Short.parseShort(string);
		}
		return -1;
	}

	/**
	 * 将字符串转换成没有时间日期
	 *
	 * @param date 日期格式的字符串
	 * @return 没有时间的日期
	 * @throws ParseException 异常
	 */
	public synchronized static Date stringToDate(String date) throws ParseException {
		return stringToCustomDateTime(DATE_WITHOUT_TIME, date);
	}

	/**
	 * 将字符串转换成没有日期的长时间
	 *
	 * @param time 时间格式的字符串
	 * @return 没有日期的长时间
	 * @throws ParseException 异常
	 */
	public synchronized static Date stringToLongTime(String time) throws ParseException {
		return stringToCustomDateTime(LONG_TIME, time);
	}

	/**
	 * 将字符串转换成没有日期的短时间
	 *
	 * @param time 时间格式的字符串
	 * @return 没有日期的短时间
	 * @throws ParseException 异常
	 */
	public synchronized static Date stringToShortTime(String time) throws ParseException {
		return stringToCustomDateTime(SHORT_TIME, time);
	}

	/**
	 * 将字符串转换成自定义格式的日期
	 *
	 * @param datetime  日期格式的字符串
	 * @param formatWay 自定义格式
	 * @return 自定义格式的日期
	 * @throws ParseException 异常
	 */
	public static Date stringToCustomDateTime(String datetime, String formatWay) throws ParseException {
		return stringToCustomDateTime(new SimpleDateFormat(formatWay), datetime);
	}

	/**
	 * 将字符串转换成包含时间日期
	 *
	 * @param datetime 日期格式的字符串
	 * @return 包含时间的日期
	 * @throws ParseException 异常
	 */
	public synchronized static Date stringToDatetime(String datetime) throws ParseException {
		return stringToCustomDateTime(DATE_WITH_LONG_TIME, datetime);
	}

	/**
	 * 将字符串转换成自定义格式的日期
	 *
	 * @param datetime 日期格式的字符串
	 * @param sdf      自定义的格式
	 * @return 自定义格式的日期
	 * @throws ParseException 异常
	 */
	public static Date stringToCustomDateTime(SimpleDateFormat sdf, String datetime) throws ParseException {
		return sdf.parse(datetime.trim());
	}

	/**
	 * 将日期转换成本地格式
	 *
	 * @param date 日期
	 * @return “yyyy年MM月dd日”格式的字符串
	 */
	public static String toLocalDate(Date date) {
		return datetimeToCustomString(new SimpleDateFormat("yyyy年MM月dd日"), date);
	}

	/**
	 * 将日期转换成长时间格式的字符串
	 *
	 * @param time 日期
	 * @return 长时间格式的字符串
	 */
	public synchronized static String longTimeToString(Date time) {
		return datetimeToCustomString(LONG_TIME, time);
	}

	/**
	 * 将日期转换成短时间格式的字符串
	 *
	 * @param time 日期
	 * @return 短时间格式的字符串
	 */
	public synchronized static String shortTimeToString(Date time) {
		return datetimeToCustomString(SHORT_TIME, time);
	}

	/**
	 * 将日期转换成自定义格式的字符串
	 *
	 * @param datetime  日期
	 * @param formatWay 自定义格式
	 * @return 自定义格式的字符串
	 */
	public static String datetimeToCustomString(Date datetime, String formatWay) {
		return datetimeToCustomString(new SimpleDateFormat(formatWay), datetime);
	}

	/**
	 * 将日期转换成自定义格式的字符串
	 *
	 * @param datetime 日期
	 * @param sdf      自定义格式
	 * @return 自定义格式的字符串
	 */
	public static String datetimeToCustomString(SimpleDateFormat sdf, Date datetime) {
		return Checker.isNull(datetime) ? "" : sdf.format(datetime);
	}

	/**
	 * 将整数格式化为指定长度的字符串
	 *
	 * @param data   整数
	 * @param length 长度
	 * @return {@link String}
	 */
	public static String numberFormat(long data, int length) {
		return String.format("%0" + length + "d", data);
	}

	/**
	 * 将java.time.LocalDate转换为java.util.Date
	 *
	 * @param localDate java.time.LocalDate
	 * @return java.util.Date
	 */
	public static Date localDateToDate(LocalDate localDate) {
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
		return Date.from(instant);
	}

	/**
	 * 将java.util.Date转换为java.time.LocalDate
	 *
	 * @param date java.util.Date
	 * @return java.time.LocalDate
	 */
	public static LocalDate dateToLocalDate(Date date) {
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		return LocalDateTime.ofInstant(instant, zone).toLocalDate();
	}
}
