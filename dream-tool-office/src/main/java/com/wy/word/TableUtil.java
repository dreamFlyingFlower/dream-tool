package com.wy.word;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.wy.bean.BeanTool;
import com.wy.collection.CollectionTool;
import com.wy.collection.IterableTool;
import com.wy.collection.MapTool;
import com.wy.lang.AssertTool;
import com.wy.util.Convert;

/**
 * Word文档中表格相关工具
 *
 * @author 飞花梦影
 * @date 2021-03-06 00:24:25
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class TableUtil {

	/**
	 * 创建空表,只有一行
	 * 
	 * @param doc {@link XWPFDocument}
	 * @return {@link XWPFTable}
	 */
	public static XWPFTable createTable(XWPFDocument doc) {
		return createTable(doc, null);
	}

	/**
	 * 创建表格并填充数据,默认表格
	 * 
	 * @param doc {@link XWPFDocument}
	 * @param data 数据
	 * @return {@link XWPFTable}
	 */
	public static XWPFTable createTable(XWPFDocument doc, Iterable<?> data) {
		AssertTool.notNull(doc, "XWPFDocument must be not null !");
		final XWPFTable table = doc.createTable();
		// 新建table的时候默认会新建一行,此处移除之
		table.removeRow(0);
		return writeTable(table, data);
	}

	/**
	 * 为table填充数据
	 *
	 * @param table {@link XWPFTable}
	 * @param data 数据
	 * @return {@link XWPFTable}
	 */
	public static XWPFTable writeTable(XWPFTable table, Iterable<?> data) {
		AssertTool.notNull(table, "XWPFTable must be not null !");
		if (IterableTool.isEmpty(data)) {
			// 数据为空,返回空表
			return table;
		}
		boolean isFirst = true;
		for (Object rowData : data) {
			writeRow(table.createRow(), rowData, isFirst);
			if (isFirst) {
				isFirst = false;
			}
		}
		return table;
	}

	/**
	 * 写一行数据
	 * 
	 * @param row 行
	 * @param rowBean 行数据
	 * @param isWriteKeyAsHead 如果为Map或者Bean,是否写标题
	 */
	@SuppressWarnings("rawtypes")
	public static void writeRow(XWPFTableRow row, Object rowBean, boolean isWriteKeyAsHead) {
		if (rowBean instanceof Iterable) {
			writeRow(row, (Iterable<?>) rowBean);
			return;
		}
		Map rowMap;
		if (rowBean instanceof Map) {
			rowMap = (Map) rowBean;
		} else if (BeanTool.isBean(rowBean.getClass())) {
			rowMap = BeanTool.beanToMap(rowBean, new LinkedHashMap<>(), false, false);
		} else {
			// 其它转为字符串默认输出
			writeRow(row, CollectionTool.newArrayList(new Object[] { rowBean }), isWriteKeyAsHead);
			return;
		}
		writeRow(row, rowMap, isWriteKeyAsHead);
	}

	/**
	 * 写行数据
	 * 
	 * @param row 行
	 * @param rowMap 行数据
	 * @param isWriteKeyAsHead 是否写标题
	 */
	public static void writeRow(XWPFTableRow row, Map<?, ?> rowMap, boolean isWriteKeyAsHead) {
		if (MapTool.isEmpty(rowMap)) {
			return;
		}
		if (isWriteKeyAsHead) {
			writeRow(row, rowMap.keySet());
			row = row.getTable().createRow();
		}
		writeRow(row, rowMap.values());
	}

	/**
	 * 写行数据
	 * 
	 * @param row 行
	 * @param rowData 行数据
	 */
	public static void writeRow(XWPFTableRow row, Iterable<?> rowData) {
		XWPFTableCell cell;
		int index = 0;
		for (Object cellData : rowData) {
			cell = getOrCreateCell(row, index);
			cell.setText(Convert.toStr(cellData));
			index++;
		}
	}

	/**
	 * 获取或创建新行<br>
	 * 存在则直接返回,不存在创建新的行
	 * 
	 * @param table {@link XWPFTable}
	 * @param index 索引（行号）,从0开始
	 * @return {@link XWPFTableRow}
	 */
	public static XWPFTableRow getOrCreateRow(XWPFTable table, int index) {
		XWPFTableRow row = table.getRow(index);
		if (null == row) {
			row = table.createRow();
		}

		return row;
	}

	/**
	 * 获取或创建新单元格<br>
	 * 存在则直接返回,不存在创建新的单元格
	 * 
	 * @param row {@link XWPFTableRow} 行
	 * @param index index 索引（列号）,从0开始
	 * @return {@link XWPFTableCell}
	 */
	public static XWPFTableCell getOrCreateCell(XWPFTableRow row, int index) {
		XWPFTableCell cell = row.getCell(index);
		if (null == cell) {
			cell = row.createCell();
		}
		return cell;
	}
}