package dream.flying.flower;

/**
 * 字符串常量
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

	/** 所有数字 */
	String NUMBER = "0123456789";

	/** and */
	String AND = "AND";

	/** or */
	String OR = "OR";

	/** 冒号分隔符 */
	String DELIMITER_COLON = ConstSymbol.COLON;

	/** 逗号分隔符 */
	String DELIMITER_COMMA = ConstSymbol.COMMA;

	/** 横杠分隔符 */
	String DELIMITER_DASH = ConstSymbol.HORIZONTAL_LINE;

	/** 下划线分隔符 */
	String DELIMITER_UNDERLINE = ConstSymbol.UNDERLINE;

	/** 空字符串 */
	String EMPTY = "";

	/** 空Json */
	String EMPTY_JSON = "{}";

	/** 一个空白字符串 */
	String ONE_SPACE = " ";

	/** null字符串 */
	String NULL = "NULL";

	/** 能填充的最大长度 */
	int PAD_LIMIT = 8192;

	/** 斜杠 */
	String SLASH = ConstSymbol.SLASH;

	/** 错误 */
	String ERROR = "ERROR";

	/** 失败 */
	String FAIL = "FAIL";

	/** 失败 */
	String FAILURE = "FAILURE";

	/** 成功 */
	String SUCCESS = "SUCCESS";

	/** 未知 */
	String UNKNOWN = "UNKNOWN";

	/** N */
	String N = "N";

	/** NO */
	String NO = "NO";

	/** Y */
	String Y = "Y";

	/** YES */
	String YES = "YES";

	/** OK */
	String OK = "OK";

	/** FALSE */
	String FALSE = "FALSE";

	/** YES */
	String TRUE = "TRUE";

	/** OFF */
	String OFF = "OFF";

	/** ON */
	String ON = "ON";

	/** 零 */
	String ZERO = "0";

	/** 1 */
	String ONE = "1";

	/** -1 */
	String ONE_NEGATIVE = "-1";
}