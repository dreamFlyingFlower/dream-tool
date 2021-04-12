package com.wy.excel;

import java.text.NumberFormat;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel格式化工具
 * 
 * @author 飞花梦影
 * @date 2021-03-31 10:50:51
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ExcelStyleTools {

	/** 默认字体 */
	private static final String ENFONT = "Times New Roman";

	/**
	 * 计算公式,如函数sum
	 * 
	 * @param cell 单元格
	 * @param formula 完整的公式,如sum(A4:C4)
	 */
	public static void calcColumn(Cell cell, String formula) {
		cell.setCellFormula(formula);
	}

	/**
	 * 整体移动行
	 * 
	 * @param sheet Sheet
	 * @param begin 开始移动的行,索引从0开始
	 * @param end 结果移动的行,索引从0开始
	 * @param num 移动行数,正数下移,负数上移
	 */
	public static void shiftRows(Sheet sheet, int begin, int end, int num) {
		sheet.shiftRows(begin, end, num);
	}

	/**
	 * 拆分窗格
	 * 
	 * @param sheet Sheet
	 * @param xSplitPos 左侧窗格的宽度,水平拆分的位置,在点的1/20处
	 * @param ySplitPos 上侧窗格的高度,垂直拆分的位置,在点的1/20处
	 * @param leftmostColumn 右侧窗格开始显示的列的索引
	 * @param topRow 下侧窗格开始显示的行的索引
	 * @param activePane 激活的哪个面板区.见 Sheet#PANE_LOWER_RIGHT等4种方式
	 */
	public static void splitPane(Sheet sheet, int xSplitPos, int ySplitPos, int leftmostColumn, int topRow,
			int activePane) {
		sheet.createSplitPane(xSplitPos, ySplitPos, leftmostColumn, topRow, activePane);
	}

	/**
	 * 冻结窗口
	 * 
	 * @param sheet Sheet
	 * @param colSplit 冻结的列索引,从0开始
	 * @param rowSplit 冻结的行索引,从0开始
	 */
	public static void freezePane(Sheet sheet, int colSplit, int rowSplit) {
		sheet.createFreezePane(colSplit, rowSplit);
	}

	/**
	 * 冻结窗口
	 * 
	 * @param sheet Sheet
	 * @param colSplit 冻结的列索引,从0开始
	 * @param rowSplit 冻结的行索引,从0开始
	 * @param leftmostColumn 右侧窗格开始显示的列的索引
	 * @param topRow 下侧窗格开始显示的行的索引
	 */
	public static void freezePane(Sheet sheet, int colSplit, int rowSplit, int leftmostColumn, int topRow) {
		sheet.createFreezePane(colSplit, rowSplit, leftmostColumn, topRow);
	}

	// 画线
	public static void setLine(HSSFWorkbook wb, HSSFPatriarch patriarch, int iRowStart, int iColStart, int iRowStop,
			int iColStop) {
		HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 350, 0, (short) (iColStart), iRowStart, (short) (iColStop),
				iRowStop);
		HSSFSimpleShape lineShape = patriarch.createSimpleShape(anchor);
		lineShape.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
	}

	// 计算行高度,实现行自动适应高度 defaultRowHeight = 12.00f;
	// 每一行的高度指定
	// 目前只实现根据回车多行来判断,不能根据单元格宽度自动回行来判断
	public static float getCellAutoHeight(String str, float defaultRowHeight) {
		if (str == null) {
			return defaultRowHeight;
		}
		float height = 0.00f;
		int n = 0;
		if (str.endsWith("\n")) {
			n = str.split("\n").length; // 回车个数
		} else {
			n = str.split("\n").length + 1; // 回车个数
		}
		height = defaultRowHeight * n;

		return height; // 计算
	}

	// 计算字符串高度
	public static float getregex(String charStr) {
		if (charStr.equals(" ")) {
			return 0.5f;
		}
		if (Pattern.compile("^[A-Za-z0-9]+$").matcher(charStr).matches()) {
			return 0.5f;
		}
		// 判断是否为全角
		if (Pattern.compile("^[\u4e00-\u9fa5]+$").matcher(charStr).matches()) {
			return 1.00f;
		}
		if (Pattern.compile("^x00-xff]+$").matcher(charStr).matches()) {
			return 1.00f;
		}
		return 0.5f;
	}

	public static HSSFFont defaultFont10(HSSFWorkbook wb) {
		HSSFFont curFont = wb.createFont(); // 设置字体
		curFont.setFontName(ENFONT);
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET); // 设置中文字体,那必须还要再对单元格进行编码设置
		curFont.setFontHeightInPoints((short) 10);
		return curFont;
	}

	public static HSSFFont defaultFont10Blod(HSSFWorkbook wb) {
		HSSFFont curFont = wb.createFont(); // 设置字体
		curFont.setFontName(ENFONT);
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET); // 设置中文字体,那必须还要再对单元格进行编码设置
		curFont.setBold(true);// 加粗
		curFont.setFontHeightInPoints((short) 10);
		return curFont;
	}

	public static HSSFFont defaultFont12(HSSFWorkbook wb) {
		HSSFFont curFont = wb.createFont(); // 设置字体
		curFont.setFontName(ENFONT);
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET); // 设置中文字体,那必须还要再对单元格进行编码设置
		curFont.setFontHeightInPoints((short) 12);
		return curFont;
	}

	public static HSSFFont blackFont12(HSSFWorkbook wb) {
		HSSFFont theFont = wb.createFont(); // 设置字体
		theFont.setFontName("黑体");
		theFont.setCharSet(HSSFFont.DEFAULT_CHARSET); // 设置中文字体,那必须还要再对单元格进行编码设置
		theFont.setFontHeightInPoints((short) 12);
		return theFont;
	}

	public static HSSFFont songBoldFont16(HSSFWorkbook wb) {
		HSSFFont curFont = wb.createFont(); // 设置字体
		curFont.setFontName("宋体");
		curFont.setCharSet(HSSFFont.DEFAULT_CHARSET); // 设置中文字体,那必须还要再对单元格进行编码设置
		curFont.setBold(true);// 加粗
		curFont.setFontHeightInPoints((short) 16);
		return curFont;
	}

	/**
	 * 格式化数据
	 * 
	 * @param wb {@link Workbook}对象
	 * @return 格式索引
	 */
	public static short money1Format(HSSFWorkbook wb) {
		HSSFDataFormat format = wb.createDataFormat();
		return format.getFormat("#,###,###.0"); // 设置格式
	}

	public static short money2Format(HSSFWorkbook wb) {
		HSSFDataFormat format = wb.createDataFormat();
		return format.getFormat("#,###,###.00"); // 设置格式
	}

	public static short rmb2Format(HSSFWorkbook wb) {
		HSSFDataFormat format = wb.createDataFormat();
		return format.getFormat("\"￥\"#,###,###.00"); // 设置格式
	}

	public static short rmb4Format(HSSFWorkbook wb) {
		HSSFDataFormat format = wb.createDataFormat();
		return format.getFormat("\"￥\"#,###,##0.00"); // 设置格式
	}

	public static short datevENFormat(HSSFWorkbook wb) {
		return HSSFDataFormat.getBuiltinFormat("m/d/yy"); // 设置格式
	}

	/**
	 * 设置时间格式
	 * 
	 * @param format 格式化字符串,默认yyyy-MM-dd HH:mm:ss
	 * @return 格式索引
	 */
	public static short dateFormat(String format) {
		return HSSFDataFormat.getBuiltinFormat(format);
	}

	/**
	 * 数据格式化,如数字,时间等
	 * 
	 * @param wb {@link Workbook}对象
	 * @param format 若为数字格式化,规则同{@link NumberFormat};时间格式,同Java格式
	 */
	public static void dateFormat(Workbook wb, String format) {
		DataFormat dataFormat = wb.createDataFormat();
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setDataFormat(dataFormat.getFormat(format));
	}

	/**
	 * 设置文本自动换行
	 * 
	 * @param cellStyle 单元格样式对象
	 */
	public static void nexLine(CellStyle cellStyle) {
		cellStyle.setWrapText(true);
	}

	public static HSSFCellStyle titlev12(HSSFWorkbook wb, HSSFFont blackFont) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setFont(blackFont);
		curStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);// 单元格垂直居中
		return curStyle;
	}

	public static HSSFCellStyle nobox(HSSFWorkbook wb) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setBorderTop(BorderStyle.NONE); // 实线右边框
		curStyle.setBorderRight(BorderStyle.NONE); // 实线右边框
		curStyle.setBorderBottom(BorderStyle.NONE); // 实线右边框
		curStyle.setBorderLeft(BorderStyle.NONE); // 实线右边框
		curStyle.setTopBorderColor((short) 0);
		return curStyle;
	}

	/**
	 * 实现打印时为白框,目的就是实现涂去上行的下边框线 by tony 20110709
	 * 
	 * @param wb
	 * @return
	 */
	public static HSSFCellStyle whiteBox(HSSFWorkbook wb) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setTopBorderColor(HSSFColorPredefined.WHITE.getIndex());
		curStyle.setRightBorderColor(HSSFColorPredefined.WHITE.getIndex());
		curStyle.setBottomBorderColor(HSSFColorPredefined.WHITE.getIndex());
		curStyle.setLeftBorderColor(HSSFColorPredefined.WHITE.getIndex());
		curStyle.setBorderTop(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderRight(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderBottom(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderLeft(BorderStyle.THIN); // 实线右边框
		return curStyle;
	}

	public static HSSFCellStyle normalv12(HSSFWorkbook wb, HSSFFont defaultFont12) {
		return bnormalv12(wb, defaultFont12);
	}

	public static HSSFCellStyle normalv10(HSSFWorkbook wb, HSSFFont defaultFont10) {
		return bnormalv12(wb, defaultFont10);
	}

	public static HSSFCellStyle bnormalv12(HSSFWorkbook wb, HSSFFont defaultFont12) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setFont(defaultFont12);
		curStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY); // 单元格垂直居中
		return curStyle;
	}

	public static HSSFCellStyle numberrv10_BorderThin(HSSFWorkbook wb, HSSFFont defaultFont10) {
		return moneyrv10_BorderThin(wb, defaultFont10, (short) -1);
	}

	public static HSSFCellStyle moneyrv10_BorderThin(HSSFWorkbook wb, HSSFFont defaultFont10, short rmb4Format) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setFont(defaultFont10);
		if (rmb4Format >= 0) {
			curStyle.setDataFormat(rmb4Format);
		}
		curStyle.setAlignment(HorizontalAlignment.RIGHT);
		curStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY); // 单元格垂直居中
		curStyle.setBorderTop(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderRight(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderBottom(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderLeft(BorderStyle.THIN); // 实线右边框
		return curStyle;
	}

	public static HSSFCellStyle moneyrv12_BorderThin(HSSFWorkbook wb, HSSFFont defaultFont12, short rmb2Format) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setFont(defaultFont12);
		curStyle.setDataFormat(rmb2Format);
		curStyle.setAlignment(HorizontalAlignment.RIGHT);
		curStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY); // 单元格垂直居中
		curStyle.setBorderTop(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderRight(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderBottom(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderLeft(BorderStyle.THIN); // 实线右边框
		return curStyle;
	}

	public static HSSFCellStyle money1(HSSFWorkbook wb, HSSFFont defaultFont10, short money1Format) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setFont(defaultFont10);
		curStyle.setDataFormat(money1Format);
		curStyle.setAlignment(HorizontalAlignment.RIGHT);
		curStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY); // 单元格垂直居中
		return curStyle;
	}

	public static HSSFCellStyle money2(HSSFWorkbook wb, HSSFFont defaultFont10, short money2Format) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setFont(defaultFont10);
		curStyle.setDataFormat(money2Format);
		curStyle.setAlignment(HorizontalAlignment.RIGHT);
		curStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY); // 单元格垂直居中
		return curStyle;
	}

	public static HSSFCellStyle datevEN(HSSFWorkbook wb, HSSFFont defaultFont10, short datevENFormat) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setFont(defaultFont10);
		curStyle.setDataFormat(datevENFormat);
		curStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY); // 单元格垂直居中
		return curStyle;
	}

	public static HSSFCellStyle notet10(HSSFWorkbook wb) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setWrapText(true); // 换行
		curStyle.setBorderTop(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderRight(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderBottom(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderLeft(BorderStyle.THIN); // 实线右边框
		curStyle.setVerticalAlignment(VerticalAlignment.TOP); // 单元格垂直居中
		return curStyle;
	}

	public static HSSFCellStyle notevt10(HSSFWorkbook wb, HSSFFont defaultFont10) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setWrapText(true); // 换行
		curStyle.setFont(defaultFont10);
		curStyle.setVerticalAlignment(VerticalAlignment.TOP); // 单元格垂直居中
		return curStyle;
	}

	public static HSSFCellStyle noterv10(HSSFWorkbook wb, HSSFFont defaultFont10) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setWrapText(true); // 换行
		curStyle.setFont(defaultFont10);
		curStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY); // 单元格垂直居中
		return curStyle;
	}

	public static HSSFCellStyle noterv10NoWrap(HSSFWorkbook wb, HSSFFont defaultFont10) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setWrapText(false); // 换行
		curStyle.setFont(defaultFont10);
		curStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY); // 单元格垂直居中
		return curStyle;
	}

	public static HSSFCellStyle notehv10(HSSFWorkbook wb, HSSFFont defaultFont10) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setWrapText(true); // 换行
		curStyle.setFont(defaultFont10);
		curStyle.setAlignment(HorizontalAlignment.CENTER);
		curStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY); // 单元格垂直居中
		return curStyle;
	}

	/**
	 * 横向居左,垂直居中
	 * 
	 * @param wb
	 * @param defaultFont10
	 * @return
	 */
	public static HSSFCellStyle notehlv10(HSSFWorkbook wb, HSSFFont defaultFont10) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setWrapText(true); // 换行
		curStyle.setFont(defaultFont10);
		curStyle.setAlignment(HorizontalAlignment.LEFT);
		curStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY); // 单元格垂直居中
		return curStyle;
	}

	// 横向居右,垂直居中
	public static HSSFCellStyle notehrv10(HSSFWorkbook wb, HSSFFont defaultFont10) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setWrapText(true); // 换行
		curStyle.setFont(defaultFont10);
		curStyle.setAlignment(HorizontalAlignment.RIGHT);
		curStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY); // 单元格垂直居中
		return curStyle;
	}

	public static HSSFCellStyle notehv10_BorderThin(HSSFWorkbook wb, HSSFFont defaultFont10) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setWrapText(true); // 换行
		curStyle.setFont(defaultFont10);
		curStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY); // 单元格垂直居中
		curStyle.setBorderTop(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderRight(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderBottom(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderLeft(BorderStyle.THIN); // 实线右边框
		return curStyle;
	}

	public static HSSFCellStyle notecv10_BorderThin(HSSFWorkbook wb, HSSFFont defaultFont10) {
		HSSFCellStyle curStyle = wb.createCellStyle();
		curStyle.setWrapText(true); // 换行
		curStyle.setFont(defaultFont10);
		curStyle.setAlignment(HorizontalAlignment.CENTER);
		curStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY); // 单元格垂直居中
		curStyle.setBorderTop(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderRight(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderBottom(BorderStyle.THIN); // 实线右边框
		curStyle.setBorderLeft(BorderStyle.THIN); // 实线右边框
		return curStyle;
	}
}