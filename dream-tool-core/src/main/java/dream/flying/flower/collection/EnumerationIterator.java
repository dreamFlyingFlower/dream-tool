package dream.flying.flower.collection;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * {@link Enumeration}枚举迭代转{@link Iterator}
 * 
 * @author 飞花梦影
 * @date 2021-03-12 16:10:36
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class EnumerationIterator<E> implements Iterator<E>, Serializable {

	private static final long serialVersionUID = -2447087161119986843L;

	private final Enumeration<E> enumeration;

	public EnumerationIterator(Enumeration<E> enumeration) {
		this.enumeration = enumeration;
	}

	public Enumeration<? extends E> getEnumeration() {
		return enumeration;
	}

	@Override
	public boolean hasNext() {
		return this.enumeration.hasMoreElements();
	}

	@Override
	public E next() {
		return this.enumeration.nextElement();
	}
}