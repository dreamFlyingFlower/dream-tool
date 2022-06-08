package com.wy.excel;

import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

/**
 * 自定义Sheet基于Sax的解析处理器
 *
 * @author 飞花梦影
 * @date 2022-06-08 10:54:52
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ReadSheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {

	/**
	 * 解析行开始
	 */
	@Override
	public void startRow(int rowNum) {
		// TODO Auto-generated method stub

	}

	/**
	 * 解析行结束
	 */
	@Override
	public void endRow(int rowNum) {
		// TODO Auto-generated method stub

	}

	/**
	 * 解析每一个单元格
	 */
	@Override
	public void cell(String cellReference, String formattedValue, XSSFComment comment) {
		// TODO Auto-generated method stub
		switch (cellReference.substring(0, 1)) {
		case "A":
			System.out.println(formattedValue);
			break;
		case "B":
			System.out.println(formattedValue);
			break;
		case "C":
			System.out.println(formattedValue);
			break;
		case "D":
			System.out.println(formattedValue);
			break;
		default:
			break;
		}
	}

	/**
	 * 处理头尾
	 */
	@Override
	public void headerFooter(String text, boolean isHeader, String tagName) {
		// TODO Auto-generated method stub
		SheetContentsHandler.super.headerFooter(text, isHeader, tagName);
	}
}