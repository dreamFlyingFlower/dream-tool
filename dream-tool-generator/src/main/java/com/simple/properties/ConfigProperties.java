package com.simple.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义配置
 * 
 * @author ParadiseWY
 * @date 2020-12-11 16:39:05
 * @git {@link https://github.com/mygodness100}
 */
@Configuration
@ConfigurationProperties(prefix = "config")
@Getter
@Setter
public class ConfigProperties {

	private CommonProperties common = new CommonProperties();

	private DatabaseProperties database = new DatabaseProperties();

	private TemplateProperties template = new TemplateProperties();
}