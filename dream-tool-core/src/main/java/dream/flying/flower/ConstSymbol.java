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

	/** 尖括号结束 */
	String ANGLE_BRACKET_END = ">";

	/** 尖括号开始 */
	String ANGLE_BRACKET_START = "<";

	/** @ */
	String AT = "@";

	/** 转义符,反斜杠 */
	String BACK_SLASH = "\\";

	/** 反单引号 */
	String BACKQUOTE = "`";

	/** 大括号结束符号 */
	String BRACE_END = "}";

	/** 大括号开始符号 */
	String BRACE_START = "{";

	/** ^ */
	String CARET = "^";

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

	/** 冒号 */
	String COLON = ":";

	/** 逗号 */
	String COMMA = ",";

	/** 回车 */
	String CR = "\r";

	/** $ */
	String DOLLAR = "$";

	/** 点 */
	String DOT = ".";

	/** 双引号 */
	String DOUBLE_QUOTE = "\"";

	/** 等号 */
	String EQUALS = "=";

	/** 井号 */
	String HASH_MARK = "#";

	/** 横线 */
	String HORIZONTAL_LINE = "-";

	/** 换行符 */
	String LINE_SEPARATOR = System.lineSeparator();

	/** 小括号结束 */
	String PARENTHESES_END = ")";

	/** 小括号开始 */
	String PARENTHESES_START = "(";

	/** 百分号 */
	String PERCENT = "%";

	/** 加号 */
	String PLUS = "+";

	/** 问号 */
	String QUESTION_MARK = "?";

	/** 分号 */
	String SEMICOLON = ";";

	/** 单引号 */
	String SINGLE_QUOTE = "'";

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

	/** * */
	String STAR = "*";

	/** 制表符 */
	String TAB = "\t";

	/** 下划线 */
	String UNDERLINE = "_";

	/** Linux换行符 */
	String UNIX_LF = "\n";

	/** Linux文件分隔符 */
	String UNIX_SEPARATOR = "/";

	/** 竖线,该分隔符进行分割的时候需要转义 */
	String VERTICAL_LINE = "|";

	/** 波浪线 */
	String WAVY_LINE = "~";

	/** Windows换行符 */
	String WIN_CRLF = "\r\n";

	/** Windows文件分隔符 */
	String WIN_SEPARATOR = "\\";
}