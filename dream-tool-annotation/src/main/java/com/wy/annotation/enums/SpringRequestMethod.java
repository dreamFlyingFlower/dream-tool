package com.wy.annotation.enums;

import java.util.Arrays;
import java.util.List;

import com.wy.lang.StrTool;

/**
 * Spring请求方式枚举类型
 * 
 * @author 飞花梦影
 * @date 2022-01-10 23:54:24
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum SpringRequestMethod implements AnnotationEnum {

	GetMapping("org.springframework.web.bind.annotation.GetMapping"),
	DeleteMapping("org.springframework.web.bind.annotation.DeleteMapping"),
	PatchMapping("org.springframework.web.bind.annotation.PatchMapping"),
	PostMapping("org.springframework.web.bind.annotation.PostMapping"),
	PutMapping("org.springframework.web.bind.annotation.PutMapping"),
	RequestMapping("org.springframework.web.bind.annotation.RequestMapping");

	/** 全路径类名 */
	private String className;

	private SpringRequestMethod(String className) {
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
		return Arrays.asList(SpringRequestMethod.values());
	}

	@Override
	public String toString() {
		return this.className;
	}

	public String getClassName() {
		return this.className;
	}

	public static boolean containRequestMethod(String requestMethodName) {
		for (SpringRequestMethod value : SpringRequestMethod.values()) {
			if (value.toString().equals(requestMethodName)) {
				return true;
			}
		}
		return false;
	}
}