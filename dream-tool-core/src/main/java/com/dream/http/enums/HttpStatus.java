package com.dream.http.enums;

/**
 * Http状态码,详见{@link https://tools.ietf.org/html/rfc7231#section-6}
 * 
 * @author 飞花梦影
 * @date 2021-03-19 16:01:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum HttpStatus {

	/**
	 * {@code 100 Continue},Informational
	 */
	CONTINUE(100, Series.INFORMATIONAL, "Continue"),
	/**
	 * {@code 101 Switching Protocols},Informational
	 */
	SWITCHING_PROTOCOLS(101, Series.INFORMATIONAL, "Switching Protocols"),
	/**
	 * {@code 102 Processing},Informational
	 */
	PROCESSING(102, Series.INFORMATIONAL, "Processing"),
	/**
	 * {@code 103 Checkpoint},Informational
	 */
	CHECKPOINT(103, Series.INFORMATIONAL, "Checkpoint"),
	/**
	 * {@code 200 OK}.Successful
	 */
	OK(200, Series.SUCCESSFUL, "OK"),
	/**
	 * {@code 201 Created}.Successful
	 */
	CREATED(201, Series.SUCCESSFUL, "Created"),
	/**
	 * {@code 202 Accepted}.Successful
	 */
	ACCEPTED(202, Series.SUCCESSFUL, "Accepted"),
	/**
	 * {@code 203 Non-Authoritative Information}.Successful
	 */
	NON_AUTHORITATIVE_INFORMATION(203, Series.SUCCESSFUL, "Non-Authoritative Information"),
	/**
	 * {@code 204 No Content}.Successful
	 */
	NO_CONTENT(204, Series.SUCCESSFUL, "No Content"),
	/**
	 * {@code 205 Reset Content}.Successful
	 */
	RESET_CONTENT(205, Series.SUCCESSFUL, "Reset Content"),
	/**
	 * {@code 206 Partial Content}.Successful
	 */
	PARTIAL_CONTENT(206, Series.SUCCESSFUL, "Partial Content"),
	/**
	 * {@code 207 Multi-Status}.Successful
	 */
	MULTI_STATUS(207, Series.SUCCESSFUL, "Multi-Status"),
	/**
	 * {@code 208 Already Reported}.Successful
	 */
	ALREADY_REPORTED(208, Series.SUCCESSFUL, "Already Reported"),
	/**
	 * {@code 226 IM Used}.Successful
	 */
	IM_USED(226, Series.SUCCESSFUL, "IM Used"),
	/**
	 * {@code 300 Multiple Choices}.
	 */
	MULTIPLE_CHOICES(300, Series.REDIRECTION, "Multiple Choices"),
	/**
	 * {@code 301 Moved Permanently}.
	 */
	MOVED_PERMANENTLY(301, Series.REDIRECTION, "Moved Permanently"),
	/**
	 * {@code 302 Found}.
	 */
	FOUND(302, Series.REDIRECTION, "Found"),
	/**
	 * {@code 303 See Other}.
	 */
	SEE_OTHER(303, Series.REDIRECTION, "See Other"),
	/**
	 * {@code 304 Not Modified}.
	 */
	NOT_MODIFIED(304, Series.REDIRECTION, "Not Modified"),
	/**
	 * {@code 307 Temporary Redirect}.
	 */
	TEMPORARY_REDIRECT(307, Series.REDIRECTION, "Temporary Redirect"),
	/**
	 * {@code 308 Permanent Redirect}.
	 */
	PERMANENT_REDIRECT(308, Series.REDIRECTION, "Permanent Redirect"),
	/**
	 * {@code 400 Bad Request}.
	 */
	BAD_REQUEST(400, Series.CLIENT_ERROR, "Bad Request"),
	/**
	 * {@code 401 Unauthorized}.
	 */
	UNAUTHORIZED(401, Series.CLIENT_ERROR, "Unauthorized"),
	/**
	 * {@code 402 Payment Required}.
	 */
	PAYMENT_REQUIRED(402, Series.CLIENT_ERROR, "Payment Required"),
	/**
	 * {@code 403 Forbidden}.
	 */
	FORBIDDEN(403, Series.CLIENT_ERROR, "Forbidden"),
	/**
	 * {@code 404 Not Found}.
	 */
	NOT_FOUND(404, Series.CLIENT_ERROR, "Not Found"),
	/**
	 * {@code 405 Method Not Allowed}.
	 */
	METHOD_NOT_ALLOWED(405, Series.CLIENT_ERROR, "Method Not Allowed"),
	/**
	 * {@code 406 Not Acceptable}.
	 */
	NOT_ACCEPTABLE(406, Series.CLIENT_ERROR, "Not Acceptable"),
	/**
	 * {@code 407 Proxy Authentication Required}.
	 */
	PROXY_AUTHENTICATION_REQUIRED(407, Series.CLIENT_ERROR, "Proxy Authentication Required"),
	/**
	 * {@code 408 Request Timeout}.
	 */
	REQUEST_TIMEOUT(408, Series.CLIENT_ERROR, "Request Timeout"),
	/**
	 * {@code 409 Conflict}.
	 */
	CONFLICT(409, Series.CLIENT_ERROR, "Conflict"),
	/**
	 * {@code 410 Gone}.
	 */
	GONE(410, Series.CLIENT_ERROR, "Gone"),
	/**
	 * {@code 411 Length Required}.
	 */
	LENGTH_REQUIRED(411, Series.CLIENT_ERROR, "Length Required"),
	/**
	 * {@code 412 Precondition failed}.
	 */
	PRECONDITION_FAILED(412, Series.CLIENT_ERROR, "Precondition Failed"),
	/**
	 * {@code 413 Payload Too Large}.
	 */
	PAYLOAD_TOO_LARGE(413, Series.CLIENT_ERROR, "Payload Too Large"),
	/**
	 * {@code 414 URI Too Long}.
	 */
	URI_TOO_LONG(414, Series.CLIENT_ERROR, "URI Too Long"),
	/**
	 * {@code 415 Unsupported Media Type}.
	 */
	UNSUPPORTED_MEDIA_TYPE(415, Series.CLIENT_ERROR, "Unsupported Media Type"),
	/**
	 * {@code 416 Requested Range Not Satisfiable}.
	 */
	REQUESTED_RANGE_NOT_SATISFIABLE(416, Series.CLIENT_ERROR, "Requested range not satisfiable"),
	/**
	 * {@code 417 Expectation Failed}.
	 */
	EXPECTATION_FAILED(417, Series.CLIENT_ERROR, "Expectation Failed"),
	/**
	 * {@code 418 I'm a teapot}.
	 */
	I_AM_A_TEAPOT(418, Series.CLIENT_ERROR, "I'm a teapot"),
	/**
	 * {@code 422 Unprocessable Entity}.
	 */
	UNPROCESSABLE_ENTITY(422, Series.CLIENT_ERROR, "Unprocessable Entity"),
	/**
	 * {@code 423 Locked}.
	 */
	LOCKED(423, Series.CLIENT_ERROR, "Locked"),
	/**
	 * {@code 424 Failed Dependency}.
	 */
	FAILED_DEPENDENCY(424, Series.CLIENT_ERROR, "Failed Dependency"),
	/**
	 * {@code 425 Too Early}.
	 */
	TOO_EARLY(425, Series.CLIENT_ERROR, "Too Early"),
	/**
	 * {@code 426 Upgrade Required}.
	 */
	UPGRADE_REQUIRED(426, Series.CLIENT_ERROR, "Upgrade Required"),
	/**
	 * {@code 428 Precondition Required}.
	 */
	PRECONDITION_REQUIRED(428, Series.CLIENT_ERROR, "Precondition Required"),
	/**
	 * {@code 429 Too Many Requests}.
	 */
	TOO_MANY_REQUESTS(429, Series.CLIENT_ERROR, "Too Many Requests"),
	/**
	 * {@code 431 Request Header Fields Too Large}.
	 */
	REQUEST_HEADER_FIELDS_TOO_LARGE(431, Series.CLIENT_ERROR, "Request Header Fields Too Large"),
	/**
	 * {@code 451 Unavailable For Legal Reasons}.
	 */
	UNAVAILABLE_FOR_LEGAL_REASONS(451, Series.CLIENT_ERROR, "Unavailable For Legal Reasons"),
	/**
	 * {@code 500 Internal Server Error}.
	 */
	INTERNAL_SERVER_ERROR(500, Series.SERVER_ERROR, "Internal Server Error"),
	/**
	 * {@code 501 Not Implemented}.
	 */
	NOT_IMPLEMENTED(501, Series.SERVER_ERROR, "Not Implemented"),
	/**
	 * {@code 502 Bad Gateway}.
	 */
	BAD_GATEWAY(502, Series.SERVER_ERROR, "Bad Gateway"),
	/**
	 * {@code 503 Service Unavailable}.
	 */
	SERVICE_UNAVAILABLE(503, Series.SERVER_ERROR, "Service Unavailable"),
	/**
	 * {@code 504 Gateway Timeout}.
	 */
	GATEWAY_TIMEOUT(504, Series.SERVER_ERROR, "Gateway Timeout"),
	/**
	 * {@code 505 HTTP Version Not Supported}.
	 */
	HTTP_VERSION_NOT_SUPPORTED(505, Series.SERVER_ERROR, "HTTP Version not supported"),
	/**
	 * {@code 506 Variant Also Negotiates}
	 */
	VARIANT_ALSO_NEGOTIATES(506, Series.SERVER_ERROR, "Variant Also Negotiates"),
	/**
	 * {@code 507 Insufficient Storage}
	 */
	INSUFFICIENT_STORAGE(507, Series.SERVER_ERROR, "Insufficient Storage"),
	/**
	 * {@code 508 Loop Detected}
	 */
	LOOP_DETECTED(508, Series.SERVER_ERROR, "Loop Detected"),
	/**
	 * {@code 509 Bandwidth Limit Exceeded}
	 */
	BANDWIDTH_LIMIT_EXCEEDED(509, Series.SERVER_ERROR, "Bandwidth Limit Exceeded"),
	/**
	 * {@code 510 Not Extended}
	 */
	NOT_EXTENDED(510, Series.SERVER_ERROR, "Not Extended"),
	/**
	 * {@code 511 Network Authentication Required}.
	 */
	NETWORK_AUTHENTICATION_REQUIRED(511, Series.SERVER_ERROR, "Network Authentication Required");

	private final int value;

	private final Series series;

	private final String reasonPhrase;

	HttpStatus(int value, Series series, String reasonPhrase) {
		this.value = value;
		this.series = series;
		this.reasonPhrase = reasonPhrase;
	}

	public int value() {
		return this.value;
	}

	public Series series() {
		return this.series;
	}

	public String getReasonPhrase() {
		return this.reasonPhrase;
	}

	/**
	 * 判断Http状态码是信息类状态
	 * 
	 * @return true->是信息类,false->否
	 */
	public boolean is1xxInformational() {
		return (series() == Series.INFORMATIONAL);
	}

	/**
	 * 判断Http状态码是请求成功类状态
	 * 
	 * @return true->是,false->否
	 */
	public boolean is2xxSuccessful() {
		return (series() == Series.SUCCESSFUL);
	}

	/**
	 * 判断Http状态码是重定向类状态
	 * 
	 * @return true->是,false->否
	 */
	public boolean is3xxRedirection() {
		return (series() == Series.REDIRECTION);
	}

	/**
	 * 判断Http状态码是客户端错误类状态
	 * 
	 * @return true->是,false->否
	 */
	public boolean is4xxClientError() {
		return (series() == Series.CLIENT_ERROR);
	}

	/**
	 * 判断Http状态码是服务端错误类状态
	 * 
	 * @return true->是,false->否
	 */
	public boolean is5xxServerError() {
		return (series() == Series.SERVER_ERROR);
	}

	/**
	 * 判断Http状态码是错误类状态
	 * 
	 * @return true->是,false->否
	 */
	public boolean isError() {
		return (is4xxClientError() || is5xxServerError());
	}

	@Override
	public String toString() {
		return this.value + " " + name();
	}

	/**
	 * 解析状态码,若不存在,抛异常
	 * 
	 * @param statusCode 状态码
	 * @return {@link HttpStatus}
	 * @throws IllegalArgumentException
	 */
	public static HttpStatus valueOf(int statusCode) {
		HttpStatus status = resolve(statusCode);
		if (status == null) {
			throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
		}
		return status;
	}

	/**
	 * 解析状态码
	 * 
	 * @param statusCode 状态码
	 * @return {@link HttpStatus} 或 null
	 */
	public static HttpStatus resolve(int statusCode) {
		for (HttpStatus status : values()) {
			if (status.value == statusCode) {
				return status;
			}
		}
		return null;
	}

	/**
	 * HttpStatus状态分类枚举
	 * 
	 * @author 飞花梦影
	 * @date 2021-03-19 17:18:36
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	public enum Series {

		INFORMATIONAL(1),
		SUCCESSFUL(2),
		REDIRECTION(3),
		CLIENT_ERROR(4),
		SERVER_ERROR(5);

		private final int value;

		Series(int value) {
			this.value = value;
		}

		public int value() {
			return this.value;
		}

		/**
		 * 解析状态码,若不存在,抛异常
		 * 
		 * @param statusCode 状态码
		 * @return {@code Series}
		 * @throws IllegalArgumentException
		 */
		public static Series valueOf(int statusCode) {
			Series series = resolve(statusCode);
			if (series == null) {
				throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
			}
			return series;
		}

		/**
		 * 解析状态码
		 * 
		 * @param statusCode 状态码
		 * @return {@code Series},或null
		 */
		public static Series resolve(int statusCode) {
			int seriesCode = statusCode / 100;
			for (Series series : values()) {
				if (series.value == seriesCode) {
					return series;
				}
			}
			return null;
		}
	}
}