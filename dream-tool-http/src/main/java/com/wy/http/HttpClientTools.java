package com.wy.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson2.JSON;
import com.wy.ConstLang;
import com.wy.collection.MapTool;
import com.wy.util.CharsetTool;

/**
 * HttpClient工具类
 * 
 * @author 飞花梦影
 * @date 2021-01-07 15:21:53
 * @git {@link https://github.com/mygodness100}
 */
public class HttpClientTools {

	/** 连接超时时间,单位毫秒 */
	public static final int TIMEOUT_CONNECT = 5000;

	/** 读取数据超时时间,单位毫秒 */
	public static final int TIMEOUT_SOCKET = 5000;

	/** 使用自定义的连接池 */
	private static PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();

	static {
		// 设置最大连接数
		manager.setMaxTotal(200);
		// 设置并发数
		manager.setDefaultMaxPerRoute(20);
		// 可使用定时任务扫描所有的httpclient,关闭无效的连接
		// manager.closeExpiredConnections();
		// 设置2次http握手的时间间隔
		manager.setValidateAfterInactivity(2000);
	}

	/**
	 * 构建HttpClient对象
	 *
	 * @param multiThread 是否多线程,true->是,false->否
	 * @return CloseableHttpClient
	 */
	public static CloseableHttpClient buildClient(boolean multiThread) {
		return multiThread ? HttpClientBuilder.create()
		        // 设置通用请求参数
		        .setDefaultRequestConfig(buildRequestConfig())
		        // 设置禁止重定向
		        .disableRedirectHandling()
		        // 设置重试策略
		        .setRetryHandler(new DefaultHttpRequestRetryHandler())
		        // 设置连接策略
		        .setConnectionManager(new PoolingHttpClientConnectionManager()).build()
		        : HttpClientBuilder.create().setDefaultRequestConfig(buildRequestConfig()).build();
	}

	/**
	 * 构建HttpClient对象
	 *
	 * @param multiThread 是否多线程,true->是,false->否
	 * @param cookieStore 是否带上cookie
	 * @param proxy httpHost
	 * @return CloseableHttpClient
	 */
	public static CloseableHttpClient buildClient(boolean multiThread, CookieStore cookieStore, HttpHost httpHost) {
		return multiThread
		        ? HttpClientBuilder.create().setDefaultCookieStore(cookieStore)
		                .setDefaultRequestConfig(buildRequestConfig(httpHost))
		                .setRetryHandler(new DefaultHttpRequestRetryHandler())
		                .setConnectionManager(new PoolingHttpClientConnectionManager()).build()
		        : HttpClientBuilder.create().setDefaultRequestConfig(buildRequestConfig(httpHost))
		                .setDefaultCookieStore(cookieStore).build();
	}

