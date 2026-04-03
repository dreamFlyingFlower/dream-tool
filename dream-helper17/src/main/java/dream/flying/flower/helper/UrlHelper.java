package dream.flying.flower.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dream.flying.flower.ConstSymbol;
import dream.flying.flower.lang.StrHelper;

/**
 * URL工具类 FIXME
 * 
 * @author 飞花梦影
 * @date 2021-03-08 16:50:53
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class UrlHelper {

	/**
	 * URL参数拼接 FIXME
	 * 
	 * @param params
	 * @return
	 */
	public static String createParams(Collection<String> params) {
		Iterator<String> it = params.iterator();
		Collection<String> result = new ArrayList<>();
		while (it.hasNext()) {
			result.add(it.next() + "=" + it.next());
		}
		return String.join("&", result);
	}

	/**
	 * URL解码,使用RFC 3986文档
	 * 
	 * <pre>
	 * transforms percent-encoded octets to
	 * characters by decoding with the UTF-8 character set. This function is primarily intended for
	 * usage with {@link java.net.URL} which unfortunately does not enforce proper URLs. As such,
	 * this method will leniently accept invalid characters or malformed percent-encoded octets and
	 * simply pass them literally through to the result string. Except for rare edge cases, this
	 * will make unencoded URLs pass through unaltered.
	 * </pre>
	 *
	 * @param url 需要解码的URL
	 * @return 解码后的URL
	 */
	public static String decodeUrl(final String url) {
		String decoded = url;
		if (url != null && url.indexOf('%') >= 0) {
			final int n = url.length();
			final StringBuilder buffer = new StringBuilder();
			final ByteBuffer bytes = ByteBuffer.allocate(n);
			for (int i = 0; i < n;) {
				if (url.charAt(i) == '%') {
					try {
						do {
							final byte octet = (byte) Integer.parseInt(url.substring(i + 1, i + 3), 16);
							bytes.put(octet);
							i += 3;
						} while (i < n && url.charAt(i) == '%');
						continue;
					} catch (final RuntimeException e) {
						// malformed percent-encoded octet, fall through and
						// append characters literally
					} finally {
						if (bytes.position() > 0) {
							bytes.flip();
							buffer.append(StandardCharsets.UTF_8.decode(bytes).toString());
							bytes.clear();
						}
					}
				}
				buffer.append(url.charAt(i++));
			}
			decoded = buffer.toString();
		}
		return decoded;
	}

	/**
	 * URLDecoder 进行URL编码
	 * 
	 * @param url 待编码的url
	 * @return 编码后字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String url) throws UnsupportedEncodingException {
		return StrHelper.isBlank(url) ? "" : URLEncoder.encode(url, StandardCharsets.UTF_8.displayName());
	}

	/**
	 * URLDecoder 进行URL解码
	 * 
	 * @param url 待解码的url
	 * @return 解码后字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(String url) throws UnsupportedEncodingException {
		return StrHelper.isBlank(url) ? "" : URLDecoder.decode(url, StandardCharsets.UTF_8.displayName());
	}

	/**
	 * 域名以及URL路径拼接
	 * 
	 * @param domain 域名
	 * @param urls URL路径列表
	 * @return 拼接后的URL
	 */
	public static String concat(String domain, String... urls) {
		return (domain.endsWith(ConstSymbol.SLASH) ? domain.substring(0, domain.length() - 1) : domain)
				+ Stream.of(urls)
						.map(url -> url.startsWith(ConstSymbol.SLASH) ? url.substring(1) : url)
						.collect(Collectors.joining(ConstSymbol.SLASH));
	}
}