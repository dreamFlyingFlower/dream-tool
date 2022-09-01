package com.simple.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 模板相关配置
 * 
 * @author ParadiseWY
 * @date 2020-12-14 23:34:37
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
public class TemplateProperties {

	/** 默认需要生成的模板地址 */
	private List<String> defaults =
	        new ArrayList<String>(Arrays.asList("template/Model.java.vm", "template/ModelDTO.java.vm",
	                "template/Convert.java.vm", "template/ModelQuery.java.vm", "template/Mapper.java.vm",
	                "template/Service.java.vm", "template/ServiceImpl.java.vm", "template/Crl.java.vm"));

	/** mybaits生成generatorConfig.xml的模板 */
	private String generatorConfig = "template/generatorConfig.xml.vm";

	/** 用户额外需要添加的模板 */
	private List<String> extras = new ArrayList<>();

	/** 当用户额外添加的模板有值时,是否还需要默认模板,默认true需要 */
	private boolean addDefaults = true;
}