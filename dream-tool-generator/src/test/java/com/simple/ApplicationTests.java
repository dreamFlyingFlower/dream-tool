package com.simple;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.simple.service.GeneratorService;

@SpringBootTest
public class ApplicationTests {

	@Autowired
	private GeneratorService generatorService;

	/**
	 * 生成数据库中所有表的文件
	 */
	@Test
	public void buildAllTable() {
		generatorService.generateCode(true);
	}

	/**
	 * 生成单表的文件
	 */
	@Test
	public void buildSingleTable() {
		generatorService.generateCode(true, "ts_menu");
	}

	/**
	 * 构建字典中所有的项为枚举
	 */
	@Test
	public void buildAllDict() {
		generatorService.generateDict("ts_dict", "ts_dict_item");
	}

	/**
	 * 构建字典中单个项为枚举
	 */
	@Test
	public void buildSingleDict() {
		generatorService.generateDict("ts_dict", "ts_dict_item", "user_status");
	}
}