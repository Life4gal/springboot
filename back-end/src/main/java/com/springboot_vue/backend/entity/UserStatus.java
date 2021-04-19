package com.springboot_vue.backend.entity;

public enum UserStatus {

	normal("正常状态"),
	blocked("注销状态");

	private final String info;

	UserStatus(String info) {
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

}
