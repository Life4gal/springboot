package com.springboot.eft.utils;

import com.springboot.eft.additional_utils.Checker;
import com.springboot.eft.additional_utils.ReflectUtils;
import com.springboot.eft.additional_utils.constant.ValueConstants;
import com.springboot.eft.service.ICategoryService;
import com.springboot.eft.service.IFileService;
import com.springboot.eft.service.IUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class ServiceUtils {

	private static final Logger logger = Logger.getLogger(ServiceUtils.class);
	private static IUserService userService;
	private static IFileService fileService;
	private static ICategoryService categoryService;

	@Autowired
	public ServiceUtils(IUserService userService, IFileService fileService, ICategoryService categoryService) {
		ServiceUtils.userService = userService;
		ServiceUtils.fileService = fileService;
		ServiceUtils.categoryService = categoryService;
	}

	public static int getUserId(String usernameOrEmail) {
		return Checker.isEmpty(usernameOrEmail) ? ValueConstants.ZERO_INT : userService.getUserId(usernameOrEmail);
	}

	public static long getFileId(String fileName) {
		return Checker.isEmpty(fileName) ? ValueConstants.ZERO_INT : fileService.getFileId(fileName);
	}

	public static int getCategoryId(String categoryName) {
		return Checker.isEmpty(categoryName) ? ValueConstants.ZERO_INT : categoryService.getIdByName(categoryName);
	}

	public static Object invokeFileFilter(Object object, String methodName, String user, String file, String
			category, int offset) {
		try {
			return ReflectUtils.invokeMethodUseBasicType(object, methodName, new Object[]{getUserId(user), getFileId
					(file), file, getCategoryId(category), offset});
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

}
