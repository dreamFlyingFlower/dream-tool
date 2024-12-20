package dream.flying.flower.helper;

import dream.flying.flower.ConstString;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.result.ResultException;

/**
 * 写一个弱密码的判断类
 * 
 * @author 飞花梦影
 * @date 2021-01-07 17:11:28
 * @git {@link https://github.com/mygodness100}
 */
public class WeakPwdHelper {

	/**
	 * 判断是否都是重复的字符
	 * 
	 * @param str 需判断的字符串
	 */
	public static void assertRepeatChar(String str) {
		char c = str.charAt(0);
		boolean flag = true;
		for (char ch : str.toCharArray()) {
			if (ch != c) {
				flag = false;
				break;
			}
		}
		if (flag) {
			throw new ResultException("字符不能全部相同");
		}
	}

	/**
	 * 判断是否为连续的数字或英文
	 * 
	 * @param str 需判断的字符串
	 */
	public static void assertContinueChar(String str) {
		if (ConstString.NUMBER.contains(str)) {
			throw new ResultException("不能是连续的数字");
		}
		if (ConstString.ALPHABET_LOWER.contains(str.toLowerCase())) {
			throw new ResultException("不能是连续的字符");
		}
	}

	/**
	 * 判断是否是以生日为基础设置的6位密码
	 * 
	 * @param idCard 身份证号
	 * @param pwd 密码
	 */
	public static void assertCardPwd(String idCard, String pwd) {
		if (StrHelper.isNotBlank(idCard)) {
			String birth = idCard.substring(6, 14);
			if (birth.indexOf(pwd) > -1) {
				throw new ResultException("不能使用自己生日作为密码");
			}
		}
	}
}