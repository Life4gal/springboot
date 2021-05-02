package com.springboot.eft.additional_utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.springboot.eft.additional_utils.constant.ValueConstants;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	private static final String TIME_FORMAT = "HHmmss";

	private static final Calendar CALENDAR = Calendar.getInstance();

	private DateUtils() {
	}

	/**
	 * 解析时间字符串
	 *
	 * @param time 时间
	 * @return {@link Time}
	 */
	public static Time parseTime(String time) {
		Assert.isTrue(Checker.isNotEmpty(time), "time is required, but value is empty");
		String[] times = time.split(ValueConstants.COLON);
		String timeStr = formatTimeString(times[0]) + ":" + (times.length > 1 ? formatTimeString(times[1]) : "00") +
				":" + (times.length > 2 ? formatTimeString(times[2]) : "00");
		return new Time(DateUtil.parseTime(timeStr).getTime());
	}

	/**
	 * 格式化字符串
	 *
	 * @param time 时间
	 * @return {@link String}
	 */
	private static String formatTimeString(String time) {
		return time.length() > 0 ? (time.length() == 1 ? "0" : "") + time : "00";
	}

	/**
	 * 将日期转换成时间戳
	 *
	 * @param date {@link Date}
	 * @return {@link Timestamp}
	 */
	public static Timestamp convertDateToTimestamp(Date date) {
		return convertDateToTimestamp(date, getCurrentTimestamp());
	}

	/**
	 * 将日期转换成时间戳
	 *
	 * @param date      {@link Date}
	 * @param elseValue 日期为空返回默认时间戳
	 * @return {@link Timestamp}
	 */
	public static Timestamp convertDateToTimestamp(Date date, Timestamp elseValue) {
		return Checker.isNull(date) ? elseValue : new Timestamp(date.getTime());
	}

	/**
	 * 将日期转换成时间
	 *
	 * @param date {@link Date}
	 * @return {@link Time}
	 */
	public static Time convertDateToTime(Date date) {
		return convertDateToTime(date, new Time(System.currentTimeMillis()));
	}

	/**
	 * 将日期转换成时间
	 *
	 * @param date      {@link Date}
	 * @param elseValue 日期为空返回默认时间
	 * @return {@link Time}
	 */
	public static Time convertDateToTime(Date date, Time elseValue) {
		return Checker.isNull(date) ? elseValue : new Time(date.getTime());
	}

	/**
	 * 第一个值是否大于第二个值
	 *
	 * @param big   第一个值
	 * @param small 第二个值
	 * @return {@link Boolean}
	 */
	public static boolean greatThan(Time big, Time small) {
		return compare(big, small, CompareWay.GT);
	}

	/**
	 * 第一个值是否等于第二个值
	 *
	 * @param man  第一个值
	 * @param wife 第二个值
	 * @return {@link Boolean}
	 */
	public static boolean equals(Time man, Time wife) {
		return compare(man, wife, CompareWay.EQ);
	}

	/**
	 * 第一个值是否小于第二个值
	 *
	 * @param small 第一个值
	 * @param big   第二个值
	 * @return {@link Boolean}
	 */
	public static boolean lessThan(Time small, Time big) {
		return compare(small, big, CompareWay.LT);
	}

	/**
	 * 将第一个值和第二个值进行比较
	 *
	 * @param aValue     第一个值
	 * @param bValue     第二个值
	 * @param compareWay 比较方式
	 * @return {@link Boolean}
	 */
	public static boolean compare(Time aValue, Time bValue, CompareWay compareWay) {
		String value = Formatter.datetimeToCustomString(aValue, TIME_FORMAT);
		int result = value.compareTo(Formatter.datetimeToCustomString(bValue, TIME_FORMAT));
		return switch (compareWay) {
			case EQ -> result == 0;
			case GT -> result > 0;
			default -> result < 0;
		};
	}

	/**
	 * 获取当前年份
	 *
	 * @return 长度为4的{@link Integer}
	 */
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 获取当前月份
	 *
	 * @return 长度2的{@link Integer}
	 */
	public static int getCurrentMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取日期格式的号数
	 *
	 * @param date 日期格式的字符串
	 * @return 类型为字符串的数值
	 */
	public static String getDay(String date) {
		return date.split(" ")[0].substring(8);
	}

	/**
	 * 获取日期格式的号数
	 *
	 * @param date 日期
	 * @return 类型为字符串的数值
	 */
	public static String getDay(Date date) {
		return getDay(Formatter.dateToString(date));
	}

	/**
	 * 获取日期格式的月份
	 *
	 * @param date 日期格式的字符串
	 * @return 类型为字符串的数值
	 */
	public static String getMonth(String date) {
		return date.substring(5, 7);
	}

	/**
	 * 获取日期格式的月份
	 *
	 * @param date 日期
	 * @return 类型为字符串的数值
	 */
	public static String getMonth(Date date) {
		return getMonth(Formatter.dateToString(date));
	}

	/**
	 * 获取日期格式的年份
	 *
	 * @param date 日期格式的字符串
	 * @return 类型为字符串的数值
	 */
	public static String getYear(String date) {
		return date.substring(0, 4);
	}

	/**
	 * 获取日期格式的年份
	 *
	 * @param date 日期
	 * @return 类型为字符串的数值
	 */
	public static String getYear(Date date) {
		return getYear(Formatter.dateToString(date));
	}

	/**
	 * 获取当前时间戳
	 *
	 * @return {@link Timestamp}
	 */
	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 计算日期为星期几
	 *
	 * @param date 日期
	 * @return 格式为“星期？”的字符串
	 * @throws ParseException 异常
	 * @throws Exception      异常
	 */
	public static String getWeekAsChinese(String date) throws ParseException, Exception {
		return getWeekAsChinese(Formatter.stringToDate(date));
	}

	/**
	 * 计算日期为星期几
	 *
	 * @param date 日期
	 * @return 格式为“星期？”的字符串
	 * @throws Exception 异常
	 */
	public static String getWeekAsChinese(Date date) throws Exception {
		String result = switch (getWeek(date)) {
			case 1 -> "日";
			case 2 -> "一";
			case 3 -> "二";
			case 4 -> "三";
			case 5 -> "四";
			case 6 -> "五";
			case 7 -> "六";
			default -> throw new Exception("something error, get week failed");
		};
		return "星期" + result;
	}

	/**
	 * 计算日期为星期几
	 *
	 * @param date 日期
	 * @return 从星期天开始，星期天是1，依次类推
	 * @throws ParseException 异常
	 */
	public static int getWeek(String date) throws ParseException {
		return getWeek(Formatter.stringToDate(date));
	}

	/**
	 * 计算日期为星期几
	 *
	 * @param date 日期
	 * @return 从星期天开始，星期天是1，依次类推
	 */
	public static int getWeek(Date date) {
		CALENDAR.setTime(date);
		return CALENDAR.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 添加小时
	 *
	 * @param date   日期
	 * @param amount 数量
	 * @return 添加后的日期
	 * @throws ParseException 异常
	 */
	public static Date addHour(String date, int amount) throws ParseException {
		return add(date, Calendar.HOUR, amount);
	}

	/**
	 * 添加分钟
	 *
	 * @param date   日期
	 * @param amount 数量
	 * @return 添加后的日期
	 * @throws ParseException 异常
	 */
	public static Date addMinute(String date, int amount) throws ParseException {
		return add(date, Calendar.MINUTE, amount);
	}

	/**
	 * 添加秒
	 *
	 * @param date   日期
	 * @param amount 数量
	 * @return 添加后的日期
	 * @throws ParseException 异常
	 */
	public static Date addSecond(String date, int amount) throws ParseException {
		return add(date, Calendar.SECOND, amount);
	}

	/**
	 * 添加年份
	 *
	 * @param date   日期
	 * @param amount 数量
	 * @return 添加后的日期
	 * @throws ParseException 异常
	 */
	public static Date addYear(String date, int amount) throws ParseException {
		return add(date, Calendar.YEAR, amount);
	}

	/**
	 * 添加月份
	 *
	 * @param date   日期
	 * @param amount 数量
	 * @return 添加后的日期
	 * @throws ParseException 异常
	 */
	public static Date addMonth(String date, int amount) throws ParseException {
		return add(date, Calendar.MONTH, amount);
	}

	/**
	 * 添加日期
	 *
	 * @param date   日期
	 * @param amount 数量
	 * @return 添加后的日期
	 * @throws ParseException 异常
	 */
	public static Date addDay(String date, int amount) throws ParseException {
		return add(date, Calendar.DATE, amount);
	}

	/**
	 * 添加小时
	 *
	 * @param date   日期
	 * @param amount 数量
	 * @return 添加后的日期
	 */
	public static Date addHour(Date date, int amount) {
		return add(date, Calendar.HOUR, amount);
	}

	/**
	 * 添加分钟
	 *
	 * @param date   日期
	 * @param amount 数量
	 * @return 添加后的日期
	 */
	public static Date addMinute(Date date, int amount) {
		return add(date, Calendar.MINUTE, amount);
	}

	/**
	 * 添加秒
	 *
	 * @param date   日期
	 * @param amount 数量
	 * @return 添加后的日期
	 */
	public static Date addSecond(Date date, int amount) {
		return add(date, Calendar.SECOND, amount);
	}

	/**
	 * 添加年份
	 *
	 * @param date   日期
	 * @param amount 数量
	 * @return 添加后的日期
	 */
	public static Date addYear(Date date, int amount) {
		return add(date, Calendar.YEAR, amount);
	}

	/**
	 * 添加月份
	 *
	 * @param date   日期
	 * @param amount 数量
	 * @return 添加后的日期
	 */
	public static Date addMonth(Date date, int amount) {
		return add(date, Calendar.MONTH, amount);
	}

	/**
	 * 添加日期
	 *
	 * @param date   日期
	 * @param amount 数量
	 * @return 添加后的日期
	 */
	public static Date addDay(Date date, int amount) {
		return add(date, Calendar.DATE, amount);
	}

	/**
	 * 日期添加
	 *
	 * @param date   日期
	 * @param field  添加区域 {@link Calendar#DATE} {@link Calendar#MONTH} {@link Calendar#YEAR}
	 * @param amount 数量
	 * @return 添加后的日期
	 * @throws ParseException 异常
	 */
	public static Date add(String date, int field, int amount) throws ParseException {
		return add(Formatter.stringToDatetime(date), field, amount);
	}

	/**
	 * 日期添加
	 *
	 * @param date   日期
	 * @param field  添加区域 {@link Calendar#DATE} {@link Calendar#MONTH} {@link Calendar#YEAR}
	 * @param amount 数量
	 * @return 添加后的日期
	 */
	public static Date add(Date date, int field, int amount) {
		CALENDAR.setTime(date);
		CALENDAR.add(field, amount);
		return CALENDAR.getTime();
	}

	public enum CompareWay {
		/**
		 * 进行小于
		 */
		LT,

		/**
		 * 进行等于比较
		 */
		EQ,

		/**
		 * 进行大于比较
		 */
		GT
	}
}
