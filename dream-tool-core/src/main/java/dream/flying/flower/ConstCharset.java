package dream.flying.flower;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 字符集编码常量
 * 
 * @author 飞花梦影
 * @date 2021-02-24 14:59:36
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ConstCharset {

	String UTF8 = "UTF-8";

	String GBK = "GBK";

	String GB2312 = "GB2312";

	String ASCII = "US-ASCII";

	String ISO_8859_1 = "ISO-8859-1";

	String UTF16BE = "UTF-16BE";

	String UTF16LE = "UTF-16LE";

	String UTF16 = "UTF-16";

	Charset CHARSET_UTF8 = StandardCharsets.UTF_8;

	Charset CHARSET_UTF16 = StandardCharsets.UTF_16;

	Charset CHARSET_UTF16BE = StandardCharsets.UTF_16BE;

	Charset CHARSET_UTF16LE = StandardCharsets.UTF_16LE;

	Charset CHARSET_ISO_8859_1 = StandardCharsets.ISO_8859_1;

	Charset CHARSET_US_ASCII = StandardCharsets.US_ASCII;

	Charset CHARSET_GBK = Charset.forName(GBK);

	Charset CHARSET_GB2312 = Charset.forName(GB2312);

	/** 默认编码集 */
	Charset DEFAULT_CHARSET = CHARSET_UTF8;

	/** 默认编码集字符串 */
	String DEFAULT_CHARSET_NAME = DEFAULT_CHARSET.name();
}