package com.wy.third.ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.wy.io.file.FileTool;

import lombok.extern.slf4j.Slf4j;

/**
 * SFTP工具类. 连接服务器,上传下载文件
 * 
 * @author 飞花梦影
 * @date 2022-03-21 14:43:16
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
public class SftpTools {

	/**
	 * 连接sftp服务器
	 * 
	 * @param host 主机
	 * @param port 端口
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	public ChannelSftp connect(String host, int port, String username, String password) {
		ChannelSftp sftp = null;
		JSch jsch = new JSch();
		try {
			Session sshSession = jsch.getSession(username, host, port);
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
		} catch (JSchException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return sftp;
	}

	/**
	 * 上传文件
	 * 
	 * @param directory 上传的目录
	 * @param uploadFile 要上传的文件
	 * @param channel
	 */
	public static void upload(String directory, String uploadFile, ChannelSftp channel) {
		createDir(directory, channel);
		try {
			channel.cd(directory);
			File file = new File(uploadFile);
			channel.put(new FileInputStream(file), file.getName());
		} catch (SftpException | FileNotFoundException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			close(channel);
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param directory 下载目录
	 * @param downloadFilePath 下载的文件
	 * @param saveFile 存在本地的路径
	 * @param channel
	 */
	public static void download(String directory, String downloadFilePath, String saveFile, ChannelSftp channel) {
		try {
			channel.cd(directory);
			File file = new File(saveFile);
			channel.get(downloadFilePath, new FileOutputStream(file));
		} catch (IOException | SftpException e) {
			log.error("sftp download exception:{}", e.getMessage());
		} finally {
			close(channel);
		}
	}

	/**
	 * 下载文件
	 * 
	 * @param directory 下载目录
	 * @param downloadFile 下载的文件
	 * @param saveFile 存在本地的路径
	 * @param channelSftp
	 */
	public static String downloadGetString(String directory, String downloadFile, String saveFile,
			ChannelSftp channelSftp) {
		try {
			channelSftp.cd(directory);
			File file = new File(saveFile);
			channelSftp.get(downloadFile, new FileOutputStream(file));
			return readFileByLines(file.getPath());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("sftp downloadGetString excetpin:{}", e.getMessage());
		}
		return null;
	}

	/**
	 * 以行为单位读取文件,常用于读面向行的格式化文件
	 */
	public static String readFileByLines(String fileName) {
		StringBuffer sb = new StringBuffer();
		File file = new File(fileName);
		try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString);
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return sb.toString();
	}

	/**
	 * 删除文件
	 * 
	 * @param directory 要删除文件所在目录
	 * @param deleteFile 要删除的文件
	 * @param sftp
	 */
	public void delete(String directory, String deleteFile, ChannelSftp sftp) {
		try {
			sftp.cd(directory);
			sftp.rm(deleteFile);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * 列出目录下的文件
	 * 
	 * @param directory 要列出的目录
	 * @param sftp
	 * @return
	 * @throws SftpException
	 */
	public Vector<?> listFiles(String directory, ChannelSftp sftp) {
		try {
			return sftp.ls(directory);
		} catch (SftpException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据传入的目录创建文件夹
	 * 
	 * @param directory
	 * @param sftp
	 * @throws SftpException
	 */
	public static void createDir(String directory, ChannelSftp sftp) {
		String[] dirArr = directory.split("/");
		StringBuffer tempStr = new StringBuffer("");
		try {
			for (int i = 1; i < dirArr.length; i++) {
				tempStr.append("/" + dirArr[i]);
				sftp.cd(tempStr.toString());
				sftp.mkdir(tempStr.toString());
			}
		} catch (SftpException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 关闭Sftp
	 * 
	 * @param channel
	 */
	public static void close(ChannelSftp channel) {
		try {
			if (channel != null) {
				channel.getSession().disconnect();
			}
		} catch (JSchException e) {
			e.getMessage();
			log.error(e.getMessage());
		}
	}

	/**
	 * 连接SFTP服务器,根据文件路径读取文件文本内容
	 * 
	 * @param dataFilePath SFTP保存的文件全路径
	 * @return 文件内容.
	 */
	public static String getFileContentFormSFTP(final ChannelSftp channelSftp, final String dataFilePath) {
		String property = System.getProperty("user.dir") + File.separator + "temp/";
		FileTool.checkDirectory(new File(property));
		String directory = dataFilePath.substring(0, dataFilePath.lastIndexOf("/")); // 文件路径
		String downloadFile = dataFilePath.substring(dataFilePath.lastIndexOf("/") + 1); // 文件名称
		String saveFile = property + "/" + downloadFile; // 保存文件路径
		log.info("==>从SFTP获取文件内容,源文件路径[" + dataFilePath + "], 保存本地的临时文件路径[" + saveFile + "]");
		return downloadGetString(directory, downloadFile, saveFile, channelSftp);
	}

	/**
	 * 从SFTP服务器上下载文件
	 * 
	 * @return
	 */
	public static File downFileFromSFTP(ChannelSftp channelSftp, final String filePath) {
		// 创建临时目录,用来存放下载的文件
		StringBuffer tempFilePath =
				new StringBuffer(System.getProperty("user.dir")).append(File.separator).append("temp");
		isDir(tempFilePath.toString());
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		String tempPath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
		// 创建临时返回文件
		String saveFile = tempFilePath + "/" + fileName;
		File returnFile = new File(saveFile);
		try {
			download(tempPath, fileName, saveFile, channelSftp);
		} finally {
			close(channelSftp);
		}
		return returnFile;
	}

	/**
	 * 传入文件夹路径,该方法能够实现创建整个路径
	 * 
	 * @param path 文件夹路径,不包含文件名称及后缀名
	 */
	public static void isDir(String path) {
		String[] paths = path.split("/");
		String filePath = "";
		for (int i = 0; i < paths.length; i++) {
			if (i == 0) {
				filePath = paths[0];
			} else {
				filePath += "/" + paths[i];
			}
			creatDir(filePath);
		}
	}

	/**
	 * 该方法用来判断文件夹是否存在,如果不存在则创建,存在则什么都不做
	 * 
	 * @param filePath
	 */
	public static void creatDir(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	// 测试例子
	public static void main(String[] args) {
		SftpTools sf = new SftpTools();
		String host = "192.168.88.40";
		int port = 3210;
		String username = "gwpayfast";
		String password = "gzzyzz.com";
		String directory = "/home/gwpayfast/";

		// String downloadFile = "Result.txt";
		// String saveFile = "F:\\123.txt";

		String uploadFile = "E:\\PINGANBANK-NET-B2C-GZ20140523clear.txt";
		// String deleteFile = "delete.txt";
		ChannelSftp sftp = sf.connect(host, port, username, password);
		upload(directory, uploadFile, sftp);
		// sf.download(directory, downloadFile, saveFile, sftp);
		// sf.delete(directory, deleteFile, sftp);
		try {
			// sf.creatDir(directory, sftp);
			// sftp.cd(directory);
			// System.out.println("finished");
			// sf.close(sftp);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			close(sftp);
		}
	}
}