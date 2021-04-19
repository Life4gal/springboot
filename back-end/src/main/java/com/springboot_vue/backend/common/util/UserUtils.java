package com.springboot_vue.backend.common.util;

import org.apache.shiro.SecurityUtils;

import com.springboot_vue.backend.entity.User;
import com.springboot_vue.backend.common.constant.Base;

public class UserUtils {

	public static User getCurrentUser() {
		return (User) SecurityUtils.getSubject().getSession().getAttribute(Base.CURRENT_USER);
	}

}
