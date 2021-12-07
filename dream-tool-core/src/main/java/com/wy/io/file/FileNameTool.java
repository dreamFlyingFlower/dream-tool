package com.wy.io.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

import com.wy.Constant;
import com.wy.io.IOCase;
import com.wy.lang.SystemTool;

/**
 * FileName工具类,{@link org.apache.commons.io.FilenameUtils}
 * 
 * @author 飞花梦影
 * @date 2021-03-08 09:46:48
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class FileNameTool {

	/** 系统文件分隔符字符 */
	private static final char SYSTEM_SEPARATOR = File.separatorChar;

	/** 与系统分隔符相对的分隔符字符 */
	private static final char OTHER_SEPARATOR;

	static {
		if (SystemTool.isWindows()) {
			OTHER_SEPARATOR = Constant.Langes.SEPARATOR_UNIX;
		} else {
			OTHER_SEPARATOR = Constant.Langes.SEPARATOR_WINDOWS;
		}
	}

	private FileNameTool() {}

	/**
	 * 判断字符是文件分隔符
	 * 
	 * @param ch 需要判断的字符
	 * @return true->是文件分隔符,false->不是文件分隔符
	 */
	public static boolean isSeparator(char ch) {
		return ch == Constant.Langes.SEPARATOR_UNIX || ch == Constant.Langes.SEPARATOR_WINDOWS;
	}

	/**
	 * 规范化路径,删除双点和单点路径,如../和./.
	 * 
	 * 将尾部斜杠双斜杠转换为单斜杠.删除单点径段,双点将导致该路径段和之前的路径段被删除.如果双点没有父路径段 将返回<code>null</code> names
	 * 
	 * <pre>
	 * /foo//               	=   /foo/
	 * /foo/./              	=   /foo/
	 * /foo/../bar          	=   /bar
	 * /foo/../bar/         	=   /bar/
	 * /foo/../bar/../baz	=   /baz
	 * //foo//./bar        	=   /foo/bar
	 * /../                		=   null
	 * ../foo               		=   null
	 * foo/bar/..          	=   foo/
	 * foo/../../bar        	=   null
	 * foo/../bar           	= 	bar
	 * //server/foo/../bar 	=    //server/bar
	 * //server/../bar      	=   null
	 * C:\foo\..\bar        		=   C:\bar
	 * C:\..\bar            		=   null
	 * ~/foo/../bar/       		=    ~/bar/
	 * ~/../bar            		=    null
	 * </pre>
	 * 
	 * @param filename 需要规范化的文件路径
	 * @return 规范化后的文件路径
	 */
	public static String normalize(String filename) {
		return doNormalize(filename, SYSTEM_SEPARATOR, true);
	}

	/**
	 * 规范化路径,删除双点和单点路径,如../和./.
	 * 
	 * <pre>
	 * /foo//               -->   /foo/
	 * /foo/./              -->   /foo/
	 * /foo/../bar          -->   /bar
	 * /foo/../bar/         -->   /bar/
	 * /foo/../bar/../baz   -->   /baz
	 * //foo//./bar         -->   /foo/bar
	 * /../                 -->   null
	 * ../foo               -->   null
	 * foo/bar/..           -->   foo/
	 * foo/../../bar        -->   null
	 * foo/../bar           -->   bar
	 * //server/foo/../bar  -->   //server/bar
	 * //server/../bar      -->   null
	 * C:\foo\..\bar        -->   C:\bar
	 * C:\..\bar            -->   null
	 * ~/foo/../bar/        -->   ~/bar/
	 * ~/../bar             -->   null
	 * </pre>
	 * 
	 * @param filename 需要规范化的文件路径
	 * @param unixSeparator unix分隔符,true->是unix分隔符,false->windows分隔符
	 * @return 规范化后的文件路径
	 */
	public static String normalize(String filename, boolean unixSeparator) {
		char separator = unixSeparator ? Constant.Langes.SEPARATOR_UNIX : Constant.Langes.SEPARATOR_WINDOWS;
		return doNormalize(filename, separator, true);
	}

	/**
	 * 规范化路径,删除双点和单点路径,同时删除末尾分隔符
	 * 
	 * <pre>
	 * /foo//               -->   /foo
	 * /foo/./              -->   /foo
	 * /foo/../bar          -->   /bar
	 * /foo/../bar/         -->   /bar
	 * /foo/../bar/../baz   -->   /baz
	 * //foo//./bar         -->   /foo/bar
	 * /../                 -->   null
	 * ../foo               -->   null
	 * foo/bar/..           -->   foo
	 * foo/../../bar        -->   null
	 * foo/../bar           -->   bar
	 * //server/foo/../bar  -->   //server/bar
	 * //server/../bar      -->   null
	 * C:\foo\..\bar        -->   C:\bar
	 * C:\..\bar            -->   null
	 * ~/foo/../bar/        -->   ~/bar
	 * ~/../bar             -->   null
	 * </pre>
	 * 
	 * @param filename 需要规范化的文件路径
	 * @return 规范化后的文件路径
	 */
	public static String normalizeNoEndSeparator(String filename) {
		return doNormalize(filename, SYSTEM_SEPARATOR, false);
	}

	/**
	 * 规范化路径,删除双点和单点路径,同时删除末尾分隔符
	 * 
	 * <pre>
	 * /foo//               -->   /foo
	 * /foo/./              -->   /foo
	 * /foo/../bar          -->   /bar
	 * /foo/../bar/         -->   /bar
	 * /foo/../bar/../baz   -->   /baz
	 * //foo//./bar         -->   /foo/bar
	 * /../                 -->   null
	 * ../foo               -->   null
	 * foo/bar/..           -->   foo
	 * foo/../../bar        -->   null
	 * foo/../bar           -->   bar
	 * //server/foo/../bar  -->   //server/bar
	 * //server/../bar      -->   null
	 * C:\foo\..\bar        -->   C:\bar
	 * C:\..\bar            -->   null
	 * ~/foo/../bar/        -->   ~/bar
	 * ~/../bar             -->   null
	 * </pre>
	 *
	 * @param filename 需要规范化的文件路径
	 * @param unixSeparator unix分隔符,true->是unix分隔符,false->windows分隔符
	 * @return 规范化后的文件路径
	 */
	public static String normalizeNoEndSeparator(String filename, boolean unixSeparator) {
		char separator = unixSeparator ? Constant.Langes.SEPARATOR_UNIX : Constant.Langes.SEPARATOR_WINDOWS;
		return doNormalize(filename, separator, false);
	}

	/**
	 * 规范化文件路径
	 *
	 * @param filename 需要规范化的文件路径
	 * @param separator 需要使用的文件分隔符
	 * @param keepSeparator 末尾是否添加分隔符,true->添加,false->删除
	 * @return 规范化后的文件路径
	 */
	private static String doNormalize(String filename, char separator, boolean keepSeparator) {
		if (filename == null) {
			return null;
		}
		int size = filename.length();
		if (size == 0) {
			return filename;
		}
		int prefix = getPrefixLength(filename);
		if (prefix < 0) {
			return null;
		}
		// +1可能还是
		char[] array = new char[size + 2];
		filename.getChars(0, filename.length(), array, 0);
		char otherSeparator = separator == SYSTEM_SEPARATOR ? OTHER_SEPARATOR : SYSTEM_SEPARATOR;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == otherSeparator) {
				array[i] = separator;
			}
		}
		// 在末尾添加额外的分隔符
		boolean lastIsDirectory = true;
		if (array[size - 1] != separator) {
			array[size++] = separator;
			lastIsDirectory = false;
		}
		// 相邻斜杠
		for (int i = prefix + 1; i < size; i++) {
			if (array[i] == separator && array[i - 1] == separator) {
				System.arraycopy(array, i, array, i - 1, size - i);
				size--;
				i--;
			}
		}
		// 处理./
		for (int i = prefix + 1; i < size; i++) {
			if (array[i] == separator && array[i - 1] == '.' && (i == prefix + 1 || array[i - 2] == separator)) {
				if (i == size - 1) {
					lastIsDirectory = true;
				}
				System.arraycopy(array, i + 1, array, i - 1, size - i);
				size -= 2;
				i--;
			}
		}
		// 处理../
		outer: for (int i = prefix + 2; i < size; i++) {
			if (array[i] == separator && array[i - 1] == '.' && array[i - 2] == '.'
					&& (i == prefix + 2 || array[i - 3] == separator)) {
				if (i == prefix + 2) {
					return null;
				}
				if (i == size - 1) {
					lastIsDirectory = true;
				}
				int j;
				for (j = i - 4; j >= prefix; j--) {
					if (array[j] == separator) {
						// 删除 b/../ from a/b/../c
						System.arraycopy(array, i + 1, array, j + 1, size - i);
						size -= i - j;
						i = j + 1;
						continue outer;
					}
				}
				// 删除 a/../ from a/../c
				System.arraycopy(array, i + 1, array, prefix, size - i);
				size -= i + 1 - prefix;
				i = prefix + 1;
			}
		}
		if (size <= 0) {
			return "";
		}
		if (size <= prefix) {
			return new String(array, 0, size);
		}
		if (lastIsDirectory && keepSeparator) {
			// 保持尾部分隔符
			return new String(array, 0, size);
		}
		// 丢掉尾部分隔符
		return new String(array, 0, size - 1);
	}

	/**
	 * 使用常规命令行样式规则将文件名连接到基本路径,{@link Path#resolve(Path)}
	 * 
	 * <pre>
	 * /foo/ + bar          -->   /foo/bar
	 * /foo + bar           -->   /foo/bar
	 * /foo + /bar          -->   /bar
	 * /foo + C:/bar        -->   C:/bar
	 * /foo + C:bar         -->   C:bar (*)
	 * /foo/a/ + ../bar     -->   foo/bar
	 * /foo/ + ../../bar    -->   null
	 * /foo/ + /bar         -->   /bar
	 * /foo/.. + /bar       -->   /bar
	 * /foo + bar/c.txt     -->   /foo/bar/c.txt
	 * /foo/c.txt + bar     -->   /foo/c.txt/bar (!)
	 * </pre>
	 * 
	 * @param basePath 基本路径
	 * @param fullFilenameToAdd 需要添加到基本路径的路径
	 * @return 拼接后的路径
	 */
	public static String concat(String basePath, String fullFilenameToAdd) {
		int prefix = getPrefixLength(fullFilenameToAdd);
		if (prefix < 0) {
			return null;
		}
		if (prefix > 0) {
			return normalize(fullFilenameToAdd);
		}
		if (basePath == null) {
			return null;
		}
		int len = basePath.length();
		if (len == 0) {
			return normalize(fullFilenameToAdd);
		}
		char ch = basePath.charAt(len - 1);
		if (isSeparator(ch)) {
			return normalize(basePath + fullFilenameToAdd);
		} else {
			return normalize(basePath + '/' + fullFilenameToAdd);
		}
	}

	/**
	 * 判断parent目录是否包含child元素
	 * 
	 * @param parent 父目录
	 * @param child 子目录
	 * @return true->父目录包含子目录
	 * @throws IOException
	 */
	public static boolean directoryContains(final String parent, final String child) throws IOException {
		if (parent == null) {
			throw new IllegalArgumentException("Directory must not be null");
		}
		if (child == null) {
			return false;
		}
		if (IOCase.SYSTEM.checkEquals(parent, child)) {
			return false;
		}
		return IOCase.SYSTEM.checkStartsWith(child, parent);
	}

	/**
	 * 将文件分隔符全部转换为Unix文件分隔符
	 * 
	 * @param path 文件路径
	 * @return 更新后的文件路径
	 */
	public static String separatorsToUnix(String path) {
		if (path == null || path.indexOf(Constant.Langes.SEPARATOR_WINDOWS) == -1) {
			return path;
		}
		return path.replace(Constant.Langes.SEPARATOR_WINDOWS, Constant.Langes.SEPARATOR_UNIX);
	}

	/**
	 * 将文件分隔符全部转换为Windows文件分隔符
	 * 
	 * @param path 文件路径
	 * @return 更新后的文件路径
	 */
	public static String separatorsToWindows(String path) {
		if (path == null || path.indexOf(Constant.Langes.SEPARATOR_UNIX) == -1) {
			return path;
		}
		return path.replace(Constant.Langes.SEPARATOR_UNIX, Constant.Langes.SEPARATOR_WINDOWS);
	}

	/**
	 * 将文件分隔符全部转换为系统文件分隔符
	 * 
	 * @param path 文件路径
	 * @return 更新后的文件路径
	 */
	public static String separatorsToSystem(String path) {
		if (path == null) {
			return null;
		}
		if (SystemTool.isWindows()) {
			return separatorsToWindows(path);
		} else {
			return separatorsToUnix(path);
		}
	}

	/**
	 * 获得文件路径前缀长度,即从起始位置到第一个/的长度,如C:/,~/
	 * 
	 * <pre>
	 * Windows:
	 * a\b\c.txt           --> ""          --> relative
	 * \a\b\c.txt          --> "\"         --> current drive absolute
	 * C:a\b\c.txt         --> "C:"        --> drive relative
	 * C:\a\b\c.txt        --> "C:\"       --> absolute
	 * \\server\a\b\c.txt  --> "\\server\" --> UNC
	 * Unix:
	 * a/b/c.txt           --> ""          --> relative
	 * /a/b/c.txt          --> "/"         --> absolute
	 * ~/a/b/c.txt         --> "~/"        --> current user
	 * ~                   --> "~/"        --> current user (slash added)
	 * ~user/a/b/c.txt     --> "~user/"    --> named user
	 * ~user               --> "~user/"    --> named user (slash added)
	 * </pre>
	 *
	 * @param filename 文件路径,若为null或:开头,返回-1
	 * @return 文件路径前缀
	 */
	public static int getPrefixLength(String filename) {
		if (filename == null) {
			return -1;
		}
		int len = filename.length();
		if (len == 0) {
			return 0;
		}
		char ch0 = filename.charAt(0);
		if (ch0 == ':') {
			return -1;
		}
		if (len == 1) {
			if (ch0 == '~') {
				return 2; // return a length greater than the input
			}
			return isSeparator(ch0) ? 1 : 0;
		} else {
			if (ch0 == '~') {
				int posUnix = filename.indexOf(Constant.Langes.SEPARATOR_UNIX, 1);
				int posWin = filename.indexOf(Constant.Langes.SEPARATOR_WINDOWS, 1);
				if (posUnix == -1 && posWin == -1) {
					return len + 1; // return a length greater than the input
				}
				posUnix = posUnix == -1 ? posWin : posUnix;
				posWin = posWin == -1 ? posUnix : posWin;
				return Math.min(posUnix, posWin) + 1;
			}
			char ch1 = filename.charAt(1);
			if (ch1 == ':') {
				ch0 = Character.toUpperCase(ch0);
				if (ch0 >= 'A' && ch0 <= 'Z') {
					if (len == 2 || isSeparator(filename.charAt(2)) == false) {
						return 2;
					}
					return 3;
				}
				return -1;
			} else if (isSeparator(ch0) && isSeparator(ch1)) {
				int posUnix = filename.indexOf(Constant.Langes.SEPARATOR_UNIX, 2);
				int posWin = filename.indexOf(Constant.Langes.SEPARATOR_WINDOWS, 2);
				if (posUnix == -1 && posWin == -1 || posUnix == 2 || posWin == 2) {
					return -1;
				}
				posUnix = posUnix == -1 ? posWin : posUnix;
				posWin = posWin == -1 ? posUnix : posWin;
				return Math.min(posUnix, posWin) + 1;
			} else {
				return isSeparator(ch0) ? 1 : 0;
			}
		}
	}

	/**
	 * 获得最后一个目录文件分隔符索引
	 * 
	 * @param filename 文件路径,null或找不到返回-1
	 * @return 最后一个目录文件分隔符索引
	 */
	public static int indexOfLastSeparator(String filename) {
		if (filename == null) {
			return -1;
		}
		int lastUnixPos = filename.lastIndexOf(Constant.Langes.SEPARATOR_UNIX);
		int lastWindowsPos = filename.lastIndexOf(Constant.Langes.SEPARATOR_WINDOWS);
		return Math.max(lastUnixPos, lastWindowsPos);
	}

	/**
	 * 获得最后一个点的索引
	 * 
	 * @param filename 文件路径,null或找不到返回-1
	 * @return 最后一个点的索引
	 */
	public static int indexOfExtension(String filename) {
		if (filename == null) {
			return -1;
		}
		int extensionPos = filename.lastIndexOf(Constant.Langes.CHAR_DOT);
		int lastSeparator = indexOfLastSeparator(filename);
		return lastSeparator > extensionPos ? -1 : extensionPos;
	}

	/**
	 * 获得文件路径前缀,即从起始位置到第一个/后的第一个字符的索引(包括/的索引)
	 * 
	 * <pre>
	 * Windows:
	 * a\b\c.txt           --> ""          --> relative
	 * \a\b\c.txt          --> "\"         --> current drive absolute
	 * C:a\b\c.txt         --> "C:"        --> drive relative
	 * C:\a\b\c.txt        --> "C:\"       --> absolute
	 * \\server\a\b\c.txt  --> "\\server\" --> UNC
	 *
	 * Unix:
	 * a/b/c.txt           --> ""          --> relative
	 * /a/b/c.txt          --> "/"         --> absolute
	 * ~/a/b/c.txt         --> "~/"        --> current user
	 * ~                   --> "~/"        --> current user (slash added)
	 * ~user/a/b/c.txt     --> "~user/"    --> named user
	 * ~user               --> "~user/"    --> named user (slash added)
	 * </pre>
	 *
	 * @param filename 文件路径
	 * @return 文件前缀
	 */
	public static String getPrefix(String filename) {
		if (filename == null) {
			return null;
		}
		int len = getPrefixLength(filename);
		if (len < 0) {
			return null;
		}
		if (len > filename.length()) {
			return filename + Constant.Langes.SEPARATOR_UNIX;
		}
		return filename.substring(0, len);
	}

	/**
	 * 获得文件路径,不包含文件前缀,即去掉从起始位置到第一个/(包括/)的字符串
	 * 
	 * <pre>
	 * C:\a\b\c.txt --> a\b\
	 * ~/a/b/c.txt  --> a/b/
	 * a.txt        --> ""
	 * a/b/c        --> a/b/
	 * a/b/c/       --> a/b/c/
	 * </pre>
	 * <p>
	 *
	 * @param filename 文件路径
	 * @return 不包含文件前缀的路径
	 */
	public static String getPath(String filename) {
		return doGetPath(filename, 1);
	}

	/**
	 * 获得文件路径,不包含第一个/之前(包含/)和最后一个/之后(包含/)的路径
	 * 
	 * <pre>
	 * C:\a\b\c.txt --> a\b
	 * ~/a/b/c.txt  --> a/b
	 * a.txt        --> ""
	 * a/b/c        --> a/b
	 * a/b/c/       --> a/b/c
	 * </pre>
	 *
	 * @param filename 文件路径
	 * @return 不包含第一个/之前(包含/)和最后一个/之后(包含/)的路径
	 */
	public static String getPathNoEndSeparator(String filename) {
		return doGetPath(filename, 0);
	}

	/**
	 * 处理文件路径
	 * 
	 * @param filename 文件路径
	 * @param separatorAdd 0省略结束分隔符,1返回
	 * @return the path
	 */
	private static String doGetPath(String filename, int separatorAdd) {
		if (filename == null) {
			return null;
		}
		int prefix = getPrefixLength(filename);
		if (prefix < 0) {
			return null;
		}
		int index = indexOfLastSeparator(filename);
		int endIndex = index + separatorAdd;
		if (prefix >= filename.length() || index < 0 || prefix >= endIndex) {
			return "";
		}
		return filename.substring(prefix, endIndex);
	}

	/**
	 * 返回文件路径的目录部分,末尾添加/
	 * 
	 * <pre>
	 * C:\a\b\c.txt --> C:\a\b\
	 * a.txt        --> ""
	 * a/b/c        --> a/b/
	 * a/b/c/       --> a/b/c/
	 * C:           --> C:
	 * C:\          --> C:\
	 * ~            --> ~/
	 * ~user        --> ~user/
	 * </pre>
	 *
	 * @param filename 文件路径
	 * @return 文件路径的目录部分,末尾添加/
	 */
	public static String getFullPath(String filename) {
		return doGetFullPath(filename, true);
	}

	/**
	 * 返回文件路径的目录部分,末尾不添加/
	 * 
	 * <pre>
	 * C:\a\b\c.txt --> C:\a\b
	 * a.txt        --> ""
	 * a/b/c        --> a/b
	 * a/b/c/       --> a/b/c
	 * C:           --> C:
	 * C:\          --> C:\
	 * ~/           --> ~
	 * ~user/       --> ~user
	 * </pre>
	 *
	 * @param filename 文件路径
	 * @return 文件路径的目录部分,末尾不添加/
	 */
	public static String getFullPathNoEndSeparator(String filename) {
		return doGetFullPath(filename, false);
	}

	/**
	 * 返回文件路径的目录部分
	 * 
	 * @param filename 文件路径
	 * @param includeSeparator 末尾是否添加/,true->添加,false->不添加
	 * @return 文件路径的目录部分
	 */
	private static String doGetFullPath(String filename, boolean includeSeparator) {
		if (filename == null) {
			return null;
		}
		int prefix = getPrefixLength(filename);
		if (prefix < 0) {
			return null;
		}
		if (prefix >= filename.length()) {
			if (includeSeparator) {
				return getPrefix(filename);
			} else {
				return filename;
			}
		}
		int index = indexOfLastSeparator(filename);
		if (index < 0) {
			return filename.substring(0, prefix);
		}
		int end = index + (includeSeparator ? 1 : 0);
		if (end == 0) {
			end++;
		}
		return filename.substring(0, end);
	}

	/**
	 * 获得文件名称,包括后缀和点,去除所有目录,即去除最后一个/之前所有字符串
	 * 
	 * @param filename 文件路径
	 * @return 文件名,包括点和后缀
	 */
	public static String getName(String filename) {
		if (filename == null) {
			return null;
		}
		int index = indexOfLastSeparator(filename);
		return filename.substring(index + 1);
	}

	/**
	 * 获得文件名称,不包括后缀和点,去除所有目录,即去除最后一个/之前所有字符串以及后缀和点
	 * 
	 * @param filename 文件路径
	 * @return 文件名,不包括点和后缀
	 */
	public static String getBaseName(String filename) {
		return removeExtension(getName(filename));
	}

	/**
	 * 获得文件后缀,不包含点
	 * 
	 * @param filename 文件路径
	 * @return 文件后缀,不包含点
	 */
	public static String getExtension(String filename) {
		if (filename == null) {
			return null;
		}
		int index = indexOfExtension(filename);
		if (index == -1) {
			return "";
		} else {
			return filename.substring(index + 1);
		}
	}

	/**
	 * 删除文件路径后缀,即删除最后一个\之后的最后一个点之后所有字符串
	 * 
	 * @param filename 文件路径
	 * @return 删除后缀后的文件路径
	 */
	public static String removeExtension(String filename) {
		if (filename == null) {
			return null;
		}
		int index = indexOfExtension(filename);
		if (index == -1) {
			return filename;
		} else {
			return filename.substring(0, index);
		}
	}

	/**
	 * 判断2个文件路径是否相同
	 *
	 * @param filename1 文件路径1
	 * @param filename2 文件路径2
	 * @return true->2个文件路径相同,false->不相同
	 */
	public static boolean equals(String filename1, String filename2) {
		return equals(filename1, filename2, false, IOCase.SENSITIVE);
	}

	/**
	 * 判断2个文件路径是否相同,使用系统默认的大小写规则
	 *
	 * @param filename1 文件路径1
	 * @param filename2 文件路径2
	 * @return true->2个文件路径相同,false->不相同
	 */
	public static boolean equalsOnSystem(String filename1, String filename2) {
		return equals(filename1, filename2, false, IOCase.SYSTEM);
	}

	/**
	 * 判断2个文件路径是否相同,规范化后,大小写敏感
	 *
	 * @param filename1 文件路径1
	 * @param filename2 文件路径2
	 * @return true->2个文件路径相同,false->不相同
	 */
	public static boolean equalsNormalized(String filename1, String filename2) {
		return equals(filename1, filename2, true, IOCase.SENSITIVE);
	}

	/**
	 * 判断2个文件路径是否相同,规范化之后使用系统默认的大小写规则
	 *
	 * @param filename1 文件路径1
	 * @param filename2 文件路径2
	 * @return true->2个文件路径相同,false->不相同
	 */
	public static boolean equalsNormalizedOnSystem(String filename1, String filename2) {
		return equals(filename1, filename2, true, IOCase.SYSTEM);
	}

	/**
	 * 判断2个文件路径是否相同
	 *
	 * @param filename1 文件路径1
	 * @param filename2 文件路径2
	 * @param normalized 是否规范化,true->规范化,false->不规范化
	 * @param caseSensitivity 大小写使用规则
	 * @return true->2个文件路径相同,false->不相同
	 */
	public static boolean equals(String filename1, String filename2, boolean normalized, IOCase caseSensitivity) {
		if (filename1 == null || filename2 == null) {
			return filename1 == null && filename2 == null;
		}
		if (normalized) {
			filename1 = normalize(filename1);
			filename2 = normalize(filename2);
			if (filename1 == null || filename2 == null) {
				throw new NullPointerException("Error normalizing one or both of the file names");
			}
		}
		if (caseSensitivity == null) {
			caseSensitivity = IOCase.SENSITIVE;
		}
		return caseSensitivity.checkEquals(filename1, filename2);
	}

	/**
	 * 判断文件路径扩展名和指定扩展名是否相同
	 *
	 * @param filename 文件路径
	 * @param extension 文件扩展名
	 * @return true->文件扩展名和指定扩展名相同,false->不同
	 */
	public static boolean isExtension(String filename, String extension) {
		if (filename == null) {
			return false;
		}
		if (extension == null || extension.length() == 0) {
			return indexOfExtension(filename) == -1;
		}
		String fileExt = getExtension(filename);
		return fileExt.equals(extension);
	}

	/**
	 * 判断文件路径扩展名时候被包含在扩展名数组中
	 *
	 * @param filename 文件路径
	 * @param extension 文件扩展名数组
	 * @return true->文件扩展名是扩展名数组中的一个,false->不是任何一种扩展名
	 */
	public static boolean isExtension(String filename, String[] extensions) {
		if (filename == null) {
			return false;
		}
		if (extensions == null || extensions.length == 0) {
			return indexOfExtension(filename) == -1;
		}
		String fileExt = getExtension(filename);
		for (String extension : extensions) {
			if (fileExt.equals(extension)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断文件路径扩展名时候被包含在扩展名集合中
	 *
	 * @param filename 文件路径
	 * @param extension 文件扩展名集合
	 * @return true->文件扩展名是扩展名集合中的一个,false->不是任何一种扩展名
	 */
	public static boolean isExtension(String filename, Collection<String> extensions) {
		if (filename == null) {
			return false;
		}
		if (extensions == null || extensions.isEmpty()) {
			return indexOfExtension(filename) == -1;
		}
		String fileExt = getExtension(filename);
		for (String extension : extensions) {
			if (fileExt.equals(extension)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查文件名是否与指定的通配符匹配,大小写敏感.?和*分别对应1个和多个通配符字符
	 * 
	 * <pre>
	 * wildcardMatch("c.txt", "*.txt")      --> true
	 * wildcardMatch("c.txt", "*.jpg")      --> false
	 * wildcardMatch("a/b/c.txt", "a/b/*")  --> true
	 * wildcardMatch("c.txt", "*.???")      --> true
	 * wildcardMatch("c.txt", "*.????")     --> false
	 * </pre>
	 * 
	 * @param filename 文件路径
	 * @param wildcardMatcher 匹配规则
	 * @return true->文件路径和匹配规则相匹配,false->不匹配
	 */
	public static boolean wildcardMatch(String filename, String wildcardMatcher) {
		return wildcardMatch(filename, wildcardMatcher, IOCase.SENSITIVE);
	}

	/**
	 * 检查文件名是否与指定的通配符匹配,大小写规则使用系统规则.?和*分别对应1个和多个通配符字符
	 * 
	 * @param filename 文件路径
	 * @param wildcardMatcher 匹配规则
	 * @return true->文件路径和匹配规则相匹配,false->不匹配
	 */
	public static boolean wildcardMatchOnSystem(String filename, String wildcardMatcher) {
		return wildcardMatch(filename, wildcardMatcher, IOCase.SYSTEM);
	}

	/**
	 * 检查文件名是否与指定的通配符匹配.?和*分别对应1个和多个通配符字符
	 * 
	 * @param filename 文件路径
	 * @param wildcardMatcher 匹配规则
	 * @param caseSensitivity 大小写匹配规则
	 * @return true->文件路径和匹配规则相匹配,false->不匹配
	 */
	public static boolean wildcardMatch(String filename, String wildcardMatcher, IOCase caseSensitivity) {
		if (filename == null && wildcardMatcher == null) {
			return true;
		}
		if (filename == null || wildcardMatcher == null) {
			return false;
		}
		if (caseSensitivity == null) {
			caseSensitivity = IOCase.SENSITIVE;
		}
		String[] wcs = splitOnTokens(wildcardMatcher);
		boolean anyChars = false;
		int textIdx = 0;
		int wcsIdx = 0;
		Stack<int[]> backtrack = new Stack<int[]>();
		// 循环处理*
		do {
			if (backtrack.size() > 0) {
				int[] array = backtrack.pop();
				wcsIdx = array[0];
				textIdx = array[1];
				anyChars = true;
			}
			// loop whilst tokens and text left to process
			while (wcsIdx < wcs.length) {
				if (wcs[wcsIdx].equals("?")) {
					textIdx++;
					if (textIdx > filename.length()) {
						break;
					}
					anyChars = false;
				} else if (wcs[wcsIdx].equals("*")) {
					anyChars = true;
					if (wcsIdx == wcs.length - 1) {
						textIdx = filename.length();
					}
				} else {
					// 匹配文本
					if (anyChars) {
						// 用任意多个字符来匹配文本
						textIdx = caseSensitivity.checkIndexOf(filename, textIdx, wcs[wcsIdx]);
						if (textIdx == -1) {
							break;
						}
						int repeat = caseSensitivity.checkIndexOf(filename, textIdx + 1, wcs[wcsIdx]);
						if (repeat >= 0) {
							backtrack.push(new int[] { wcsIdx, repeat });
						}
					} else {
						// 从当前位置开始匹配
						if (!caseSensitivity.checkRegionMatches(filename, textIdx, wcs[wcsIdx])) {
							break;
						}
					}
					// 匹配文本标记,移动文本索引到匹配标记的末尾
					textIdx += wcs[wcsIdx].length();
					anyChars = false;
				}
				wcsIdx++;
			}
			// 完全匹配
			if (wcsIdx == wcs.length && textIdx == filename.length()) {
				return true;
			}
		} while (backtrack.size() > 0);
		return false;
	}

	/**
	 * 将字符串使用?和*拆分成多个部分,多个*将合并成一个,规范化正则
	 * 
	 * @param text 需要拆分的字符串
	 * @return 拆分之后的数组
	 */
	private static String[] splitOnTokens(String text) {
		if (text.indexOf('?') == -1 && text.indexOf('*') == -1) {
			return new String[] { text };
		}
		char[] array = text.toCharArray();
		ArrayList<String> list = new ArrayList<String>();
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			if (array[i] == '?' || array[i] == '*') {
				if (buffer.length() != 0) {
					list.add(buffer.toString());
					buffer.setLength(0);
				}
				if (array[i] == '?') {
					list.add("?");
				} else if (list.isEmpty() || i > 0 && list.get(list.size() - 1).equals("*") == false) {
					list.add("*");
				}
			} else {
				buffer.append(array[i]);
			}
		}
		if (buffer.length() != 0) {
			list.add(buffer.toString());
		}
		return list.toArray(new String[list.size()]);
	}
}