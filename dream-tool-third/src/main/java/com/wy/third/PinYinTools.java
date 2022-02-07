package com.wy.third;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音工具类
 *
 * @author 飞花梦影
 * @date 2022-01-27 10:04:04
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class PinYinTools {

	private final static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
	static {
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
	}

	/**
	 * 将汉字转拼音
	 * 
	 * @param str 待转换的字符串
	 * @param fill 间隔符
	 * @return 转换后的拼音
	 */
	public static String CN2Pinyin(String str) {
		return CN2Pinyin(str, "");
	}

	/**
	 * 将汉字转拼音
	 * 
	 * @param str 待转换的字符串
	 * @param fill 间隔符
	 * @return 转换后的拼音
	 */
	public static String CN2Pinyin(String str, String fill) {
		try {
			StringBuffer sb = new StringBuffer();
			if (fill == null) {
				fill = "";
			}
			boolean isCn = true;
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (i > 0 && isCn) {
					sb.append(fill);
				}
				if (c == ' ') {
					sb.append(fill);
				}
				// 判断是否为中文
				if (c >= '\u4e00' && c <= '\u9fa5') {
					isCn = true;
					sb.append(PinyinHelper.toHanyuPinyinStringArray(c, format)[0]);
				} else {
					if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
						sb.append(c);
					}
					isCn = false;
				}
			}
			return sb.toString();
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将每个汉字拼音的第一个字母拼接起来
	 * 
	 * @param str 待操作的汉字
	 * @return 拼接后的字符串
	 */
	public static String strFirst2Pinyin(String str) {
		try {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if (c >= '\u4e00' && c <= '\u9fa5') {
					sb.append(PinyinHelper.toHanyuPinyinStringArray(c, format)[0].charAt(0));
				}
			}
			return sb.toString();
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return null;
	}
}