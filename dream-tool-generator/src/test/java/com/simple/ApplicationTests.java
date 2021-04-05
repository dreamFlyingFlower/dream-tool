package com.simple;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.simple.service.GeneratorService;

@SpringBootTest
public class ApplicationTests {

	@Autowired
	private GeneratorService generatorService;

	@Test
	public void buildAllTable() {
		generatorService.generateCode(true);
	}

	@Test
	public void buildSingleTable() {
		generatorService.generateCode(true, "ts_menu");
	}
}