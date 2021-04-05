package com.wy.json;

/**
 * JSONException
 *
 * @author 飞花梦影
 * @date 2021-03-19 12:29:08
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JSONException extends RuntimeException {

	private static final long serialVersionUID = 0;

	public JSONException(final String message) {
		super(message);
	}

	public JSONException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public JSONException(final Throwable cause) {
		super(cause.getMessage(), cause);
	}
}