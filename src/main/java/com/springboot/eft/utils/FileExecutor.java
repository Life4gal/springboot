package com.springboot.eft.utils;

import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.io.watch.WatchMonitor;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.eft.constant.ValueConstants;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import com.springboot.eft.utils.interfaces.SimpleHutoolWatcher;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileExecutor extends FileUtils {

	private static final Logger logger = Logger.getLogger(FileExecutor.class);

	private FileExecutor() {
	}

	/**
	 * 监听文件
	 *
	 * @param file    待监听的文件
	 * @param watcher {@link SimpleHutoolWatcher}
	 */
	public static void watchFile(String file, SimpleHutoolWatcher watcher) {
		watchFile(file, watcher, false);
	}

	/**
	 * 监听文件
	 *
	 * @param file               待监听的文件
	 * @param watcher            {@link SimpleHutoolWatcher}
	 * @param shouldFirstExecute 第一次是否执行 {@link SimpleHutoolWatcher#doSomething()}
	 */
	public static void watchFile(String file, SimpleHutoolWatcher watcher, boolean shouldFirstExecute) {
		if (shouldFirstExecute) {
			watcher.doSomething();
		}
		SimpleWatcher simpleWatcher = new SimpleWatcher() {
			@Override
			public void onModify(WatchEvent<?> event, Path currentPath) {
				watcher.onModify(event, currentPath);
				watcher.doSomething();
			}
		};
		try (WatchMonitor monitor = WatchMonitor.createAll(file, simpleWatcher)) {
			monitor.start();
		}
	}

	/**
	 * 解析JSON
	 *
	 * @param url {@link URL}
	 * @return {@link JSONArray}
	 * @throws IOException
	 */
	public static JSONArray parseJsonArray(URL url) throws IOException {
		String path = url.toString();
		if (path.startsWith(ValueConstants.LOCAL_FILE_URL)) {
			return parseJsonArray(NetUtils.urlToString(url));
		} else {
			return JSONArray.parseArray(read(url));
		}
	}

	/**
	 * 解析JSON
	 *
	 * @param file 文件路径
	 * @return {@link JSONArray}
	 * @throws IOException
	 */
	public static JSONArray parseJsonArray(String file) throws IOException {
		return parseJsonArray(new File(file));
	}

	/**
	 * 解析JSON
	 *
	 * @param file {@link File}
	 * @return {@link JSONArray}
	 * @throws IOException
	 */
	public static JSONArray parseJsonArray(File file) throws IOException {
		return JSONArray.parseArray(readFile(file));
	}

	/**
	 * 解析JSON
	 *
	 * @param url {@link URL}
	 * @return {@link JSONObject}
	 * @throws IOException
	 */
	public static JSONObject parseJsonObject(URL url) throws IOException {
		String path = url.toString();
		if (path.startsWith(ValueConstants.LOCAL_FILE_URL)) {
			return parseJsonObject(NetUtils.urlToString(url));
		} else {
			return JSONObject.parseObject(read(url));
		}
	}

	/**
	 * 解析JSON
	 *
	 * @param file 文件路径
	 * @return {@link JSONObject}
	 * @throws IOException
	 */
	public static JSONObject parseJsonObject(String file) throws IOException {
		return parseJsonObject(new File(file));
	}

	/**
	 * 解析JSON
	 *
	 * @param file {@link File}
	 * @return {@link JSONObject}
	 * @throws IOException
	 */
	public static JSONObject parseJsonObject(File file) throws IOException {
		return JSONObject.parseObject(readFile(file));
	}

	/**
	 * 重命名文件，自动创建不存在的文件夹
	 *
	 * @param sourceFile 源文件
	 * @param destFile   目的文件
	 */
	public static void renameTo(String sourceFile, String destFile) {
		renameTo(new File(sourceFile), new File(destFile));
	}

	/**
	 * 重命名文件，自动创建不存在的文件夹
	 *
	 * @param sourceFile 源文件
	 * @param destFile   目的文件
	 */
	public static void renameTo(File sourceFile, File destFile) {
		if (sourceFile.exists() && !destFile.exists()) {
			createFolder(destFile.getParent());
			sourceFile.renameTo(destFile);
		}
	}

	/**
	 * 列出文件夹所有文件，不递归
	 *
	 * @param director 文件夹
	 * @return 所有文件
	 */
	public static File[] listFile(String director) {
		return listFile(new File(director));
	}

	/**
	 * 列出文件夹所有文件，不递归
	 *
	 * @param director 文件夹
	 * @return 所有文件
	 */
	public static File[] listFile(File director) {
		String basePath = director.getAbsolutePath() + ValueConstants.SEPARATOR;
		String[] filePath = director.list();
		File[] files = null;
		if (Checker.isNotNull(filePath)) {
			files = new File[filePath.length];
			int i = 0;
			for (String s : filePath) {
				files[i++] = new File(basePath + s);
			}
		}
		return files;
	}

	/**
	 * 获取文件后缀（包括点号）
	 *
	 * @param fileName 文件名
	 * @return 后缀
	 */
	public static String getSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf(ValueConstants.DOT_SIGN));
	}

	/**
	 * 获取文件后缀（包括点号）
	 *
	 * @param file 文件
	 * @return 后缀
	 */
	public static String getSuffix(File file) {
		return getSuffix(file.getName());
	}

	/**
	 * 获取文件后缀名（没有点号）
	 *
	 * @param fileName 文件名
	 * @return 后缀名
	 */
	public static String getFileSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf(ValueConstants.DOT_SIGN) + 1);
	}

	/**
	 * 获取文件后缀名（没有点号）
	 *
	 * @param file 文件
	 * @return 后缀名
	 */
	public static String getFileSuffix(File file) {
		return getFileSuffix(file.getName());
	}

	/**
	 * 扫描文件夹下面所有文件
	 *
	 * @param folder 文件夹
	 * @return 文件路径列表
	 */
	public static String[] scanFolderAsArray(String folder) {
		List<String> list = scanFolder(folder);
		String[] arrays = new String[list.size()];
		int i = 0;
		for (String item : list) {
			arrays[i++] = item;
		}
		return arrays;
	}

	/**
	 * 扫描文件夹下面所有文件
	 *
	 * @param folder 文件夹
	 * @return 文件路径列表
	 */
	public static List<String> scanFolder(String folder) {
		return scanFolder(new File(folder));
	}

	/**
	 * 扫描文件夹下面所有文件
	 *
	 * @param folder 文件夹
	 * @return 文件路径列表
	 */
	public static List<String> scanFolder(File folder) {
		String parent = folder.getAbsolutePath() + ValueConstants.SEPARATOR;
		List<String> list = new ArrayList<>(16);
		if (folder.isDirectory()) {
			String[] children = folder.list();
			if (Checker.isNotNull(children)) {
				for (String child : children) {
					File file = new File(parent + child);
					if (file.isDirectory()) {
						list.addAll(scanFolder(file));
					} else {
						list.add(file.getAbsolutePath());
					}
				}
			}
		}
		return list;
	}

	/**
	 * 从网络链接中读取内容
	 *
	 * @param url 网络链接
	 * @return {@link String}
	 * @throws IOException 异常
	 */
	public static String read(String url) throws IOException {
		return read(new URL(url));
	}

	/**
	 * 从网络链接中读取内容
	 *
	 * @param url 网络链接
	 * @return {@link String}
	 * @throws IOException 异常
	 */
	public static String read(URL url) throws IOException {
		return NetUtils.getDataOfUrl(url);
	}

	/**
	 * 读取输入流，需手动关闭流
	 *
	 * @param is  输入流
	 * @param off 偏移
	 * @param len 长度
	 * @return 内容
	 * @throws IOException 异常
	 */
	public static String read(InputStream is, int off, int len) throws IOException {
		byte[] bs = new byte[len];
		is.read(bs, off, len);
		return new String(bs);
	}

	/**
	 * 读取输入流，需手动关闭流
	 *
	 * @param is 输入流
	 * @return 内容
	 * @throws IOException 异常
	 */
	public static String read(InputStream is) throws IOException {
		return read(is, 0, is.available());
	}

	/**
	 * 批量复制文件夹
	 *
	 * @param directories   文件夹路径数组
	 * @param storageFolder 存储目录
	 * @throws IOException 异常
	 */
	public static void copyDirectories(String[] directories, String storageFolder) throws IOException {
		copyDirectories(getFiles(directories), storageFolder);
	}

	/**
	 * 批量复制文件夹
	 *
	 * @param directories   文件夹数组
	 * @param storageFolder 存储目录
	 * @throws IOException 异常
	 */
	public static void copyDirectories(File[] directories, String storageFolder) throws IOException {
		storageFolder = checkFolder(storageFolder) + ValueConstants.SEPARATOR;
		for (File directory : directories) {
			copyDirectory(directory, new File(storageFolder + directory.getName()));
		}
	}

	/**
	 * 批量复制文件夹
	 *
	 * @param directories            文件夹路径数组
	 * @param destinationDirectories 目标文件夹路径数组，与文件夹一一对应
	 * @throws IOException 异常
	 */
	public static void copyDirectories(String[] directories, String[] destinationDirectories) throws IOException {
		copyDirectories(getFiles(directories), getFiles(destinationDirectories));
	}

	/**
	 * 批量复制文件夹
	 *
	 * @param directories            文件夹数组
	 * @param destinationDirectories 目标文件夹数组，与文件夹一一对应
	 * @throws IOException 异常
	 */
	public static void copyDirectories(File[] directories, File[] destinationDirectories) throws IOException {
		int length = Integer.min(directories.length, destinationDirectories.length);
		for (int i = 0; i < length; i++) {
			copyDirectory(directories[i], destinationDirectories[i]);
		}
	}

	/**
	 * 批量复制文件，使用原文件名
	 *
	 * @param files         文件路径数组
	 * @param storageFolder 存储目录
	 * @throws IOException 异常
	 */
	public static void copyFiles(String[] files, String storageFolder) throws IOException {
		copyFiles(getFiles(files), storageFolder);
	}

	/**
	 * 批量复制文件，并重命名
	 *
	 * @param files            文件路径数组
	 * @param destinationFiles 目标文件路径数组，与文件路径数组一一对应
	 * @throws IOException 异常
	 */
	public static void copyFiles(String[] files, String[] destinationFiles) throws IOException {
		copyFiles(getFiles(files), getFiles(destinationFiles));
	}

	/**
	 * 新建文件夹，如果文件夹存在则不创建
	 *
	 * @param directory 文件夹
	 * @return 文件夹是否创建成功（如果文件夹存在同样返回true）
	 */
	public static boolean createFolder(String directory) {
		return createFolder(new File(directory));
	}

	/**
	 * 新建文件夹，如果文件夹存在则不创建
	 *
	 * @param director 文件夹
	 * @return 文件夹是否创建成功（如果文件夹存在同样返回true）
	 */
	public static boolean createFolder(File director) {
		return director.exists() || (createFolder(director.getParent()) && director.mkdir());
	}

	/**
	 * 检查文件夹，如果不存在则创建，如果是文件则获取上级目录
	 *
	 * @param directory 文件夹
	 * @return 文件夹路径
	 */
	private static String checkFolder(String directory) {
		File dir = new File(directory);
		if (dir.isFile()) {
			directory = dir.getParent();
		}
		createFolder(directory);
		return directory;
	}

	/**
	 * 批量复制文件，使用原文件名
	 *
	 * @param files         文件数组
	 * @param storageFolder 存储目录
	 * @throws IOException 异常
	 */
	public static void copyFiles(File[] files, String storageFolder) throws IOException {
		storageFolder = checkFolder(storageFolder) + ValueConstants.SEPARATOR;
		for (File file : files) {
			copyFile(file, new File(storageFolder + file.getName()));
		}
	}

	/**
	 * 批量复制文件，并重命名
	 *
	 * @param files            文件数组
	 * @param destinationFiles 目标文件数组，与文件数组一一对应
	 * @throws IOException 异常
	 */
	public static void copyFiles(File[] files, File[] destinationFiles) throws IOException {
		int length = Integer.min(files.length, destinationFiles.length);
		for (int i = 0; i < length; i++) {
			copyFile(files[i], destinationFiles[i]);
		}
	}

	/**
	 * 拆分文件
	 *
	 * @param filePath    文件路径
	 * @param splitPoints 拆分点数组（函数将根据拆分点来分割文件）
	 * @throws IOException 异常
	 */
	public static void splitFile(String filePath, long[] splitPoints) throws IOException {
		splitFile(new File(filePath), splitPoints);
	}

	/**
	 * 拆分文件
	 *
	 * @param file        文件
	 * @param splitPoints 拆分点数组（函数将根据拆分点来分割文件）
	 * @throws IOException 异常
	 */
	public static void splitFile(File file, long[] splitPoints) throws IOException {
		splitFile(file, splitPoints, file.getParent());
	}

	/**
	 * 拆分文件
	 *
	 * @param filePath      文件路径
	 * @param splitPoints   拆分点数组（函数将根据拆分点来分割文件）
	 * @param storageFolder 保存路径
	 * @throws IOException 异常
	 */
	public static void splitFile(String filePath, long[] splitPoints, String storageFolder) throws IOException {
		splitFile(new File(filePath), splitPoints, storageFolder);
	}

	/**
	 * 拆分文件
	 *
	 * @param file          文件
	 * @param splitPoints   拆分点数组（函数将根据拆分点来分割文件）
	 * @param storageFolder 保存路径
	 * @throws IOException 异常
	 */
	public static void splitFile(File file, long[] splitPoints, String storageFolder) throws IOException {
		splitPoints = ArrayUtils.concatArrays(new long[]{0}, splitPoints, new long[]{file.length()});
		String[] fileName = file.getName().split("\\.", 2);
		storageFolder += ValueConstants.SEPARATOR;
		logger.info("start to split file '" + file.getAbsolutePath() + "'");
		for (int i = 0; i < splitPoints.length - 1; i++) {
			long start = splitPoints[i], end = splitPoints[i + 1];
			long length = end - start;
			String filePath = storageFolder + fileName[0] + "_" + (i + 1) + "." + fileName[1];
			createNewFile(filePath);
			while (length > Integer.MAX_VALUE) {
				saveFile(filePath, readFile(file, start, Integer.MAX_VALUE), true);
				start += Integer.MAX_VALUE;
				length -= Integer.MAX_VALUE;
			}
			if (length > 0) {
				saveFile(filePath, readFile(file, start, (int) length), true);
			}
		}
	}

	/**
	 * 批量重命名文件
	 *
	 * @param folder 文件夹
	 * @param prefix 文件前缀
	 * @param suffix 文件后缀
	 * @param start  开始位置
	 */
	public static void renameFiles(String folder, String prefix, String suffix, int start) {
		renameFiles(scanFolderAsArray(folder), prefix, suffix, start);
	}

	/**
	 * 批量重命名文件
	 *
	 * @param filePath 文件路径数组
	 * @param prefix   文件前缀
	 * @param suffix   文件后缀
	 * @param start    开始位置
	 */
	public static void renameFiles(String[] filePath, String prefix, String suffix, int start) {
		renameFiles(getFiles(filePath), prefix, suffix, start);
	}

	/**
	 * 批量重命名文件
	 *
	 * @param filePath 文件数组
	 * @param prefix   文件前缀
	 * @param suffix   文件后缀
	 * @param start    开始位置
	 */
	public static void renameFiles(File[] filePath, String prefix, String suffix, int start) {
		String[] fileNames = new String[filePath.length];
		for (int i = 0; i < fileNames.length; i++) {
			fileNames[i] = prefix + (start++) + suffix;
		}
		renameFiles(filePath, fileNames);
	}

	/**
	 * 批量重命名文件
	 *
	 * @param folder    文件夹
	 * @param fileNames 文件名，与文件数组一一对应
	 */
	public static void renameFiles(String folder, String[] fileNames) {
		renameFiles(scanFolderAsArray(folder), fileNames);
	}

	/**
	 * 重命名多个文件
	 *
	 * @param filePath  文件路径数组
	 * @param fileNames 文件名，与文件数组一一对应
	 */
	public static void renameFiles(String[] filePath, String[] fileNames) {
		renameFiles(getFiles(filePath), fileNames);
	}

	/**
	 * 重命名多个文件
	 *
	 * @param files     文件数组
	 * @param fileNames 文件名，与文件数组一一对应
	 */
	public static void renameFiles(File[] files, String[] fileNames) {
		int length = Integer.min(files.length, fileNames.length);
		for (int i = 0; i < length; i++) {
			String fileName = files[i].getParent() + ValueConstants.SEPARATOR + fileNames[i];
			if (!fileName.contains(".")) {
				fileName += files[i].getName().substring(files[i].getName().lastIndexOf("."));
			}
			if (files[i].renameTo(new File(fileName))) {
				logger.info("rename file '" + files[i].getAbsolutePath() + "' to '" + fileName + "'");
			} else {
				logger.info("rename file '" + files[i].getAbsolutePath() + "' error");
			}
		}
	}

	/**
	 * 合并文件
	 *
	 * @param filePath            文件路径数组
	 * @param destinationFilePath 目标文件路径
	 * @param filterRegex         过滤规则（正则表达式，所有文件使用同一个过滤规则，为空时不过滤）
	 * @throws IOException 异常
	 */
	public static void mergeFiles(String[] filePath, String destinationFilePath, String filterRegex) throws IOException {
		mergeFiles(getFiles(filePath), new File(destinationFilePath), filterRegex);
	}

	/**
	 * 合并文件
	 *
	 * @param files           文件数组
	 * @param destinationFile 目标文件
	 * @param filterRegex     过滤规则（正则表达式，所有文件使用同一个过滤规则，为空时不过滤）
	 * @throws IOException 异常
	 */
	public static void mergeFiles(File[] files, File destinationFile, String filterRegex) throws IOException {
		createNewFile(destinationFile);
		for (File file : files) {
			mergeFiles(file, destinationFile, filterRegex);
		}
	}

	/**
	 * 合并文件
	 *
	 * @param filePath            文件路径数组
	 * @param destinationFilePath 目标文件路径
	 * @param filterRegex         过滤规则数组（正则表达式，与文件数组一一对应，即第个文件使用一个过滤规则，为空时不过滤）
	 * @throws IOException 异常
	 */
	public static void mergeFiles(String[] filePath, String destinationFilePath, String[] filterRegex) throws IOException {
		mergeFiles(getFiles(filePath), new File(destinationFilePath), filterRegex);
	}

	/**
	 * 合并文件
	 *
	 * @param files           文件数组
	 * @param destinationFile 目标文件
	 * @param filterRegex     过滤规则数组（正则表达式，与文件数组一一对应，即第个文件使用一个过滤规则，为空时不过滤）
	 * @throws IOException 异常
	 */
	public static void mergeFiles(File[] files, File destinationFile, String[] filterRegex) throws IOException {
		if (filterRegex.length < files.length) {
			filterRegex = ArrayUtils.concatArrays(filterRegex, new String[files.length - filterRegex.length]);
		}
		createNewFile(destinationFile);
		int i = 0;
		for (File file : files) {
			mergeFiles(file, destinationFile, filterRegex[i++]);
		}
	}

	/**
	 * 根据文件路径数组获取文件数组
	 *
	 * @param filePath 文件路径数组
	 * @return 文件数组
	 */
	public static File[] getFiles(String[] filePath) {
		return getFiles(Arrays.asList(filePath));
	}

	/**
	 * 根据文件路径数组获取文件数组
	 *
	 * @param paths 路径数组
	 * @return 文件数组
	 */
	public static File[] getFiles(List<String> paths) {
		File[] files = new File[paths.size()];
		int i = 0;
		for (String file : paths) {
			files[i++] = new File(file);
		}
		return files;
	}

	/**
	 * 合并文件
	 *
	 * @param file            待读取文件
	 * @param destinationFile 目标文件
	 * @param filter          过滤规则
	 * @throws IOException 异常
	 */
	private static void mergeFiles(File file, File destinationFile, String filter) throws IOException {
		String content = readFile(file);
		if (Checker.isNotEmpty(filter)) {
			content = content.replaceAll(filter, ValueConstants.EMPTY_STRING);
		}
		saveFile(destinationFile, content, ValueConstants.TRUE);
	}

	/**
	 * 创建文件，如果文件不存在的话
	 *
	 * @param filePath 文件路径
	 * @return 文件是否创建成功（如果文件已经存在同样返回true）
	 * @throws IOException 异常
	 */
	public static boolean createFile(String filePath) throws IOException {
		return createFile(new File(filePath));
	}

	/**
	 * 创建文件，如果文件不存在的话
	 *
	 * @param file 文件
	 * @return 文件是否创建成功（如果文件已经存在同样返回true）
	 * @throws IOException 异常
	 */
	public static boolean createFile(File file) throws IOException {
		return file.exists() || file.createNewFile();
	}

	/**
	 * 创建一个新的空文件（如果文件存在，则删除重新创建）
	 *
	 * @param filePath 文件路径
	 * @return 文件是否创建成功
	 * @throws IOException 异常
	 */
	public static boolean createNewFile(String filePath) throws IOException {
		return createNewFile(new File(filePath));
	}

	/**
	 * 创建一个新的空文件（如果文件存在，则删除重新创建）
	 *
	 * @param file 文件
	 * @return 文件是否创建成功
	 * @throws IOException 异常
	 */
	public static boolean createNewFile(File file) throws IOException {
		createFolder(file.getParent());
		return (!file.exists() || file.delete()) && file.createNewFile();
	}

	/**
	 * 删除文件或文件夹
	 *
	 * @param filePath 文件路径
	 * @return 是否删除成功
	 */
	public static boolean deleteFile(String filePath) {
		return deleteFile(new File(filePath));
	}

	/**
	 * 删除文件或文件夹
	 *
	 * @param file 文件
	 * @return 是否删除成功
	 */
	public static boolean deleteFile(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				try {
					deleteDirectory(file);
					return true;
				} catch (IOException e) {
					logger.error(e.getMessage());
					return false;
				}
			} else {
				return file.delete();
			}
		}
		return false;
	}

	/**
	 * 保存文件，覆盖原内容
	 *
	 * @param path    文件路径
	 * @param content 内容
	 * @throws IOException 异常
	 */
	public static void saveFile(String path, String content) throws IOException {
		saveFile(path, content, false);
	}

	/**
	 * 保存文件
	 *
	 * @param path    文件路径
	 * @param content 内容
	 * @param append  保存方式
	 * @throws IOException 异常
	 */
	public static void saveFile(String path, String content, boolean append) throws IOException {
		saveFile(new File(path), content, append);
	}

	/**
	 * 保存文件，覆盖原内容
	 *
	 * @param file    文件
	 * @param content 内容
	 * @throws IOException 异常
	 */
	public static void saveFile(File file, String content) throws IOException {
		saveFile(file, content, false);
	}

	/**
	 * 读取文件
	 *
	 * @param path 路径
	 * @return {@link String}
	 * @throws IOException 异常
	 */
	public static String readFile(String path) throws IOException {
		if (path.startsWith(ValueConstants.CLASSPATH_PREFIX)) {
			return read(FileExecutor.class.getResourceAsStream(path.substring(ValueConstants.CLASSPATH_PREFIX.length())));
		}
		return readFile(new File(path));
	}

	/**
	 * 读取文件
	 *
	 * @param file 文件
	 * @return {@link String}
	 * @throws IOException 异常
	 */
	public static String readFile(File file) throws IOException {
		StringBuilder content = new StringBuilder();
		String line;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\r\n");
			}
		}
		return content.toString();
	}

	/**
	 * 从指定位置读取指定长度
	 *
	 * @param file   文件
	 * @param start  开始位置
	 * @param length 读取长度
	 * @return 文件内容
	 * @throws IOException 异常
	 */
	public static String readFile(File file, long start, int length) throws IOException {
		byte[] bs = new byte[length];
		try (FileInputStream fis = new FileInputStream(file)) {
			fis.skip(start);
			fis.read(bs);
		}
		return new String(bs);
	}

	/**
	 * 保存日志文件（插入方式）
	 *
	 * @param logPath 路径
	 * @param content 内容
	 * @throws IOException 异常
	 */
	public static void saveLogFile(String logPath, String content) throws IOException {
		File file = new File(logPath);
		if (file.exists()) {
			content += readFile(file);
		}
		saveFile(file, content);
	}

	/**
	 * 保存文件
	 *
	 * @param file    文件
	 * @param content 内容
	 * @param append  保存方式
	 * @throws IOException 异常
	 */
	public static synchronized void saveFile(File file, String content, boolean append) throws IOException {
		if (Checker.isNotNull(file)) {
			if (!file.exists()) {
				file.createNewFile();
			}
			try (BufferedWriter out = new BufferedWriter(new FileWriter(file, append))) {
				out.write(content);
			}
			logger.info("save file '" + file.getAbsolutePath() + "' success");
		}
	}
}
