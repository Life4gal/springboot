package com.springboot.eft.web.constroller;

import com.alibaba.fastjson.JSONObject;
import com.springboot.eft.additional_utils.Checker;
import com.springboot.eft.additional_utils.constant.ValueConstants;
import com.springboot.eft.annotation.AuthInterceptor;
import com.springboot.eft.config.SettingConfig;
import com.springboot.eft.enums.InterceptorLevel;
import com.springboot.eft.modules.constant.DefaultValues;
import com.springboot.eft.service.ICommonService;
import com.springboot.eft.utils.ControllerUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/common")
@Api(value = "/common", description = "公共接口")
public class CommonController {

	private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

	private final ICommonService commonService;

	private final HttpServletRequest request;

	private final JSONObject jsonObject;

	@Autowired
	public CommonController(ICommonService commonService, HttpServletRequest request, JSONObject jsonObject) {
		this.commonService = commonService;
		this.request = request;
		this.jsonObject = jsonObject;
	}

	@ApiOperation(value = "获取头像资源")
	@AuthInterceptor(InterceptorLevel.NONE)
	@RequestMapping(value = "/avatar/{name}", method = RequestMethod.GET)
	public void getAvatar(HttpServletResponse response, @PathVariable("name") String name) throws IOException {
		String path = SettingConfig.getAvatarStoragePath() + ValueConstants.SEPARATOR + name;
		ControllerUtils.loadResource(response, path, ValueConstants.FALSE);
	}

	@ApiOperation(value = "上传头像")
	@ApiImplicitParam(name = "multipartFile", value = "头像", required = true)
	@AuthInterceptor(InterceptorLevel.USER)
	@RequestMapping(value = "/avatar", method = RequestMethod.POST)
	public String avatarUpload(@RequestParam("file") MultipartFile multipartFile) {
		String name = commonService.uploadAvatar(multipartFile);
		if (Checker.isEmpty(name)) {
			jsonObject.put("error", "文件格式不合法");
		} else {
			jsonObject.put("success", "/common/avatar/" + name);
		}
		return jsonObject.toString();
	}

	@ApiOperation(value = "发送验证码")
	@AuthInterceptor(InterceptorLevel.NONE)
	@RequestMapping(value = "/{email}/code", method = RequestMethod.POST)
	public String sendVerifyCode(@PathVariable("email") String email) {
		int code = commonService.sendVerifyCode(email);
		if (code > 0) {
			request.getSession().setAttribute(DefaultValues.CODE_STRING, code);
			logger.info("verify code: " + code);
			jsonObject.put("status", "success");
		} else {
			jsonObject.put("status", "error");
		}
		return jsonObject.toString();
	}

	@ApiOperation(value = "验证验证码是否正确")
	@AuthInterceptor(InterceptorLevel.NONE)
	@RequestMapping(value = "/{code}/verification", method = RequestMethod.PUT)
	public String verifyCode(@PathVariable("code") String code) {
		boolean isSuccess = Checker.checkNull(code).equals(String.valueOf(request.getSession().getAttribute
				(DefaultValues.CODE_STRING)));
		return ControllerUtils.getResponse(isSuccess);
	}
}
