package dream.flying.flower.io.function;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * {@link Consumer},会抛异常 {@link IOException}
 * 
 * @author 飞花梦影
 * @date 2021-02-19 09:06:06
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@FunctionalInterface
public interface IOConsumer<T> {

	/**
	 * 处理接收的参数并抛异常
	 *
	 * @param t 接收的参数
	 * @throws IOException 当发生I/O异常
	 */
	void accept(T t) throws IOException;

	/**
	 * 链式调用方法,返回当前接口的实现类,并调用下一个实现类的accept()
	 *
	 * @param after 下一个被调用的类
	 * @return 调用了accept()之后的实现类
	 * @throws NullPointerException 当after为null
	 */
	default IOConsumer<T> andThen(final IOConsumer<? super T> after) {
		Objects.requireNonNull(after);
		return (final T t) -> {
			accept(t);
			after.accept(t);
		};
	}
}