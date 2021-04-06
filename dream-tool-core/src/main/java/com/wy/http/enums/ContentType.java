package com.wy.http.enums;

import java.awt.PageAttributes.MediaType;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.activation.MimeType;

import com.wy.annotation.Nullable;
import com.wy.collection.LinkedCaseInsensitiveMap;
import com.wy.collection.MapTool;
import com.wy.lang.AssertTool;
import com.wy.lang.StrTool;

/**
 * org.springframework.http.MediaType,org.apache.http.entity.ContentType
 * 
 * @author 飞花梦影
 * @date 2021-03-19 17:24:14
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ContentType {

	protected static final String WILDCARD_TYPE = "*";

	private static final String PARAM_CHARSET = "charset";

	private final String type;

	private final String subtype;

	private final Map<String, String> parameters;

	private static final BitSet TOKEN;

	@Nullable
	private Charset resolvedCharset;

	@Nullable
	private volatile String toStringValue;

	/**
	 * Public constant media type that includes all media ranges (i.e. "&#42;/&#42;").
	 */
	public static final ContentType ALL;

	/**
	 * A String equivalent of {@code MediaType#ALL}.
	 */
	public static final String ALL_VALUE = "*/*";

	/**
	 * Public constant media type for {@code application/atom+xml}.
	 */
	public static final ContentType APPLICATION_ATOM_XML;

	/**
	 * A String equivalent of {@code application/atom+xml}.
	 */
	public static final String APPLICATION_ATOM_XML_VALUE = "application/atom+xml";

	/**
	 * Public constant media type for {@code application/cbor}.
	 * 
	 * @since 5.2
	 */
	public static final ContentType APPLICATION_CBOR;

	/**
	 * A String equivalent of {@code application/cbor}.
	 * 
	 * @since 5.2
	 */
	public static final String APPLICATION_CBOR_VALUE = "application/cbor";

	/**
	 * Public constant media type for {@code application/x-www-form-urlencoded}.
	 */
	public static final ContentType APPLICATION_FORM_URLENCODED;

	/**
	 * A String equivalent of {@code application/x-www-form-urlencoded}.
	 */
	public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";

	/**
	 * Public constant media type for {@code application/json}.
	 */
	public static final ContentType APPLICATION_JSON;

	/**
	 * A String equivalent of {@code application/json}.
	 * 
	 * @see #APPLICATION_JSON_UTF8_VALUE
	 */
	public static final String APPLICATION_JSON_VALUE = "application/json";

	/**
	 * Public constant media type for {@code application/json;charset=UTF-8}.
	 * 
	 * @deprecated as of 5.2 in favor of {@code #APPLICATION_JSON} since major browsers like Chrome
	 *             <a href="https://bugs.chromium.org/p/chromium/issues/detail?id=438464"> now
	 *             comply with the specification</a> and interpret correctly UTF-8 special
	 *             characters without requiring a {@code charset=UTF-8} parameter.
	 */
	@Deprecated
	public static final ContentType APPLICATION_JSON_UTF8;

	/**
	 * A String equivalent of {@link MediaType#APPLICATION_JSON_UTF8}.
	 * 
	 * @deprecated as of 5.2 in favor of {@link #APPLICATION_JSON_VALUE} since major browsers like
	 *             Chrome <a href="https://bugs.chromium.org/p/chromium/issues/detail?id=438464">
	 *             now comply with the specification</a> and interpret correctly UTF-8 special
	 *             characters without requiring a {@code charset=UTF-8} parameter.
	 */
	@Deprecated
	public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

	/**
	 * Public constant media type for {@code application/octet-stream}.
	 */
	public static final ContentType APPLICATION_OCTET_STREAM;

	/**
	 * A String equivalent of {@code application/octet-stream}.
	 */
	public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";

	/**
	 * Public constant media type for {@code application/pdf}.
	 */
	public static final ContentType APPLICATION_PDF;

	/**
	 * A String equivalent of {@code application/pdf}.
	 * 
	 * @since 4.3
	 */
	public static final String APPLICATION_PDF_VALUE = "application/pdf";

	/**
	 * Public constant media type for {@code application/problem+json}.
	 */
	public static final ContentType APPLICATION_PROBLEM_JSON;

	/**
	 * A String equivalent of {@code application/problem+json}.
	 */
	public static final String APPLICATION_PROBLEM_JSON_VALUE = "application/problem+json";

	/**
	 * Public constant media type for {@code application/problem+json}.
	 */
	public static final ContentType APPLICATION_PROBLEM_JSON_UTF8;

	/**
	 * A String equivalent of {@code application/problem+json;charset=UTF-8}.
	 */
	@Deprecated
	public static final String APPLICATION_PROBLEM_JSON_UTF8_VALUE = "application/problem+json;charset=UTF-8";

	/**
	 * Public constant media type for {@code application/problem+xml}.
	 */
	public static final ContentType APPLICATION_PROBLEM_XML;

	/**
	 * A String equivalent of {@code application/problem+xml}.
	 */
	public static final String APPLICATION_PROBLEM_XML_VALUE = "application/problem+xml";

	/**
	 * Public constant media type for {@code application/rss+xml}.
	 */
	public static final ContentType APPLICATION_RSS_XML;

	/**
	 * A String equivalent of {@code MediaType#APPLICATION_RSS_XML}.
	 */
	public static final String APPLICATION_RSS_XML_VALUE = "application/rss+xml";

	/**
	 * Public constant media type for {@code application/x-ndjson}.
	 */
	public static final ContentType APPLICATION_NDJSON;

	/**
	 * A String equivalent of {@code application/x-ndjson}.
	 */
	public static final String APPLICATION_NDJSON_VALUE = "application/x-ndjson";

	/**
	 * Public constant media type for {@code application/stream+json}.
	 * 
	 * @deprecated as of 5.3, see notice on {@code #APPLICATION_STREAM_JSON_VALUE}.
	 */
	@Deprecated
	public static final ContentType APPLICATION_STREAM_JSON;

	/**
	 * A String equivalent of {@link MediaType#APPLICATION_STREAM_JSON}.
	 * 
	 * @deprecated as of 5.3 since it originates from the W3C Activity Streams specification which
	 *             has a more specific purpose and has been since replaced with a different mime
	 *             type. Use {@link #APPLICATION_NDJSON} as a replacement or any other
	 *             line-delimited JSON format (e.g. JSON Lines, JSON Text Sequences).
	 */
	@Deprecated
	public static final String APPLICATION_STREAM_JSON_VALUE = "application/stream+json";

	/**
	 * Public constant media type for {@code application/xhtml+xml}.
	 */
	public static final ContentType APPLICATION_XHTML_XML;

	/**
	 * A String equivalent of {@code application/xhtml+xml}.
	 */
	public static final String APPLICATION_XHTML_XML_VALUE = "application/xhtml+xml";

	/**
	 * Public constant media type for {@code application/xml}.
	 */
	public static final ContentType APPLICATION_XML;

	/**
	 * A String equivalent of {@code application/xml}.
	 */
	public static final String APPLICATION_XML_VALUE = "application/xml";

	/**
	 * Public constant media type for {@code image/gif}.
	 */
	public static final ContentType IMAGE_GIF;

	/**
	 * A String equivalent of {@code image/gif}.
	 */
	public static final String IMAGE_GIF_VALUE = "image/gif";

	/**
	 * Public constant media type for {@code image/jpeg}.
	 */
	public static final ContentType IMAGE_JPEG;

	/**
	 * A String equivalent of {@code image/jpeg}.
	 */
	public static final String IMAGE_JPEG_VALUE = "image/jpeg";

	/**
	 * Public constant media type for {@code image/png}.
	 */
	public static final ContentType IMAGE_PNG;

	/**
	 * A String equivalent of {@code image/png}.
	 */
	public static final String IMAGE_PNG_VALUE = "image/png";

	/**
	 * Public constant media type for {@code multipart/form-data}.
	 */
	public static final ContentType MULTIPART_FORM_DATA;

	/**
	 * A String equivalent of {@code multipart/form-data}.
	 */
	public static final String MULTIPART_FORM_DATA_VALUE = "multipart/form-data";

	/**
	 * Public constant media type for {@code multipart/mixed}.
	 */
	public static final ContentType MULTIPART_MIXED;

	/**
	 * A String equivalent of {@code multipart/mixed}.
	 */
	public static final String MULTIPART_MIXED_VALUE = "multipart/mixed";

	/**
	 * Public constant media type for {@code multipart/related}.
	 */
	public static final ContentType MULTIPART_RELATED;

	/**
	 * A String equivalent of {@code multipart/related}.
	 */
	public static final String MULTIPART_RELATED_VALUE = "multipart/related";

	/**
	 * Public constant media type for {@code text/event-stream}.
	 */
	public static final ContentType TEXT_EVENT_STREAM;

	/**
	 * A String equivalent of {@code text/event-stream}.
	 */
	public static final String TEXT_EVENT_STREAM_VALUE = "text/event-stream";

	/**
	 * Public constant media type for {@code text/html}.
	 */
	public static final ContentType TEXT_HTML;

	/**
	 * A String equivalent of {@code text/html}.
	 */
	public static final String TEXT_HTML_VALUE = "text/html";

	/**
	 * Public constant media type for {@code text/markdown}.
	 */
	public static final ContentType TEXT_MARKDOWN;

	/**
	 * A String equivalent of {@code text/markdown}.
	 */
	public static final String TEXT_MARKDOWN_VALUE = "text/markdown";

	/**
	 * Public constant media type for {@code text/plain}.
	 */
	public static final ContentType TEXT_PLAIN;

	/**
	 * A String equivalent of {@code text/plain}.
	 */
	public static final String TEXT_PLAIN_VALUE = "text/plain";

	/**
	 * Public constant media type for {@code text/xml}.
	 */
	public static final ContentType TEXT_XML;

	/**
	 * A String equivalent of {@code text/xml}.
	 */
	public static final String TEXT_XML_VALUE = "text/xml";

	private static final String PARAM_QUALITY_FACTOR = "q";

	static {
		ALL = new ContentType("*", "*");
		APPLICATION_ATOM_XML = new ContentType("application", "atom+xml");
		APPLICATION_CBOR = new ContentType("application", "cbor");
		APPLICATION_FORM_URLENCODED = new ContentType("application", "x-www-form-urlencoded");
		APPLICATION_JSON = new ContentType("application", "json");
		APPLICATION_JSON_UTF8 = new ContentType("application", "json", StandardCharsets.UTF_8);
		APPLICATION_NDJSON = new ContentType("application", "x-ndjson");
		APPLICATION_OCTET_STREAM = new ContentType("application", "octet-stream");
		APPLICATION_PDF = new ContentType("application", "pdf");
		APPLICATION_PROBLEM_JSON = new ContentType("application", "problem+json");
		APPLICATION_PROBLEM_JSON_UTF8 = new ContentType("application", "problem+json", StandardCharsets.UTF_8);
		APPLICATION_PROBLEM_XML = new ContentType("application", "problem+xml");
		APPLICATION_RSS_XML = new ContentType("application", "rss+xml");
		APPLICATION_STREAM_JSON = new ContentType("application", "stream+json");
		APPLICATION_XHTML_XML = new ContentType("application", "xhtml+xml");
		APPLICATION_XML = new ContentType("application", "xml");
		IMAGE_GIF = new ContentType("image", "gif");
		IMAGE_JPEG = new ContentType("image", "jpeg");
		IMAGE_PNG = new ContentType("image", "png");
		MULTIPART_FORM_DATA = new ContentType("multipart", "form-data");
		MULTIPART_MIXED = new ContentType("multipart", "mixed");
		MULTIPART_RELATED = new ContentType("multipart", "related");
		TEXT_EVENT_STREAM = new ContentType("text", "event-stream");
		TEXT_HTML = new ContentType("text", "html");
		TEXT_MARKDOWN = new ContentType("text", "markdown");
		TEXT_PLAIN = new ContentType("text", "plain");
		TEXT_XML = new ContentType("text", "xml");
	}

	static {
		// variable names refer to RFC 2616, section 2.2
		BitSet ctl = new BitSet(128);
		for (int i = 0; i <= 31; i++) {
			ctl.set(i);
		}
		ctl.set(127);

		BitSet separators = new BitSet(128);
		separators.set('(');
		separators.set(')');
		separators.set('<');
		separators.set('>');
		separators.set('@');
		separators.set(',');
		separators.set(';');
		separators.set(':');
		separators.set('\\');
		separators.set('\"');
		separators.set('/');
		separators.set('[');
		separators.set(']');
		separators.set('?');
		separators.set('=');
		separators.set('{');
		separators.set('}');
		separators.set(' ');
		separators.set('\t');

		TOKEN = new BitSet(128);
		TOKEN.set(0, 128);
		TOKEN.andNot(ctl);
		TOKEN.andNot(separators);
	}

	/**
	 * Create a new {@code MediaType} for the given primary type.
	 * <p>
	 * The {@linkplain #getSubtype() subtype} is set to "&#42;", parameters empty.
	 * 
	 * @param type the primary type
	 * @throws IllegalArgumentException if any of the parameters contain illegal characters
	 */
	public ContentType(String type) {
		this(type, WILDCARD_TYPE);
	}

	/**
	 * Create a new {@code MediaType} for the given primary type and subtype.
	 * <p>
	 * The parameters are empty.
	 * 
	 * @param type the primary type
	 * @param subtype the subtype
	 * @throws IllegalArgumentException if any of the parameters contain illegal characters
	 */
	public ContentType(String type, String subtype) {
		this(type, subtype, Collections.emptyMap());
	}

	/**
	 * Create a new {@code MediaType} for the given type, subtype, and character set.
	 * 
	 * @param type the primary type
	 * @param subtype the subtype
	 * @param charset the character set
	 * @throws IllegalArgumentException if any of the parameters contain illegal characters
	 */
	public ContentType(String type, String subtype, Charset charset) {
		this(type, subtype, Collections.singletonMap(PARAM_CHARSET, charset.name()));
		this.resolvedCharset = charset;
	}

	/**
	 * Create a new {@code MediaType} for the given type, subtype, and quality value.
	 * 
	 * @param type the primary type
	 * @param subtype the subtype
	 * @param qualityValue the quality value
	 * @throws IllegalArgumentException if any of the parameters contain illegal characters
	 */
	public ContentType(String type, String subtype, double qualityValue) {
		this(type, subtype, Collections.singletonMap(PARAM_QUALITY_FACTOR, Double.toString(qualityValue)));
	}

	/**
	 * Copy-constructor that copies the type, subtype and parameters of the given {@code MediaType},
	 * and allows to set the specified character set.
	 * 
	 * @param other the other media type
	 * @param charset the character set
	 * @throws IllegalArgumentException if any of the parameters contain illegal characters
	 */
	public ContentType(ContentType other, Charset charset) {
		this(other.getType(), other.getSubtype(), addCharsetParameter(charset, other.getParameters()));
		this.resolvedCharset = charset;
	}

	private static Map<String, String> addCharsetParameter(Charset charset, Map<String, String> parameters) {
		Map<String, String> map = new LinkedHashMap<>(parameters);
		map.put(PARAM_CHARSET, charset.name());
		return map;
	}

	/**
	 * Copy-constructor that copies the type and subtype of the given {@code MediaType}, and allows
	 * for different parameters.
	 * 
	 * @param other the other media type
	 * @param parameters the parameters, may be {@code null}
	 * @throws IllegalArgumentException if any of the parameters contain illegal characters
	 */
	public ContentType(ContentType other, @Nullable Map<String, String> parameters) {
		this(other.getType(), other.getSubtype(), parameters);
	}

	/**
	 * Create a new {@code MediaType} for the given type, subtype, and parameters.
	 * 
	 * @param type the primary type
	 * @param subtype the subtype
	 * @param parameters the parameters, may be {@code null}
	 * @throws IllegalArgumentException if any of the parameters contain illegal characters
	 */
	public ContentType(String type, String subtype, @Nullable Map<String, String> parameters) {
		AssertTool.notBlank(type, "'type' must not be empty");
		AssertTool.notBlank(subtype, "'subtype' must not be empty");
		checkToken(type);
		checkToken(subtype);
		this.type = type.toLowerCase(Locale.ENGLISH);
		this.subtype = subtype.toLowerCase(Locale.ENGLISH);
		if (MapTool.isNotEmpty(parameters)) {
			Map<String, String> map = new LinkedCaseInsensitiveMap<>(parameters.size(), Locale.ENGLISH);
			parameters.forEach((attribute, value) -> {
				checkParameters(attribute, value);
				map.put(attribute, value);
			});
			this.parameters = Collections.unmodifiableMap(map);
		} else {
			this.parameters = Collections.emptyMap();
		}
	}

	/**
	 * Create a new {@code MediaType} for the given {@link MimeType}. The type, subtype and
	 * parameters information is copied and {@code MediaType}-specific checks on parameters are
	 * performed.
	 * 
	 * @param mimeType the MIME type
	 * @throws IllegalArgumentException if any of the parameters contain illegal characters
	 */
	public ContentType(ContentType other) {
		this.type = other.type;
		this.subtype = other.subtype;
		this.parameters = other.parameters;
		this.resolvedCharset = other.resolvedCharset;
		this.toStringValue = other.toStringValue;
		getParameters().forEach(this::checkParameters);
	}

	private void checkToken(String token) {
		for (int i = 0; i < token.length(); i++) {
			char ch = token.charAt(i);
			if (!TOKEN.get(ch)) {
				throw new IllegalArgumentException("Invalid token character '" + ch + "' in token \"" + token + "\"");
			}
		}
	}

	protected void checkParameters(String attribute, String value) {
		AssertTool.notBlank(attribute, "'attribute' must not be empty");
		AssertTool.notBlank(value, "'value' must not be empty");
		checkToken(attribute);
		if (PARAM_CHARSET.equals(attribute)) {
			if (this.resolvedCharset == null) {
				this.resolvedCharset = Charset.forName(unquote(value));
			}
		} else if (!isQuotedString(value)) {
			checkToken(value);
		}
		if (PARAM_QUALITY_FACTOR.equals(attribute)) {
			value = unquote(value);
			double d = Double.parseDouble(value);
			AssertTool.isTrue(d >= 0D && d <= 1D,
					"Invalid quality value \"" + value + "\": should be between 0.0 and 1.0");
		}
	}

	private boolean isQuotedString(String s) {
		if (s.length() < 2) {
			return false;
		} else {
			return ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'")));
		}
	}

	protected String unquote(String s) {
		return (isQuotedString(s) ? s.substring(1, s.length() - 1) : s);
	}

	/**
	 * Return the primary type.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Return the subtype.
	 */
	public String getSubtype() {
		return this.subtype;
	}

	/**
	 * Return the quality factor, as indicated by a {@code q} parameter, if any. Defaults to
	 * {@code 1.0}.
	 * 
	 * @return the quality factor as double value
	 */
	public double getQualityValue() {
		String qualityFactor = getParameter(PARAM_QUALITY_FACTOR);
		return (qualityFactor != null ? Double.parseDouble(unquote(qualityFactor)) : 1D);
	}

	/**
	 * Return the subtype suffix as defined in RFC 6839.
	 * 
	 * @since 5.3
	 */
	@Nullable
	public String getSubtypeSuffix() {
		int suffixIndex = this.subtype.lastIndexOf('+');
		if (suffixIndex != -1 && this.subtype.length() > suffixIndex) {
			return this.subtype.substring(suffixIndex + 1);
		}
		return null;
	}

	/**
	 * Return the character set, as indicated by a {@code charset} parameter, if any.
	 * 
	 * @return the character set, or {@code null} if not available
	 * @since 4.3
	 */
	@Nullable
	public Charset getCharset() {
		return this.resolvedCharset;
	}

	/**
	 * Return a generic parameter value, given a parameter name.
	 * 
	 * @param name the parameter name
	 * @return the parameter value, or {@code null} if not present
	 */
	@Nullable
	public String getParameter(String name) {
		return this.parameters.get(name);
	}

	/**
	 * Return all generic parameter values.
	 * 
	 * @return a read-only map (possibly empty, never {@code null})
	 */
	public Map<String, String> getParameters() {
		return this.parameters;
	}

	/**
	 * Indicates whether the {@linkplain #getType() type} is the wildcard character
	 * <code>&#42;</code> or not.
	 */
	public boolean isWildcardType() {
		return WILDCARD_TYPE.equals(getType());
	}

	/**
	 * Indicates whether the {@linkplain #getSubtype() subtype} is the wildcard character
	 * <code>&#42;</code> or the wildcard character followed by a suffix (e.g.
	 * <code>&#42;+xml</code>).
	 * 
	 * @return whether the subtype is a wildcard
	 */
	public boolean isWildcardSubtype() {
		return WILDCARD_TYPE.equals(getSubtype()) || getSubtype().startsWith("*+");
	}

	/**
	 * Indicate whether this {@code MediaType} includes the given media type.
	 * <p>
	 * For instance, {@code text/*} includes {@code text/plain} and {@code text/html}, and
	 * {@code application/*+xml} includes {@code application/soap+xml}, etc. This method is
	 * <b>not</b> symmetric.
	 * <p>
	 * Simply calls {@link MimeType#includes(MimeType)} but declared with a {@code MediaType}
	 * parameter for binary backwards compatibility.
	 * 
	 * @param other the reference media type with which to compare
	 * @return {@code true} if this media type includes the given media type; {@code false}
	 *         otherwise
	 */
	public boolean includes(@Nullable ContentType other) {
		if (other == null) {
			return false;
		}
		if (isWildcardType()) {
			// */* includes anything
			return true;
		} else if (getType().equals(other.getType())) {
			if (getSubtype().equals(other.getSubtype())) {
				return true;
			}
			if (isWildcardSubtype()) {
				// Wildcard with suffix, e.g. application/*+xml
				int thisPlusIdx = getSubtype().lastIndexOf('+');
				if (thisPlusIdx == -1) {
					return true;
				} else {
					// application/*+xml includes application/soap+xml
					int otherPlusIdx = other.getSubtype().lastIndexOf('+');
					if (otherPlusIdx != -1) {
						String thisSubtypeNoSuffix = getSubtype().substring(0, thisPlusIdx);
						String thisSubtypeSuffix = getSubtype().substring(thisPlusIdx + 1);
						String otherSubtypeSuffix = other.getSubtype().substring(otherPlusIdx + 1);
						if (thisSubtypeSuffix.equals(otherSubtypeSuffix) && WILDCARD_TYPE.equals(thisSubtypeNoSuffix)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Return a replica of this instance with the quality value of the given {@code MediaType}.
	 * 
	 * @return the same instance if the given MediaType doesn't have a quality value, or a new one
	 *         otherwise
	 */
	public ContentType copyQualityValue(ContentType mediaType) {
		if (!mediaType.getParameters().containsKey(PARAM_QUALITY_FACTOR)) {
			return this;
		}
		Map<String, String> params = new LinkedHashMap<>(getParameters());
		params.put(PARAM_QUALITY_FACTOR, mediaType.getParameters().get(PARAM_QUALITY_FACTOR));
		return new ContentType(this, params);
	}

	/**
	 * Return a replica of this instance with its quality value removed.
	 * 
	 * @return the same instance if the media type doesn't contain a quality value, or a new one
	 *         otherwise
	 */
	public ContentType removeQualityValue() {
		if (!getParameters().containsKey(PARAM_QUALITY_FACTOR)) {
			return this;
		}
		Map<String, String> params = new LinkedHashMap<>(getParameters());
		params.remove(PARAM_QUALITY_FACTOR);
		return new ContentType(this, params);
	}

	/**
	 * Tokenize the given comma-separated string of {@code MimeType} objects into a
	 * {@code List<String>}. Unlike simple tokenization by ",", this method takes into account
	 * quoted parameters.
	 * 
	 * @param mimeTypes the string to tokenize
	 * @return the list of tokens
	 */
	public static List<String> tokenize(String mimeTypes) {
		if (StrTool.isBlank(mimeTypes)) {
			return Collections.emptyList();
		}
		List<String> tokens = new ArrayList<>();
		boolean inQuotes = false;
		int startIndex = 0;
		int i = 0;
		while (i < mimeTypes.length()) {
			switch (mimeTypes.charAt(i)) {
			case '"':
				inQuotes = !inQuotes;
				break;
			case ',':
				if (!inQuotes) {
					tokens.add(mimeTypes.substring(startIndex, i));
					startIndex = i + 1;
				}
				break;
			case '\\':
				i++;
				break;
			}
			i++;
		}
		tokens.add(mimeTypes.substring(startIndex));
		return tokens;
	}

	/**
	 * Re-create the given mime types as media types.
	 * 
	 * @since 5.0
	 */
	public static List<ContentType> asMediaTypes(List<ContentType> mimeTypes) {
		List<ContentType> mediaTypes = new ArrayList<>(mimeTypes.size());
		for (ContentType mimeType : mimeTypes) {
			mediaTypes.add(ContentType.asMediaType(mimeType));
		}
		return mediaTypes;
	}

	/**
	 * Re-create the given mime type as a media type.
	 */
	public static ContentType asMediaType(ContentType mimeType) {
		if (mimeType instanceof ContentType) {
			return (ContentType) mimeType;
		}
		return new ContentType(mimeType.getType(), mimeType.getSubtype(), mimeType.getParameters());
	}

	/**
	 * Return a string representation of the given list of {@code MediaType} objects.
	 * <p>
	 * This method can be used to for an {@code Accept} or {@code Content-Type} header.
	 * 
	 * @param mediaTypes the media types to create a string representation for
	 * @return the string representation
	 */
	public static String toString(Collection<ContentType> mediaTypes) {
		StringBuilder builder = new StringBuilder();
		for (Iterator<? extends ContentType> iterator = mediaTypes.iterator(); iterator.hasNext();) {
			ContentType mimeType = iterator.next();
			mimeType.appendTo(builder);
			if (iterator.hasNext()) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}

	protected void appendTo(StringBuilder builder) {
		builder.append(this.type);
		builder.append('/');
		builder.append(this.subtype);
		appendTo(this.parameters, builder);
	}

	private void appendTo(Map<String, String> map, StringBuilder builder) {
		map.forEach((key, val) -> {
			builder.append(';');
			builder.append(key);
			builder.append('=');
			builder.append(val);
		});
	}

	/**
	 * Sorts the given list of {@code MediaType} objects by specificity.
	 * <p>
	 * Given two media types:
	 * <ol>
	 * <li>if either media type has a {@linkplain #isWildcardType() wildcard type}, then the media
	 * type without the wildcard is ordered before the other.</li>
	 * <li>if the two media types have different {@linkplain #getType() types}, then they are
	 * considered equal and remain their current order.</li>
	 * <li>if either media type has a {@linkplain #isWildcardSubtype() wildcard subtype}, then the
	 * media type without the wildcard is sorted before the other.</li>
	 * <li>if the two media types have different {@linkplain #getSubtype() subtypes}, then they are
	 * considered equal and remain their current order.</li>
	 * <li>if the two media types have different {@linkplain #getQualityValue() quality value}, then
	 * the media type with the highest quality value is ordered before the other.</li>
	 * <li>if the two media types have a different amount of {@linkplain #getParameter(String)
	 * parameters}, then the media type with the most parameters is ordered before the other.</li>
	 * </ol>
	 * <p>
	 * For example: <blockquote>audio/basic &lt; audio/* &lt; *&#047;*</blockquote>
	 * <blockquote>audio/* &lt; audio/*;q=0.7; audio/*;q=0.3</blockquote>
	 * <blockquote>audio/basic;level=1 &lt; audio/basic</blockquote> <blockquote>audio/basic ==
	 * text/html</blockquote> <blockquote>audio/basic == audio/wave</blockquote>
	 * 
	 * @param mediaTypes the list of media types to be sorted
	 * @see <a href="https://tools.ietf.org/html/rfc7231#section-5.3.2">HTTP 1.1: Semantics and
	 *      Content, section 5.3.2</a>
	 */
	public static void sortBySpecificity(List<ContentType> mediaTypes) {
		AssertTool.notNull(mediaTypes, "'mediaTypes' must not be null");
		if (mediaTypes.size() > 1) {
			mediaTypes.sort(SPECIFICITY_COMPARATOR);
		}
	}

	/**
	 * Sorts the given list of {@code MediaType} objects by quality value.
	 * <p>
	 * Given two media types:
	 * <ol>
	 * <li>if the two media types have different {@linkplain #getQualityValue() quality value}, then
	 * the media type with the highest quality value is ordered before the other.</li>
	 * <li>if either media type has a {@linkplain #isWildcardType() wildcard type}, then the media
	 * type without the wildcard is ordered before the other.</li>
	 * <li>if the two media types have different {@linkplain #getType() types}, then they are
	 * considered equal and remain their current order.</li>
	 * <li>if either media type has a {@linkplain #isWildcardSubtype() wildcard subtype}, then the
	 * media type without the wildcard is sorted before the other.</li>
	 * <li>if the two media types have different {@linkplain #getSubtype() subtypes}, then they are
	 * considered equal and remain their current order.</li>
	 * <li>if the two media types have a different amount of {@linkplain #getParameter(String)
	 * parameters}, then the media type with the most parameters is ordered before the other.</li>
	 * </ol>
	 * 
	 * @param mediaTypes the list of media types to be sorted
	 * @see #getQualityValue()
	 */
	public static void sortByQualityValue(List<ContentType> mediaTypes) {
		AssertTool.notNull(mediaTypes, "'mediaTypes' must not be null");
		if (mediaTypes.size() > 1) {
			mediaTypes.sort(QUALITY_VALUE_COMPARATOR);
		}
	}

	/**
	 * Sorts the given list of {@code MediaType} objects by specificity as the primary criteria and
	 * quality value the secondary.
	 * 
	 * @see MediaType#sortBySpecificity(List)
	 * @see MediaType#sortByQualityValue(List)
	 */
	public static void sortBySpecificityAndQuality(List<ContentType> mediaTypes) {
		AssertTool.notNull(mediaTypes, "'mediaTypes' must not be null");
		if (mediaTypes.size() > 1) {
			mediaTypes.sort(ContentType.SPECIFICITY_COMPARATOR.thenComparing(ContentType.QUALITY_VALUE_COMPARATOR));
		}
	}

	/**
	 * Comparator used by {@link #sortByQualityValue(List)}.
	 */
	public static final Comparator<ContentType> QUALITY_VALUE_COMPARATOR = (mediaType1, mediaType2) -> {
		double quality1 = mediaType1.getQualityValue();
		double quality2 = mediaType2.getQualityValue();
		int qualityComparison = Double.compare(quality2, quality1);
		if (qualityComparison != 0) {
			return qualityComparison; // audio/*;q=0.7 < audio/*;q=0.3
		} else if (mediaType1.isWildcardType() && !mediaType2.isWildcardType()) { // */* < audio/*
			return 1;
		} else if (mediaType2.isWildcardType() && !mediaType1.isWildcardType()) { // audio/* > */*
			return -1;
		} else if (!mediaType1.getType().equals(mediaType2.getType())) { // audio/basic == text/html
			return 0;
		} else { // mediaType1.getType().equals(mediaType2.getType())
			if (mediaType1.isWildcardSubtype() && !mediaType2.isWildcardSubtype()) { // audio/* <
																						// audio/basic
				return 1;
			} else if (mediaType2.isWildcardSubtype() && !mediaType1.isWildcardSubtype()) { // audio/basic
																							// >
																							// audio/*
				return -1;
			} else if (!mediaType1.getSubtype().equals(mediaType2.getSubtype())) { // audio/basic ==
																					// audio/wave
				return 0;
			} else {
				int paramsSize1 = mediaType1.getParameters().size();
				int paramsSize2 = mediaType2.getParameters().size();
				return Integer.compare(paramsSize2, paramsSize1); // audio/basic;level=1 <
																	// audio/basic
			}
		}
	};

	/**
	 * Comparator used by {@link #sortBySpecificity(List)}.
	 */
	public static final Comparator<ContentType> SPECIFICITY_COMPARATOR = new SpecificityComparator<ContentType>() {

		@Override
		protected int compareParameters(ContentType mediaType1, ContentType mediaType2) {
			double quality1 = mediaType1.getQualityValue();
			double quality2 = mediaType2.getQualityValue();
			int qualityComparison = Double.compare(quality2, quality1);
			if (qualityComparison != 0) {
				return qualityComparison; // audio/*;q=0.7
											// <
											// audio/*;q=0.3
			}
			return super.compareParameters(mediaType1, mediaType2);
		}
	};

	/**
	 * Comparator to sort {@link MimeType MimeTypes} in order of specificity.
	 *
	 * @param <T> the type of mime types that may be compared by this comparator
	 */
	public static class SpecificityComparator<T extends ContentType> implements Comparator<T> {

		@Override
		public int compare(T mimeType1, T mimeType2) {
			if (mimeType1.isWildcardType() && !mimeType2.isWildcardType()) { // */* < audio/*
				return 1;
			} else if (mimeType2.isWildcardType() && !mimeType1.isWildcardType()) { // audio/* > */*
				return -1;
			} else if (!mimeType1.getType().equals(mimeType2.getType())) { // audio/basic ==
																			// text/html
				return 0;
			} else { // mediaType1.getType().equals(mediaType2.getType())
				if (mimeType1.isWildcardSubtype() && !mimeType2.isWildcardSubtype()) { // audio/* <
																						// audio/basic
					return 1;
				} else if (mimeType2.isWildcardSubtype() && !mimeType1.isWildcardSubtype()) { // audio/basic
																								// >
																								// audio/*
					return -1;
				} else if (!mimeType1.getSubtype().equals(mimeType2.getSubtype())) { // audio/basic
																						// ==
																						// audio/wave
					return 0;
				} else { // mediaType2.getSubtype().equals(mediaType2.getSubtype())
					return compareParameters(mimeType1, mimeType2);
				}
			}
		}

		protected int compareParameters(T mimeType1, T mimeType2) {
			int paramsSize1 = mimeType1.getParameters().size();
			int paramsSize2 = mimeType2.getParameters().size();
			return Integer.compare(paramsSize2, paramsSize1); // audio/basic;level=1 < audio/basic
		}
	}
}