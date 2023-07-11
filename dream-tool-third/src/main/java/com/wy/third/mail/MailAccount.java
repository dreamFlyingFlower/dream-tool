package com.wy.third.mail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Properties;

import com.wy.io.file.FileTool;
import com.wy.lang.AssertTool;
import com.wy.lang.StrTool;
import com.wy.util.CharsetTool;
import com.wy.util.ResourceTool;

/**
 * Mail邮件账号信息
 * 
 * @author 飞花梦影
 * @date 2021-04-01 10:28:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MailAccount implements Serializable {

	private static final long serialVersionUID = -8236374181406015099L;

	// -----------properties相关属性
	/**
	 * SMTP协议
	 */
	private static final String MAIL_PROTOCOL = "mail.transport.protocol";

	/**
	 * SMTP域名
	 */
	private static final String SMTP_HOST = "mail.smtp.host";

	/**
	 * SMTP端口
	 */
	private static final String SMTP_PORT = "mail.smtp.port";

	/**
	 * SMTP是否验证
	 */
	private static final String SMTP_AUTH = "mail.smtp.auth";

	/**
	 * SMTP服务器连接超时时间
	 */
	private static final String SMTP_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";

	/**
	 * SMTP服务器响应超时时间
	 */
	private static final String SMTP_TIMEOUT = "mail.smtp.timeout";

	/**
	 * SMTP是否使用starttls
	 */
	private static final String STARTTLS_ENABLE = "mail.smtp.starttls.enable";

	/**
	 * SMTP是否使用SSL安全加密
	 */
	private static final String SSL_ENABLE = "mail.smtp.ssl.enable";

	/**
	 * SMTP使用SSL时的协议
	 */
	private static final String SSL_PROTOCOLS = "mail.smtp.ssl.protocols";

	/**
	 * SMTP连接Socket的类
	 */
	private static final String SOCKET_FACTORY = "mail.smtp.socketFactory.class";

	/**
	 * SMTP连接Socket的回调
	 */
	private static final String SOCKET_FACTORY_FALLBACK = "mail.smtp.socketFactory.fallback";

	/**
	 * SMTP连接Socket的端口
	 */
	private static final String SOCKET_FACTORY_PORT = "smtp.socketFactory.port";
	// -----------properties相关属性 结束

	/**
	 * 邮件读取配置文件地址
	 */
	private static final String[] MAIL_PROPERTIES_PATHS = new String[] { "mail.properties", "config/mail.properties" };

	/**
	 * SMTP服务器域名,如smtp.sina.com,smtp.qq.com
	 */
	private String host;

	/**
	 * SMTP服务端口,qq是465或576,其他默认是25
	 */
	private Integer port;

	/**
	 * 是否需要用户名密码验证
	 */
	private Boolean auth;

	/**
	 * 发送者用户名
	 */
	private String username;

	/**
	 * 发送者密码,qq是开启smtp服务的密钥,其他是邮箱密码
	 */
	private String password;

	/**
	 * 发送方邮件地址,遵循RFC-822标准
	 */
	private String fromAddress;

	/**
	 * 编码用于编码邮件正文和发送人,收件人等中文
	 */
	private Charset charset = CharsetTool.defaultCharset();

	/**
	 * 对于超长参数是否切分为多份,默认为false,国内邮箱附件不支持切分的附件名
	 */
	private boolean splitlongparameters;

	/**
	 * 使用STARTTLS安全连接,STARTTLS是对纯文本通信协议的扩展.它将纯文本连接升级为加密连接(TLS或SSL),而不是使用一个单独的加密通信端口
	 */
	private boolean starttls = false;

	/**
	 * 使用SSL安全连接,默认不开启
	 */
	private Boolean ssl = false;

	/**
	 * SSL协议,多个协议用空格分隔
	 */
	private String sslProtocols;

	/**
	 * 指定实现javax.net.SocketFactory接口的类的名称,这个类将被用于创建SMTP的套接字
	 */
	private String socketFactoryClass = "javax.net.ssl.SSLSocketFactory";

	/**
	 * 如果设置为true,未能创建一个套接字使用指定的套接字工厂类将导致使用java.net.Socket创建的套接字类, 默认值为true
	 */
	private boolean socketFactoryFallback;

	/**
	 * 指定的端口连接到在使用指定的套接字工厂,如果没有设置,将使用默认端口
	 */
	private int socketFactoryPort = 465;

	/**
	 * SMTP超时时长,单位毫秒,默认不超时
	 */
	private long timeout;

	/**
	 * Socket连接超时值,单位毫秒,默认不超时
	 */
	private long connectionTimeout;

	public MailAccount() {
		super();
	}

	public MailAccount(String host, Integer port, Boolean auth, String username, String password, String fromAddress,
			boolean debug, Charset charset, boolean splitlongparameters, boolean starttls, Boolean ssl,
			String sslProtocols, String socketFactoryClass, boolean socketFactoryFallback, int socketFactoryPort,
			long timeout, long connectionTimeout) {
		super();
		this.host = host;
		this.port = port;
		this.auth = auth;
		this.username = username;
		this.password = password;
		this.fromAddress = fromAddress;
		this.charset = charset;
		this.splitlongparameters = splitlongparameters;
		this.starttls = starttls;
		this.ssl = ssl;
		this.sslProtocols = sslProtocols;
		this.socketFactoryClass = socketFactoryClass;
		this.socketFactoryFallback = socketFactoryFallback;
		this.socketFactoryPort = socketFactoryPort;
		this.timeout = timeout;
		this.connectionTimeout = connectionTimeout;
	}

	public static Properties loadProperties() throws IOException {
		return ResourceTool.readProperties(MAIL_PROPERTIES_PATHS);
	}

	public static Properties loadProperties(String path) throws IOException {
		InputStream inputStream = FileTool.newInputStream(new File(path));
		Properties properties = new Properties();
		if (null != inputStream) {
			properties.load(inputStream);
		}
		return properties;
	}

	/**
	 * 配置参数默认值
	 */
	public MailAccount buildDefaultProperty() {
		AssertTool.notBlank(this.fromAddress);
		if (StrTool.isBlank(this.host)) {
			// 如果SMTP地址为空,默认使用smtp.<发件人邮箱后缀>
			this.host = String.format("smtp.%s", StrTool.subAfter(this.fromAddress, "@"));
		}
		if (StrTool.isBlank(this.username)) {
			// 如果用户名为空,默认为发件人邮箱前缀
			this.username = StrTool.subBefore(fromAddress, "@");
		}
		if (null == this.auth) {
			// 如果密码非空白,则使用认证模式
			this.auth = (!StrTool.isBlank(this.password));
		}
		if (null == this.port) {
			// 端口在SSL状态下默认与socketFactoryPort一致,非SSL状态下默认为25
			this.port = (null != this.ssl && this.ssl) ? this.socketFactoryPort : 25;
		}
		if (null == this.charset) {
			this.charset = CharsetTool.defaultCharset();
		}
		return this;
	}

	/**
	 * MailAccount转换为Properties
	 *
	 * @return {@link Properties}
	 */
	public Properties buildProperties() {
		// 全局系统参数
		final Properties properties = new Properties();
		// 邮件服务类型
		properties.put(MAIL_PROTOCOL, "smtp");
		// 邮件服务器
		properties.put(SMTP_HOST, this.host);
		// 邮件服务器端口
		properties.put(SMTP_PORT, this.port);
		// 开启验证
		properties.put(SMTP_AUTH, this.auth);
		if (this.timeout > 0) {
			properties.put(SMTP_TIMEOUT, this.timeout);
		}
		if (this.connectionTimeout > 0) {
			properties.put(SMTP_CONNECTION_TIMEOUT, this.connectionTimeout);
		}
		if (this.starttls) {
			properties.put(STARTTLS_ENABLE, true);
			if (null == this.ssl) {
				// 为了兼容旧版本,当用户没有此项配置时,按照starttls开启状态时对待
				this.ssl = true;
			}
		}
		// SSL安全连接  
		// {@link MailTools#getProp} FIXME 是否切换默认的socket_factory
		if (null != this.ssl && this.ssl) {
			properties.put(SSL_ENABLE, true);
			properties.put(SOCKET_FACTORY, socketFactoryClass);
			properties.put(SOCKET_FACTORY_FALLBACK, this.socketFactoryFallback);
			properties.put(SOCKET_FACTORY_PORT, this.socketFactoryPort);
			if (StrTool.isNotBlank(this.sslProtocols)) {
				properties.put(SSL_PROTOCOLS, this.sslProtocols);
			}
		}
		return properties;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Boolean getAuth() {
		return auth;
	}

	public void setAuth(Boolean auth) {
		this.auth = auth;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public boolean isSplitlongparameters() {
		return splitlongparameters;
	}

	public void setSplitlongparameters(boolean splitlongparameters) {
		this.splitlongparameters = splitlongparameters;
	}

	public boolean isStarttls() {
		return starttls;
	}

	public void setStarttls(boolean starttls) {
		this.starttls = starttls;
	}

	public Boolean getSsl() {
		return ssl;
	}

	public void setSsl(Boolean ssl) {
		this.ssl = ssl;
	}

	public String getSslProtocols() {
		return sslProtocols;
	}

	public void setSslProtocols(String sslProtocols) {
		this.sslProtocols = sslProtocols;
	}

	public String getSocketFactoryClass() {
		return socketFactoryClass;
	}

	public void setSocketFactoryClass(String socketFactoryClass) {
		this.socketFactoryClass = socketFactoryClass;
	}

	public boolean isSocketFactoryFallback() {
		return socketFactoryFallback;
	}

	public void setSocketFactoryFallback(boolean socketFactoryFallback) {
		this.socketFactoryFallback = socketFactoryFallback;
	}

	public int getSocketFactoryPort() {
		return socketFactoryPort;
	}

	public void setSocketFactoryPort(int socketFactoryPort) {
		this.socketFactoryPort = socketFactoryPort;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public long getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
}