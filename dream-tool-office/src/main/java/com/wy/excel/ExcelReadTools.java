package com.wy.excel;

import java.io.InputStream;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 读取百万级数据的报表
 *
 * @author 飞花梦影
 * @date 2022-06-08 10:53:46
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ExcelReadTools {

	public static void read(String path) throws Exception {
		// 根据Excel获取OPCPackage对象
		try (OPCPackage pkg = OPCPackage.open(path, PackageAccess.READ);) {
			// 创建XSSFReader对象
			XSSFReader reader = new XSSFReader(pkg);
			// 获取SharedStringsTable对象
			SharedStringsTable sst = reader.getSharedStringsTable();
			// 获取StylesTable对象
			StylesTable styles = reader.getStylesTable();
			// 创建Sax的XmlReader对象
			XMLReader parser = XMLReaderFactory.createXMLReader();
			// 设置处理器
			parser.setContentHandler(new XSSFSheetXMLHandler(styles, sst, new ReadSheetHandler(), false));
			XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) reader.getSheetsData();
			// 逐行读取
			while (sheets.hasNext()) {
				InputStream sheetstream = sheets.next();
				InputSource sheetSource = new InputSource(sheetstream);
				try {
					parser.parse(sheetSource);
				} finally {
					sheetstream.close();
				}
			}
		}
	}
}