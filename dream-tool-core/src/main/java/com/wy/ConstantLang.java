package com.wy;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 核心工具公共参数
 *
 * @author 飞花梦影
 * @date 2021-02-18 22:27:12
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ConstantLang {

	String[] MONEY_NUM = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾", "佰", "仟", "万", "亿" };

	String[] MONEY_UNIT = new String[] { "分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
			"佰", "仟" };

	String HOLE_NUM = "1234567890";

	String HOLE_ALPHABET = "abcdefghijklmnopqrstuvwxyz";

	/**
	 * hex中包含的字符
	 */
	public static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 索引未找到时的返回值
	 */
	int INDEX_NOT_FOUND = -1;

	/**
	 * 默认编码集
	 */
	Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	/**
	 * 未知
	 */
	String STR_UNKNOWN = "UNKNOWN";

	/**
	 * 成功
	 */
	String STR_SUCCESS = "SUCCESS";

	/**
	 * 失败
	 */
	String STR_FAILURE = "FAILURE";

	/**
	 * 错误
	 */
	String STR_ERROR = "ERROR";

	/**
	 * 能填充的最大长度
	 */
	int PAD_LIMIT = 8192;

	/**
	 * 空字符串
	 */
	String STR_EMPTY = "";

	/**
	 * 一个空白字符串
	 */
	String STR_SPACE = " ";

	/**
	 * Unix换行符
	 */
	String STR_LF = "\n";

	/**
	 * 回车
	 */
	String STR_CR = "\r";

	/**
	 * 点
	 */
	String STR_DOT = ".";

	/**
	 * unix换行符
	 */
	char CHAR_LF = '\n';

	/**
	 * 回车
	 */
	char CHAR_CR = '\r';

	/**
	 * null的unicode字符
	 */
	char CHAR_NUL = '\0';

	/**
	 * 点
	 */
	char CHAR_DOT = '.';
}