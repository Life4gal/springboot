package com.springboot.eft.utils.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.springboot.eft.utils.Formatter;

/**
 * 继承自{@link JSONObject}
 */
public class PrettyJsonObject extends JSONObject {

	/**
	 * 重写toString方法，对JSON进行格式化
	 *
	 * @return {@link String}
	 */
	@Override
	public String toString() {
		try (SerializeWriter out = new SerializeWriter()) {
			new JSONSerializer(out).write(this);
			return Formatter.formatJson(out.toString());
		}
	}
}
