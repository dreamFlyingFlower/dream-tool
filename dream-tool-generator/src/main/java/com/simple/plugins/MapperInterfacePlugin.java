package com.simple.plugins;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.util.StringUtils;

import com.wy.annotation.Example;

/**
 * 自定义Mapper接口,未使用
 * 
 * @author 飞花梦影
 * @date 2022-05-18 14:44:22
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Example
public class MapperInterfacePlugin extends PluginAdapter {

	/** 是否生成mapper接口,默认生成 */
	private Boolean validMapper;

	/** 通用Mapper接口 */
	private Set<String> baseMappers = new HashSet<>();

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		validMapper = StringUtils.hasText(this.properties.getProperty("validMapper"))
		        ? Boolean.parseBoolean(this.properties.getProperty("validMapper"))
		        : true;
		String baseMappers = this.properties.getProperty("baseMappers");
		if (StringUtility.stringHasValue(baseMappers)) {
			Collections.addAll(this.baseMappers, baseMappers.split(","));
		}
	}

	/**
	 * 使用mybatis生成mapper接口,返回true的时候表示生成
	 */
	@Override
	public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
		if (!validMapper) {
			return false;
		}
		if (this.baseMappers.size() == 0) {
			return true;
		}
		// 获取实体类
		FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		// import 实体类
		interfaze.addImportedType(entityType);
		// 添加 @Mapper 注解
		interfaze.addAnnotation("@Mapper");
		// import 接口
		interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
		for (String mapper : baseMappers) {
			interfaze.addImportedType(new FullyQualifiedJavaType(mapper));
			interfaze.addSuperInterface(new FullyQualifiedJavaType(mapper + "<" + entityType.getShortName() + ">"));
		}
		return true;
	}
}