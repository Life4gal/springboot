package com.springboot.eft.additional_utils.model;

public class CheckResult<T> {
	private static final int DEFAULT_ERROR_CODE = 400;
	private static final String DEFAULT_ERROR_MESSAGE = "参数输入错误";
	public boolean passed = true;
	public ResultObject resultObject = null;

	public CheckResult() {
	}

	public static ResultObject getErrorResult(int code, String message) {
		return new ResultObject(code, message, "error");
	}

	public static ResultObject getErrorResult() {
		return getErrorResult(DEFAULT_ERROR_CODE, "参数输入错误");
	}

	public static ResultObject getErrorResult(int code) {
		return getErrorResult(code, "参数输入错误");
	}

	public static ResultObject getErrorResult(String message) {
		return getErrorResult(DEFAULT_ERROR_CODE, message);
	}
}
