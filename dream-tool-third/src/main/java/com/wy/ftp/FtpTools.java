package com.wy.ftp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import lombok.extern.slf4j.Slf4j;

/**
 * FTP文件工具类
 * 
 * @author 飞花梦影
 * @date 2022-03-21 14:31:24
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public class FtpTools {

	/**
	 * 初始化FTPClient实例
	 * 
	 * @param timeout 连接超时时间,单位毫秒
	 * @return FTPClient
	 */
	public static FTPClient newInstance(int timeout) {
		FTPClient ftpClient = new FTPClient();
		// 设置连接超时间
		ftpClient.setConnectTimeout(timeout);
		ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		return ftpClient;
	}

	/**
	 * 得到当前ftp目录下的文件列表
	 * 
	 * @param tempDir
	 * @return
	 */
	public static FTPFile[] listFiles(FTPClient ftpClient, String tempDir) {
		FTPFile[] ff = null;
		try {
			ff = ftpClient.listFiles(tempDir);
		} catch (IOException e) {
			return null;
		}
		return ff;
	}

	/**
	 * FTP 连接 登陆
	 * 
	 * @param hostname 连接域名
	 * @param port 连接端口
	 * @param username 登录用户名
	 * @param password 登录密码
	 * @return true->连接成功;false->连接失败
	 */
	public static boolean connect(String hostname, int port, String username, String password) {
		FTPClient ftpClient = newInstance(20000);
		try {
			ftpClient.connect(hostname, port);
			log.info("FTP 远程连接成功");
			if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				if (ftpClient.login(username, password)) {
					log.info("FTP 远程登陆成功");
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 从FTP远程服务器下载文件
	 * 
	 * @param ftpClient FTPClient实例
	 * @param remote 远程地址
	 * @param out 输入流
	 * @return true->下载成功;false->下载失败
	 */
	public static boolean download(FTPClient ftpClient, String remote, OutputStream out) {
		log.info("FTP 远程连接,文件开始下载... ...");
		ftpClient.enterLocalPassiveMode();
		boolean result = false;
		try {
			result = ftpClient.retrieveFile(remote, out);
			out.close();
			close(ftpClient);
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	/**
	 * 关闭FTP连接
	 * 
	 * @param ftpClient FTPClient
	 */
	public static void close(FTPClient ftpClient) {
		if (ftpClient != null && ftpClient.isConnected()) {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}