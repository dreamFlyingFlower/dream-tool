package com.wy;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * 常量
 * 
 * @author 飞花梦影
 * @date 2021-03-07 16:42:15
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface Constant {

	interface Pager {

		/** 分页参数,默认当前页数字段 */
		String PAGE_INDEX = "pageIndex";

		/** 分页参数,默认每页数据量字段 */
		String PAGE_SIZE = "pageSize";

		/** 分页参数pageSize,默认每页数据量 */
		int PAGE_SIZE_NUM = 10;

		/** 分页参数,默认总页数字段 */
		String TOTAL_PAGE = "totalPage";

		/** 分页参数,默认总数字段 */
		String TOTAL = "total";
	}

	/**
	 * Array数组常量
	 * 
	 * @author 飞花梦影
	 * @date 2021-03-10 14:53:55
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	interface Arrayes {

		/** 用于hex输出 */
		char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		/** 用于hex输出 */
		char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		/** 空byte[]数组 */
		byte[] EMPTY_BYTE = new byte[0];

		/** 空Class数组 */
		Class<?>[] EMPTY_CLASS = new Class[0];

		/** 空Field数组 */
		Field[] EMPTY_FIELD = new Field[0];

		/** 空文件数组 */
		File[] EMPTY_FILE = new File[0];

		/** 空Method数组 */
		Method[] EMPTY_METHOD = new Method[0];

		/** 空对象数组 */
		Object[] EMPTY_OBJECT = new Object[0];

		/** Throwable数组 */
		Throwable[] EMPTY_THROWABLE = new Throwable[0];

		/** 空Type数组 */
		Type[] EMPTY_TYPE = new Type[0];

		String[] MONEY_NUM = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾", "佰", "仟", "万", "亿" };

		String[] MONEY_UNIT = new String[] { "分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆",
				"拾", "佰", "仟" };
	}

	/**
	 * Class,Reflect常量
	 * 
	 * @author 飞花梦影
	 * @date 2021-03-10 14:54:31
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	interface Classes {

		/** CGLIB代理的class的分隔符. */
		String CGLIB_CLASS_SEPARATOR = "$$";

		/** 内部类分隔符 */
		char INNER_CLASS_SEPARATOR_CHAR = '$';

		/** 将基本类型名称映射到数组类名中使用的相应缩写 */
		Map<String, String> ABBREVIATION_MAP = new IdentityHashMap<String, String>() {

			private static final long serialVersionUID = 1L;
			{
				put("int", "I");
				put("boolean", "Z");
				put("float", "F");
				put("long", "J");
				put("short", "S");
				put("byte", "B");
				put("double", "D");
				put("char", "C");
			}
		};

		/** 基本类型字符串和基本类型class对应Map */
		Map<String, Class<?>> NAME_PRIMITIVE_MAP = new IdentityHashMap<String, Class<?>>() {

			private static final long serialVersionUID = 1L;
			{
				put("boolean", Boolean.TYPE);
				put("byte", Byte.TYPE);
				put("char", Character.TYPE);
				put("short", Short.TYPE);
				put("int", Integer.TYPE);
				put("long", Long.TYPE);
				put("double", Double.TYPE);
				put("float", Float.TYPE);
				put("void", Void.TYPE);
			}
		};

		/** 基本类型和对应的包装类型的Map */
		Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP = new IdentityHashMap<Class<?>, Class<?>>() {

			private static final long serialVersionUID = 1L;
			{
				put(Boolean.TYPE, Boolean.class);
				put(Byte.TYPE, Byte.class);
				put(Character.TYPE, Character.class);
				put(Short.TYPE, Short.class);
				put(Integer.TYPE, Integer.class);
				put(Long.TYPE, Long.class);
				put(Double.TYPE, Double.class);
				put(Float.TYPE, Float.class);
				put(Void.TYPE, Void.TYPE);
			}
		};

		/** 包装类和对应的基本类型的Map */
		Map<Class<?>, Class<?>> WRAPPER_PRIMITIVE_MAP = new IdentityHashMap<Class<?>, Class<?>>(12) {

			private static final long serialVersionUID = 1L;
			{
				for (final Map.Entry<Class<?>, Class<?>> entry : ConstantClass.PRIMITIVE_WRAPPER_MAP.entrySet()) {
					put(entry.getValue(), entry.getKey());
				}
			}
		};
	}

	/**
	 * Escape转义字符
	 * 
	 * @author 飞花梦影
	 * @date 2021-03-10 15:01:47
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	interface Escape {

		/** &转义-> &amp; */
		String AMP = "&amp;";

		/** 单引号转义-> &#039;,在新版本中使用->&apos; */
		String APOS = "&#039;";

		/** 大于号转义-> &gt; */
		String GT = "&gt;";

		/** 小于号转义-> &lt; */
		String LT = "&lt;";

		/** 空格转义-> &nbsp; */
		String NBSP = "&nbsp;";

		/** 双引号转义-> &quot; */
		String QUOTE = "&quot;";
	}

	/**
	 * IO流以及File,Path常量
	 * 
	 * @author 飞花梦影
	 * @date 2021-03-10 14:54:04
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	interface IO {

		/** 流结束 */
		int EOF = -1;

		/** 根据系统自动调整的目录分隔符 */
		char DIR_SEPARATOR = File.separatorChar;

		/** unix系统目录分隔符 */
		char DIR_SEPARATOR_UNIX = '/';

		/** windows系统目录分隔符 */
		char DIR_SEPARATOR_WINDOWS = '\\';

		/** 默认字节缓冲大小 */
		int DEFAULT_BUFFER_SIZE = 8192;

		/** 默认字节缓冲数组,8k */
		byte[] DEFAULT_BUFFER_BYTE = new byte[DEFAULT_BUFFER_SIZE];

		/** 临时文件名默认前缀 */
		String FILE_TEMP_PREFIX = "paradise";

		/** 根据系统自动调整的换行符 */
		String LINE_SEPERATOR = System.lineSeparator();

		/** unix换行符 */
		String LINE_SEPARATOR_UNIX = "\n";

		/** windows换行符 */
		String LINE_SEPARATOR_WINDOWS = "\r\n";

		/** 1kb == 1024b */
		long ONE_KB = 1024;

		/** 默认字节缓冲数组 {@link #skip(InputStream, long)} */
		byte[] SKIP_BYTE_BUFFER = new byte[DEFAULT_BUFFER_SIZE];

		/** 文件为null时的提示信息 */
		String TOAST_FILE_NULL = "the file can't be null";

		/** 文件为null时的提示信息 */
		String TOAST_FILE_NULL_FORMAT = "the %s can't be null";

		/** 目录为null时的提示信息 */
		String TOAST_DIR_NULL = "the directory can't be null";

		/** 目录为null时的提示信息 */
		String TOAST_DIR_NULL_FORMAT = "the %s can't be null";
	}

	/**
	 * 通用常量
	 * 
	 * @author 飞花梦影
	 * @date 2021-03-10 14:53:16
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	interface Langes {

		/** 反斜杠 */
		char CHAR_BACKSLASH = '\\';

		/** 回车 */
		char CHAR_CR = '\r';

		/** 点 */
		char CHAR_DOT = '.';

		/** 空字符 */
		char CHAR_EMPTY = ' ';

		/** unix换行符 */
		char CHAR_LF = '\n';

		/** null的unicode字符 */
		char CHAR_NUL = '\0';

		/** 大括号开始符号 */
		char CHAR_DELIM_START = '{';

		/** 大括号结束符号 */
		char CHAR_DELIM_END = '}';

		/** 默认编码集 */
		Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

		/** 默认编码集字符串 */
		String DEFAULT_CHARSET_NAME = DEFAULT_CHARSET.name();

		/** 空Json */
		String EMPTY_JSON = "{}";

		/** 所有小写字符 */
		String HOLE_ALPHABET = "abcdefghijklmnopqrstuvwxyz";

		/** 索引未找到时的返回值 */
		int INDEX_NOT_FOUND = -1;

		/** 能填充的最大长度 */
		int PAD_LIMIT = 8192;

		/** Unix系统文件分隔符 */
		char SEPARATOR_UNIX = '/';

		/** Windows系统文件分隔符 */
		char SEPARATOR_WINDOWS = '\\';

		/** 反斜杠 */
		String STR_BACKSLASH = "\\";

		/** 回车 */
		String STR_CR = "\r";

		/** 大括号开始符号 */
		String STR_DELIM_START = "{";

		/** 大括号结束符号 */
		String STR_DELIM_END = "}";

		/** 点 */
		String STR_DOT = ".";

		/** 空字符串 */
		String STR_EMPTY = "";

		/** 错误 */
		String STR_ERROR = "ERROR";

		/** 失败 */
		String STR_FAILURE = "FAILURE";

		/** Unix换行符 */
		String STR_LF = "\n";

		/** 一个空白字符串 */
		String STR_SPACE = " ";

		/** 成功 */
		String STR_SUCCESS = "SUCCESS";

		/** 未知 */
		String STR_UNKNOWN = "UNKNOWN";

		/** 秒与毫秒换算 */
		int TIME_MILLSECONDS = 1000;

		/** 秒与微秒换算 */
		int TIME_MICROSECONDS = 1000000;

		/** 秒与纳秒换算 */
		int TIME_NANOSECONDS = 1000000000;
	}
}