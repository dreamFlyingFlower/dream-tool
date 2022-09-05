package com.simple.common;

import java.util.List;
import java.util.Map;

/**
 * 数据库接口
 * 
 * @author ParadiseWY
 * @date 2020-12-11 15:26:52
 * @git {@link https://github.com/mygodness100}
 */
public interface GeneratorMapper {

	List<Map<String, Object>> selectLists(Map<String, Object> map);

	Map<String, String> selectTableinfo(String tableName);

	List<Map<String, String>> selectColumninfos(String tableName);

	List<Map<String, Object>> selectDatas(String tableName);
}