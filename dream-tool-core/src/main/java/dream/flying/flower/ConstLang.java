package dream.flying.flower;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 基础常量
 *
 * @author 飞花梦影
 * @date 2022-05-15 15:05:17
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface ConstLang {

	/** 默认编码集 */
	Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	/** 默认编码集字符串 */
	String DEFAULT_CHARSET_NAME = DEFAULT_CHARSET.name();

	/** 空Json */
	String EMPTY_JSON = "{}";

	/** 所有数字 */
	String HOLE_NUMBER = "0123456789";

	/** 所有小写字符 */
	String HOLE_ALPHABET_LOWER = "abcdefghijklmnopqrstuvwxyz";

	/** 所有大写字母 */
	String HOLE_ALPHABET_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/** 索引未找到时的返回值 */
	int INDEX_NOT_FOUND = -1;

	/** 能填充的最大长度 */
	int PAD_LIMIT = 8192;

	/** 空字符串 */
	String STR_EMPTY = "";

	/** 错误 */
	String STR_ERROR = "ERROR";

	/** 失败 */
	String STR_FAILURE = "FAILURE";

	/** 空字符 */
	char CHAR_EMPTY = ' ';

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