package dream.flying.flower.helper;

import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.text.HtmlFilter;

/**
 * Escape转义工具类 FIXME
 * 
 * @author 飞花梦影
 * @date 2021-03-10 15:12:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class EscapeHelper {

	public static final String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";

	private static final char[][] TEXT = new char[64][];

	private static final String QUOT = "&quot;";

	private static final String AMP = "&amp;";

	private static final String APOS = "&apos;";

	private static final String GT = "&gt;";

	private static final String LT = "&lt;";

	static {
		for (int i = 0; i < 64; i++) {
			TEXT[i] = new char[] { (char) i };
		}
		TEXT['\''] = "&#039;".toCharArray(); // 单引号
		TEXT['"'] = "&#34;".toCharArray(); // 单引号
		TEXT['&'] = "&#38;".toCharArray(); // &符
		TEXT['<'] = "&#60;".toCharArray(); // 小于号
		TEXT['>'] = "&#62;".toCharArray(); // 大于号
	}

	/**
	 * 转义文本中的HTML字符为安全的字符
	 * 
	 * @param text 被转义的文本
	 * @return 转义后的文本
	 */
	public static String escape(String text) {
		return encode(text);
	}

	/**
	 * 还原被转义的HTML特殊字符
	 * 
	 * @param content 包含转义符的HTML内容
	 * @return 转换后的字符串
	 */
	public static String unescape(String content) {
		return decode(content);
	}

	/**
	 * 清除所有HTML标签,但是不删除标签内的内容
	 * 
	 * @param content 文本
	 * @return 清除标签后的文本
	 */
	public static String clean(String content) {
		return new HtmlFilter().filter(content);
	}

	/**
	 * Escape编码
	 * 
	 * @param text 被编码的文本
	 * @return 编码后的字符
	 */
	private static String encode(String text) {
		int len;
		if ((text == null) || ((len = text.length()) == 0)) {
			return "";
		}
		StringBuilder buffer = new StringBuilder(len + (len >> 2));
		char c;
		for (int i = 0; i < len; i++) {
			c = text.charAt(i);
			if (c < 64) {
				buffer.append(TEXT[c]);
			} else {
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	/**
	 * Escape解码
	 * 
	 * @param content 被转义的内容
	 * @return 解码后的字符串
	 */
	public static String decode(String content) {
		if (StrHelper.isBlank(content)) {
			return content;
		}
		StringBuilder tmp = new StringBuilder(content.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < content.length()) {
			pos = content.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (content.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(content.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(content.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(content.substring(lastPos));
					lastPos = content.length();
				} else {
					tmp.append(content.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	/**
	 * XML字符转义包括(<,>,',&,")五个字符
	 * 
	 * @param value 所需转义的字符串
	 * @return 转义后的字符串
	 */
	public static String escapeXml(String value) {
		StringBuilder writer = new StringBuilder();
		char[] chars = value.trim().toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			switch (c) {
			case '<':
				writer.append(LT);
				break;
			case '>':
				writer.append(GT);
				break;
			case '\'':
				writer.append(APOS);
				break;
			case '&':
				writer.append(AMP);
				break;
			case '\"':
				writer.append(QUOT);
				break;
			default:
				if ((c == 0x9) || (c == 0xA) || (c == 0xD) || ((c >= 0x20) && (c <= 0xD7FF))
				        || ((c >= 0xE000) && (c <= 0xFFFD)) || ((c >= 0x10000) && (c <= 0x10FFFF)))
					writer.append(c);
			}
		}
		return writer.toString();
	}
}