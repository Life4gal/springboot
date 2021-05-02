package com.springboot.eft.additional_utils.model;

import com.alibaba.fastjson.JSONObject;

public class ResultObject<T> {
	public int code = 200;
	public String message = "验证通过";
	public String status = "success";
	public T data = null;

	public ResultObject() {
	}

	public ResultObject(int code) {
		this.code = code;
	}

	public ResultObject(String message) {
		this.message = message;
	}

	public ResultObject(T data) {
		this.data = data;
	}

	public ResultObject(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public ResultObject(int code, T data) {
		this.code = code;
		this.data = data;
	}

	public ResultObject(String message, T data) {
		this.message = message;
		this.data = data;
	}

	public ResultObject(int code, String message, String status) {
		this.message = message;
		this.code = code;
		this.status = status;
	}

	public ResultObject(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public ResultObject(int code, String message, String status, T data) {
		this.data = data;
		this.code = code;
		this.message = message;
		this.status = status;
	}

	public ResultObject<T> setCode(int code) {
		this.code = code;
		return this;
	}

	public ResultObject<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public ResultObject<T> setStatus(String status) {
		this.status = status;
		return this;
	}

	public ResultObject<T> setData(T data) {
		this.data = data;
		return this;
	}

	public ResultObject<T> copyFrom(ResultObject<T> resultObject) {
		this.message = resultObject.message;
		this.status = resultObject.status;
		this.code = resultObject.code;
		return this;
	}

	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
