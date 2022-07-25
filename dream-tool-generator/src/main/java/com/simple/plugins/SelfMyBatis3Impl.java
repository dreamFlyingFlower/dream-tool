package com.simple.plugins;

import java.util.List;

import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

/**
 * 自定义Mapper实现类,在generateMapper.xml的context的targetRuntime中使用,填写完整的路径名
 *
 * @author 飞花梦影
 * @date 2022-07-25 16:44:13
 */
public class SelfMyBatis3Impl extends IntrospectedTableMyBatis3Impl {

	@Override
	protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, List<String> warnings,
	        ProgressCallback progressCallback) {
		super.calculateXmlMapperGenerator(javaClientGenerator, warnings, progressCallback);
	}

	@Override
	protected String calculateMyBatis3XmlMapperFileName() {
		return super.calculateMyBatis3XmlMapperFileName();
	}

	@Override
	protected void calculateJavaClientAttributes() {
		super.calculateJavaClientAttributes();
	}
}