package com.wy.collection;

import java.util.Iterator;
import java.util.function.Function;

import com.wy.lang.AssertTool;

/**
 * 使用给定的转换函数,转换源{@link Iterator}为新类型的{@link Iterator}
 * 
 * @author 飞花梦影
 * @date 2021-03-12 16:20:26
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class TransformerIterator<K, V> implements Iterator<V> {

	/** 源迭代 */
	private final Iterator<? extends K> backingIterator;

	/** 源迭代中元素转换为指定类型的方法 */
	private final Function<? super K, ? extends V> function;

	public TransformerIterator(Iterator<? extends K> backingIterator, Function<? super K, ? extends V> function) {
		this.backingIterator = AssertTool.notNull(backingIterator);
		this.function = AssertTool.notNull(function);
	}

	@Override
	public final boolean hasNext() {
		return backingIterator.hasNext();
	}

	@Override
	public final V next() {
		return function.apply(backingIterator.next());
	}

	@Override
	public final void remove() {
		backingIterator.remove();
	}
}