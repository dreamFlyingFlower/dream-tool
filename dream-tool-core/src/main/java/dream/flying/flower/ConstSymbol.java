package dream.flying.flower;

/**
 * 常用符号常量
 *
 * @author 飞花梦影
 * @date 2022-05-15 15:05:17
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface ConstSymbol {

	/** & */
	String AND = "&";

	/** 转义符,反斜杠 */
	String BACKSLASH = "\\";

	/** 大括号结束符号 */
	String BRACE_END = "}";

	/** 大括号开始符号 */
	String BRACE_START = "{";

	/** 反斜杠 */
	char CHAR_BACKSLASH = '\\';

	/** 大括号结束符号 */
	char CHAR_BRACE_END = '}';

	/** 大括号开始符号 */
	char CHAR_BRACE_START = '{';

	/** 回车 */
	char CHAR_CR = '\r';

	/** 点 */
	char CHAR_DOT = '.';

	/** null的unicode字符 */
	char CHAR_NUL = '\0';

	/** unix换行符 */
	char CHAR_UNIX_LF = '\n';

	/** Unix系统文件分隔符 */
	char CHAR_UNIX_SEPARATOR = '/';

	/** Windows系统文件分隔符 */
	char CHAR_WIN_SEPARATOR = '\\';

	/** 逗号 */
	String COMMA = ",";

	/** 回车 */
	String CR = "\r";

	/** 点 */
	String DOT = ".";

	/** 横线 */
	String HORIZONTAL_LINE = "-";

	/** 换行符 */
	String LINE_SEPARATOR = System.lineSeparator();

	/** 小括号结束 */
	String PARENTHESES_END = ")";

	/** 小括号开始 */
	String PARENTHESES_START = "(";

	/** 斜杠 */
	String SLASH = "/";

	/** 特殊字符串 */
	String SPECIAL = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#�?…�?&*（）—�?+|{}【�?‘；：�?“�?。，、？]";

	/** 竖线拆分 */
	String SPLIT_VERTICAL_LINE = "\\|";

	/** 中括号结束 */
	String SQUARE_BRACKETS_END = "]";

	/** 中括号开始 */
	String SQUARE_BRACKETS_START = "[";

	/** 下划线 */
	String UNDERLINE = "_";

	/** Linux换行符 */
	String UNIX_LF = "\n";

	/** Linux文件分隔符 */
	String UNIX_SEPARATOR = "/";

	/** 竖线,该分隔符进行分割的时候需要转义 */
	String VERTICAL_LINE = "|";

	/** Windows换行符 */
	String WIN_CRLF = "\r\n";

	/** Windows文件分隔符 */
	String WIN_SEPARATOR = "\\";
}