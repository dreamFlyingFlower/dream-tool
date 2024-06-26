package dream.flying.flower.common;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import dream.flying.flower.lang.AssertHelper;
import dream.flying.flower.lang.StrHelper;

/**
 * 该国际化配置类只能在本项目中使用,不可作为工具类对外使用
 * 
 * 配置文件需要至少一个默认的,不带任何后缀的properties文件.其他需要国际化的语言,
 * 需要在文件名后以_分开,如:message_zh_CN,message是默认的文件,zh是国家,CN是语言.
 * 有多个国际化类型时,需要写多个message的配置文件,必须是properties文件
 * 
 * @author 飞花梦影
 * @date 2021-02-19 09:15:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Internation {

	private static ResourceBundle resource;

	static {
		resource = ResourceBundle.getBundle("internation/messages", Locale.getDefault());
	}

	public static ResourceBundle get() {
		return resource;
	}

	public static Object get(String key) {
		AssertHelper.notBlank(key, "key");
		return resource.getObject(key);
	}

	public static String getStr(String key) {
		AssertHelper.notBlank(key, "key");
		return resource.getString(key);
	}

	public static String getStr(String key, String defaultVal) {
		AssertHelper.notBlank(key, "key");
		String val = resource.getString(key);
		return StrHelper.isBlank(val) ? defaultVal : val;
	}

	public static Set<String> getKeys() {
		return resource.keySet();
	}

	public static String[] getStrs(String key) {
		AssertHelper.notBlank(key, "key");
		return resource.getStringArray(key);
	}

	public static boolean hasKey(String key) {
		AssertHelper.notBlank(key, "key");
		return resource.containsKey(key);
	}

	public static Map<String, Object> getMap() {
		Map<String, Object> result = new HashMap<>();
		for (String key : resource.keySet()) {
			result.put(key, resource.getObject(key));
		}
		return result;
	}
}