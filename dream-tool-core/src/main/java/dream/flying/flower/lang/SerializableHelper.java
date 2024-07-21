package dream.flying.flower.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import dream.flying.flower.binary.HexHelper;

/**
 * 序列化与反序列化工具类
 *
 * @author 飞花梦影
 * @date 2024-07-19 23:31:51
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SerializableHelper {

	/**
	 * 将对象序列化为字节数组
	 * 
	 * @param object 实例对象
	 * @return 字节数组
	 */
	public final static byte[] serialize(Object object) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(512);
				ObjectOutputStream oos = new ObjectOutputStream(bos);) {
			oos.writeObject(object);
			oos.flush();
			return bos.toByteArray();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 将字节数组反序列化为对象
	 * 
	 * @param <T> 对象泛型
	 * @param byteArray 字节数组
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public final static <T> T deserialize(byte[] byteArray) {
		try (ObjectInputStream oip = new ObjectInputStream(new ByteArrayInputStream(byteArray));) {
			return (T) oip.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 将对象序列化为HEX String
	 * 
	 * @param serializable 序列化对象
	 * @return 序列化后的HEX String
	 */
	public final static String serializeHex(Serializable serializable) {
		return HexHelper.encodeHexString(serialize(serializable));
	}

	/**
	 * 将HEX String反序列化为对象
	 * 
	 * @param hex HEX String
	 * @return T
	 */
	public final static <T> T deserializeHex(String hex) {
		return SerializableHelper.deserialize(HexHelper.decode(hex));
	}
}