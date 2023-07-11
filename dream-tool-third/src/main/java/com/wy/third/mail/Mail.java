package com.wy.third.mail;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;

import com.wy.enums.TipEnum;
import com.wy.lang.StrTool;

/**
 * Mail邮件实体类
 * 
 * @author 飞花梦影
 * @date 2021-04-01 10:02:24
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Mail implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 邮箱帐户信息以及一些客户端配置信息
	 */
	private MailAccount mailAccount;

	/**
	 * 所有发送方的信息,属性同MailAccount,从文件中读取时,将不读取MailAccount属性
	 */
	private Properties properties;

	/**
	 * 是否打印发送日志
	 */
	private boolean debug;

	/**
	 * 邮件主题
	 */
	private String subject;

	/**
	 * 邮件内容
	 */
	private String content;

	/**
	 * 接收者邮件地址
	 */
	private String[] toAddress;

	/**
	 * 抄送对象
	 */
	private String[] ccs;

	/**
	 * 密送对象
	 */
	private String[] bccs;

	/**
	 * 是否为HTML
	 */
	private boolean html;

	/**
	 * 附件
	 */
	private String[] attachs;

	/**
	 * 内嵌内容
	 */
	private String[] relateds;

	/**
	 * 正文,附件和图片的混合部分
	 */
	private final Multipart multipart = new MimeMultipart();

	/**
	 * 从默认配置文件中加载配置,顺序为mail.properties->config/mail.properties,后面的覆盖前面的
	 */
	public Mail() {
		try {
			this.properties = MailAccount.loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从指定配置文件中加载配置,文件地址必须是绝对路径
	 */
	public Mail(String path) {
		try {
			this.properties = MailAccount.loadProperties(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Mail(MailAccount mailAccount) {
		super();
		this.mailAccount = mailAccount;
		this.mailAccount.buildDefaultProperty();
		this.properties = this.mailAccount.buildProperties();
	}

	public Mail(MailAccount mailAccount, String[] toAddress, String subject, String content) {
		this(mailAccount);
		this.toAddress = toAddress;
		this.subject = subject;
		this.content = content;
		checkParam();
	}

	public Mail(MailAccount mailAccount, String[] toAddress, String subject, String content, String[] ccs) {
		this(mailAccount, toAddress, subject, content);
		this.ccs = ccs;
	}

	public Mail(MailAccount mailAccount, String[] toAddress, String subject, String content, String[] ccs,
			String[] bccs) {
		this(mailAccount, toAddress, subject, content, ccs);
		this.bccs = bccs;
	}

	public Mail(MailAccount mailAccount, String[] toAddress, String subject, String content, String[] ccs,
			String[] bccs, String[] attachs) {
		this(mailAccount, toAddress, subject, content, ccs, bccs);
		this.attachs = attachs;
	}

	public Mail(MailAccount mailAccount, String[] toAddress, String subject, String content, String[] ccs,
			String[] bccs, String[] attachs, String[] relateds) {
		this(mailAccount, toAddress, subject, content, ccs, bccs, attachs);
		this.relateds = relateds;
	}

	/**
	 * 检查邮件参数
	 * 
	 * @return 错误信息,null表示检查正常
	 */
	public String checkParam() {
		if (this.toAddress == null || this.toAddress.length == 0) {
			return TipEnum.TIP_MAIL_TO_NULL.getMsg();
		}
		if (StrTool.isBlank(this.subject)) {
			this.subject = "来自" + this.mailAccount.getFromAddress() + "的邮件";
		}
		if (StrTool.isBlank(this.content)) {
			return TipEnum.TIP_MAIL_CONTENT_NULL.getMsg();
		}
		return null;
	}

	public MailAccount getMailAccount() {
		return mailAccount;
	}

	public void setMailAccount(MailAccount mailAccount) {
		this.mailAccount = mailAccount;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getToAddress() {
		return toAddress;
	}

	public void setToAddress(String[] toAddress) {
		this.toAddress = toAddress;
	}

	public String[] getCcs() {
		return ccs;
	}

	public void setCcs(String[] ccs) {
		this.ccs = ccs;
	}

	public String[] getBccs() {
		return bccs;
	}

	public void setBccs(String[] bccs) {
		this.bccs = bccs;
	}

	public boolean isHtml() {
		return html;
	}

	public void setHtml(boolean html) {
		this.html = html;
	}

	public String[] getAttachs() {
		return attachs;
	}

	public void setAttachs(String[] attachs) {
		this.attachs = attachs;
	}

	public String[] getRelateds() {
		return relateds;
	}

	public void setRelateds(String[] relateds) {
		this.relateds = relateds;
	}

	public Multipart getMultipart() {
		return multipart;
	}
}