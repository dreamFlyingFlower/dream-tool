package com.wy.excel;

import java.text.NumberFormat;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.PaneType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.wy.lang.StrTool;

/**
 * Excel单元格样式工具
 * 
 * @author 飞花梦影
 * @date 2021-03-31 10:50:51
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ExcelStyleTools {

	/** 默认字体 */
	public static final String DEFAULT_FONT_ENFONT = "Times New Roman";

	/** 默认字体大小 */
	public static final short DEFAULT_FONT_SIZE = 12;

	/** 默认格式化无符号金钱格式 */
	public static final String DEFAULT_FORMAT_MONEY = "#,###,##0.00";

	/**
	 * 画线
	 * 
	 * @param patriarch
	 * @param iRowStart
	 * @param iColStart
	 * @param iRowStop
	 * @param iColStop
	 */
	public static void addLine(HSSFPatriarch patriarch, int iRowStart, int iColStart, int iRowStop, int iColStop) {
		HSSFClientAnchor anchor =
				new HSSFClientAnchor(0, 0, 350, 0, (short) (iColStart), iRowStart, (short) (iColStop), iRowStop);
		HSSFSimpleShape lineShape = patriarch.createSimpleShape(anchor);
		lineShape.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
	}

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
	 * @param paneType 激活的哪个面板区
	 */
	public static void splitPane(Sheet sheet, int xSplitPos, int ySplitPos, int leftmostColumn, int topRow,
			PaneType paneType) {
		sheet.createSplitPane(xSplitPos, ySplitPos, leftmostColumn, topRow, paneType);
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

	/**
	 * 计算行高度,实现行自动适应高度 defaultRowHeight = 12.00f;
	 * 每一行的高度指定,目前只实现根据回车多行来判断,不能根据单元格宽度自动回行来判断
	 * 
	 * @param str
	 * @param defaultRowHeight
	 * @return
	 */
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

	/**
	 * 计算字符串高度
	 * 
	 * @param charStr
	 * @return
	 */
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

	/**
	 * 设置10号正常大小的Times New Roman
	 * 
	 * @param workbook Workbook
	 * @return Font
	 */
	public static Font font10(Workbook workbook) {
		return font(workbook, DEFAULT_FONT_ENFONT, (short) 10, false);
	}

	/**
	 * 设置为10号加粗的Times New Roman
	 * 
	 * @param workbook Workbook
	 * @return Font
	 */
	public static Font font10Blod(Workbook workbook) {
		return font(workbook, DEFAULT_FONT_ENFONT, (short) 10, true);
	}

	/**
	 * 设置12号正常大小的Times New Roman
	 * 
	 * @param workbook Workbook
	 * @return Font
	 */
	public static Font font12(Workbook workbook) {
		return font(workbook, DEFAULT_FONT_ENFONT, DEFAULT_FONT_SIZE, false);
	}

	/**
	 * 设置12号加粗的Times New Roman
	 * 
	 * @param workbook Workbook
	 * @return Font
	 */
	public static Font font12Bold(Workbook workbook) {
		return font(workbook, DEFAULT_FONT_ENFONT, DEFAULT_FONT_SIZE, true);
	}

	/**
	 * 设置12号正常大小的黑色字体
	 * 
	 * @param workbook Workbook
	 * @return Font
	 */
	public static Font font12Black(Workbook workbook) {
		return font(workbook, "黑体", DEFAULT_FONT_SIZE, false);
	}

	/**
	 * 设置16号正常大小的Times New Roman
	 * 
	 * @param workbook Workbook
	 * @param fontName 字体
	 * @return Font
	 */
	public static Font font16(Workbook workbook) {
		return font(workbook, DEFAULT_FONT_ENFONT, (short) 16, false);
	}

	/**
	 * 设置16号加粗的Times New Roman
	 * 
	 * @param workbook Workbook
	 * @return Font
	 */
	public static Font font16Bold(Workbook workbook) {
		return font(workbook, DEFAULT_FONT_ENFONT, (short) 16, true);
	}

	/**
	 * 设置16号正常大小的黑色字体
	 * 
	 * @param workbook Workbook
	 * @return Font
	 */
	public static Font font16Black(Workbook workbook) {
		return font(workbook, "黑体", (short) 16, false);
	}

	/**
	 * 设置指定字体
	 * 
	 * @param workbook Workbook
	 * @param fontName 字体
	 * @param fontSize 字体大小
	 * @param bold 是否加粗
	 * @return Font
	 */
	public static Font font(Workbook workbook, String fontName, short fontSize, boolean bold) {
		Font font = workbook.createFont();
		font.setFontName(fontName);
		// 设置中文字体,那必须还要再对单元格进行编码设置
		font.setCharSet(Font.DEFAULT_CHARSET);
		font.setFontHeightInPoints(fontSize);
		font.setBold(bold);
		return font;
	}

	/**
	 * 格式化数据为金钱格式,默认保留2位小数.0显示为0.00
	 * 
	 * @param workbook {@link Workbook}对象
	 * @return 格式索引
	 */
	public static short formatMoney(Workbook workbook) {
		return formatMoney(workbook, 2);
	}

	/**
	 * 格式化数据为金钱格式,保留指定位小数.0显示为0.00
	 * 
	 * @param workbook {@link Workbook}对象
	 * @param scale 保留小数位,0不保留小数
	 * @return 格式索引
	 */
	public static short formatMoney(Workbook workbook, int scale) {
		return formatMoney(workbook, scale,
				new StringBuilder("#,###,##0").append(scale > 0 ? "." + StrTool.repeat("0", scale) : "").toString());
	}

	/**
	 * 格式化数据为金钱格式,保留指定位小数.0显示为.00
	 * 
	 * @param workbook {@link Workbook}对象
	 * @param scale 保留小数位,0不保留小数
	 * @param format 格式化字符串
	 * @return 格式索引
	 */
	public static short formatMoney(Workbook workbook, int scale, String format) {
		scale = scale > 0 ? scale : 0;
		format = StrTool.isBlank(format) ? DEFAULT_FORMAT_MONEY : format;
		return workbook.createDataFormat().getFormat(format);
	}

	/**
	 * 格式化数据为金钱格式,0表示为.00
	 * 
	 * @param workbook {@link Workbook}对象
	 * @return 格式索引
	 */
	public static short formatMoneyNo0(Workbook workbook) {
		return formatMoneyNo0(workbook, 2);
	}

	/**
	 * 格式化数据为金钱格式,0表示为.00
	 * 
	 * @param workbook {@link Workbook}对象
	 * @param scale 保留小数位,0不保留小数
	 * @return 格式索引
	 */
	public static short formatMoneyNo0(Workbook workbook, int scale) {
		return formatMoney(workbook, scale,
				new StringBuilder("#,###,###").append(scale > 0 ? "." + StrTool.repeat("0", scale) : "").toString());
	}

	/**
	 * 格式化数据为金钱格式,带符号￥,默认保留2位小数.0表示为0.00
	 * 
	 * @param workbook {@link Workbook}对象
	 * @return 格式索引
	 */
	public static short formatMoneySymbol(Workbook workbook) {
		return formatMoneySymbol(workbook, 2);
	}

	/**
	 * 格式化数据为金钱格式,带符号￥,保留指定位小数.0显示为0.00
	 * 
	 * @param workbook {@link Workbook}对象
	 * @param scale 保留小数位,0不保留小数
	 * @return 格式索引
	 */
	public static short formatMoneySymbol(Workbook workbook, int scale) {
		return formatMoney(workbook, scale, new StringBuilder("\"￥\"#,###,##0")
				.append(scale > 0 ? "." + StrTool.repeat("0", scale) : "").toString());
	}

	/**
	 * 格式化数据为金钱格式,带符号￥,默认保留2位小数,0表示为.00
	 * 
	 * @param workbook {@link Workbook}对象
	 * @return 格式索引
	 */
	public static short formatMoneySymbolNo0(Workbook workbook) {
		return formatMoneySymbolNo0(workbook, 2);
	}

	/**
	 * 格式化数据为金钱格式,带符号￥,保留指定位小数,0表示为.00
	 * 
	 * @param workbook {@link Workbook}对象
	 * @param scale 保留小数位,0不保留小数
	 * @return 格式索引
	 */
	public static short formatMoneySymbolNo0(Workbook workbook, int scale) {
		return formatMoney(workbook, scale, new StringBuilder("\"￥\"#,###,###")
				.append(scale > 0 ? "." + StrTool.repeat("0", scale) : "").toString());
	}

	/**
	 * 格式化日期格式
	 * 
	 * @return 格式索引
	 */
	public static short formatDate() {
		return (short) BuiltinFormats.getBuiltinFormat("m/d/yy");
	}

	/**
	 * 格式化日期格式
	 * 
	 * @param format 格式化字符串,默认yyyy-MM-dd HH:mm:ss
	 * @return 格式索引
	 */
	public static short formatDate(String format) {
		return (short) BuiltinFormats.getBuiltinFormat(format);
	}

	/**
	 * 数据格式化,如数字,时间等
	 * 
	 * @param workbook {@link Workbook}对象
	 * @param format 若为数字格式化,规则同{@link NumberFormat};时间格式,同Java格式
	 * @return CellStyle
	 */
	public static CellStyle formatDate(Workbook workbook, String format) {
		DataFormat dataFormat = workbook.createDataFormat();
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setDataFormat(dataFormat.getFormat(format));
		return cellStyle;
	}

	/**
	 * 设置文本自动换行
	 * 
	 * @param cellStyle 单元格样式对象
	 */
	public static void wrapText(CellStyle cellStyle) {
		cellStyle.setWrapText(true);
	}

	/**
	 * 设置居中的12号Times New Roman字体,并加粗
	 * 
	 * @param workbook Workbook
	 * @return CellStyle
	 */
	public static CellStyle center12(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font12Bold(workbook));
		centerAll(cellStyle);
		return cellStyle;
	}

	/**
	 * 设置单元格无边框
	 * 
	 * @param workbook Workbook
	 * @return CellStyle
	 */
	public static CellStyle nobox(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		borderNoneAll(cellStyle);
		return cellStyle;
	}

	/**
	 * 实现打印时为白框,目的就是实现涂去上行的下边框线
	 * 
	 * @param workbook Workbook
	 * @return
	 */
	public static CellStyle whiteBox(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setTopBorderColor(HSSFColorPredefined.WHITE.getIndex());
		cellStyle.setRightBorderColor(HSSFColorPredefined.WHITE.getIndex());
		cellStyle.setBottomBorderColor(HSSFColorPredefined.WHITE.getIndex());
		cellStyle.setLeftBorderColor(HSSFColorPredefined.WHITE.getIndex());
		borderThinAll(cellStyle);
		return cellStyle;
	}

	public static CellStyle numberrv10_BorderThin(Workbook workbook, Font font10) {
		return moneyrv10_BorderThin(workbook, font10, (short) -1);
	}

	public static CellStyle moneyrv10_BorderThin(Workbook workbook, Font font10, short rmb4Format) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font10);
		if (rmb4Format >= 0) {
			cellStyle.setDataFormat(rmb4Format);
		}
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
		borderThinAll(cellStyle);
		return cellStyle;
	}

	public static CellStyle moneyrv12_BorderThin(Workbook workbook, Font font12, short rmb2Format) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font12);
		cellStyle.setDataFormat(rmb2Format);
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
		borderThinAll(cellStyle);
		return cellStyle;
	}

	public static CellStyle money1(Workbook workbook, Font font10, short money1Format) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font10);
		cellStyle.setDataFormat(money1Format);
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
		return cellStyle;
	}

	public static CellStyle money2(Workbook workbook, Font font10, short money2Format) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font10);
		cellStyle.setDataFormat(money2Format);
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
		return cellStyle;
	}

	public static CellStyle datevEN(Workbook workbook, Font font10, short datevENFormat) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font10);
		cellStyle.setDataFormat(datevENFormat);
		cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
		return cellStyle;
	}

	public static CellStyle notet10(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
		borderThinAll(cellStyle);
		return cellStyle;
	}

	public static CellStyle notevt10(Workbook workbook, Font font10) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setFont(font10);
		cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
		return cellStyle;
	}

	public static CellStyle noterv10(Workbook workbook, Font font10) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setFont(font10);
		cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
		return cellStyle;
	}

	public static CellStyle noterv10NoWrap(Workbook workbook, Font font10) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(false);
		cellStyle.setFont(font10);
		cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
		return cellStyle;
	}

	public static CellStyle notehv10(Workbook workbook, Font font10) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setFont(font10);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
		return cellStyle;
	}

	/**
	 * 横向居左,垂直居中
	 * 
	 * @param workbook
	 * @param font10
	 * @return
	 */
	public static CellStyle notehlv10(Workbook workbook, Font font10) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setFont(font10);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
		return cellStyle;
	}

	/**
	 * 横向居右,垂直居中
	 * 
	 * @param workbook
	 * @param font10
	 * @return
	 */
	public static CellStyle notehrv10(Workbook workbook, Font font10) {
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setFont(font10);
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);
		cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
		return cellStyle;
	}

	/**
	 * 水平垂直居中
	 * 
	 * @param cellStyle CellStyle
	 */
	public static void centerAll(CellStyle cellStyle) {
		// 水平居中
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		// 垂直居中
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	}

	/**
	 * 单元格无任何边框
	 * 
	 * @param cellStyle CellStyle
	 */
	public static void borderNoneAll(CellStyle cellStyle) {
		cellStyle.setTopBorderColor((short) 0);
		cellStyle.setBorderTop(BorderStyle.NONE);
		cellStyle.setBorderRight(BorderStyle.NONE);
		cellStyle.setBorderBottom(BorderStyle.NONE);
		cellStyle.setBorderLeft(BorderStyle.NONE);
	}

	/**
	 * 单元格实线边框
	 * 
	 * @param cellStyle CellStyle
	 */
	public static void borderThinAll(CellStyle cellStyle) {
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
	}

	/**
	 * 设置单元格为10号字体,通用
	 * 
	 * @param workbook
	 * @param font10
	 * @return
	 */
	public static CellStyle borderThin(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
		centerBorderThin(cellStyle);
		return cellStyle;
	}

	/**
	 * 设置单元格为10号字体,通用
	 * 
	 * @param cellStyle
	 * @param font10
	 * @return
	 */
	public static void centerBorderThin(CellStyle cellStyle) {
		centerAll(cellStyle);
		borderThinAll(cellStyle);
	}

	/**
	 * 限制默认Sheet中指定列所有单元格的文本最大长度
	 * 
	 * @param workbook Workbook
	 * @param maxLength 最大长度
	 * @param col 限制列,从0开始
	 */
	public static void setMaxlength(Workbook workbook, int maxLength, int col) {
		Sheet sheet = workbook.createSheet();
		setMaxlength(sheet, maxLength, col);
	}

	/**
	 * 限制指定Sheet中指定列所有单元格的文本最大长度
	 * 
	 * @param workbook Workbook
	 * @param sheetName sheet页名称
	 * @param maxLength 最大长度
	 * @param col 限制列,从0开始
	 */
	public static void setMaxlength(Workbook workbook, String sheetName, int maxLength, int col) {
		Sheet sheet = workbook.createSheet(sheetName);
		setMaxlength(sheet, maxLength, col);
	}

	/**
	 * 限制指定Sheet中指定列所有单元格的文本最大长度
	 * 
	 * @param sheet Sheet
	 * @param maxLength 最大长度
	 * @param col 限制列,从0开始
	 */
	public static void setMaxlength(Sheet sheet, int maxLength, int col) {
		DataValidationHelper dataValidationHelper = null;
		if (sheet instanceof HSSFSheet) {
			dataValidationHelper = new HSSFDataValidationHelper((HSSFSheet) sheet);
		} else if (sheet instanceof XSSFSheet) {
			dataValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		}
		if (null != dataValidationHelper) {
			setMaxlength(sheet, dataValidationHelper, maxLength, col);
		}
	}

	/**
	 * 限制指定Sheet中指定列所有单元格的文本最大长度,只支持2007版本之前
	 * 
	 * @param sheet HSSFSheet
	 * @param maxLength 最大长度
	 * @param col 限制列,从0开始
	 */
	public static void setMaxlength(HSSFSheet sheet, int maxLength, int col) {
		DataValidationHelper dataValidationHelper = new HSSFDataValidationHelper(sheet);
		setMaxlength(sheet, dataValidationHelper, maxLength, col);
	}

	/**
	 * 限制指定Sheet中指定列所有单元格的文本最大长度,支持2007版本之后
	 * 
	 * @param sheet XSSFSheet
	 * @param maxLength 最大长度
	 * @param col 限制列,从0开始
	 */
	public static void setMaxlength(XSSFSheet sheet, int maxLength, int col) {
		DataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheet);
		setMaxlength(sheet, dataValidationHelper, maxLength, col);
	}

	/**
	 * 限制指定Sheet中指定列所有单元格的文本最大长度
	 * 
	 * @param sheet Sheet
	 * @param dataValidationHelper 数据验证
	 * @param maxLength 最大长度
	 * @param col 限制列,从0开始
	 */
	public static void setMaxlength(Sheet sheet, DataValidationHelper dataValidationHelper, int maxLength, int col) {
		// 设置类型为文本
		DataValidationConstraint dvConstraint =
				dataValidationHelper.createNumericConstraint(DataValidationConstraint.ValidationType.TEXT_LENGTH,
						DataValidationConstraint.OperatorType.LESS_OR_EQUAL, maxLength + "", maxLength + "");
		CellRangeAddressList addressList = new CellRangeAddressList(1, 65535, col, col);
		DataValidation validation = dataValidationHelper.createValidation(dvConstraint, addressList);
		validation.createErrorBox("错误提示", "长度不能超过" + maxLength);
		validation.setShowErrorBox(true);
		sheet.addValidationData(validation);
	}

	public static FontBuilder fontBuilder(Workbook workbook) {
		return new FontBuilder(workbook);
	}

	public static class FontBuilder {

		private Font font;

		public FontBuilder(Workbook workbook) {
			font = workbook.createFont();
		}

		public FontBuilder fontName(String fontName) {
			font.setFontName(fontName);
			return this;
		}

		public FontBuilder bold(boolean bold) {
			font.setBold(bold);
			return this;
		}

		public Font build() {
			return font;
		}
	}
}