package com.springboot.eft.utils.office;

import com.springboot.eft.utils.Checker;
import com.springboot.eft.utils.ReflectUtils;
import org.apache.log4j.Logger;
import org.apache.poi.POIXMLDocument;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

public class MsUtils {

	private static final Logger logger = Logger.getLogger(MsUtils.class);

	private MsUtils() {
	}

	/**
	 * 保存Office文档
	 *
	 * @param object {@link POIXMLDocument} 对象
	 * @param path   输出路径
	 * @throws IOException               异常
	 * @throws NoSuchMethodException     异常
	 * @throws IllegalAccessException    异常
	 * @throws InvocationTargetException 异常
	 */
	public static void writeTo(Object object, String path) throws IOException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		OutputStream os = new FileOutputStream(path);
		ReflectUtils.invokeMethod(object, "write", new Class<?>[]{OutputStream.class}, new Object[]{os});
		os.close();
		logger.info("文件已输出：" + path);
	}

	static Integer checkInteger(Integer integer) {
		return Checker.isNull(integer) ? 0 : integer;
	}
}
