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

	/** 所有数字 */
	String HOLE_NUMBER = "0123456789";

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