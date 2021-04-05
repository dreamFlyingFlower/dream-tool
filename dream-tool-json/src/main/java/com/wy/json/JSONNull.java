package com.wy.json;

/**
 * JSON null,应对null值
 * 
 * @author 飞花梦影
 * @date 2021-03-19 14:26:29
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JSONNull {

	@Override
	protected final Object clone() {
		return this;
	}

	@Override
	public boolean equals(Object object) {
		return object == null || object == this;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return "null";
	}
}