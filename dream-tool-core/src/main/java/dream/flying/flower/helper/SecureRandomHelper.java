package dream.flying.flower.helper;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import dream.flying.flower.ConstLang;
import dream.flying.flower.lang.AssertHelper;

/**
 * SecureRandom随机数字,随机字符串工具类,比使用Random随机数更安全
 * 
 * <pre>
 * 1.Random最多生成48位随机值,SecureRandom最多可生成128位随机值
 * 2.SecureRandom不使用固定种子值,而是从操作系统/dev/random 随机数文件中不断获取新的种子值
 * 3.SHA1PRNG:Windows下默认的随机数生成算法.该算法基于SHA-1算法,但通过添加额外的步骤来提高随机性.
 * 		该算法使用一个种子来初始化随机数生成器,使用SHA-1算法来计算一个哈希值,这个哈希值会被用来产生随机数
 * 4.NativePRNGBlocking:Linux下默认的随机数生成算法.
 * 		算法初始播种时使用/dev/random中的20个字节初始化内部 SHA1PRNG 实例,当调用nextInt()等:
 * 		使用内部 SHA1PRNG 实例的输出和从 /dev/random 读取的数据的进行 异或.
 * 		算法每次计算随机数需要从/dev/random文件中获取数值.当/dev/random的随机数不足时,NativePRNGBlocking将会被阻塞.
 * 		在桌面应用程序中,/dev/random文件很少会受到阻塞,因为它可以收集用户的鼠标、点击等事件.
 * 		然而,在Web程序中,由于并发度较高,生成/dev/random数据可能会出现不足的情况
 * 		如有人使用 NativePRNGBlocking 算法,在线上环境服务启动时,一直被阻塞,就是因为/dev/random数据较少,NativePRNGBlocking 初始化被阻塞
 * 5.NativePRNGNonBlocking:为了避免获取随机数被阻塞,NativePRNGNonBlocking选择从/dev/urandom中获取随机数.
 * 		在/dev/urandom 和 /dev/random之间有一些区别:
 * 		/dev/random通过收集系统上的环境噪声(如硬件噪声、磁盘活动等)来生成随机数,它只在系统上具有足够的环境噪声时才能生成高质量的随机数;
 * 		/dev/urandom是一个伪随机数生成器设备文件,它使用内部熵池持续生成随机数,不管系统上的环境噪声有多少.
 * 		因此,/dev/urandom生成随机数的速度比/dev/random快得多
 * 6./dev/urandom生成的随机数质量稍差,但是稳定;/dev/random生成的随机数质量较高,但是在系统噪音较少时生成随机数据较慢,可能会阻塞部分应用.
 * 		建议一般情况下使用NativePRNGNonBlocking来读取/dev/urandom,这样可以换取稳定的随机数据输出,虽然牺牲了一些随机数的质量.
 * 		使用阻塞算法可能导致线上问题
 * 7.SecureRandom并不是线程安全的,可以使用synchronized关键字同步,或者 使用ThreadLocal 为每个线程保存一个 SecureRandom实例
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-04-15 16:01:43
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SecureRandomHelper {

	private static final Random RANDOM = new SecureRandom();

	public SecureRandomHelper() {
		super();
	}

	/**
	 * 获得一个随机的boolean值
	 * 
	 * @return boolean值
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean nextBoolean() throws NoSuchAlgorithmException {
		return RANDOM.nextBoolean();
	}

	/**
	 * 获得一个随机的字节数组
	 *
	 * @param 字节数组大小,大于0
	 * @return 随机字节数组
	 */
	public static byte[] nextBytes(final int count) {
		AssertHelper.isTrue(count >= 0, "Count cannot be negative.");
		final byte[] result = new byte[count];
		RANDOM.nextBytes(result);
		return result;
	}

	/**
	 * 返回0到{@link Double#MAX_VALUE}之间的随机值
	 *
	 * @return 随机double值
	 */
	public static double nextDouble() {
		return nextDouble(0, Double.MAX_VALUE);
	}

	/**
	 * 返回一个范围内的随机double值,含头不含尾
	 *
	 * @param start 最小值,必须是正数
	 * @param end 最大值,不会返回该值
	 * @return 随机double值
	 */
	public static double nextDouble(final double start, final double end) {
		AssertHelper.isTrue(end >= start, "Start value must be smaller or equal to end value.");
		AssertHelper.isTrue(start >= 0, "Both range values must be non-negative.");
		if (start == end) {
			return start;
		}
		return start + ((end - start) * RANDOM.nextDouble());
	}

	/**
	 * 返回0到{@link Float#MAX_VALUE}之间的随机值
	 *
	 * @return 随机float值
	 */
	public static float nextFloat() {
		return nextFloat(0, Float.MAX_VALUE);
	}

	/**
	 * 返回一个范围内的随机float值,含头不含尾
	 *
	 * @param start 最小值,必须是正数
	 * @param end 最大值,不会返回该值
	 * @return 随机float值
	 */
	public static float nextFloat(final float start, final float end) {
		AssertHelper.isTrue(end >= start, "Start value must be smaller or equal to end value.");
		AssertHelper.isTrue(start >= 0, "Both range values must be non-negative.");
		if (start == end) {
			return start;
		}
		return start + ((end - start) * RANDOM.nextFloat());
	}

	/**
	 * 返回0到 {@link Integer#MAX_VALUE}之间的随机值
	 *
	 * @return 随机int值
	 */
	public static int nextInt() {
		return nextInt(0, Integer.MAX_VALUE);
	}

	/**
	 * 返回一个范围内的随机int值,含头不含尾
	 *
	 * @param start 最小值,必须是正整数
	 * @param end 最大值,不会返回该值
	 * @return 随机int值
	 */
	public static int nextInt(final int start, final int end) {
		AssertHelper.isTrue(end >= start, "Start value must be smaller or equal to end value.");
		AssertHelper.isTrue(start >= 0, "Both range values must be non-negative.");
		if (start == end) {
			return start;
		}
		return start + RANDOM.nextInt(end - start);
	}

	/**
	 * 返回0到{@link Long#MAX_VALUE}之间的随机值
	 *
	 * @return 随机long值
	 */
	public static long nextLong() {
		return nextLong(0, Long.MAX_VALUE);
	}

	/**
	 * 返回一个范围内的随机long值,含头不含尾
	 *
	 * @param start 最小值,必须是正整数
	 * @param end 最大值,不会返回该值
	 * @return 随机long值
	 */
	public static long nextLong(final long start, final long end) {
		AssertHelper.isTrue(end >= start, "Start value must be smaller or equal to end value.");
		AssertHelper.isTrue(start >= 0, "Both range values must be non-negative.");
		if (start == end) {
			return start;
		}
		return (long) nextDouble(start, end);
	}

	/**
	 * 返回一个指定长度的随机字符串,不包含数字和字符
	 *
	 * @param count 随机字符串长度
	 * @return 随机字符串
	 */
	public static String random(final int count) {
		return random(count, false, false);
	}

	/**
	 * 从指定字符数组中返回一个指定长度的随机字符串
	 *
	 * @param count 随机字符串长度
	 * @param chars 目标字符数组,可为null,但不可为''
	 * @return 随机字符串
	 */
	public static String random(final int count, final char... chars) {
		if (chars == null) {
			return random(count, 0, 0, false, false, null, RANDOM);
		}
		return random(count, 0, chars.length, false, false, chars, RANDOM);
	}

	/**
	 * 从指定字符数组中返回一个指定长度的随机字符串
	 *
	 * @param count 随机字符串长度
	 * @param chars 目标字符数组,可为null,但不可为''
	 * @return 随机字符串
	 */
	public static String random(final int count, final String chars) {
		if (chars == null) {
			return random(count, 0, 0, false, false, null, RANDOM);
		}
		return random(count, chars.toCharArray());
	}

	/**
	 * 返回一个指定长度的随机字符串,可指定是否包含数字或字母
	 *
	 * @param count 随机字符串长度
	 * @param letters 是否包含字母,true->包含,false->不包含
	 * @param numbers 是否包含数字,true->包含,false->不包含
	 * @return 随机字符串
	 */
	public static String random(final int count, final boolean letters, final boolean numbers) {
		return random(count, 0, 0, letters, numbers);
	}

	/**
	 * 返回一个指定长度的随机字符串,可指定是否包含数字或字母
	 *
	 * @param count 随机字符串长度
	 * @param start ASCII中开始位置
	 * @param end ASCII中结束位置
	 * @param letters 是否包含字母,true->包含,false->不包含
	 * @param numbers 是否包含数字,true->包含,false->不包含
	 * @return 随机字符串
	 */
	public static String random(final int count, final int start, final int end, final boolean letters,
			final boolean numbers) {
		return random(count, start, end, letters, numbers, null, RANDOM);
	}

	/**
	 * 返回一个指定长度的随机字符串,可指定是否包含数字或字母
	 *
	 * @param count 随机字符串长度
	 * @param start 指定字符数组中开始索引
	 * @param end 指定字符数组中结束索引
	 * @param letters 是否包含字母,true->包含,false->不包含
	 * @param numbers 是否包含数字,true->包含,false->不包含
	 * @param chars 可以选择的字符数组,若为null,则可选择所有的字符
	 * @return 随机字符串
	 */
	public static String random(final int count, final int start, final int end, final boolean letters,
			final boolean numbers, final char... chars) {
		return random(count, start, end, letters, numbers, chars, RANDOM);
	}

	/**
	 * 返回一个指定长度的随机字符串,可指定是否包含数字或字母
	 *
	 * @param count 随机字符串长度
	 * @param start 指定字符数组中开始索引
	 * @param end 指定字符数组中结束索引
	 * @param letters 是否包含字母,true->包含,false->不包含
	 * @param numbers 是否包含数字,true->包含,false->不包含
	 * @param chars 可以选择的字符数组,若为null,则可选择所有的字符
	 * @param random 随机源实例
	 * @return 随机字符串
	 */
	public static String random(int count, int start, int end, final boolean letters, final boolean numbers,
			final char[] chars, final Random random) {
		if (count == 0) {
			return ConstLang.STR_EMPTY;
		} else if (count < 0) {
			throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
		}
		if (chars != null && chars.length == 0) {
			throw new IllegalArgumentException("The chars array must not be empty");
		}

		if (start == 0 && end == 0) {
			if (chars != null) {
				end = chars.length;
			} else {
				if (!letters && !numbers) {
					end = Character.MAX_CODE_POINT;
				} else {
					end = 'z' + 1;
					start = ' ';
				}
			}
		} else {
			if (end <= start) {
				throw new IllegalArgumentException(
						"Parameter end (" + end + ") must be greater than start (" + start + ")");
			}
		}
		final int zero_digit_ascii = 48;
		final int first_letter_ascii = 65;
		if (chars == null && (numbers && end <= zero_digit_ascii || letters && end <= first_letter_ascii)) {
			throw new IllegalArgumentException(
					"Parameter end (" + end + ") must be greater then (" + zero_digit_ascii + ") for generating digits "
							+ "or greater then (" + first_letter_ascii + ") for generating letters.");
		}
		final StringBuilder builder = new StringBuilder(count);
		final int gap = end - start;
		while (count-- != 0) {
			int codePoint;
			if (chars == null) {
				codePoint = random.nextInt(gap) + start;
				switch (Character.getType(codePoint)) {
				case Character.UNASSIGNED:
				case Character.PRIVATE_USE:
				case Character.SURROGATE:
					count++;
					continue;
				}
			} else {
				codePoint = chars[random.nextInt(gap) + start];
			}
			final int numberOfChars = Character.charCount(codePoint);
			if (count == 0 && numberOfChars > 1) {
				count++;
				continue;
			}
			if (letters && Character.isLetter(codePoint) || numbers && Character.isDigit(codePoint)
					|| !letters && !numbers) {
				builder.appendCodePoint(codePoint);
				if (numberOfChars == 2) {
					count--;
				}
			} else {
				count++;
			}
		}
		return builder.toString();
	}

	/**
	 * 返回一个随机长度的随机字符串,只包含ASCII从32到127之间的字符,包含大小写字符,数字和特殊字符
	 *
	 * @param count 随机字符串长度
	 * @return 随机字符串
	 */
	public static String randomAscii(final int count) {
		return random(count, 32, 127, false, false);
	}

	/**
	 * 返回一个随机长度的随机字符串,只包含ASCII从32到127之间的字符,包含大小写字符,数字和特殊字符
	 *
	 * @param minLength 随机长度最小值
	 * @param maxLength 随机长度最大值
	 * @return 随机字符串
	 */
	public static String randomAscii(final int minLength, final int maxLength) {
		return randomAscii(nextInt(minLength, maxLength));
	}

	/**
	 * 返回一个指定长度的随机字符串,只包含a-z,A-Z
	 *
	 * @param count 随机字符串长度
	 * @return 随机字符串
	 */
	public static String randomAlphabetic(final int count) {
		return random(count, true, false);
	}

	/**
	 * 返回一个随机长度的随机字符串,只包含a-z,A-Z
	 *
	 * @param minLength 随机长度最小值
	 * @param maxLength 随机长度最大值
	 * @return 随机字符串
	 */
	public static String randomAlphabetic(final int minLength, final int maxLength) {
		return randomAlphabetic(nextInt(minLength, maxLength));
	}

	/**
	 * 返回一个指定长度的随机字符串,只包含a-z,A-Z,0-9
	 *
	 * @param count 随机字符串长度
	 * @return 随机字符串
	 */
	public static String randomAlphanumeric(final int count) {
		return random(count, true, true);
	}

	/**
	 * 返回一个随机长度的随机字符串,只包含a-z,A-Z,0-9
	 *
	 * @param minLength 随机长度最小值
	 * @param maxLength 随机长度最大值
	 * @return 随机字符串
	 */
	public static String randomAlphanumeric(final int minLength, final int maxLength) {
		return randomAlphanumeric(nextInt(minLength, maxLength));
	}

	/**
	 * 返回一个指定长度的随机字符串,只包含ASCII从33到126之间的字符 POSIX [:graph:]
	 *
	 * @param count 随机字符串长度
	 * @return 随机字符串
	 */
	public static String randomGraph(final int count) {
		return random(count, 33, 126, false, false);
	}

	/**
	 * 返回一个随机长度的随机字符串,只包含ASCII从33到126之间的字符 POSIX [:graph:]
	 *
	 * @param minLength 随机长度最小值
	 * @param maxLength 随机长度最大值
	 * @return 随机字符串
	 */
	public static String randomGraph(final int minLength, final int maxLength) {
		return randomGraph(nextInt(minLength, maxLength));
	}

	/**
	 * 返回一个指定长度的随机字符串,只包含0-9
	 *
	 * @param count 随机字符串长度
	 * @return 随机字符串
	 */
	public static String randomNumeric(final int count) {
		return random(count, false, true);
	}

	/**
	 * 返回一个随机长度的随机字符串,只包含0-9
	 *
	 * @param minLength 随机长度最小值
	 * @param maxLength 随机长度最大值
	 * @return 随机字符串
	 */
	public static String randomNumeric(final int minLength, final int maxLength) {
		return randomNumeric(nextInt(minLength, maxLength));
	}

	/**
	 * 返回一个指定长度的随机字符串,只包含ASCII从32到126之间的字符 POSIX [:graph:]
	 *
	 * @param count 随机字符串长度
	 * @return 随机字符串
	 */
	public static String randomPrint(final int count) {
		return random(count, 32, 126, false, false);
	}

	/**
	 * 返回一个随机长度的随机字符串,只包含ASCII从32到126之间的字符 POSIX [:graph:]
	 *
	 * @param minLength 随机长度最小值
	 * @param maxLength 随机长度最大值
	 * @return 随机字符串
	 */
	public static String randomPrint(final int minLength, final int maxLength) {
		return randomPrint(nextInt(minLength, maxLength));
	}
}