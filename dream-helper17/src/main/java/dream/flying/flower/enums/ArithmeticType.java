package dream.flying.flower.enums;

import java.util.stream.Stream;

/**
 * 简单运算类型
 *
 * @author 飞花梦影
 * @date 2024-12-09 13:23:47
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum ArithmeticType {

	PLUS("+"),
	SUBTRACT("-"),
	MULTIPLY("*"),
	DIVIDE("/");

	private String arithmeticSymbol;

	private ArithmeticType(String arithmeticSymbol) {
		this.arithmeticSymbol = arithmeticSymbol;
	}

	public String getArithmeticSymbol() {
		return arithmeticSymbol;
	}

	public static ArithmeticType getByArithmeticType(String arithmeticSymbol) {
		return Stream.of(values())
				.filter(t -> t.getArithmeticSymbol().equalsIgnoreCase(arithmeticSymbol))
				.findFirst()
				.orElse(null);
	}

	public static ArithmeticType get(int code) {
		return Stream.of(values()).filter(t -> t.ordinal() == code).findFirst().orElse(null);
	}

	public static ArithmeticType get(String code) {
		return Stream.of(values()).filter(t -> t.name().equalsIgnoreCase(code)).findFirst().orElse(null);
	}

	public static Integer calculate(ArithmeticType arithmeticType, Integer... params) {
		if (PLUS == arithmeticType) {
			return Stream.of(params).reduce(params[0], (p, n) -> p + n);
		}
		if (SUBTRACT == arithmeticType) {
			return Stream.of(params).reduce(params[0], (p, n) -> p - n);
		}
		if (MULTIPLY == arithmeticType) {
			return Stream.of(params).reduce(params[0], (p, n) -> p * n);
		}
		if (DIVIDE == arithmeticType) {
			return Stream.of(params).filter(t -> 0 == t).reduce(params[0], (p, n) -> 0 != n ? p / n : p);
		}
		throw new ArithmeticException("Error arithmetic type");
	}
}