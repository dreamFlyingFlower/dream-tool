package com.wy;

import java.io.File;

/**
 * IO常量
 * 
 * @author 飞花梦影
 * @date 2021-02-19 08:57:09
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ConstantIO {

	/**
	 * 流结束
	 */
	int EOF = -1;

	/**
	 * 根据系统自动调整的目录分隔符
	 */
	char DIR_SEPARATOR = File.separatorChar;

	/**
	 * unix系统目录分隔符
	 */
	char DIR_SEPARATOR_UNIX = '/';

	/**
	 * windows系统目录分隔符
	 */
	char DIR_SEPARATOR_WINDOWS = '\\';

	/**
	 * 根据系统自动调整的换行符
	 */
	String LINE_SEPERATOR = System.lineSeparator();

	/**
	 * unix换行符
	 */
	String LINE_SEPARATOR_UNIX = "\n";

	/**
	 * windows换行符
	 */
	String LINE_SEPARATOR_WINDOWS = "\r\n";
}