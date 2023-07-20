package com.wy.lang;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Shorts;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2023-07-20 15:41:27
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class NumberTools {

	public static double[] concat(double[]... array) {
		return Doubles.concat(array);
	}

	public static float[] concat(float[]... array) {
		return Floats.concat(array);
	}

	public static int[] concat(int[]... array) {
		return Ints.concat(array);
	}

	public static long[] join(long[]... array) {
		return Longs.concat(array);
	}

	public static short[] join(short[]... array) {
		return Shorts.concat(array);
	}

	public static String join(String delimiter, final double... array) {
		return Doubles.join(delimiter, array);
	}

	public static String join(String delimiter, final float... array) {
		return Floats.join(delimiter, array);
	}

	public static String join(String delimiter, final int... array) {
		return Ints.join(delimiter, array);
	}

	public static String join(String delimiter, final long... array) {
		return Longs.join(delimiter, array);
	}

	public static String join(String delimiter, final short... array) {
		return Shorts.join(delimiter, array);
	}
}