	/**
	 * 构建cookie信息
	 * 
	 * @param params cookie信息
	 * @return CookieStore
	 */
	public static CookieStore buildCookie(Map<String, String> params) {
		BasicCookieStore cookieStore = null;
		if (null != params && !params.isEmpty()) {
			cookieStore = new BasicCookieStore();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				cookieStore.addCookie(new BasicClientCookie(entry.getKey(), entry.getValue()));
			}
		}
		return cookieStore;
	}

	/**
	 * 构建httpGet请求
	 *
	 * @param url 请求地址
	 * @param params 参数
	 * @param charset 字符编码集
	 * @return HttpGet
	 */
	public static HttpGet buildGet(String url, Map<String, Object> params, Charset chatset) {
		return new HttpGet(buildParamsGet(url, params, chatset));
	}

	/**
	 * 构建post请求
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param charset 字符编码集
	 * @return HttpPost
	 */
	public static HttpPost buildPost(String url, Map<String, Object> params, Charset charset) {
		HttpPost post = new HttpPost(url);
		setCommonHttpMethod(post);
		if (params != null) {
			post.setEntity(buildParamsPost(params, charset));
		}
		return post;
	}

	/**
	 * 构建post请求
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param charset 字符编码集
	 * @param headerMap 请求头
	 * @return HttpPost
	 */
	public static HttpPost buildPost(String url, Map<String, Object> params, Charset charset,
	        Map<String, Object> headerMap) {
		HttpPost post = new HttpPost(url);
		setCommonHttpMethod(post);
		setHeader(post, headerMap);
		if (params != null) {
			post.setEntity(buildParamsPost(params, charset));
		}
		return post;
	}

	/**
	 * 构建post json请求
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param charset 字符编码集
	 * @return HttpPost
	 */
	public static HttpPost buildPostJson(String url, Map<String, Object> params, Charset charset) {
		HttpPost post = new HttpPost(url);
		setJsonHttpMethod(post);
		// 创建请求内容
		if (params != null) {
			StringEntity stringEntity = new StringEntity(JSON.toJSONString(params), charset);
			stringEntity
			        .setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()));
			post.setEntity(stringEntity);
		}
		return post;
	}

	/**
	 * Get请求将参数拼接到url后
	 *
	 * @param url http地址
	 * @param params 参数
	 * @param charset 字符编码集
	 * @return 完整url
	 */
	public static URI buildParamsGet(String url, Map<String, Object> params, Charset charset) {
		try {
			URIBuilder builder = new URIBuilder(url);
			if (MapTool.isEmpty(params)) {
				return builder.build();
			}
			for (String key : params.keySet()) {
				builder.addParameter(key, Objects.isNull(params.get(key)) ? "" : String.valueOf(params.get(key)));
			}
			return builder.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Post将参数转换成表单请求对象
	 * 
	 * @param params 参数
	 * @param charset 字符编码集
	 * @return 表单请求对象
	 */
	public static UrlEncodedFormEntity buildParamsPost(Map<String, Object> params, Charset charset) {
		charset = CharsetTool.defaultCharset(charset);
		List<NameValuePair> ps = new ArrayList<>();
		for (String key : params.keySet()) {
			ps.add(new BasicNameValuePair(key, Objects.isNull(params.get(key)) ? "" : String.valueOf(params.get(key))));
		}
		// URLEncodedUtils.format(ps, charset);
		// 模拟表单
		return new UrlEncodedFormEntity(ps, charset);
	}

	/**
	 * 构建公用RequestConfig
	 * 
	 * @param httpHost
	 * @return
	 */
	public static RequestConfig buildRequestConfig() {
		// 设置连接超时时间,从连接池中获取连接的最长时间,数据传输的最长时间
		return RequestConfig.custom().setConnectTimeout(TIMEOUT_CONNECT).setConnectionRequestTimeout(TIMEOUT_CONNECT)
		        .setSocketTimeout(TIMEOUT_SOCKET).build();
	}

	/**
	 * 构建公用RequestConfig
	 * 
	 * @param httpHost
	 * @return
	 */
	public static RequestConfig buildRequestConfig(HttpHost httpHost) {
		// 设置连接超时时间,从连接池中获取连接的最长时间,数据传输的最长时间
		return RequestConfig.custom().setProxy(httpHost).setConnectTimeout(TIMEOUT_CONNECT)
		        .setConnectionRequestTimeout(TIMEOUT_CONNECT).setSocketTimeout(TIMEOUT_SOCKET).build();
	}

	/**
	 * 使用自定义连接池调用get请求
	 * 
	 * @param url 请求地址
	 * @return JSON字符串结果
	 */
	public static String poolGet(String url) {
		HttpGet httpGet = new HttpGet(url);
		try (CloseableHttpClient client = HttpClients.custom().setConnectionManager(manager).build();
		        CloseableHttpResponse response = client.execute(httpGet);) {
			return Objects.nonNull(response.getEntity())
			        ? EntityUtils.toString(response.getEntity(), CharsetTool.defaultCharset())
			        : null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送简单的http get请求
	 * 
	 * @param url 请求地址
	 * @return 字符串结果
	 */
	public static String get(String url) {
		return get(url, null);
	}

	/**
	 * 发送http get请求
	 * 
	 * @param url url
	 * @param params 参数
	 * @return 请求结果
	 */
	public static String get(String url, Map<String, Object> params) {
		return get(url, params, CharsetTool.defaultCharset());
	}

	/**
	 * 发送http get请求
	 * 
	 * @param url url
	 * @param params 参数
	 * @param charset 字符集
	 * @return 请求结果
	 */
	public static String get(String url, Map<String, Object> params, Charset charset) {
		HttpGet httpGet = buildGet(url, params, charset);
		try (CloseableHttpClient httpclient = buildClient(true);
		        CloseableHttpResponse response = httpclient.execute(httpGet);) {
			// 直接可将返回的流转成字符串,但是转换一次后流就关闭了
			return null != response.getEntity()
			        ? EntityUtils.toString(response.getEntity(), CharsetTool.defaultCharset(charset))
			        : null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送http get请求
	 * 
	 * @param url url
	 * @param params 参数
	 * @param charset 字符集
	 * @return 请求结果
	 */
	public static String get(String url, Map<String, Object> params, String charset) {
		return get(url, params, CharsetTool.defaultCharset(charset));
	}

	/**
	 * 发送复杂http get请求
	 * 
	 * @param url url
	 * @param params 参数
	 * @param headers 请求头参数
	 * @param cookieStore cookie
	 * @param proxy 代理
	 * @param charset 字符集
	 * @return JSON序列化后结果字符串
	 */
	public static String sendGet(String url, Map<String, Object> params, Map<String, Object> headers,
	        CookieStore cookieStore, HttpHost httpHost, Charset charset) {
		HttpGet httpGet = buildGet(url, params, charset);
		setHeader(httpGet, headers);
		try (CloseableHttpClient httpClient = buildClient(true, cookieStore, httpHost);
		        CloseableHttpResponse response = httpClient.execute(httpGet);) {
			// 直接可将返回的流转成字符串,但是转换一次后流就关闭了
			return null != response.getEntity()
			        ? EntityUtils.toString(response.getEntity(), CharsetTool.defaultCharset(charset))
			        : null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Post表单请求
	 * 
	 * @param url 请求地址
	 * @return JSON字符串结果
	 */
	public static String postForm(String url) {
		return postForm(url, null, null, null);
	}

	/**
	 * Post表单请求
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @return JSON字符串结果
	 */
	public static String postForm(String url, Map<String, Object> params) {
		return postForm(url, params, null, null);
	}

	/**
	 * Post表单请求
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param charset 字符集
	 * @return JSON字符串结果
	 */
	public static String postForm(String url, Map<String, Object> params, Charset charset) {
		return postForm(url, params, null, charset);
	}

	/**
	 * Post表单请求
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param charsetName 字符编码集名
	 * @return JSON字符串结果
	 */
	public static String postForm(String url, Map<String, Object> params, String charsetName) {
		return postForm(url, params, null, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * Post表单请求
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param headers 请求头
	 * @return JSON字符串结果
	 */
	public static String postForm(String url, Map<String, Object> params, Map<String, Object> headers) {
		return postForm(url, params, headers, null);
	}

	/**
	 * Post表单请求
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param headers 请求头
	 * @param charset 字符集
	 * @return JSON字符串结果
	 */
	public static String postForm(String url, Map<String, Object> params, Map<String, Object> headers,
	        Charset charset) {
		HttpPost postMethod = buildPost(url, params, charset);
		setHeader(postMethod, headers);
		try (CloseableHttpClient client = buildClient(true);
		        CloseableHttpResponse response = client.execute(postMethod);) {
			return null != response.getEntity() ? EntityUtils.toString(response.getEntity(), charset) : null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Post表单请求
	 * 
	 * @param url 请求地址
	 * @return 请求响应,但获取不到结果,只能获取头信息
	 */
	public static HttpResponse postFormResponse(String url) {
		return postFormResponse(url, null, null, null);
	}

	/**
	 * Post表单请求
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @return 请求响应,但获取不到结果,只能获取头信息
	 */
	public static HttpResponse postFormResponse(String url, Map<String, Object> params) {
		return postFormResponse(url, params, null, null);
	}

	/**
	 * Post表单请求
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param charset 字符集
	 * @return 请求响应,但获取不到结果,只能获取头信息
	 */
	public static HttpResponse postFormResponse(String url, Map<String, Object> params, Charset charset) {
		return postFormResponse(url, params, null, charset);
	}

	/**
	 * Post表单请求
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param charsetName 字符编码集名
	 * @return 请求响应,但获取不到结果,只能获取头信息
	 */
	public static HttpResponse postFormResponse(String url, Map<String, Object> params, String charsetName) {
		return postFormResponse(url, params, null, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * Post表单请求
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param headers 请求头
	 * @return 请求响应,但获取不到结果,只能获取头信息
	 */
	public static HttpResponse postFormResponse(String url, Map<String, Object> params, Map<String, Object> headers) {
		return postFormResponse(url, params, headers, null);
	}

	/**
	 * Post表单请求
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param headers 请求头
	 * @param charset 字符编码
	 * @return 请求响应,但获取不到结果,只能获取头信息
	 */
	public static HttpResponse postFormResponse(String url, Map<String, Object> params, Map<String, Object> headers,
	        Charset charset) {
		HttpPost postMethod = buildPost(url, params, charset);
		setHeader(postMethod, headers);
		try (CloseableHttpClient client = buildClient(true);
		        CloseableHttpResponse response = client.execute(postMethod);) {
			return response;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Post Json请求,参数将被序列化成字符串之后发送
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @return JSON字符串结果
	 */
	public static String postJson(String url, Map<String, Object> params) {
		return postJson(url, params, null, CharsetTool.defaultCharset());
	}

	/**
	 * Post Json请求,参数将被序列化成字符串之后发送
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param charset 字符集
	 * @return JSON字符串结果
	 */
	public static String postJson(String url, Map<String, Object> params, Charset charset) {
		return postJson(url, params, null, charset);
	}

	/**
	 * Post Json请求,参数将被序列化成字符串之后发送
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param charsetName 字符编码名
	 * @return JSON字符串结果
	 */
	public static String postJson(String url, Map<String, Object> params, String charsetName) {
		return postJson(url, params, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * Post Json请求,参数将被序列化成字符串之后发送
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param headers 请求头
	 * @param charset 字符集
	 * @return JSON字符串结果
	 */
	public static String postJson(String url, Map<String, Object> params, Map<String, Object> headers,
	        Charset charset) {
		HttpPost httpPost = buildPostJson(url, params, charset);
		setHeader(httpPost, headers);
		try (CloseableHttpClient httpClient = buildClient(true);
		        CloseableHttpResponse response = httpClient.execute(httpPost);) {
			return Optional.ofNullable(response).isPresent()
			        ? EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
			        : null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Post Json请求,参数将被序列化成字符串之后发送
	 * 
	 * @param url 请求地址
	 * @param params 参数
	 * @param headers 请求头
	 * @param charsetName 字符编码名
	 * @return JSON字符串结果
	 */
	public static String postJson(String url, Map<String, Object> params, Map<String, Object> headers,
	        String charsetName) {
		return postJson(url, params, headers, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 设置HttpMethod通用配置
	 *
	 * @param httpRequestBase
	 */
	public static void setCommonHttpMethod(HttpRequestBase httpRequestBase) {
		// 保证消息一次性传输完,使用默认模式会出现异常
		httpRequestBase.setProtocolVersion(HttpVersion.HTTP_1_0);
		httpRequestBase.setHeader(HTTP.CONTENT_ENCODING, ConstLang.DEFAULT_CHARSET.displayName());
	}

	/**
	 * 设置成消息体的长度 setting MessageBody length
	 *
	 * @param httpRequestBase 请求
	 * @param httpEntity 请求体
	 */
	public static void setContentLength(HttpRequestBase httpRequestBase, HttpEntity httpEntity) {
		if (httpEntity == null) {
			return;
		}
		httpRequestBase.setHeader(HTTP.CONTENT_LEN, String.valueOf(httpEntity.getContentLength()));
	}

	/**
	 * 设置请起头
	 * 
	 * @param httpRequestBase 请求
	 * @param headers 请求头
	 */
	public static void setHeader(HttpRequestBase httpRequestBase, Map<String, Object> headers) {
		if (MapTool.isNotEmpty(headers)) {
			for (Map.Entry<String, Object> entry : headers.entrySet()) {
				httpRequestBase.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
	}

	/**
	 * 设置HttpMethod通用配置
	 *
	 * @param httpRequestBase
	 */
	public static void setJsonHttpMethod(HttpRequestBase httpRequestBase) {
		httpRequestBase.setHeader(HTTP.CONTENT_ENCODING, ConstLang.DEFAULT_CHARSET.displayName());
		httpRequestBase.setHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
	}
}