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
public interface ConstString {

	/** 所有小写字符 */
	String ALPHABET_LOWER = "abcdefghijklmnopqrstuvwxyz";

	/** 所有大写字母 */
	String ALPHABET_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/** and */
	String AND = "and";

	/** 一个空字符 */
	char CHAR_SPACE = ' ';

	/** 默认编码集 */
	Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	/** 默认编码集字符串 */
	String DEFAULT_CHARSET_NAME = DEFAULT_CHARSET.name();

	/** 冒号分隔符 */
	String DELIMITER_COLON = ConstSymbol.COLON;

	/** 逗号分隔符 */
	String DELIMITER_COMMA = ConstSymbol.COMMA;

	/** 横杠分隔符 */
	String DELIMITER_DASH = ConstSymbol.HORIZONTAL_LINE;

	/** 下划线分隔符 */
	String DELIMITER_UNDERLINE = ConstSymbol.UNDERLINE;

	/** 空Json */
	String EMPTY_JSON = "{}";

	/** FALSE */
	String FALSE = "FALSE";

	/** N */
	String N = "N";

	/** NO */
	String NO = "NO";

	/** null字符串 */
	String NULL = "NULL";

	/** 所有数字 */
	String NUMBER = "0123456789";

	/** OFF */
	String OFF = "OFF";

	/** ON */
	String ON = "ON";

	/** or */
	String OR = "or";

	/** 能填充的最大长度 */
	int PAD_LIMIT = 8192;

	/** 斜杠 */
	String SLASH = ConstSymbol.SLASH;

	/** 空字符串 */
	String STR_EMPTY = "";

	/** 错误 */
	String STR_ERROR = "ERROR";

	/** 失败 */
	String STR_FAILURE = "FAILURE";

	/** 一个空白字符串 */
	String STR_SPACE = " ";

	/** 成功 */
	String SUCCESS = "SUCCESS";

	/** YES */
	String TRUE = "TRUE";

	/** 未知 */
	String UNKNOWN = "UNKNOWN";

	/** Y */
	String Y = "Y";

	/** YES */
	String YES = "YES";

	/** 零 */
	String ZERO = "0";
}