package com.wy;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 数组相关常量
 *
 * @author 飞花梦影
 * @date 2022-05-15 15:00:55
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface ConstArray {

	/** 用于hex输出 */
	char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/** 用于hex输出 */
	char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/** 空byte[]数组 */
	byte[] EMPTY_BYTE = new byte[0];

	/** 空Class数组 */
	Class<?>[] EMPTY_CLASS = new Class[0];

	/** 空Field数组 */
	Field[] EMPTY_FIELD = new Field[0];

	/** 空文件数组 */
	File[] EMPTY_FILE = new File[0];

	/** 空Method数组 */
	Method[] EMPTY_METHOD = new Method[0];

	/** 空对象数组 */
	Object[] EMPTY_OBJECT = new Object[0];

	/** Throwable数组 */
	Throwable[] EMPTY_THROWABLE = new Throwable[0];

	/** 空Type数组 */
	Type[] EMPTY_TYPE = new Type[0];

	String[] MONEY_NUM = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "拾", "佰", "仟", "万", "亿" };

	String[] MONEY_UNIT =
			new String[] { "分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟" };
}