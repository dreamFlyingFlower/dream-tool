package dream.flying.flower.db;

import java.util.ArrayList;

/**
 * SQL工具类
 *
 * @author 飞花梦影
 * @date 2024-07-17 23:46:54
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class SqlHelper {

	public static ArrayList<String> sqlInjection;

	static {
		sqlInjection = new ArrayList<String>();
		sqlInjection.add("--");
		sqlInjection.add(";");
		sqlInjection.add("/");
		sqlInjection.add("\\");
		sqlInjection.add("#");
		sqlInjection.add("drop");
		sqlInjection.add("create");
		sqlInjection.add("delete");
		sqlInjection.add("alter");
		sqlInjection.add("truncate");
		sqlInjection.add("update");
		sqlInjection.add("insert");
		sqlInjection.add("and");
		sqlInjection.add("or");
	}

	public static boolean filtersSQLInjection(String filters) {
		for (String str : sqlInjection) {
			if (filters.indexOf(str) > -1) {
				return true;
			}
		}
		return false;
	}
}