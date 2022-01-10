package com.wy.annotation.enums;

import java.util.Arrays;
import java.util.List;

import com.wy.lang.StrTool;

/**
 * Spring相关注解.注意,需要在使用的项目中存在指定的注解类,否则编译无法通过
 *
 * @author 飞花梦影
 * @date 2022-01-04 09:58:32
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public enum SpringEnum implements AnnotationEnum {

	Autowired("org.springframework.beans.factory.annotation.Autowired"),
	RestController("org.springframework.web.bind.annotation.RestController"),
	Controller("org.springframework.stereotype"),
	RequestBody("org.springframework.web.bind.annotation.RequestBody"),
	RequestParam("org.springframework.web.bind.annotation.RequestParam");

	/** 全路径类名 */
	private String className;

	private SpringEnum(String className) {
		this.className = className;
	}

	@Override
	public String packageName() {
		if (StrTool.isBlank(className)) {
			return null;
		}
		return this.className.substring(0, this.className.lastIndexOf("."));
	}

	@Override
	public String simpleName() {
		return this.className.substring(this.className.lastIndexOf(".") + 1);
	}

	@Override
	public List<? extends AnnotationEnum> value() {
		return Arrays.asList(SpringEnum.values());
	}

	@Override
	public String toString() {
		return this.className;
	}

	public String getClassName() {
		return this.className;
	}
}