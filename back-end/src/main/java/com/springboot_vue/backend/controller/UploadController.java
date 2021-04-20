package com.springboot_vue.backend.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.springboot_vue.backend.common.annotation.LogAnnotation;
import com.springboot_vue.backend.common.constant.ResultCode;
import com.springboot_vue.backend.common.result.Result;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);


	@Value("${upload.path}")
	private String baseFolderPath;

	@PostMapping("/upload")
	@RequiresAuthentication
	@LogAnnotation(module = "文件上传", operation = "文件上传")
	public Result upload(HttpServletRequest request, MultipartFile image) {

		Result r = new Result();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		StringBuffer url = new StringBuffer();
		String filePath = sdf.format(new Date());

		File baseFolder = new File(baseFolderPath + filePath);
		if (!baseFolder.exists()) {
			baseFolder.mkdirs();
		}

		url.append(request.getScheme())
				.append("://")
				.append(request.getServerName())
				.append(":")
				.append(request.getServerPort())
				.append(request.getContextPath())
				.append("/")
				.append(filePath);

		String imgName = UUID.randomUUID() + "_" + Objects.requireNonNull(image.getOriginalFilename()).replaceAll(" ", "");

		try {
			File dest = new File(baseFolder, imgName);
			image.transferTo(dest);

			url.append("/").append(imgName);

			r.setResultCode(ResultCode.SUCCESS);

			r.simple().put("url", url);

		} catch (IOException e) {
			logger.error("文件上传错误 , uri: {} , caused by: ", request.getRequestURI(), e);
			r.setResultCode(ResultCode.UPLOAD_ERROR);
		}

		return r;
	}

}
