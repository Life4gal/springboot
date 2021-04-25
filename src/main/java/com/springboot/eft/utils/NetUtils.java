package com.springboot.eft.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.google.common.net.HttpHeaders;
import com.springboot.eft.constant.ValueConstants;
import com.springboot.eft.utils.encryption.JavaEncrypt;
import com.springboot.eft.utils.interfaces.MultipartFileService;
import com.springboot.eft.utils.model.CheckResult;
import com.springboot.eft.utils.model.ResultObject;
import com.springboot.eft.utils.model.SimpleMultipartFile;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NetUtils {

	/**
	 * 协议
	 */
	public static final String PROTOCOL_KEY = "$protocol";

	/**
	 * 主机
	 */
	public static final String HOST_KEY = "$host";

	/**
	 * 路径
	 */
	public static final String PATH_KEY = "$path";

	private NetUtils() {
	}

	/**
	 * 响应文件
	 *
	 * @param response     {@link HttpServletResponse}
	 * @param local        文件路径
	 * @param isDownloaded 是否下载
	 * @throws IOException
	 */
	public static void responseFile(HttpServletResponse response, String local, boolean isDownloaded) throws IOException {
		if (Checker.isExists(local)) {
			File file = new File(local);
			try (FileInputStream in = new FileInputStream(file); ServletOutputStream os = response.getOutputStream()) {
				byte[] b;
				while (in.available() > 0) {
					b = in.available() > 1024 ? new byte[1024] : new byte[in.available()];
					in.read(b, 0, b.length);
					os.write(b, 0, b.length);
				}
				os.flush();
			}
			if (isDownloaded) {
				String fn = new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
				response.setHeader("Content-Disposition", "attachment;filename=" + fn);
			}
		} else {
			response.setStatus(404);
		}
	}

	/**
	 * 获取服务端错误消息
	 *
	 * @param request {@link HttpServletRequest}
	 * @param e       {@link Exception}
	 * @return {@link Map}
	 */
	public static Map<String, String> getServerErrorMap(HttpServletRequest request, Exception e) {
		Map<String, String> attributes = new HashMap<>(4);
		attributes.put("code", "500");
		attributes.put("message", e.getMessage());
		String queryString = request.getQueryString();
		attributes.put("url", request.getRequestURI() + (Checker.isEmpty(queryString) ? "" : "?" + queryString));
		return attributes;
	}

	/**
	 * 生成令牌
	 *
	 * @return 令牌
	 */
	public static String generateToken() {
		String timestampByBase64 = Utils.rightTrim(Base64.encode(String.valueOf(System.currentTimeMillis())), "=");
		return (RandomUtil.simpleUUID() + timestampByBase64).toLowerCase();
	}

	/**
	 * 上传文件，简化业务代码
	 *
	 * @param bytes                MultipartFile#getBytes
	 * @param simpleMultipartFile  请设置一下值：{@link SimpleMultipartFile#setOriginalFilename(String)}, {@link
	 *                             SimpleMultipartFile#setSize(Long)}, {@link SimpleMultipartFile#setStoragePath(String)}
	 * @param multipartFileService 请至少实现 {@link MultipartFileService} 接口中的一个方法
	 * @param <T>                  原型类
	 * @return {@link ResultObject}
	 */
	public static <T> ResultObject<T> upload(byte[] bytes, SimpleMultipartFile simpleMultipartFile,
	                                         MultipartFileService<T> multipartFileService) {
		ResultObject<T> resultObject = new ResultObject<>();
		String md5 = JavaEncrypt.MD5.digestHex(bytes);
		String filename = md5 + FileExecutor.getSuffix(simpleMultipartFile.getOriginalFilename());
		// 文件本地路径
		String localPath = simpleMultipartFile.getStoragePath() + filename;
		simpleMultipartFile.setFilename(filename).setMd5(md5);
		Boolean exists = multipartFileService.existsMultipartFile(simpleMultipartFile);
		boolean shouldWrite = false;
		T t = null;
		if (Checker.isNull(exists)) {
			t = multipartFileService.getBySimpleMultipartFile(simpleMultipartFile);
			if (Checker.isNull(t)) {
				// 不存在时则可以写入磁盘
				shouldWrite = true;
			}
		} else if (!exists) {
			// 不存在时则可以写入磁盘
			shouldWrite = true;
		}
		if (shouldWrite) {
			try {
				// 写入磁盘
				FileExecutor.writeByteArrayToFile(new File(localPath), bytes);
			} catch (IOException e) {
				LoggerUtils.error(e.getMessage());
				return resultObject.copyFrom(CheckResult.getErrorResult("内部错误：文件写入失败"));
			}
			// 写入数据库（需要重写方法）
			t = multipartFileService.saveEntity(simpleMultipartFile);
		}
		return resultObject.setData(t);
	}

	/**
	 * DELETE请求
	 *
	 * @param url  请求的URL
	 * @param body 内容正文
	 * @return 响应内容
	 */
	public static String delete(String url, String body) {
		return request(HttpRequest.delete(replaceLocalHost(url)), body).body();
	}

	/**
	 * DELETE请求
	 *
	 * @param url  请求的URL
	 * @param body 内容正文
	 * @return 响应内容
	 */
	public static String delete(String url, Map<String, String> body) {
		return delete(url, JSONObject.toJSONString(body));
	}

	/**
	 * DELETE请求
	 *
	 * @param url  请求的URL
	 * @param body 内容正文
	 * @return 响应内容
	 */
	public static String delete(String url, JSONObject body) {
		return delete(url, body.toJSONString());
	}

	/**
	 * PUT请求
	 *
	 * @param url  请求的URL
	 * @param body 内容正文
	 * @return 响应内容
	 */
	public static String put(String url, String body) {
		return request(HttpRequest.put(replaceLocalHost(url)), body).body();
	}

	/**
	 * PUT请求
	 *
	 * @param url  请求的URL
	 * @param body 内容正文
	 * @return 响应内容
	 */
	public static String put(String url, Map<String, String> body) {
		return put(url, JSONObject.toJSONString(body));
	}

	/**
	 * PUT请求
	 *
	 * @param url  请求的URL
	 * @param body 内容正文
	 * @return 响应内容
	 */
	public static String put(String url, JSONObject body) {
		return put(url, body.toJSONString());
	}

	/**
	 * GET请求
	 *
	 * @param url  请求的URL
	 * @param body 内容正文
	 * @return 响应内容
	 */
	public static String get(String url, String body) {
		return request(HttpRequest.get(replaceLocalHost(url)), body).body();
	}

	/**
	 * GET请求
	 *
	 * @param url  请求的URL
	 * @param body 内容正文
	 * @return 响应内容
	 */
	public static String get(String url, Map<String, String> body) {
		return get(url, JSONObject.toJSONString(body));
	}

	/**
	 * GET请求
	 *
	 * @param url  请求的URL
	 * @param body 内容正文
	 * @return 响应内容
	 */
	public static String get(String url, JSONObject body) {
		return get(url, body.toJSONString());
	}

	/**
	 * POST请求
	 *
	 * @param url  请求的URL
	 * @param body 内容正文
	 * @return 响应内容
	 */
	public static String post(String url, String body) {
		return request(HttpRequest.post(replaceLocalHost(url)), body).body();
	}

	/**
	 * POST请求
	 *
	 * @param url  请求的URL
	 * @param body 内容正文
	 * @return 响应内容
	 */
	public static String post(String url, Map<String, String> body) {
		return post(url, JSONObject.toJSONString(body));
	}

	/**
	 * POST请求
	 *
	 * @param url  请求的URL
	 * @param body 内容正文
	 * @return 响应内容
	 */
	public static String post(String url, JSONObject body) {
		return post(url, body.toJSONString());
	}

	/**
	 * 发出网络请求
	 *
	 * @param request {@link HttpRequest}
	 * @param body    请求正文
	 * @return {@link HttpResponse}
	 */
	public static HttpResponse request(HttpRequest request, String body) {
		return request(request, body, "application/json");
	}

	/**
	 * 发出网络请求
	 *
	 * @param request     {@link HttpRequest}
	 * @param body        请求正文
	 * @param contentType 内容类型
	 * @return {@link HttpResponse}
	 */
	public static HttpResponse request(HttpRequest request, String body, String contentType) {
		return request.contentType(contentType).body(body).contentLength(body.length()).execute();
	}

	/**
	 * 替换localhost为127.0.0.1
	 *
	 * @param domain 域名
	 * @return 替换后的域名
	 */
	public static String replaceLocalHost(String domain) {
		return domain.replaceAll("localhost", "127.0.0.1");
	}

	/**
	 * 是否是 ajax请求
	 *
	 * @param request {@link HttpServletRequest}
	 * @return {@link Boolean}
	 */
	public static boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equalsIgnoreCase(request.getHeader(HttpHeaders.X_REQUESTED_WITH));
	}

	/**
	 * 解析URL
	 *
	 * @param url url
	 * @return {@link Map}
	 */
	public static Map<String, String> parseUrl(String url) {
		Map<String, String> result = new HashMap<>(8);
		result.put(PROTOCOL_KEY, ValueConstants.EMPTY_STRING);
		result.put(HOST_KEY, ValueConstants.EMPTY_STRING);
		result.put(PATH_KEY, ValueConstants.EMPTY_STRING);
		if (Checker.isNotEmpty(url)) {
			String[] pros;
			final String protocolSplit = "://";
			if (url.contains(protocolSplit)) {
				pros = url.split(protocolSplit);
			} else {
				pros = new String[]{"", url};
			}
			// 设置主机、协议、路径
			result.put(PROTOCOL_KEY, pros[0]);
			if (pros.length < ValueConstants.TWO_INT) {
				pros = new String[]{pros[0], ValueConstants.EMPTY_STRING};
			}
			if (pros[1].contains(ValueConstants.SPLASH_STRING)) {
				int lastIndex = pros[1].lastIndexOf(ValueConstants.SPLASH_STRING);
				if (pros[1].startsWith(ValueConstants.SPLASH_STRING)) {
					// 文件协议
					result.put(PATH_KEY, pros[1].substring(1));
				} else if (pros[1].contains(ValueConstants.SPLASH_STRING)) {
					int index = pros[1].indexOf("/");
					// 设置主机
					result.put(HOST_KEY, pros[1].substring(0, index));
					// 设置参数
					if (pros[1].contains(ValueConstants.QUESTION_MARK)) {
						lastIndex = pros[1].indexOf(ValueConstants.QUESTION_MARK);
						String[] params = pros[1].split("\\?")[1].split("&");
						for (String param : params) {
							String[] kv = param.split("=");
							result.put(kv[0], kv[1]);
						}
					}
					// 设置路径
					if (lastIndex > index) {
						String path = pros[1].substring(index + 1, lastIndex);
						path = path.endsWith(ValueConstants.SPLASH_STRING) ? path.substring(0, path.length() - 1) : path;
						result.put(PATH_KEY, path);
					}
				} else {
					result.put(HOST_KEY, pros[1]);
				}
			} else {
				result.put(HOST_KEY, pros[1]);
			}
		}
		return result;
	}

	/**
	 * 脚本过滤
	 *
	 * @param string {@link String}
	 * @return 过滤后的字符串
	 */
	public static String scriptFilter(String string) {
		return Checker.checkNull(string).replaceAll(ValueConstants.SCRIPT_FILTER_PATTERN, ValueConstants.EMPTY_STRING);
	}

	/**
	 * 清除所有Cookie
	 *
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @return {@link Boolean}
	 */
	public static boolean clearCookie(HttpServletRequest request, HttpServletResponse response) {
		return clearCookie(request.getCookies(), response);
	}

	/**
	 * 清除所有Cookie
	 *
	 * @param cookies  {@link Cookie}
	 * @param response {@link HttpServletResponse}
	 * @return {@link Boolean}
	 */
	public static boolean clearCookie(Cookie[] cookies, HttpServletResponse response) {
		if (Checker.isNotEmpty(cookies)) {
			for (Cookie cookie : cookies) {
				removeCookie(cookie, response);
			}
			return true;
		}
		return false;
	}

	/**
	 * 删除指定Cookie
	 *
	 * @param name     Cookie名
	 * @param request  {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @return {@link Boolean}
	 */
	public static boolean removeCookie(String name, HttpServletRequest request, HttpServletResponse response) {
		return removeCookie(name, request.getCookies(), response);
	}

	/**
	 * 删除指定Cookie
	 *
	 * @param name     Cookie名
	 * @param cookies  {@link Cookie}
	 * @param response {@link HttpServletResponse}
	 * @return {@link Boolean}
	 */
	public static boolean removeCookie(String name, Cookie[] cookies, HttpServletResponse response) {
		if (Checker.isNotEmpty(name) && Checker.isNotEmpty(cookies)) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return removeCookie(cookie, response);
				}
			}
		}
		return false;
	}

	/**
	 * 删除指定Cookie
	 *
	 * @param cookie   {@link Cookie}
	 * @param response {@link HttpServletResponse}
	 * @return {@link Boolean}
	 */
	public static boolean removeCookie(Cookie cookie, HttpServletResponse response) {
		if (Checker.isNotNull(cookie)) {
			cookie.setMaxAge(0);
			return addCookie(cookie, response);
		}
		return false;
	}

	/**
	 * 添加Cookie
	 *
	 * @param cookie   {@link Cookie}
	 * @param response {@link HttpServletResponse}
	 * @return {@link Boolean}
	 */
	public static boolean addCookie(Cookie cookie, HttpServletResponse response) {
		if (Checker.isNotNull(cookie) && Checker.isNotNull(response)) {
			response.addCookie(cookie);
			return true;
		}
		return false;
	}

	/**
	 * 添加Cookie
	 *
	 * @param response {@link HttpServletResponse}
	 * @param name     Cookie名
	 * @param value    Cookie值
	 * @return {@link Boolean}
	 */
	public static boolean addCookie(HttpServletResponse response, String name, String value) {
		return addCookie(new Cookie(name, value), response);
	}

	/**
	 * 添加Cookie
	 *
	 * @param response {@link HttpServletResponse}
	 * @param name     Cookie名
	 * @param value    Cookie值
	 * @param expiry   有效期
	 * @param uri      路径
	 * @return {@link Boolean}
	 */
	public static boolean addCookie(HttpServletResponse response, String name, String value, int expiry, String uri) {
		Cookie cookie = new Cookie(name, value);
		if (expiry > 0) {
			cookie.setMaxAge(expiry);
		}
		if (Checker.isNotEmpty(uri)) {
			cookie.setPath(uri);
		}
		return addCookie(cookie, response);
	}

	/**
	 * 通过名称获取Cookie
	 *
	 * @param name    Cookie名
	 * @param request {@link HttpServletRequest}
	 * @return {@link Cookie}
	 */
	public static Cookie getCookie(String name, HttpServletRequest request) {
		return getCookie(name, request.getCookies());
	}

	/**
	 * 通过名称获取Cookie
	 *
	 * @param name    Cookie名
	 * @param cookies cookie数组
	 * @return {@link Cookie}
	 */
	public static Cookie getCookie(String name, Cookie[] cookies) {
		if (Checker.isNotEmpty(cookies)) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * whois查询
	 *
	 * @param domain 域名
	 * @return whois信息
	 * @throws IOException                  异常
	 * @throws XPathExpressionException     异常
	 * @throws ParserConfigurationException 异常
	 */
	public static String whois(String domain) throws IOException, XPathExpressionException,
			ParserConfigurationException {
		String whois = evaluate(ValueConstants.WHOIS_DOMAIN_XPATH, getHtmlFromUrl("http://whois.chinaz.com/" + domain));
		return whois.replaceAll("\\[whois\\s?反查]", ValueConstants.EMPTY_STRING).replaceAll("\\s{2,}", "\r\n");
	}

	/**
	 * 获取ip归属地
	 *
	 * @param ip ip地址
	 * @return 归属地
	 * @throws IOException                  异常
	 * @throws XPathExpressionException     异常
	 * @throws ParserConfigurationException 异常
	 */
	public static String getLocationByIp(String ip) throws IOException, XPathExpressionException,
			ParserConfigurationException {
		return evaluate(ValueConstants.IP_REGION_XPATH, getHtmlFromUrl("http://ip.chinaz.com/" + ip));
	}

	/**
	 * XPath解析HTML内容
	 *
	 * @param xpath xpath表达式
	 * @param html  html内容
	 * @return 解析结果
	 * @throws XPathExpressionException     异常
	 * @throws ParserConfigurationException 异常
	 */
	public static String evaluate(String xpath, String html) throws XPathExpressionException,
			ParserConfigurationException {
		HtmlCleaner hc = new HtmlCleaner();
		TagNode tn = hc.clean(html);
		Document document = new DomSerializer(new CleanerProperties()).createDOM(tn);
		XPath xPath = XPathFactory.newInstance().newXPath();
		return xPath.evaluate(xpath, document);
	}

	/**
	 * 获取网页内容
	 *
	 * @param url 链接
	 * @return {@link String}
	 * @throws IOException 异常
	 */
	public static String getHtmlFromUrl(String url) throws IOException {
		return getDocumentFromUrl(url).html();
	}

	/**
	 * 获取HTML文档
	 *
	 * @param url 链接
	 * @return {@link org.jsoup.nodes.Document}
	 * @throws IOException 异常
	 */
	public static org.jsoup.nodes.Document getDocumentFromUrl(String url) throws IOException {
		return Jsoup.connect(url).get();
	}

	/**
	 * 获取计算机名
	 *
	 * @return 计算机名
	 */
	public static String getComputerName() {
		return System.getenv().get("COMPUTERNAME");
	}

	/**
	 * 获取系统名称
	 *
	 * @return 系统名称
	 */
	public static String getSystemName() {
		return System.getProperty("os.name");
	}

	/**
	 * 获取系统架构
	 *
	 * @return 系统架构
	 */
	public static String getSystemArch() {
		return System.getProperty("os.arch");
	}

	/**
	 * 获取系统版本
	 *
	 * @return 系统版本
	 */
	public static String getSystemVersion() {
		return System.getProperty("os.version");
	}

	/**
	 * 获取Mac地址
	 *
	 * @return mac地址
	 * @throws UnknownHostException 异常
	 * @throws SocketException      异常
	 */
	public static String getMacAddress() throws UnknownHostException, SocketException {
		byte[] mac = NetworkInterface.getByInetAddress(Inet4Address.getLocalHost()).getHardwareAddress();
		StringBuilder jointMac = new StringBuilder();
		for (byte aMac : mac) {
			String macPart = Integer.toHexString(aMac & 0xFF);
			jointMac.append(macPart.length() == 1 ? "0" + macPart : macPart).append("-");
		}
		String macFormat = jointMac.toString();
		return macFormat.substring(0, macFormat.length() - 1);
	}

	/**
	 * 获取公网IP和归属地
	 *
	 * @return 公网ip、address，如：{"ip":"127.0.0.1","address":"your ip location"}
	 * @throws IOException 异常
	 */
	public static JSONObject getPublicIpAndLocation() throws IOException {
		return FileExecutor.parseJsonObject(new URL(ValueConstants.URL_OF_PUBLIC_IP_SEARCH));
	}

	/**
	 * 获取本地ip地址
	 *
	 * @return ip
	 * @throws UnknownHostException 异常
	 */
	public static String getLocalIp() throws UnknownHostException {
		return Inet4Address.getLocalHost().getHostAddress();
	}

	/**
	 * 将URL转换成String
	 *
	 * @param url {@link URL}
	 * @return {@link String}
	 */
	public static String urlToString(URL url) {
		return Checker.isNull(url) ? "" : url.toString().replaceAll("file:" + (Checker.isWindows() ? "/?" : ""), "");
	}

	/**
	 * 获取URL中的数据
	 *
	 * @param url 网络链接
	 * @return {@link String}
	 * @throws IOException 异常
	 */
	public static String getDataOfUrl(String url) throws IOException {
		return getDataOfUrl(new URL(url));
	}

	/**
	 * 获取URL中的数据
	 *
	 * @param url 网络链接
	 * @return {@link String}
	 * @throws IOException 异常
	 */
	public static String getDataOfUrl(URL url) throws IOException {
		return FileExecutor.read(getInputStreamOfUrl(url));
	}

	/**
	 * 获取URL的InputStream对象
	 *
	 * @param url 网络链接
	 * @return {@link InputStream}
	 * @throws IOException 异常
	 */
	public static InputStream getInputStreamOfUrl(String url) throws IOException {
		return getInputStreamOfUrl(new URL(url));
	}

	/**
	 * 获取URL的InputStream对象
	 *
	 * @param url 网络链接
	 * @return {@link InputStream}
	 * @throws IOException 异常
	 */
	public static InputStream getInputStreamOfUrl(URL url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		return getInputStreamOfConnection(conn);
	}

	/**
	 * 获取HttpURLConnection的InputStream对象
	 *
	 * @param connection 链接对象
	 * @return {@link InputStream}
	 * @throws IOException 异常
	 */
	public static InputStream getInputStreamOfConnection(HttpURLConnection connection) throws IOException {
		return setDefaultValue(connection).getInputStream();
	}

	private static HttpURLConnection setDefaultValue(HttpURLConnection connection) {
		connection.setConnectTimeout(1000 * 6);
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty("User-Agent", ValueConstants.USER_AGENT[0]);
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Accept", "*/*");
		return connection;
	}
}
