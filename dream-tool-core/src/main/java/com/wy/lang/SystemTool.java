package com.wy.lang;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import com.wy.Constant;
import com.wy.io.file.FileNameTool;
import com.wy.result.ResultException;
import com.wy.util.ThreadMonitor;

/**
 * System工具类
 * 
 * @author 飞花梦影
 * @date 2021-03-04 20:26:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SystemTool {

	/** Windows系统属性前缀 */
	private static final String OS_NAME_WINDOWS_PREFIX = "Windows";

	/** Awt Toolkit,如Windows上为sun.awt.windows.WToolkit,在没有GUI的系统上为null */
	public static final String AWT_TOOLKIT = getSystemProperty("awt.toolkit");

	/** Java文件编码集,如UTF-8 */
	public static final String FILE_ENCODING = getSystemProperty("file.encoding");

	private SystemTool() {}

	/**
	 * Returns the free space on a drive or volume in kilobytes by invoking the command line.
	 * 
	 * <pre>
	 * FileSystemUtils.freeSpaceKb("C:"); // Windows
	 * FileSystemUtils.freeSpaceKb("/volume"); // *nix
	 * </pre>
	 * 
	 * The free space is calculated via the command line. It uses 'dir /-c' on Windows, 'df -kP' on
	 * AIX/HP-UX and 'df -k' on other Unix.
	 * <p>
	 * In order to work, you must be running Windows, or have a implementation of Unix df that supports
	 * GNU format when passed -k (or -kP). If you are going to rely on this code, please check that it
	 * works on your OS by running some simple tests to compare the command line with the output from
	 * this class. If your operating system isn't supported, please raise a JIRA call detailing the
	 * exact result from df -k and as much other detail as possible, thanks.
	 *
	 * @param path the path to get free space for, not null, not empty on Unix
	 * @return the amount of free drive space on the drive or volume in kilobytes
	 * @throws IllegalArgumentException if the path is invalid
	 * @throws IllegalStateException if an error occurred in initialisation
	 * @throws IOException if an error occurs when finding the free space
	 * @since 1.2, enhanced OS support in 1.3
	 */
	public static long freeSpaceKb(String path) throws IOException {
		return freeSpaceKb(path, -1);
	}

	/**
	 * Returns the free space on a drive or volume in kilobytes by invoking the command line.
	 * 
	 * <pre>
	 * FileSystemUtils.freeSpaceKb("C:"); // Windows
	 * FileSystemUtils.freeSpaceKb("/volume"); // *nix
	 * </pre>
	 * 
	 * The free space is calculated via the command line. It uses 'dir /-c' on Windows, 'df -kP' on
	 * AIX/HP-UX and 'df -k' on other Unix.
	 * <p>
	 * In order to work, you must be running Windows, or have a implementation of Unix df that supports
	 * GNU format when passed -k (or -kP). If you are going to rely on this code, please check that it
	 * works on your OS by running some simple tests to compare the command line with the output from
	 * this class. If your operating system isn't supported, please raise a JIRA call detailing the
	 * exact result from df -k and as much other detail as possible, thanks.
	 *
	 * @param path the path to get free space for, not null, not empty on Unix
	 * @param timeout The timout amount in milliseconds or no timeout if the value is zero or less
	 * @return the amount of free drive space on the drive or volume in kilobytes
	 * @throws IllegalArgumentException if the path is invalid
	 * @throws IllegalStateException if an error occurred in initialisation
	 * @throws IOException if an error occurs when finding the free space
	 */
	public static long freeSpaceKb(String path, long timeout) throws IOException {
		return freeSpaceOS(path, PropertySystem.OS_NAME, true, timeout);
	}

	/**
	 * Returns the disk size of the volume which holds the working directory.
	 * <p>
	 * Identical to:
	 * 
	 * <pre>
	 * freeSpaceKb(new File(".").getAbsolutePath())
	 * </pre>
	 * 
	 * @return the amount of free drive space on the drive or volume in kilobytes
	 * @throws IllegalStateException if an error occurred in initialisation
	 * @throws IOException if an error occurs when finding the free space
	 */
	public static long freeSpaceKb() throws IOException {
		return freeSpaceKb(-1);
	}

	/**
	 * Returns the disk size of the volume which holds the working directory.
	 * <p>
	 * Identical to:
	 * 
	 * <pre>
	 * freeSpaceKb(new File(".").getAbsolutePath())
	 * </pre>
	 * 
	 * @param timeout The timout amount in milliseconds or no timeout if the value is zero or less
	 * @return the amount of free drive space on the drive or volume in kilobytes
	 * @throws IllegalStateException if an error occurred in initialisation
	 * @throws IOException if an error occurs when finding the free space
	 * @since 2.0
	 */
	public static long freeSpaceKb(long timeout) throws IOException {
		return freeSpaceKb(new File(".").getAbsolutePath(), timeout);
	}

	// -----------------------------------------------------------------------
	/**
	 * Returns the free space on a drive or volume in a cross-platform manner. Note that some OS's are
	 * NOT currently supported, including OS/390.
	 * 
	 * <pre>
	 * FileSystemUtils.freeSpace("C:"); // Windows
	 * FileSystemUtils.freeSpace("/volume"); // *nix
	 * </pre>
	 * 
	 * The free space is calculated via the command line. It uses 'dir /-c' on Windows and 'df' on *nix.
	 *
	 * @param path the path to get free space for, not null, not empty on Unix
	 * @param os the operating system code
	 * @param kb whether to normalize to kilobytes
	 * @param timeout The timout amount in milliseconds or no timeout if the value is zero or less
	 * @return the amount of free drive space on the drive or volume
	 * @throws IllegalArgumentException if the path is invalid
	 * @throws IllegalStateException if an error occurred in initialisation
	 * @throws IOException if an error occurs when finding the free space
	 */
	static long freeSpaceOS(String path, String os, boolean kb, long timeout) throws IOException {
		if (path == null) {
			throw new IllegalArgumentException("Path must not be empty");
		}
		if (isOSNameMatch(PropertySystem.OS_NAME, "windows")) {
			return kb ? freeSpaceWindows(path, timeout) / Constant.IO.ONE_KB : freeSpaceWindows(path, timeout);
		} else if (isOSNameMatch(PropertySystem.OS_NAME, "unix")) {
			return freeSpaceUnix(path, kb, false, timeout);
		} else {
			throw new IllegalStateException("Unsupported operating system");
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Find free space on the *nix platform using the 'df' command.
	 *
	 * @param path the path to get free space for
	 * @param kb whether to normalize to kilobytes
	 * @param posix whether to use the posix standard format flag
	 * @param timeout The timout amount in milliseconds or no timeout if the value is zero or less
	 * @return the amount of free drive space on the volume
	 * @throws IOException if an error occurs
	 */
	static long freeSpaceUnix(String path, boolean kb, boolean posix, long timeout) throws IOException {
		if (path.length() == 0) {
			throw new IllegalArgumentException("Path must not be empty");
		}

		// build and run the 'dir' command
		String flags = "-";
		if (kb) {
			flags += "k";
		}
		if (posix) {
			flags += "P";
		}
		String[] cmdAttribs = flags.length() > 1 ? new String[] { "df", flags, path } : new String[] { "df", path };

		// perform the command, asking for up to 3 lines (header, interesting, overflow)
		List<String> lines = performCommand(cmdAttribs, 3, timeout);
		if (lines.size() < 2) {
			// unknown problem, throw exception
			throw new IOException("Command line '" + "df" + "' did not return info as expected " + "for path '" + path
					+ "'- response was " + lines);
		}
		String line2 = lines.get(1); // the line we're interested in

		// Now, we tokenize the string. The fourth element is what we want.
		StringTokenizer tok = new StringTokenizer(line2, " ");
		if (tok.countTokens() < 4) {
			// could be long Filesystem, thus data on third line
			if (tok.countTokens() == 1 && lines.size() >= 3) {
				String line3 = lines.get(2); // the line may be interested in
				tok = new StringTokenizer(line3, " ");
			} else {
				throw new IOException("Command line '" + "df" + "' did not return data as expected " + "for path '"
						+ path + "'- check path is valid");
			}
		} else {
			tok.nextToken(); // Ignore Filesystem
		}
		tok.nextToken(); // Ignore 1K-blocks
		tok.nextToken(); // Ignore Used
		String freeSpace = tok.nextToken();
		return parseBytes(freeSpace, path);
	}

	// -----------------------------------------------------------------------
	/**
	 * Find free space on the Windows platform using the 'dir' command.
	 *
	 * @param path the path to get free space for, including the colon
	 * @param timeout The timout amount in milliseconds or no timeout if the value is zero or less
	 * @return the amount of free drive space on the drive
	 * @throws IOException if an error occurs
	 */
	static long freeSpaceWindows(String path, long timeout) throws IOException {
		path = FileNameTool.normalize(path, false);
		if (path.length() > 0 && path.charAt(0) != '"') {
			path = "\"" + path + "\"";
		}

		// build and run the 'dir' command
		String[] cmdAttribs = new String[] { "cmd.exe", "/C", "dir /a /-c " + path };

		// read in the output of the command to an ArrayList
		List<String> lines = performCommand(cmdAttribs, Integer.MAX_VALUE, timeout);

		// now iterate over the lines we just read and find the LAST
		// non-empty line (the free space bytes should be in the last element
		// of the ArrayList anyway, but this will ensure it works even if it's
		// not, still assuming it is on the last non-blank line)
		for (int i = lines.size() - 1; i >= 0; i--) {
			String line = lines.get(i);
			if (line.length() > 0) {
				return parseDir(line, path);
			}
		}
		// all lines are blank
		throw new IOException("Command line 'dir /-c' did not return any info " + "for path '" + path + "'");
	}

	/**
	 * 获得系统环境变量
	 *
	 * @param name 环境变量名
	 * @param defaultValue 默认值
	 * @return 环境变量值
	 */
	public static String getEnvVariable(final String name, final String defaultValue) {
		try {
			final String value = System.getenv(name);
			return value == null ? defaultValue : value;
		} catch (final SecurityException ex) {
			return defaultValue;
		}
	}

	/**
	 * 获得JRE目录
	 *
	 * @return JRE目录
	 * @throws SecurityException
	 */
	public static File getJavaHome() {
		return new File(PropertyJava.JAVA_HOME);
	}

	/**
	 * 获得Windows操作系统的电脑名或主机名
	 *
	 * @return 主机名
	 */
	public static String getHostName() {
		return WindowsOS.IS_OS_WINDOWS ? System.getenv("COMPUTERNAME") : System.getenv("HOSTNAME");
	}

	/**
	 * 获得系统临时文件目录
	 *
	 * @return 系统临时文件目录
	 * @throws SecurityException
	 */
	public static File getJavaIoTmpDir() {
		return new File(PropertyJava.JAVA_IO_TMPDIR);
	}

	/**
	 * 获得本地服务器mac地址,获取失败返回null
	 * 
	 * @return mac地址,失败返回null
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public static String getLocalMac() throws SocketException, UnknownHostException {
		try (Formatter formatter = new Formatter();) {
			NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			byte[] mac = ni.getHardwareAddress();
			for (int i = 0; i < mac.length; i++) {
				formatter.format(Locale.getDefault(), "%02X%s", mac[i], (i < mac.length - 1) ? "-" : "").toString();
			}
			return formatter.toString();
		}
	}

	/**
	 * 返回进程pid
	 * 
	 * @return 当前进程pid字符串
	 */
	public static String getPid() {
		String info = ManagementFactory.getRuntimeMXBean().getName();
		return info.split("@")[0];
	}

	/**
	 * 根据给定的参数获得系统属性,不同系统有不同值,也可能为null
	 *
	 * @param property 系统参数
	 * @return 系统参数值
	 */
	public static String getSystemProperty(final String property) {
		return System.getProperty(property);
	}

	/**
	 * 当前项目的根目录
	 *
	 * @return 当前项目的根目录
	 * @throws SecurityException
	 */
	public static File getUserDir() {
		return new File(PropertyUser.USER_DIR);
	}

	/**
	 *
	 * 获得用户根目录
	 *
	 * @return 用户根目录
	 * @throws SecurityException
	 */
	public static File getUserHome() {
		return new File(PropertyUser.USER_HOME);
	}

	/**
	 * 获得用户账号名
	 *
	 * @return 账号名
	 * @throws SecurityException
	 */
	public static String getUserName() {
		return PropertyUser.USER_NAME;
	}

	/**
	 * 获得用户账号名
	 *
	 * @param defaultValue 默认账号名
	 * @return 账号名
	 * @throws SecurityException
	 */
	public static String getUserName(final String defaultValue) {
		return null == PropertyUser.USER_NAME ? defaultValue : PropertyUser.USER_NAME;
	}

	/**
	 * 判断当前系统java.awt.headless是否为真
	 *
	 * @return true当java.awt.headless为"true"
	 */
	public static boolean isJavaAwtHeadless() {
		return Boolean.TRUE.toString().equals(PropertyJava.JAVA_AWT_HEADLESS);
	}

	/**
	 * 判断当前系统JDK版本是否为指定版本
	 *
	 * @param versionPrefix 指定版本前缀,如1.8
	 * @return true当匹配
	 */
	public static boolean isJavaVersionMatch(final String versionPrefix) {
		return isJavaVersionMatch(PropertyJava.JAVA_SPECIFICATION_VERSION, versionPrefix);
	}

	/**
	 * 判断JDK版本是否匹配
	 *
	 * @param version 真实版本号
	 * @param versionPrefix 简写版本号
	 * @return true当版本号匹配
	 */
	public static boolean isJavaVersionMatch(final String version, final String versionPrefix) {
		return null == null ? false : version.startsWith(versionPrefix);
	}

	/**
	 * 判断当前操作系统名和版本是否匹配
	 *
	 * @param osNamePrefix 操作系统前缀
	 * @param osVersionPrefix 操作系统版本前缀
	 * @return true当操作系统名和版本匹配
	 */
	public static boolean isOsMatch(final String osNamePrefix, final String osVersionPrefix) {
		return isOSMatch(PropertySystem.OS_NAME, PropertySystem.OS_VERSION, osNamePrefix, osVersionPrefix);
	}

	/**
	 * 判断指定操作系统名和操作系统版本是否匹配
	 *
	 * @param osName 操作系统名
	 * @param osVersion 操作系统版本
	 * @param osNamePrefix 操作系统简称
	 * @param osVersionPrefix 操作系统版本号简写
	 * @return true当操作系统名和版本匹配
	 */
	public static boolean isOSMatch(final String osName, final String osVersion, final String osNamePrefix,
			final String osVersionPrefix) {
		if (osName == null || osVersion == null) {
			return false;
		}
		return isOSNameMatch(osName, osNamePrefix) && isOSVersionMatch(osVersion, osVersionPrefix);
	}

	/**
	 * 判断操作系统名是否匹配
	 *
	 * @param osNamePrefix 操作系统前缀
	 * @return true当操作系统名匹配
	 */
	public static boolean isOSNameMatch(final String osNamePrefix) {
		return isOSNameMatch(PropertySystem.OS_NAME.toLowerCase(), osNamePrefix);
	}

	/**
	 * 判断操作系统名是否匹配
	 *
	 * @param osName 操作系统名
	 * @param osNamePrefix 操作系统简称
	 * @return true当操作系统名匹配
	 */
	public static boolean isOSNameMatch(final String osName, final String osNamePrefix) {
		return null == osName ? false : osName.startsWith(osNamePrefix.toLowerCase());
	}

	/**
	 * 判断操作系统版本是否匹配
	 *
	 * @param osVersion 操作系统版本
	 * @param osVersionPrefix 操作系统版本号简写
	 * @return true当操作系统版本匹配
	 */
	public static boolean isOSVersionMatch(final String osVersion, final String osVersionPrefix) {
		if (StrTool.isEmpty(osVersion)) {
			return false;
		}
		final String[] versionPrefixParts = osVersionPrefix.split("\\.");
		final String[] versionParts = osVersion.split("\\.");
		for (int i = 0; i < Math.min(versionPrefixParts.length, versionParts.length); i++) {
			if (!versionPrefixParts[i].equals(versionParts[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 通过文件分隔符判断是Unix系统
	 * 
	 * @return true->是Unix系统,false->不是
	 */
	public static boolean isUnix() {
		return File.separatorChar == Constant.Langes.SEPARATOR_UNIX;
	}

	/**
	 * 通过文件分隔符判断是Windows系统
	 * 
	 * @return true->是Windows系统,false->不是
	 */
	public static boolean isWindows() {
		return File.separatorChar == Constant.Langes.SEPARATOR_WINDOWS;
	}

	/**
	 * Opens the process to the operating system.
	 *
	 * @param cmdAttribs the command line parameters
	 * @return the process
	 * @throws IOException if an error occurs
	 */
	static Process openProcess(String[] cmdAttribs) throws IOException {
		return Runtime.getRuntime().exec(cmdAttribs);
	}

	/**
	 * Parses the Windows dir response last line
	 *
	 * @param line the line to parse
	 * @param path the path that was sent
	 * @return the number of bytes
	 * @throws IOException if an error occurs
	 */
	static long parseDir(String line, String path) throws IOException {
		// read from the end of the line to find the last numeric
		// character on the line, then continue until we find the first
		// non-numeric character, and everything between that and the last
		// numeric character inclusive is our free space bytes count
		int bytesStart = 0;
		int bytesEnd = 0;
		int j = line.length() - 1;
		innerLoop1: while (j >= 0) {
			char c = line.charAt(j);
			if (Character.isDigit(c)) {
				// found the last numeric character, this is the end of
				// the free space bytes count
				bytesEnd = j + 1;
				break innerLoop1;
			}
			j--;
		}
		innerLoop2: while (j >= 0) {
			char c = line.charAt(j);
			if (!Character.isDigit(c) && c != ',' && c != '.') {
				// found the next non-numeric character, this is the
				// beginning of the free space bytes count
				bytesStart = j + 1;
				break innerLoop2;
			}
			j--;
		}
		if (j < 0) {
			throw new IOException("Command line 'dir /-c' did not return valid info " + "for path '" + path + "'");
		}

		// remove commas and dots in the bytes count
		StringBuilder buf = new StringBuilder(line.substring(bytesStart, bytesEnd));
		for (int k = 0; k < buf.length(); k++) {
			if (buf.charAt(k) == ',' || buf.charAt(k) == '.') {
				buf.deleteCharAt(k--);
			}
		}
		return parseBytes(buf.toString(), path);
	}

	// -----------------------------------------------------------------------
	/**
	 * Parses the bytes from a string.
	 * 
	 * @param freeSpace the free space string
	 * @param path the path
	 * @return the number of bytes
	 * @throws IOException if an error occurs
	 */
	static long parseBytes(String freeSpace, String path) throws IOException {
		try {
			long bytes = Long.parseLong(freeSpace);
			if (bytes < 0) {
				throw new IOException("Command line '" + "df" + "' did not find free space in response " + "for path '"
						+ path + "'- check path is valid");
			}
			return bytes;

		} catch (NumberFormatException ex) {
			throw new ResultException("Command line '" + "df" + "' did not return numeric data as expected "
					+ "for path '" + path + "'- check path is valid", ex);
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Performs the os command.
	 *
	 * @param cmdAttribs the command line parameters
	 * @param max The maximum limit for the lines returned
	 * @param timeout The timout amount in milliseconds or no timeout if the value is zero or less
	 * @return the parsed data
	 * @throws IOException if an error occurs
	 */
	static List<String> performCommand(String[] cmdAttribs, int max, long timeout) throws IOException {
		// this method does what it can to avoid the 'Too many open files' error
		// based on trial and error and these links:
		// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4784692
		// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4801027
		// http://forum.java.sun.com/thread.jspa?threadID=533029&messageID=2572018
		// however, its still not perfect as the JDK support is so poor
		// (see commond-exec or ant for a better multi-threaded multi-os solution)
		List<String> lines = new ArrayList<String>(20);
		Process proc = openProcess(cmdAttribs);
		try (InputStream in = proc.getInputStream();
				OutputStream out = proc.getOutputStream();
				InputStream err = proc.getErrorStream();
				BufferedReader inr = new BufferedReader(new InputStreamReader(in));) {
			Thread monitor = ThreadMonitor.start(timeout);
			String line = inr.readLine();
			while (line != null && lines.size() < max) {
				line = line.toLowerCase(Locale.ENGLISH).trim();
				lines.add(line);
				line = inr.readLine();
			}
			proc.waitFor();
			ThreadMonitor.stop(monitor);
			if (proc.exitValue() != 0) {
				// os command problem, throw exception
				throw new IOException("Command line returned OS error code '" + proc.exitValue() + "' for command "
						+ Arrays.asList(cmdAttribs));
			}
			if (lines.isEmpty()) {
				// unknown problem, throw exception
				throw new IOException(
						"Command line did not return any info " + "for command " + Arrays.asList(cmdAttribs));
			}
			return lines;
		} catch (InterruptedException ex) {
			throw new ResultException("Command line threw an InterruptedException " + "for command "
					+ Arrays.asList(cmdAttribs) + " timeout=" + timeout, ex);
		} finally {
			if (proc != null) {
				proc.destroy();
			}
		}
	}

	/**
	 * Java版本
	 *
	 * @author 飞花梦影
	 * @date 2021-03-04 22:51:52
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	public static class JavaVersion {

		/** 判断是否为JDK1.1 */
		public static final boolean IS_JAVA_1_1 = isJavaVersionMatch("1.1");

		/** 判断是否为JDK1.2 */
		public static final boolean IS_JAVA_1_2 = isJavaVersionMatch("1.2");

		/** 判断是否为JDK1.3 */
		public static final boolean IS_JAVA_1_3 = isJavaVersionMatch("1.3");

		/** 判断是否为JDK1.4 */
		public static final boolean IS_JAVA_1_4 = isJavaVersionMatch("1.4");

		/** 判断是否为JDK1.5 */
		public static final boolean IS_JAVA_1_5 = isJavaVersionMatch("1.5");

		/** 判断是否为JDK1.6 */
		public static final boolean IS_JAVA_1_6 = isJavaVersionMatch("1.6");

		/** 判断是否为JDK1.7 */
		public static final boolean IS_JAVA_1_7 = isJavaVersionMatch("1.7");

		/** 判断是否为JDK1.8 */
		public static final boolean IS_JAVA_1_8 = isJavaVersionMatch("1.8");

		/** 判断是否为JDK1.9 */
		public static final boolean IS_JAVA_9 = isJavaVersionMatch("9");

		/** 判断是否为JDK1.10 */
		public static final boolean IS_JAVA_10 = isJavaVersionMatch("10");

		/** 判断是否为JDK1.11 */
		public static final boolean IS_JAVA_11 = isJavaVersionMatch("11");

		/** 判断是否为JDK1.12 */
		public static final boolean IS_JAVA_12 = isJavaVersionMatch("12");

		/** 判断是否为JDK1.13 */
		public static final boolean IS_JAVA_13 = isJavaVersionMatch("13");

		/** 判断是否为JDK1.14 */
		public static final boolean IS_JAVA_14 = isJavaVersionMatch("14");

		/** 判断是否为JDK1.15 */
		public static final boolean IS_JAVA_15 = isJavaVersionMatch("15");

		/** 判断是否为JDK1.16 */
		public static final boolean IS_JAVA_16 = isJavaVersionMatch("16");
	}

	/**
	 * 关于Java的系统属性
	 *
	 * @author 飞花梦影
	 * @date 2021-03-04 23:04:43
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	public static class PropertyJava {

		/** Java Awt Fonts,如null */
		public static final String JAVA_AWT_FONTS = getSystemProperty("java.awt.fonts");

		/** Java Awt Graphicsenv,.如:sun.awt.Win32GraphicsEnvironment */
		public static final String JAVA_AWT_GRAPHICSENV = getSystemProperty("java.awt.graphicsenv");

		/** Java Awt Headless,如:null,true,false */
		public static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");

		/** Java Awt Printerjob */
		public static final String JAVA_AWT_PRINTERJOB = getSystemProperty("java.awt.printerjob");

		/** Java当前项目的class文件目录以及JDK自带jar以外的jar包 */
		public static final String JAVA_CLASS_PATH = getSystemProperty("java.class.path");

		/** Java 类格式版本号,可能为null */
		public static final String JAVA_CLASS_VERSION = getSystemProperty("java.class.version");

		/** Java JIT编译器版本号 */
		public static final String JAVA_COMPILER = getSystemProperty("java.compiler");

		/** Java Endorsed目录 */
		public static final String JAVA_ENDORSED_DIRS = getSystemProperty("java.endorsed.dirs");

		/** Java Ext目录 */
		public static final String JAVA_EXT_DIRS = getSystemProperty("java.ext.dirs");

		/** JRE目录 */
		public static final String JAVA_HOME = getSystemProperty("java.home");

		/** Java默认的临时文件目录 */
		public static final String JAVA_IO_TMPDIR = getSystemProperty("java.io.tmpdir");

		/** Classpath地址集合,多个用分号隔开.如E:/java/jre/bin;E:/java/jre/lib/amd64; */
		public static final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path");

		/** Java运行时环境名.如:Java(TM) SE Runtime Environment */
		public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");

		/** Java运行时环境完整版本号,如1.8.0_144-b01 */
		public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");

		/** 执行时环境JDK规范名称,如Java Platform API Specification */
		public static final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name");

		/** 执行时环境JDK规范供应商,如Oracle Corporation */
		public static final String JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor");

		/** 执行时环境JDK规范版本号,如1.8 */
		public static final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version");

		/** java.util.prefs.PreferencesFactory如null */
		public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY = getSystemProperty(
				"java.util.prefs.PreferencesFactory");

		/** Java执行时环境供应商,如Oracle Corporation */
		public static final String JAVA_VENDOR = getSystemProperty("java.vendor");

		/** Java执行时环境供应商URL,如http://java.oracle.com/ */
		public static final String JAVA_VENDOR_URL = getSystemProperty("java.vendor.url");

		/** Java执行时环境版本号,带小版本,如1.8.0_144 */
		public static final String JAVA_VERSION = getSystemProperty("java.version");

		/** Java虚拟机模式信息,如mixed mode */
		public static final String JAVA_VM_INFO = getSystemProperty("java.vm.info");

		/** Java虚拟机实现名称,如Java HotSpot(TM) 64-Bit Server VM */
		public static final String JAVA_VM_NAME = getSystemProperty("java.vm.name");

		/** Java虚拟机规范名称,如Java Virtual Machine Specification */
		public static final String JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name");

		/** Java虚拟机供应商名称,如Oracle Corporation */
		public static final String JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor");

		/** Java虚拟机规范版本号,如1.8 */
		public static final String JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version");

		/** Java虚拟机实现供应商,如Oracle Corporation */
		public static final String JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor");

		/** Java虚拟机实现版本号,如25.144-b01 */
		public static final String JAVA_VM_VERSION = getSystemProperty("java.vm.version");

	}

	/**
	 * 关于操作系统的属性
	 *
	 * @author 飞花梦影
	 * @date 2021-03-04 23:04:58
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	public static class PropertySystem {

		/** 操作系统架构,如AMD64 */
		public static final String OS_ARCH = getSystemProperty("os.arch");

		/** 操作系统名称,如Windows 10 */
		public static final String OS_NAME = getSystemProperty("os.name");

		/** 操作系统版本号,如10.0 */
		public static final String OS_VERSION = getSystemProperty("os.version");

	}

	/**
	 * 关于用户的系统属性
	 *
	 * @author 飞花梦影
	 * @date 2021-03-04 23:05:09
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	public static class PropertyUser {

		/** 操作系统国家或地区缩写,如CN */
		public static final String USER_COUNTRY = getSystemProperty("user.country") == null
				? getSystemProperty("user.region")
				: getSystemProperty("user.country");

		/** 当前项目的根目录 */
		public static final String USER_DIR = getSystemProperty("user.dir");

		/** 用户根目录 */
		public static final String USER_HOME = getSystemProperty("user.home");

		/** 用户所在地区系统默认语言缩写,如zh */
		public static final String USER_LANGUAGE = getSystemProperty("user.language");

		/** 用户账号名 */
		public static final String USER_NAME = getSystemProperty("user.name");

		/** 用户所在地区时区,如Asia/ShangHai,可能为"" */
		public static final String USER_TIMEZONE = getSystemProperty("user.timezone");

	}

	/**
	 * 其它操作系统
	 *
	 * @author 飞花梦影
	 * @date 2021-03-04 22:54:46
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	public static class OtherOS {

		/** 判断操作系统是否为OS/400 */
		public static final boolean IS_OS_400 = isOSNameMatch("OS/400");

		/** 判断操作系统是否为OS/2 */
		public static final boolean IS_OS_OS2 = isOSNameMatch("OS/2");
	}

	/**
	 * Unix系统以及基于Unix的系统
	 *
	 * @author 飞花梦影
	 * @date 2021-03-04 22:52:42
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	public static class UnixOS {

		/** 判断操作系统是否为AIX */
		public static final boolean IS_OS_AIX = isOSNameMatch("AIX");

		/** 判断操作系统是否为FreeBSD */
		public static final boolean IS_OS_FREE_BSD = isOSNameMatch("FreeBSD");

		/** 判断操作系统是否为HP-UX */
		public static final boolean IS_OS_HP_UX = isOSNameMatch("HP-UX");

		/** 判断操作系统是否为Irix */
		public static final boolean IS_OS_IRIX = isOSNameMatch("Irix");

		/** 判断操作系统是否为Linux */
		public static final boolean IS_OS_LINUX = isOSNameMatch("Linux") || isOSNameMatch("LINUX");

		/** 判断当前操作系统是否为Mac */
		public static final boolean IS_OS_MAC = isOSNameMatch("Mac");

		/** 判断当前操作系统是否为Mac OS X */
		public static final boolean IS_OS_MAC_OSX = isOSNameMatch("Mac OS X");

		/** 判断当前操作系统是否为Mac OS X 10.0 */
		public static final boolean IS_OS_MAC_OSX_CHEETAH = isOsMatch("Mac OS X", "10.0");

		/** 判断当前操作系统是否为Mac OS X 10.1 */
		public static final boolean IS_OS_MAC_OSX_PUMA = isOsMatch("Mac OS X", "10.1");

		/** 判断当前操作系统是否为Mac OS X 10.2 */
		public static final boolean IS_OS_MAC_OSX_JAGUAR = isOsMatch("Mac OS X", "10.2");

		/** 判断当前操作系统是否为Mac OS X 10.3 */
		public static final boolean IS_OS_MAC_OSX_PANTHER = isOsMatch("Mac OS X", "10.3");

		/** 判断当前操作系统是否为Mac OS X 10.4 */
		public static final boolean IS_OS_MAC_OSX_TIGER = isOsMatch("Mac OS X", "10.4");

		/** 判断当前操作系统是否为Mac OS X 10.5 */
		public static final boolean IS_OS_MAC_OSX_LEOPARD = isOsMatch("Mac OS X", "10.5");

		/** 判断当前操作系统是否为Mac OS X 10.6 */
		public static final boolean IS_OS_MAC_OSX_SNOW_LEOPARD = isOsMatch("Mac OS X", "10.6");

		/** 判断当前操作系统是否为Mac OS X 10.7 */
		public static final boolean IS_OS_MAC_OSX_LION = isOsMatch("Mac OS X", "10.7");

		/** 判断当前操作系统是否为Mac OS X 10.8 */
		public static final boolean IS_OS_MAC_OSX_MOUNTAIN_LION = isOsMatch("Mac OS X", "10.8");

		/** 判断当前操作系统是否为Mac OS X 10.9 */
		public static final boolean IS_OS_MAC_OSX_MAVERICKS = isOsMatch("Mac OS X", "10.9");

		/** 判断当前操作系统是否为Mac OS X 10.10 */
		public static final boolean IS_OS_MAC_OSX_YOSEMITE = isOsMatch("Mac OS X", "10.10");

		/** 判断当前操作系统是否为Mac OS X 10.11 */
		public static final boolean IS_OS_MAC_OSX_EL_CAPITAN = isOsMatch("Mac OS X", "10.11");

		/** 判断操作系统是否为NetBSD */
		public static final boolean IS_OS_NET_BSD = isOSNameMatch("NetBSD");

		/** 判断操作系统是否为OpenBSD */
		public static final boolean IS_OS_OPEN_BSD = isOSNameMatch("OpenBSD");

		/** 判断操作系统是否为Solaris */
		public static final boolean IS_OS_SOLARIS = isOSNameMatch("Solaris");

		/** 判断操作系统是否为SunOS */
		public static final boolean IS_OS_SUN_OS = isOSNameMatch("SunOS");

		/** 判断操作系统是否是Unix或基于Unix的操作系统 */
		public static final boolean IS_OS_UNIX = IS_OS_AIX || IS_OS_FREE_BSD || IS_OS_HP_UX || IS_OS_IRIX || IS_OS_LINUX
				|| IS_OS_MAC_OSX || IS_OS_NET_BSD || IS_OS_OPEN_BSD || IS_OS_SOLARIS || IS_OS_SUN_OS;
	}

	/**
	 * Windows系统
	 *
	 * @author 飞花梦影
	 * @date 2021-03-04 22:52:08
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	public static class WindowsOS {

		/** 判断当前操作系统是否为Windows */
		public static final boolean IS_OS_WINDOWS = isOSNameMatch(OS_NAME_WINDOWS_PREFIX);

		/** 判断当前操作系统是否为Windows 2000 */
		public static final boolean IS_OS_WINDOWS_2000 = isOSNameMatch(OS_NAME_WINDOWS_PREFIX + " 2000");

		/** 判断当前操作系统是否为Windows 2003 */
		public static final boolean IS_OS_WINDOWS_2003 = isOSNameMatch(OS_NAME_WINDOWS_PREFIX + " 2003");

		/** 判断当前操作系统是否为Windows Server 2008 */
		public static final boolean IS_OS_WINDOWS_2008 = isOSNameMatch(OS_NAME_WINDOWS_PREFIX + " Server 2008");

		/** 判断当前操作系统是否为Windows Server 2012 */
		public static final boolean IS_OS_WINDOWS_2012 = isOSNameMatch(OS_NAME_WINDOWS_PREFIX + " Server 2012");

		/** 判断当前操作系统是否为Windows 95 */
		public static final boolean IS_OS_WINDOWS_95 = isOSNameMatch(OS_NAME_WINDOWS_PREFIX + " 95");

		/** 判断当前操作系统是否为Windows 98 */
		public static final boolean IS_OS_WINDOWS_98 = isOSNameMatch(OS_NAME_WINDOWS_PREFIX + " 98");

		/** 判断当前操作系统是否为Windows ME */
		public static final boolean IS_OS_WINDOWS_ME = isOSNameMatch(OS_NAME_WINDOWS_PREFIX + " Me");

		/** 判断当前操作系统是否为Windows NT */
		public static final boolean IS_OS_WINDOWS_NT = isOSNameMatch(OS_NAME_WINDOWS_PREFIX + " NT");

		/** 判断当前操作系统是否为Windows XP */
		public static final boolean IS_OS_WINDOWS_XP = isOSNameMatch(OS_NAME_WINDOWS_PREFIX + " XP");

		/** 判断当前操作系统是否为Windows Vista */
		public static final boolean IS_OS_WINDOWS_VISTA = isOSNameMatch(OS_NAME_WINDOWS_PREFIX + " Vista");

		/** 判断当前操作系统是否为Windows 7 */
		public static final boolean IS_OS_WINDOWS_7 = isOSNameMatch(OS_NAME_WINDOWS_PREFIX + " 7");

		/** 判断当前操作系统是否为Windows 8 */
		public static final boolean IS_OS_WINDOWS_8 = isOSNameMatch(OS_NAME_WINDOWS_PREFIX + " 8");

		/** 判断当前操作系统是否为Windows 10 */
		public static final boolean IS_OS_WINDOWS_10 = isOSNameMatch(OS_NAME_WINDOWS_PREFIX + " 10");
	}
}