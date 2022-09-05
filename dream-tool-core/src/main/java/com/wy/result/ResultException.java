package com.wy.result;

import com.wy.common.StatusMsg;
import com.wy.enums.TipEnum;

public class ResultException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int code;

	public int getCode() {
		return code;
	}

	public ResultException() {
		this(TipEnum.TIP_SYS_ERROR);
	}

	public ResultException(StatusMsg statusMsg) {
		this(null, statusMsg.getCode(), statusMsg.getMsg());
	}

	public ResultException(CharSequence message) {
		this(0, message);
	}

	public ResultException(String format, Object... args) {
		this(null, 0, format, args);
	}

	public ResultException(int code, CharSequence message) {
		this(null, code, message);
	}

	public ResultException(int code, String format, Object... args) {
		this(null, code, format, args);
	}

	public ResultException(Throwable ex) {
		this(ex, 0);
	}

	public ResultException(Throwable ex, int code) {
		this(ex, code, ex.getMessage());
	}

	public ResultException(Throwable ex, CharSequence message) {
		this(ex, 0, message);
	}

	public ResultException(Throwable ex, String format, Object... args) {
		this(ex, 0, format, args);
	}

	public ResultException(Throwable ex, int code, CharSequence message) {
		super(message.toString(), ex);
		this.code = code;
	}

	public ResultException(Throwable ex, int code, String format, Object... args) {
		super(String.format(format, args), ex);
		this.code = code;
	}

	public static void throwException() throws ResultException {
		throw new ResultException(TipEnum.TIP_SYS_ERROR);
	}

	public static void throwException(StatusMsg statusMsg) throws ResultException {
		throw new ResultException(statusMsg);
	}

	public static void throwResultException(CharSequence message) {
		throw new ResultException(0, message);
	}

	public static void throwException(String format, Object... args) {
		throw new ResultException(null, 0, format, args);
	}

	public static void throwException(int code, CharSequence message) {
		throw new ResultException(null, code, message);
	}

	public static void throwException(int code, String format, Object... args) {
		throw new ResultException(null, code, format, args);
	}

	public static void throwException(Throwable throwable) {
		throwable.printStackTrace();
		throw new ResultException(throwable, 0);
	}

	public static void throwException(Throwable throwable, int code) {
		throwable.printStackTrace();
		throw new ResultException(throwable, code, throwable.getMessage());
	}

	public static void throwResultException(Throwable throwable, CharSequence message) {
		throwException(throwable, 0, message);
	}

	public static void throwException(Throwable throwable, String format, Object... args) {
		throwException(throwable, 0, format, args);
	}

	public static void throwException(Throwable throwable, int code, CharSequence message) {
		throwable.printStackTrace();
		throw new ResultException(throwable, code, message);
	}

	public static void throwException(Throwable throwable, int code, String format, Object... args) {
		throwable.printStackTrace();
		throw new ResultException(throwable, code, String.format(format, args));
	}
}