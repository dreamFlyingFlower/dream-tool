package com.wy.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.sun.mail.util.MailSSLSocketFactory;
import com.wy.digest.DigestTool;
import com.wy.lang.StrTool;
import com.wy.result.Result;
import com.wy.util.CharsetTool;

/**
 * Javax.mail邮件发送 FIXME
 * 
 * @author 飞花梦影
 * @date 2021-04-01 10:01:38
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class MailTools {

	/**
	 * 邮件发送参数校验
	 * 
	 * @param mail 邮件信息
	 * @return 结果
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public static <T> Result<T> sendMail(final Mail mail) throws MessagingException, UnsupportedEncodingException {
		String error = mail.checkParam();
		if (StrTool.isNotBlank(error)) {
			return Result.error(error);
		}
		Properties prop = mail.getProperties();
		// 身份验证
		MailAuthenticator auth = new MailAuthenticator(mail.getMailAccount().getUsername(),
				mail.getMailAccount().getPassword());
		// 根据会话属性和验证构造一个发送邮件的session
		Session sessionMail = Session.getInstance(prop, auth);
		// 设置日志级别
		sessionMail.setDebug(mail.isDebug());
		// 创建一个邮件消息
		Message message = new MimeMessage(sessionMail);
		// 设置主题
		message.setSubject(mail.getSubject());
		// 设置发送时间
		message.setSentDate(new Date());
		// 创建一个发送者地址
		Address from = new InternetAddress(mail.getMailAccount().getFromAddress());
		message.setFrom(from);
		// 邮件接收者地址,群发
		Address[] tos = address(mail.getToAddress());
		if (tos == null || tos.length == 0) {
			return Result.error("邮件发送地址为空或添加发送地址失败");
		}
		message.setRecipients(Message.RecipientType.TO, tos);
		// 处理抄送和密送
		handleBccsCss(mail, message);
		// 设置内容,普通邮件
		// multipart和content只能有一个,但是content可以包含一个multipart,也只能包含一个
		// multipart可以包含多个multibodypart
		// 设置内容,html内容,创建一个容器类,可不传参数,related标识有内嵌,mixed标识有附件
		Multipart part = new MimeMultipart("mixed");
		if (StrTool.isNotBlank(mail.getContent())) {
			// 创建一个包含html内容的容器
			MimeBodyPart html = new MimeBodyPart();
			// 设置html邮件的普通内容
			html.setContent(mail.getContent(), "text/html;charset=utf8");
			part.addBodyPart(html);
		}
		// 处理附件
		handlerAttach(mail, part);
		// 处理内嵌文件
		handlerEmbed(mail, part);
		message.setContent(part);
		message.saveChanges();
		Transport.send(message);
		return Result.ok();
	}

	/**
	 * 通用邮件处理
	 */
	public static Properties getProp(String host, int port, boolean isSSL) {
		Properties prop = new Properties();
		prop.put("mail.transport.protocol", "smtp");// 邮件服务类型
		prop.put("mail.smtp.auth", "true"); // 开启验证
		prop.put("mail.host", host);// 邮件服务器
		prop.put("mail.port", port);// 邮箱端口,qq端口是465
		// 是否开启ssl加密验证
		if (isSSL) {
			try {
				MailSSLSocketFactory sf = new MailSSLSocketFactory();
				sf.setTrustAllHosts(true);
				prop.put("mail.smtp.ssl.enable", "true");
				prop.put("mail.smtp.ssl.socketFactory", sf);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return prop;
	}

	/**
	 * 添加发送地址
	 * 
	 * @param ads 邮件地址
	 * @return 邮件网络地址
	 * @throws AddressException
	 */
	private static InternetAddress[] address(String[] ads) throws AddressException {
		if (ads == null || ads.length == 0) {
			return null;
		}
		int length = ads.length;
		InternetAddress[] des = new InternetAddress[length];
		for (int i = 0; i < length; i++) {
			des[i] = new InternetAddress(ads[i]);
		}
		return des;
	}

	/**
	 * 处理抄送,密送
	 * 
	 * @param mail 邮件信息
	 * @param message 邮件信息
	 * @throws MessagingException
	 */
	private static void handleBccsCss(Mail mail, Message message) throws MessagingException {
		// 抄送
		Address[] ccs = address(mail.getCcs());
		// 密送
		Address[] bccs = address(mail.getBccs());
		// 设置邮件接收者
		if (null != ccs && ccs.length != 0) {
			message.setRecipients(Message.RecipientType.CC, ccs);
		}
		if (null != bccs && bccs.length != 0) {
			message.setRecipients(Message.RecipientType.BCC, bccs);
		}
	}

	/**
	 * 处理附件
	 * 
	 * @param mail 邮件信息
	 * @param part 附件信息
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	private static void handlerAttach(Mail mail, Multipart part)
			throws UnsupportedEncodingException, MessagingException {
		// 添加附件
		if (mail.getAttachs() != null && mail.getAttachs().length > 0) {
			String[] attachs = mail.getAttachs();
			for (String attach : attachs) {
				handlerAttach(part, attach, DigestTool.UUID());
			}
		}
	}

	/**
	 * 处理附件
	 * 
	 * @param multiparts 单个附件信息
	 * @param srcFile 附件绝对地址
	 * @param contentId 邮件唯一标识
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	private static void handlerAttach(Multipart multiparts, String srcFile, String contentId)
			throws MessagingException, UnsupportedEncodingException {
		MimeBodyPart attach = new MimeBodyPart();
		File file = new File(srcFile);
		attach.setDataHandler(new DataHandler(new FileDataSource(file)));
		attach.setFileName(MimeUtility.encodeText(file.getName(), CharsetTool.defaultCharset().name(),
				CharsetTool.defaultCharset().name()));
		attach.setContentID(contentId);
		multiparts.addBodyPart(attach);
	}

	/**
	 * 处理内嵌资源
	 * 
	 * @param mail 邮件信息
	 * @param part 附件信息
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	private static void handlerEmbed(Mail mail, Multipart part)
			throws MessagingException, UnsupportedEncodingException {
		// 添加内嵌资源
		if (mail.getRelateds() != null && mail.getRelateds().length > 0) {
			String[] relateds = mail.getRelateds();
			for (String related : relateds) {
				MimeBodyPart image = new MimeBodyPart();
				String contentId = DigestTool.UUID();
				image.setContent(String.format("<img src=cid:%s width=500 height=600 />", "text/html;charset=utf8"),
						contentId);
				part.addBodyPart(image);
				handlerAttach(part, related, contentId);
			}
		}
	}
}