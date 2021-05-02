package com.springboot.eft.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.springboot.eft.EftApplication;
import com.springboot.eft.modules.constant.ConfigConstants;
import com.springboot.eft.service.IConfigService;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl implements IConfigService {

	@Override
	public String getGlobalConfig() {
		JSONObject jsonObject = (JSONObject) EftApplication.settings.getObjectUseEval(ConfigConstants
				.GLOBAL_OF_SETTINGS).clone();
		jsonObject.remove(ConfigConstants.UPLOAD_PATH_OF_GLOBAL);
		jsonObject.remove(ConfigConstants.TOKEN_PATH_OF_GLOBAL);
		jsonObject.remove(ConfigConstants.UPLOAD_FORM_OF_SETTING);
		return jsonObject.toString();
	}

	@Override
	public String getUserConfig() {
		JSONObject jsonObject = (JSONObject) EftApplication.settings.getObjectUseEval(ConfigConstants.USER_OF_SETTINGS)
				.clone();
		jsonObject.remove(ConfigConstants.EMAIL_CONFIG_OF_USER);
		return jsonObject.toString();
	}
}
