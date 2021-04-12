package com.wy.word;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.wy.io.file.FileTool;

/**
 * Word Document工具
 * 
 * @author 飞花梦影
 * @date 2021-03-06 00:23:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class DocTools {

	/**
	 * 创建{@link XWPFDocument}，如果文件已存在则读取之，否则创建新的
	 * 
	 * @param file docx文件
	 * @return {@link XWPFDocument}
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static XWPFDocument create(File file) throws IOException, InvalidFormatException {
		return FileTool.createFile(file, true) ? new XWPFDocument(OPCPackage.open(file)) : new XWPFDocument();
	}
}