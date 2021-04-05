package com.wy.word;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * Word工具类
 *
 * @author 飞花梦影
 * @date 2021-03-06 00:22:59
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class WordUtil {

	/**
	 * 创建Word 07格式的生成器
	 * 
	 * @return {@link Word07Writer}
	 */
	public static Word07Writer getWriter() {
		return new Word07Writer();
	}

	/**
	 * 创建Word 07格式的生成器
	 * 
	 * @param destFile 目标文件
	 * @return {@link Word07Writer}
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static Word07Writer getWriter(File destFile) throws InvalidFormatException, IOException {
		return new Word07Writer(destFile);
	}
}
