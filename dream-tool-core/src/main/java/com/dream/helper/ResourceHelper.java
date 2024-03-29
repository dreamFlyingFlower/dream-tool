package com.dream.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.dream.lang.AssertHelper;
import com.dream.reflect.ClassHelper;

/**
 * Resource工具类,{@link org.springframework.util.ResourceUtils}
 * 
 * @see org.springframework.core.io.Resource
 * @see org.springframework.core.io.ClassPathResource
 * @see org.springframework.core.io.FileSystemResource
 * @see org.springframework.core.io.UrlResource
 * @see org.springframework.core.io.ResourceLoader
 * 
 * @author 飞花梦影
 * @date 2021-03-06 19:54:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ResourceHelper {

	/** 从类路径记载的伪URL前缀: "classpath:" */
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	/** 从系统文件加载的URL前缀: "file:". */
	public static final String FILE_URL_PREFIX = "file:";

	/** 从jar包加载的URL前缀: "jar:". */
	public static final String JAR_URL_PREFIX = "jar:";

	/** 从Tomcat上的war文件加载的URL前缀: "war:" */
	public static final String WAR_URL_PREFIX = "war:";

	/** 文件系统中文件的URL协议: "file" */
	public static final String URL_PROTOCOL_FILE = "file";

	/** jar文件条目的URL协议: "jar" */
	public static final String URL_PROTOCOL_JAR = "jar";

	/** war文件条目的URL协议: "war" */
	public static final String URL_PROTOCOL_WAR = "war";

	/** zip文件条目的URL协议: "zip". */
	public static final String URL_PROTOCOL_ZIP = "zip";

	/** WebSphere jar文件天目的URL协议: "wsjar". */
	public static final String URL_PROTOCOL_WSJAR = "wsjar";

	/** JBoss jar文件条目的URL协议: "vfszip". */
	public static final String URL_PROTOCOL_VFSZIP = "vfszip";

	/** JBoss文件系统资源的URL协议: "vfsfile". */
	public static final String URL_PROTOCOL_VFSFILE = "vfsfile";

	/** 通用JBoss VFS资源的URL协议: "vfs". */
	public static final String URL_PROTOCOL_VFS = "vfs";

	/** 常规jar文件的扩展名: ".jar". */
	public static final String JAR_FILE_EXTENSION = ".jar";

	/** JAR URL和JAR内文件路径之间的分隔符: "!/". */
	public static final String JAR_URL_SEPARATOR = "!/";

	/** Tomcat上warurl和jar部分之间的特殊分隔符 */
	public static final String WAR_URL_SEPARATOR = "*/";

	/**
	 * 从给定的jar或war的URL资源地址中(可能指向jar文件中的资源或jar文件本身)提取最外层存档的URL
	 * 
	 * @param jarUrl 原始的Jar URL资源地址
	 * @return jar包的真实URL地址
	 * @throws MalformedURLException if no valid jar file URL could be extracted
	 */
	public static URL extractArchiveURL(URL jarUrl) throws MalformedURLException {
		String urlFile = jarUrl.getFile();
		int endIndex = urlFile.indexOf(WAR_URL_SEPARATOR);
		if (endIndex != -1) {
			// Tomcat's "war:file:...mywar.war*/WEB-INF/lib/myjar.jar!/myentry.txt"
			String warFile = urlFile.substring(0, endIndex);
			if (URL_PROTOCOL_WAR.equals(jarUrl.getProtocol())) {
				return new URL(warFile);
			}
			int startIndex = warFile.indexOf(WAR_URL_PREFIX);
			if (startIndex != -1) {
				return new URL(warFile.substring(startIndex + WAR_URL_PREFIX.length()));
			}
		}
		// Regular "jar:file:...myjar.jar!/myentry.txt"
		return extractJarFileURL(jarUrl);
	}

	/**
	 * 从给定的URL中提取真实的Jar文件URL
	 * 
	 * @param jarUrl 原始的Jar URL资源地址
	 * @return jar包的真实URL地址
	 * @throws MalformedURLException if no valid jar file URL could be extracted
	 */
	public static URL extractJarFileURL(URL jarUrl) throws MalformedURLException {
		String urlFile = jarUrl.getFile();
		int separatorIndex = urlFile.indexOf(JAR_URL_SEPARATOR);
		if (separatorIndex != -1) {
			String jarFile = urlFile.substring(0, separatorIndex);
			try {
				return new URL(jarFile);
			} catch (MalformedURLException ex) {
				// Probably no protocol in original jar URL, like "jar:C:/mypath/myjar.jar".
				// This usually indicates that the jar file resides in the file system.
				if (!jarFile.startsWith("/")) {
					jarFile = "/" + jarFile;
				}
				return new URL(FILE_URL_PREFIX + jarFile);
			}
		} else {
			return jarUrl;
		}
	}

	/**
	 * 获得项目根目录的绝对路径地址
	 * 
	 * @return 根目录的绝对路径地址
	 */
	public static String getRootPath() {
		return ResourceHelper.class.getResource("/").getPath().replace("target/classes/", "");
	}

	/**
	 * 获得class根目录的绝对路径地址
	 * 
	 * @return class根目录的绝对路径地址
	 */
	public static String getRootClassPath() {
		return ResourceHelper.class.getResource("/").getPath();
	}

	/**
	 * 判断给定的URL指向文件系统资源,如file,vfsfile,vfs
	 * 
	 * @param url 需要判断的URL
	 * @return true->URL指向文件系统,false->URL不指向文件系统
	 */
	public static boolean isFileURL(URL url) {
		String protocol = url.getProtocol();
		return (URL_PROTOCOL_FILE.equals(protocol) || URL_PROTOCOL_VFSFILE.equals(protocol)
				|| URL_PROTOCOL_VFS.equals(protocol));
	}

	/**
	 * 判断给定的URL指向Jar文件资源,如jar,war,zip,vfszip,wsjar
	 * 
	 * @param url 需要判断的URL
	 * @return true->URL指向Jar文件资源,false->URL不指向Jar文件资源
	 */
	public static boolean isJarURL(URL url) {
		String protocol = url.getProtocol();
		return (URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_WAR.equals(protocol)
				|| URL_PROTOCOL_ZIP.equals(protocol) || URL_PROTOCOL_VFSZIP.equals(protocol)
				|| URL_PROTOCOL_WSJAR.equals(protocol));
	}

	/**
	 * 判断给定的URL指向文件系统中的Jar包,文件后缀只能是.jar
	 * 
	 * @param url 需要判断的URL
	 * @return true->URL指向一个.jar包,false->URL不指向一个.jar包
	 */
	public static boolean isJarFileURL(URL url) {
		return (URL_PROTOCOL_FILE.equals(url.getProtocol())
				&& url.getPath().toLowerCase().endsWith(JAR_FILE_EXTENSION));
	}

	/**
	 * 判断给定的资源地址是一个URL,如classpath或标准的URL地址
	 * 
	 * @param resourceLocation 需要判断的资源路径
	 * @return true->资源路径是一个URL,false->资源路径不是一个URL
	 * @see {@link java.net.URL}
	 */
	public static boolean isUrl(String resourceLocation) {
		if (resourceLocation == null) {
			return false;
		}
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			return true;
		}
		try {
			new URL(resourceLocation);
			return true;
		} catch (MalformedURLException ex) {
			return false;
		}
	}

	/**
	 * 读取资源文件
	 * 
	 * @param path 资源文件地址
	 * @return Properties
	 * @throws IOException
	 */
	public static Properties readProperties(String path) throws IOException {
		InputStream inputStream = ResourceHelper.class.getResourceAsStream(path);
		if (null == inputStream) {
			inputStream = ResourceHelper.class.getClassLoader().getResourceAsStream(path);
		}
		Properties properties = new Properties();
		if (null != inputStream) {
			properties.load(inputStream);
		}
		return properties;
	}

	/**
	 * 读取资源文件,后读的文件同名属性将覆盖前面读的文件
	 * 
	 * @param paths 资源文件地址
	 * @return Properties
	 * @throws IOException
	 */
	public static Properties readProperties(String... paths) throws IOException {
		return readProperties(Arrays.asList(paths));
	}

	/**
	 * 读取资源文件,后读的文件同名属性将覆盖前面读的文件
	 * 
	 * @param paths 资源文件地址
	 * @return Properties
	 * @throws IOException
	 */
	public static Properties readProperties(List<String> paths) throws IOException {
		Properties properties = new Properties();
		InputStream inputStream = null;
		for (String path : paths) {
			inputStream = ResourceHelper.class.getResourceAsStream(path);
			if (null == inputStream) {
				inputStream = ResourceHelper.class.getClassLoader().getResourceAsStream(path);
			}
			if (null != inputStream) {
				properties.load(inputStream);
			}
		}
		return properties;
	}

	/**
	 * 将给定的字符串资源地址解析为{@link File}
	 * 
	 * @param resourceLocation 需要解析的资源地址,如"classpath:"伪URL, "file:" URL, 或者普通文件地址
	 * @return {@link File}
	 * @throws FileNotFoundException if the resource cannot be resolved to a file in the file system
	 */
	public static File toFile(String resourceLocation) throws FileNotFoundException {
		AssertHelper.notNull(resourceLocation, "Resource location must not be null");
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
			String description = "class path resource [" + path + "]";
			ClassLoader cl = ClassHelper.getClassLoader();
			URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
			if (url == null) {
				throw new FileNotFoundException(
						description + " cannot be resolved to absolute file path because it does not exist");
			}
			return toFile(url, description);
		}
		try {
			// try URL
			return toFile(new URL(resourceLocation));
		} catch (MalformedURLException ex) {
			// no URL -> treat as file path
			return new File(resourceLocation);
		}
	}

	/**
	 * 将给定的URL资源解析为{@link File}
	 * 
	 * @param uri 需要解析的URL资源
	 * @return {@link File}
	 * @throws FileNotFoundException if the URL cannot be resolved to a file in the file system
	 */
	public static File toFile(URL url) throws FileNotFoundException {
		return toFile(url, "URL");
	}

	/**
	 * 将给定的URL资源解析为{@link File}
	 * 
	 * @param uri 需要解析的URL资源
	 * @param description 创建URL的原始资源描述,如class path location
	 * @return {@link File}
	 * @throws FileNotFoundException if the URL cannot be resolved to a file in the file system
	 */
	public static File toFile(URL url, String description) throws FileNotFoundException {
		AssertHelper.notNull(url, "Resource URL must not be null");
		if (!URL_PROTOCOL_FILE.equals(url.getProtocol())) {
			throw new FileNotFoundException(description + " cannot be resolved to absolute file path "
					+ "because it does not reside in the file system: " + url);
		}
		try {
			return new File(toURI(url).getSchemeSpecificPart());
		} catch (URISyntaxException ex) {
			// Fallback for URLs that are not valid URIs (should hardly ever happen).
			return new File(url.getFile());
		}
	}

	/**
	 * 将给定的URI资源解析为{@link File}
	 * 
	 * @param uri 需要解析的URI资源
	 * @return {@link File}
	 * @throws FileNotFoundException if the URL cannot be resolved to a file in the file system
	 */
	public static File toFile(URI uri) throws FileNotFoundException {
		return toFile(uri, "URI");
	}

	/**
	 * 将给定的URI资源解析为{@link File}
	 * 
	 * @param uri 需要解析的URI资源
	 * @param description 创建URI的原始资源描述,如class path location
	 * @return {@link File}
	 * @throws FileNotFoundException if the URL cannot be resolved to a file in the file system
	 */
	public static File toFile(URI uri, String description) throws FileNotFoundException {
		AssertHelper.notNull(uri, "Resource URI must not be null");
		if (!URL_PROTOCOL_FILE.equals(uri.getScheme())) {
			throw new FileNotFoundException(description + " cannot be resolved to absolute file path "
					+ "because it does not reside in the file system: " + uri);
		}
		return new File(uri.getSchemeSpecificPart());
	}

	/**
	 * 将URL转换为一个URI实例,首先用%20替代空白
	 * 
	 * @param url URL资源实例
	 * @return URI实例
	 * @throws URISyntaxException if the URL wasn't a valid URI
	 * @see java.net.URL#toURI()
	 */
	public static URI toURI(URL url) throws URISyntaxException {
		return toURI(url.toString());
	}

	/**
	 * 将给定的地址字符串转换为一个URI实例,首先用%20替代空白
	 * 
	 * @param location 资源地址字符串
	 * @return URI实例
	 * @throws URISyntaxException if the location wasn't a valid URI
	 */
	public static URI toURI(String location) throws URISyntaxException {
		return new URI(location.trim().replace(" ", "%20"));
	}

	/**
	 * 将给定的地址字符串转换为一个{@code java.net.URL}实例
	 * 
	 * @param location 资源地址字符串,如"classpath:"伪URL, "file:" URL, 或者普通文件地址
	 * @return 对应的URL实例
	 * @throws FileNotFoundException if the resource cannot be resolved to a URL
	 */
	public static URL toURL(String location) throws FileNotFoundException {
		AssertHelper.notNull(location, "Resource location must not be null");
		if (location.startsWith(CLASSPATH_URL_PREFIX)) {
			String path = location.substring(CLASSPATH_URL_PREFIX.length());
			ClassLoader cl = ClassHelper.getClassLoader();
			URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
			if (url == null) {
				String description = "class path resource [" + path + "]";
				throw new FileNotFoundException(description + " cannot be resolved to URL because it does not exist");
			}
			return url;
		}
		try {
			// try URL
			return new URL(location);
		} catch (MalformedURLException ex) {
			// no URL -> treat as file path
			try {
				return new File(location).toURI().toURL();
			} catch (MalformedURLException ex2) {
				throw new FileNotFoundException(
						"Resource location [" + location + "] is neither a URL not a well-formed file path");
			}
		}
	}

	/**
	 * 将URL连接中的usecache设置为true当连接实例的类名以JNLP开头时,默认为false
	 * 
	 * @param con URLConnection
	 */
	public static void useCachesIfNecessary(URLConnection con) {
		con.setUseCaches(con.getClass().getSimpleName().startsWith("JNLP"));
	}
}