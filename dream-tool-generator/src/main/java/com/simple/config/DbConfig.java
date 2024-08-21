package com.simple.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.simple.common.GeneratorMapper;
import com.simple.mapper.MySQLGeneratorMapper;
import com.simple.mapper.OracleGeneratorMapper;
import com.simple.mapper.PostgreSQLGeneratorMapper;
import com.simple.mapper.SQLServerGeneratorMapper;
import com.simple.properties.ConfigProperties;

import dream.flying.flower.result.ResultException;

/**
 * 数据库配置
 * 
 * @author ParadiseWY
 * @date 2020-12-11 16:36:03
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
public class DbConfig {

	@Autowired
	private ConfigProperties config;

	@Autowired
	private MySQLGeneratorMapper mySQLGeneratorMapper;

	@Autowired
	private OracleGeneratorMapper oracleGeneratorMapper;

	@Autowired
	private SQLServerGeneratorMapper sqlserverGeneratorMapper;

	@Autowired
	private PostgreSQLGeneratorMapper postgreSQLGeneratorMapper;

	@Bean
	@Primary
	GeneratorMapper getGeneratorDao() {
		if ("mysql".equalsIgnoreCase(config.getDatabase().getDatabaseType())) {
			return mySQLGeneratorMapper;
		} else if ("oracle".equalsIgnoreCase(config.getDatabase().getDatabaseType())) {
			return oracleGeneratorMapper;
		} else if ("sqlserver".equalsIgnoreCase(config.getDatabase().getDatabaseType())) {
			return sqlserverGeneratorMapper;
		} else if ("postgresql".equalsIgnoreCase(config.getDatabase().getDatabaseType())) {
			return postgreSQLGeneratorMapper;
		} else {
			throw new ResultException("不支持当前数据库:" + config.getDatabase().getDatabaseType());
		}
	}
}