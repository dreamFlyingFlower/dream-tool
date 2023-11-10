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

	/** 空boolean数组 */
	boolean[] EMPTY_BOOLEAN = new boolean[0];

	/** 空Boolean数组 */
	Boolean[] EMPTY_BOOLEAN_BOXED = new Boolean[0];

	/** 空byte[]数组 */
	byte[] EMPTY_BYTE = new byte[0];

	/** 空Byte数组 */
	Byte[] EMPTY_BYTE_BOXED = new Byte[0];

	/** 空char数组 */
	char[] EMPTY_CHAR = new char[0];

	/** 空Character数组 */
	Character[] EMPTY_CHAR_BOXED = new Character[0];

	/** 空Class数组 */
	Class<?>[] EMPTY_CLASS = new Class[0];

	/** 空Field数组 */
	Field[] EMPTY_FIELD = new Field[0];

	/** 空文件数组 */
	File[] EMPTY_FILE = new File[0];

	/** 空float数组 */
	float[] EMPTY_FLOAT = new float[0];

	/** 空Float数组 */
	Float[] EMPTY_FLOAT_BOXED = new Float[0];

	/** 空Method数组 */
	Method[] EMPTY_METHOD = new Method[0];

	/** 空对象数组 */
	Object[] EMPTY_OBJECT = new Object[0];

	/** 空short数组 */
	short[] EMPTY_SHORT = new short[0];

	/** 空Short数组 */
	Short[] EMPTY_SHORT_BOXED = new Short[0];

	/** 空String数组 */
	String[] EMPTY_STRING = new String[0];

	/** Throwable数组 */
	Throwable[] EMPTY_THROWABLE = new Throwable[0];

	/** 空Type数组 */
	Type[] EMPTY_TYPE = new Type[0];

	String[] MONEY_NUM = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	String[] MONEY_UNIT = new String[] { "厘", "分", "角", "元", "拾", "佰", "仟", "万", "亿", "兆", "京", "垓", "秭", "穰", "沟", "涧",
			"正", "载", "极", "恒河沙", "阿僧祇", "那由他", "不可思议", "无量", "大数", "全仕祥", "古戈尔" };

	String[] MATH_UNIT = new String[] { "个", "十", "百", "千", "万", "亿", "兆", "京", "垓", "秭", "穰", "沟", "涧", "正", "载", "极",
			"恒河沙", "阿僧祇", "那由他", "不可思议", "无量", "大数", "全仕祥", "古戈尔" };
}