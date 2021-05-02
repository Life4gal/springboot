package com.springboot.eft.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.eft.EftApplication;
import com.springboot.eft.additional_utils.Checker;
import com.springboot.eft.additional_utils.FileExecutor;
import com.springboot.eft.additional_utils.Formatter;
import com.springboot.eft.additional_utils.RandomUtils;
import com.springboot.eft.additional_utils.constant.ValueConstants;
import com.springboot.eft.modules.constant.ConfigConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;

public class TokenConfig {

	private static final Logger logger = LoggerFactory.getLogger(TokenConfig.class);

	public static String generateToken(String token, int userId) {
		if (Checker.isNotEmpty(token)) {
			EftApplication.tokens.remove(token);
		}
		return generateToken(userId);
	}

	public static String generateToken(int userId) {
		String token = RandomUtils.getRandomStringOnlyLetter(ValueConstants.THIRTY_TWO_INT);
		EftApplication.tokens.put(token, userId);
		saveToken();
		return token;
	}

	public static void saveToken() {
		String tokens = Formatter.mapToJson(EftApplication.tokens);
		try {
			FileExecutor.saveFile(SettingConfig.getStoragePath(ConfigConstants.TOKEN_OF_SETTINGS), tokens);
		} catch (Exception e) {
			logger.error("save token error： " + e.getMessage());
		}
	}

	public static Hashtable<String, Integer> loadToken() {
		Hashtable<String, Integer> tokens = new Hashtable<>(ValueConstants.SIXTEEN_INT);
		try {
			String token = FileExecutor.readFile(SettingConfig.getStoragePath(ConfigConstants.TOKEN_OF_SETTINGS));
			JSONArray array = JSON.parseArray(token);
			array.forEach(object -> {
				JSONObject jsonObject = (JSONObject) object;
				tokens.put(jsonObject.getString(ValueConstants.KEY_STRING), jsonObject.getInteger(ValueConstants
						.VALUE_STRING));
			});
		} catch (Exception e) {
			logger.error("load token error: " + e.getMessage());
		}
		return tokens;
	}

	public static void removeTokenByValue(int userId) {
		if (userId > 0) {
			String removeKey = "";
			for (String key : EftApplication.tokens.keySet()) {
				if (EftApplication.tokens.get(key) == userId) {
					removeKey = key;
					break;
				}
			}
			if (Checker.isNotEmpty(removeKey)) {
				EftApplication.tokens.remove(removeKey);
				TokenConfig.saveToken();
			}
		}
	}

}
