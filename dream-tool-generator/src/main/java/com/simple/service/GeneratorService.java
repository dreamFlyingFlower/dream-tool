package com.simple.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simple.common.BasePager;
import com.simple.common.GeneratorMapper;
import com.simple.model.Tableinfo;
import com.simple.properties.ConfigProperties;
import com.simple.utils.GenerateUtils;
import com.simple.utils.VelocityUtils;

import dream.flying.flower.collection.MapHelper;
import dream.flying.flower.helper.ArrayHelper;
import dream.flying.flower.helper.DateHelper;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.result.Result;

/**
 * 代码生成器
 * 
 * @author ParadiseWY
 * @date 2020-12-11 17:04:23
 * @git {@link https://github.com/mygodness100}
 */
@Service
public class GeneratorService {

	@Autowired
	private GeneratorMapper generatorMapper;

	@Autowired
	private ConfigProperties config;

	@Autowired
	private DataSourceProperties dataSource;

	public Result<?> queryList(BasePager pager) {
		PageHelper.startPage(pager.getPageIndex(), pager.getPageSize());
		List<Map<String, Object>> list = generatorMapper
				.selectLists(JSON.parseObject(JSON.toJSONString(pager), new TypeReference<Map<String, Object>>() {
				}));
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		return Result.page(list, pageInfo.getPageNum(), pageInfo.getPageSize(), total);
	}

	/**
	 * 生成数据库中所有表信息
	 * 
	 * @param localOrRemote 本地生成或远程下载,true本地,false远程打包成zip
	 * @return 远程生成时的字节数组
	 */
	public byte[] generateCode(boolean localOrRemote) {
		List<Map<String, Object>> list = generatorMapper.selectLists(null);
		return generateCode(list, localOrRemote);
	}

	/**
	 * 生成数据库中指定表的信息
	 * 
	 * @param localOrRemote 本地生成或远程下载,true本地,false远程打包成zip
	 * @param tableNames 需要生成的表名
	 * @return 远程生成时的字节数组
	 */
	public byte[] generateCode(boolean localOrRemote, String... tableNames) {
		// 所有表信息
		List<Map<String, Object>> dbTables = generatorMapper.selectLists(null);
		// 需要处理的表信息
		List<Map<String, Object>> needTables = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> dbTable : dbTables) {
			for (String tableName : tableNames) {
				if (Objects.equals(dbTable.get("tableName").toString(), tableName)) {
					needTables.add(dbTable);
				}
			}
		}
		return generateCode(needTables, localOrRemote);
	}

	public byte[] generateCode(List<Map<String, Object>> dbTables, boolean localOrRemote) {
		// 所有字段信息
		List<Map<String, String>> dbColumns = generatorMapper.selectColumninfos(null);
		// 所有表与各自字段的对应关系
		Map<String, List<Map<String, String>>> handlerColumns = GenerateUtils.handlerColumns(dbColumns);
		return generateCode(dbTables, handlerColumns, localOrRemote);
	}

	private byte[] generateCode(List<Map<String, Object>> dbTables,
			Map<String, List<Map<String, String>>> handlerColumns, boolean localOrRemote) {
		generateMapperXml(dbTables, handlerColumns, localOrRemote);
		dataSource.setUrl(dataSource.getUrl().replaceAll("&", "&amp;"));
		// 所有表的信息,需要添加到mybatis自动生成xml的模板中
		for (Map<String, Object> dbTable : dbTables) {
			String tableName = dbTable.get("tableName").toString();
			// 处理表信息
			Tableinfo tableinfo =
					GenerateUtils.generateTableinfo(dbTable, handlerColumns.get(tableName), config, dataSource);
			// 生成代码
			if (localOrRemote) {
				GenerateUtils.generateCode(tableinfo, config, localOrRemote, dataSource);
			} else {
				return GenerateUtils.generateCode(tableinfo, config, localOrRemote, dataSource);
			}
		}
		// if (MongoManager.isMongo()) {
		// return GenerateUtils.generatorMongoCode(tableNames, config);
		// }
		return null;
	}

	@Async
	public void generateMapperXml(List<Map<String, Object>> dbTables,
			Map<String, List<Map<String, String>>> handlerColumns, boolean localOrRemote) {
		dataSource.setUrl(dataSource.getUrl().replaceAll("&", "&amp;"));
		for (Map<String, Object> dbTable : dbTables) {
			String tableName = dbTable.get("tableName").toString();
			// 处理表信息
			GenerateUtils.generateTableinfoMap(dbTable, handlerColumns.get(tableName), config);
		}
		GenerateUtils.generateMapperXml(dbTables, config, localOrRemote, dataSource);
	}

	/**
	 * 将字典表中所有字典转换为枚举
	 * 
	 * @param tableName 字典表名
	 */
	public void generateDict(String tableName, String tableItemName) {
		generateDict(tableName, tableItemName, new String[0]);
	}

	public void generateDict(String tableName, String tableItemName, String... dictCodes) {
		// 表数据
		List<Map<String, Object>> dbTables =
				generatorMapper.selectLists(MapHelper.builder("tableName", tableName).build());
		// 所有字段信息
		List<Map<String, String>> dbColumns = generatorMapper.selectColumninfos(tableName);
		// 处理表信息
		Tableinfo tableinfo = GenerateUtils.generateTableinfo(dbTables.get(0), dbColumns, config, dataSource);
		List<Map<String, Object>> selectDatas = generatorMapper.selectDatas(tableName);
		Map<Object, Map<String, Object>> mapDictCode2Dicts =
				selectDatas.stream().collect(Collectors.toMap(k -> k.get("dict_code"), v -> v));
		// 查询字典项数据
		List<Map<String, Object>> selectDataItems = generatorMapper.selectDatas(tableItemName);
		Map<Object, List<Map<String, Object>>> mapDictCode2DictItems = selectDataItems.stream()
				.filter(t -> ArrayHelper.isNotEmpty(dictCodes) ? !ArrayHelper.contains(dictCodes, t.get("dict_code"))
						: true)
				.collect(Collectors.groupingBy(k -> k.get("dict_code")));

		// 封装模板数据
		for (Map.Entry<Object, List<Map<String, Object>>> entry : mapDictCode2DictItems.entrySet()) {
			String className = entry.getKey().toString();
			className = className.endsWith("state") || className.endsWith("status") || className.endsWith("type")
					? StrHelper.underline2Hump(className)
					: StrHelper.underline2Hump(className) + "Enum";
			className = StrHelper.firstUpper(className);
			tableinfo.setClassName(className);
			entry.getValue().stream().forEach(t -> {
				t.put("dict_code", t.get("dict_code").toString().toUpperCase());
			});
			Map<String,
					Object> map = MapHelper.builder("tableinfo", tableinfo)
							.put("columns", tableinfo.getColumns())
							.put("className", className)
							.put("comment", mapDictCode2Dicts.get(entry.getKey()).get("dict_name"))
							.put("dictItems", entry.getValue())
							.put("common", config.getCommon())
							.put("datetime", DateHelper.formatDateTime())
							.build();
			VelocityUtils.generateFiles(map, tableinfo,
					new ArrayList<String>(Arrays.asList("template/DictEnum.java.vm")), true);
		}
	}
}