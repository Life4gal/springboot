package com.springboot.eft.config;

import com.springboot.eft.EftApplication;
import com.springboot.eft.additional_utils.Checker;
import com.springboot.eft.additional_utils.FileExecutor;
import com.springboot.eft.additional_utils.Formatter;
import com.springboot.eft.additional_utils.constant.ValueConstants;
import com.springboot.eft.modules.constant.ConfigConstants;
import com.springboot.eft.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static com.springboot.eft.EftApplication.settings;

public class SettingConfig {

	private static final String WINDOWS = "windows";

	private static final String MAC = "mac";

	private static final String LINUX = "linux";

	private static final Logger logger = LoggerFactory.getLogger(SettingConfig.class);

	private static final OsName currentOS;

	static {
		if (Checker.isWindows()) {
			currentOS = OsName.WINDOWS;
		} else if (Checker.isMacOS()) {
			currentOS = OsName.MAC;
		} else {
			currentOS = OsName.LINUX;
		}
	}

	public static int[] getAuth(String jsonPath) {
		int[] auth = new int[5];
		for (int i = 0; i < ConfigConstants.AUTH_OF_SETTINGS.length; i++) {
			String key = jsonPath + ValueConstants.DOT_SIGN + ConfigConstants.AUTH_OF_SETTINGS[i];
			auth[i] = settings.getBooleanUseEval(key) ? 1 : 0;
		}
		return auth;
	}

	public static String getUploadStoragePath() {
		String parent = getStoragePath(ConfigConstants.UPLOAD_PATH_OF_SETTING);
		String formatWay = EftApplication.settings.getStringUseEval(ConfigConstants.UPLOAD_FORM_OF_SETTING);
		String childPath = ValueConstants.SEPARATOR + Formatter.datetimeToCustomString(new Date(), formatWay);
		String path = parent + childPath;
		if (!FileExecutor.createFolder(path)) {
			path = ConfigConstants.DEFAULT_UPLOAD_PATH + childPath;
			FileExecutor.createFolder(path);
		}
		logger.info("upload path: " + path);
		return path;
	}

	public static String getAvatarStoragePath() {
		String path = getStoragePath(ConfigConstants.UPLOAD_PATH_OF_SETTING) + ValueConstants.SEPARATOR + "avatar";
		FileExecutor.createFolder(path);
		return path;
	}

	public static String getStoragePath(String path) {
		path += ValueConstants.DOT_SIGN;
		if (currentOS == OsName.WINDOWS) {
			path += WINDOWS;
		} else if (currentOS == OsName.MAC) {
			path += MAC;
		} else {
			path += LINUX;
		}
		return CommonUtils.checkPath(EftApplication.settings.getStringUseEval(path));
	}

	/**
	 * 当前系统名称
	 */
	public enum OsName {
		/**
		 * windows系统
		 */
		WINDOWS,

		/**
		 * MacOS系统
		 */
		MAC,

		/**
		 * Linux系统
		 */
		LINUX
	}

}
