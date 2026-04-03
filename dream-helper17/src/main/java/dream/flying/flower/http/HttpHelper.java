package dream.flying.flower.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import dream.flying.flower.bean.BeanHelper;
import dream.flying.flower.collection.MapHelper;
import dream.flying.flower.helper.CharsetHelper;
import dream.flying.flower.http.enums.ContentType;
import dream.flying.flower.http.enums.HttpMethod;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.reflect.ClassHelper;

/**
 * Http工具类,使用JDK自带类
 * 
 * @author 飞花梦影
 * @date 2021-04-06 15:13:25
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class HttpHelper {

	/** post默认请求方式,表单形式 */
	static final String DEFAULT_CONTENTTYPE = ContentType.APPLICATION_FORM_URLENCODED_VALUE;

	/** 默认超时时间,1分钟 */
	static final int TIMEOUT = 60000;

	/**
	 * url拼接
	 * 
	 * @param url 请求地址
	 * @param params 参数列表
	 * @return 拼接后的URL
	 * @throws UnsupportedEncodingException
	 */
	public static String buildParams(String url, Map<String, Object> params) throws UnsupportedEncodingException {
		if (StrHelper.isBlank(url) || MapHelper.isEmpty(params)) {
			return url;
		}
		return MessageFormat.format("{0}?{1}", url, buildParams(params));
	}

	/**
	 * 参数拼接
	 * 
	 * @param params 参数列表
	 * @return 拼接后的URL
	 * @throws UnsupportedEncodingException
	 */
	public static String buildParams(Map<String, Object> params) throws UnsupportedEncodingException {
		return buildParams(params, CharsetHelper.defaultCharset().name());
	}

	/**
	 * 参数拼接
	 * 
	 * @param params 参数列表
	 * @param charsetName 字符编码名
	 * @return 拼接后的URL
	 * @throws UnsupportedEncodingException
	 */
	public static String buildParams(Map<String, Object> params, String charsetName)
			throws UnsupportedEncodingException {
		List<String> list = new ArrayList<>();
		for (String key : params.keySet()) {
			list.add(URLEncoder.encode(serializerParams(key, params.get(key)), charsetName));
		}
		return String.join("&", list);
	}

	/**
	 * 文件下载
	 * 
	 * @param downloadAddress 下载地址
	 * @param desFile 本地存储地址
	 */
	public static void httpDownload(String downloadAddress, String desFile) {
		httpDownload(downloadAddress, new File(desFile));
	}

	/**
	 * 文件下载
	 * 
	 * @param downloadAddress 下载地址
	 * @param desFile 本地存储地址
	 */
	public static void httpDownload(String downloadAddress, File desFile) {
		httpDownload(downloadAddress, desFile, 5);
	}

	/**
	 * 文件下载
	 * 
	 * @param downloadAddress 下载地址
	 * @param desFile 本地存储地址
	 * @param threadCount 下载线程数
	 */
	public static void httpDownload(String downloadAddress, File desFile, int threadCount) {
		httpDownload(downloadAddress, desFile, threadCount, HttpMethod.GET.name());
	}

	/**
	 * 文件下载
	 * 
	 * @param downloadAddress 文件下载地址
	 * @param desFile 本地存储地址
	 * @param threadCount 下载线程数
	 */
	public static void httpDownload(String downloadAddress, String desFile, int threadCount) {
		httpDownload(downloadAddress, new File(desFile), threadCount, HttpMethod.GET.name());
	}

	/**
	 * 文件下载
	 * 
	 * @param downloadAddress 文件下载地址
	 * @param desFile 本地存储地址
	 * @param threadCount 下载线程数
	 * @param method 请求远程文件方式
	 */
	public static void httpDownload(String downloadAddress, File desFile, int threadCount, String method) {
		HttpDownloads.download(downloadAddress, desFile, threadCount, method);
	}

	/**
	 * get请求,无参数或参数已经拼接到url后面
	 * 
	 * @param url 请求地址
	 * @return 请求结果
	 * @throws IOException
	 */
	public static String sendGet(String url) throws IOException {
		return sendGet(url, null);
	}

	/**
	 * 发送get请求,实体类
	 * 
	 * @return 请求结果
	 * @throws IOException
	 */
	public static <T> String sendGet(String desUrl, T t) throws IOException {
		Map<String, Object> map = BeanHelper.beanToMap(t);
		return sendGet(desUrl, map);
	}

	/**
	 * get请求,默认请求头application/json;charset=utf8
	 * 
	 * @param params:参数
	 * @return 请求结果
	 * @throws IOException
	 */
	public static String sendGet(String desUrl, Map<String, Object> params) throws IOException {
		return sendGet(desUrl, params, CharsetHelper.defaultCharset().name());
	}

	/**
	 * get请求,默认请求头application/json;charset=utf8
	 * 
	 * @param desUrl 请求地址
	 * @param params 请求参数
	 * @param charsetName 字符编码名
	 * @return 请求结果
	 * @throws IOException
	 */
	public static String sendGet(String desUrl, Map<String, Object> params, String charsetName) throws IOException {
		return sendGet(desUrl, params, charsetName, TIMEOUT);
	}

	/**
	 * 发送get请求,只适合基本类型参数组成的map
	 * 
	 * @param desUrl 请求地址
	 * @param params 请求参数
	 * @param charsetName 字符编码名
	 * @param timeout 超时时间,单位秒
	 * @return 请求结果
	 * @throws IOException
	 */
	public static String sendGet(String desUrl, Map<String, Object> params, String charsetName, int timeout)
			throws IOException {
		String encodeUrl = buildParams(desUrl, params);
		URL url = new URL(encodeUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置请求方式,不写默认get,请求方式必须大写
		conn.setRequestMethod(HttpMethod.GET.name());
		// 设置超时时间
		conn.setConnectTimeout(timeout);
		// 设置是否缓存请求
		conn.setDefaultUseCaches(false);
		conn.setUseCaches(false);
		// 需要输出参数
		conn.setDoOutput(true);
		// 设置属性请求
		// 维持长连接
		conn.setRequestProperty("Connection", "keep-Alive");
		conn.setRequestProperty("Content-Encoding", "gzip");
		conn.setRequestProperty("Contert-length", String.valueOf(desUrl.length()));
		conn.setRequestProperty("charset", charsetName);
		// 链接
		conn.connect();
		// 获得响应状态
		int respCode = conn.getResponseCode();
		if (HttpURLConnection.HTTP_OK == respCode) {
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charsetName));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			conn.disconnect();
			// 上面的程序等同于apache.common.io的ioutils.toString(in)方法
			// 若上面的方法发生ioexception : premature eof,则使用下面程序
			// Scanner scan = new
			// Scanner(conn.getInputStream(),StandardCharsets.UTF_8.displayName());
			// while(scan.hasNextLine()) {
			// sb.append(scan.nextLine());
			// }
			// 结果,可进行json转换
			return sb.toString();
		}
		return null;
	}

	/**
	 * Post请求,无参数或参数已经拼接到url后面
	 * 
	 * @param desUrl 请求地址
	 * @return 请求结果
	 * @throws IOException
	 */
	public static String sendPost(String desUrl) throws IOException {
		return sendPost(desUrl, null);
	}

	/**
	 * Post请求,参数为实体类
	 * 
	 * @param desUrl 请求地址
	 * @param params 由Map或Bean转换而成的json字符串参数
	 * @return 请求结果
	 * @throws IOException
	 */
	public static String sendPost(String desUrl, String params) throws IOException {
		return sendPost(desUrl, params, DEFAULT_CONTENTTYPE);
	}

	/**
	 * Post请求,默认请求头application/json;charset=utf8
	 * 
	 * @param desUrl 请求地址
	 * @param params 由Map或Bean转换而成的json字符串参数
	 * @param contentType 请求类型
	 * @return 请求结果
	 * @throws IOException
	 */
	public static String sendPost(String desUrl, String params, String contentType) throws IOException {
		return sendPost(desUrl, params, contentType, CharsetHelper.defaultCharset().name());
	}

	/**
	 * Post请求,默认请求头application/json;charset=utf8
	 * 
	 * @param desUrl 请求地址
	 * @param params 由Map或Bean转换而成的json字符串参数
	 * @param contentType 请求类型
	 * @param charsetName 字符编码名
	 * @return 请求结果
	 * @throws IOException
	 */
	public static String sendPost(String desUrl, String params, String contentType, String charsetName)
			throws IOException {
		return sendPost(desUrl, params, contentType, charsetName, TIMEOUT);
	}

	/**
	 * Post请求
	 * 
	 * @param desUrl 请求地址
	 * @param params 由Map或Bean转换而成的json字符串参数
	 * @param contentType 请求类型
	 * @param charsetName 字符编码名
	 * @param timeout 超时时间,单位秒
	 * @return 请求结果
	 * @throws IOException
	 */
	public static String sendPost(String desUrl, String params, String contentType, String charsetName, int timeout)
			throws IOException {
		URL url = new URL(desUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(HttpMethod.POST.name());
		conn.setConnectTimeout(timeout);
		conn.setDefaultUseCaches(false);
		conn.setUseCaches(false);
		// 需要输出参数
		conn.setDoOutput(true);
		// 设置属性请求
		// 维持长连接
		conn.setRequestProperty("Connection", "keep-Alive");
		conn.setRequestProperty("charset", CharsetHelper.defaultCharset(charsetName).name());
		conn.setRequestProperty("Content-Encoding", "gzip");
		conn.setRequestProperty("Contert-length", String.valueOf(desUrl.length()));
		conn.setRequestProperty("Content-type", StrHelper.getDefault(contentType, DEFAULT_CONTENTTYPE));
		conn.connect();
		if (StrHelper.isNotBlank(params)) {
			// 参数输出
			DataOutputStream dataout = new DataOutputStream(conn.getOutputStream());
			// 将参数输出到连接
			dataout.writeBytes(params);
			// 输出完成后刷新并关闭流
			dataout.flush();
			// 重要且易忽略步骤 (关闭流,切记!)
			dataout.close();
		}
		// 获得响应状态
		int respCode = conn.getResponseCode();
		if (HttpURLConnection.HTTP_OK == respCode) {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), CharsetHelper.defaultCharset(charsetName)));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			conn.disconnect();
			return sb.toString();
		}
		return null;
	}

	/**
	 * 判断是否需要对值进行序列化
	 * 
	 * @param key 请求key
	 * @param value 需要序列化的值
	 * @return key=value
	 */
	public static String serializerParams(String key, Object value) {
		if (key == null) {
			return null;
		} else if (value == null) {
			return key + "=";
		} else if (ClassHelper.isPrimitiveStr(value.getClass())) {
			return key + "=" + value;
		} else {
			return key + "=" + String.valueOf(value);
		}
	}

	private static TrustManager[] loadKeyStore() throws Exception {
		KeyStore trustStore = KeyStore.getInstance("JKS");
		// Java信任域名证书cacerts,密码默认为changeit
		trustStore.load(HttpHelper.class.getResourceAsStream("/cacerts"), "changeit".toCharArray());
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustStore);
		return tmf.getTrustManagers();
	}

	private static KeyManager[] loadP12(String keyStorePassword) throws Exception {
		KeyStore clientStore = KeyStore.getInstance("PKCS12");
		clientStore.load(HttpHelper.class.getResourceAsStream("/cert.p12"), keyStorePassword.toCharArray());
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(clientStore, keyStorePassword.toCharArray());
		return kmf.getKeyManagers();
	}

	private static SSLSocketFactory buildSslSocketFactory(KeyManager[] kms, TrustManager[] tms) throws Exception {
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kms, tms, new SecureRandom());
		return sslContext.getSocketFactory();
	}

	/**
	 * HTTPS调用
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式
	 * @param params 参数
	 * @param keyStorePassword keyStore密码
	 * @return 结果
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String params, String keyStorePassword) {
		StringBuffer buffer = new StringBuffer();
		try {
			// 加载keyStore(JKS)文件
			TrustManager[] tms = loadKeyStore();

			// 加载P12文件
			KeyManager[] kms = loadP12(keyStorePassword);

			// 从SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory sslSocketFactory = buildSslSocketFactory(kms, tms);

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (javax.net.ssl.HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(sslSocketFactory);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setRequestMethod(requestMethod);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setInstanceFollowRedirects(true);
			httpUrlConn.setRequestProperty("Content-Type", "application/json");
			httpUrlConn.setRequestProperty("Accept", "application/json");
			httpUrlConn.setConnectTimeout(2000);
			httpUrlConn.setReadTimeout(3000);
			httpUrlConn.connect();
			// 当有数据需要提交时
			if (null != params) {
				try (OutputStream outputStream = new DataOutputStream(httpUrlConn.getOutputStream());) {
					outputStream.write(params.getBytes("UTF-8"));
					outputStream.flush();
				}
			}
			// 将返回的输入流转换成字符串
			try (InputStream inputStream = httpUrlConn.getInputStream();
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {
				String str = null;
				while ((str = bufferedReader.readLine()) != null) {
					buffer.append(str);
				}
				return buffer.toString();
			} finally {
				if (httpUrlConn != null) {
					httpUrlConn.disconnect();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

}