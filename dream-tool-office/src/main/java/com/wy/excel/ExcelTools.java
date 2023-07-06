package com.wy.excel;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.wy.ConstOffice;
import com.wy.collection.ListTool;
import com.wy.enums.TipFormatEnum;
import com.wy.io.file.FileTool;
import com.wy.lang.AssertTool;
import com.wy.lang.NumberTool;
import com.wy.lang.StrTool;
import com.wy.result.ResultException;
import com.wy.util.DateTool;

/**
 * Apache操作excel,需要导入包:poi,xmlbeans,curvesapi,poi-ooxml-schemas,poi-ooxml
 * 
 * {@link HSSFWorkbook}:处理Excel2003以前,包括2003的版本,文件扩展名是.xls
 * {@link XSSFWorkbook}:处理Excel2007的版本,文件扩展名是.xlsx
 * {@link SXSSFWorkbook}:处理超大数据集,需要指定每次写入时最大留存的对象个数,避免内存溢出.
 * 最大只能写104W左右数据,实际业务中应该根据数据条数进行拆分,分成多个Excel或多个Sheet
 * 
 * @author 飞花梦影
 * @date 2020-11-23 16:11:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ExcelTools {

	/**
	 * Excel中添加图片
	 * 
	 * @param wb Workbook
	 * @param targetFilePath 输出文件地址
	 * @param picPath 图片地址
	 * @param beginRow 从第几行开始绘制图片
	 * @param beginCol 从第几列开始绘制图片
	 */
	public static void addPicture(Workbook wb, String targetFilePath, String picPath, int beginRow, int beginCol) {
		addPicture(wb, wb.createSheet().createDrawingPatriarch(), targetFilePath, picPath, beginRow, beginCol);
	}

	/**
	 * Excel中添加图片
	 * 
	 * @param wb Workbook
	 * @param patriarch 绘图对象
	 * @param targetFilePath 输出文件地址
	 * @param picPath 图片地址
	 * @param beginRow 从第几行开始绘制图片
	 * @param beginCol 从第几列开始绘制图片
	 */
	public static void addPicture(Workbook wb, Drawing<?> patriarch, String targetFilePath, String picPath,
			int beginRow, int beginCol) {
		AssertTool.isPositiveInteger(beginRow);
		AssertTool.isPositiveInteger(beginCol);
		FileTool.checkFile(picPath);
		int pictureIdx = -1;
		try (FileInputStream stream = new FileInputStream(picPath);) {
			byte[] bytes = IOUtils.toByteArray(stream);
			// 读取图片到二进制数组
			stream.read(bytes);
			// 向Excel添加一张图片,并返回该图片在Excel中的图片集合中的下标
			pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 绘图工具类
		CreationHelper helper = wb.getCreationHelper();
		// 创建锚点,设置图片坐标
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setRow1(beginRow);
		anchor.setCol1(beginCol);
		// 创建图片
		Picture picture = patriarch.createPicture(anchor, pictureIdx);
		picture.resize();
		try (FileOutputStream fos = new FileOutputStream(targetFilePath);) {
			// 写入文件
			wb.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 指定图片类型为jpg
	 * 
	 * @param wb Workbook
	 * @param picPath 图片地址
	 * @param beginRow 从第几行开始绘制图片
	 * @param beginCol 从第几列开始绘制图片
	 * @throws IOException
	 */
	public static void addPicture(Workbook wb, String picPath, int beginRow, int beginCol) throws IOException {
		addPicture(wb, wb.createSheet().createDrawingPatriarch(), picPath, beginRow, beginCol);
	}

	/**
	 * 指定图片类型为jpg
	 * 
	 * @param wb Workbook
	 * @param patriarch 绘图对象
	 * @param picPath 图片地址
	 * @param beginRow 从第几行开始绘制图片
	 * @param beginCol 从第几列开始绘制图片
	 */
	public static void addPicture(Workbook wb, Drawing<?> patriarch, String picPath, int beginRow, int beginCol) {
		addPicture(wb, patriarch, picPath, beginRow, beginCol, beginRow, beginCol + 1);
	}

	/**
	 * 指定图片类型为jpg
	 * 
	 * @param wb Workbook
	 * @param patriarch 绘图对象
	 * @param picPath 图片地址
	 * @param beginRow 从第几行开始绘制图片
	 * @param beginCol 从第几列开始绘制图片
	 * @param endRow 到第几行结束绘制图片
	 * @param endCol 到第几列结束绘制图片
	 */
	public static void addPicture(Workbook wb, Drawing<?> patriarch, String picPath, int beginRow, int beginCol,
			int endRow, int endCol) {
		File imgFile = FileTool.checkFile(picPath);
		try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();) {
			BufferedImage bufferImg = ImageIO.read(imgFile);
			ImageIO.write(bufferImg, "jpg", byteArrayOut);
			// 左,上(0-255),右(0-1023),下
			HSSFClientAnchor anchor =
					new HSSFClientAnchor(20, 1, 1018, 0, (short) (beginCol), beginRow, (short) (endCol), endRow);
			patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导出excel数据表格:<br>
	 * 1.默认导出的文件名:数据导出<br>
	 * 2.默认文件后缀为xls<br>
	 * 3.默认文件名字节数组为本地默认编码<br>
	 * 4.默认导出时文件名的编码为本地默认编码<br>
	 * 5.默认每个sheet页最大行数为65535<br>
	 * 6.默认每个sheet页都有标题
	 * 
	 * @param resp 响应
	 * @param datas 需要导出的数据
	 */
	default <T> void exportExcel(List<T> datas, HttpServletResponse resp) {
		exportExcel(datas, resp, ConstOffice.EXCEL_DEFAULT_FILE_NAME);
	}

	default <T> void exportExcel(List<T> datas, HttpServletResponse resp, String excelName) {
		exportExcel(datas, resp, excelName, Charset.defaultCharset());
	}

	default <T> void exportExcel(List<T> datas, HttpServletResponse resp, String excelName, Charset encode) {
		exportExcel(datas, resp, excelName, encode, Charset.defaultCharset());
	}

	default <T> void exportExcel(List<T> datas, HttpServletResponse resp, String excelName, Charset encode,
			Charset decode) {
		exportExcel(datas, resp, excelName, encode.displayName(), decode.displayName());
	}

	default <T> void exportExcel(List<T> datas, HttpServletResponse resp, String excelName, String encode,
			String decode) {
		exportExcel(datas, resp, excelName, encode, decode, ConstOffice.EXCEL_SHEET_MAX_ROW);
	}

	default <T> void exportExcel(List<T> datas, HttpServletResponse resp, String excelName, String encode,
			String decode, int sheetMax) {
		exportExcel(datas, resp, excelName, encode, decode, sheetMax, true);
	}

	/**
	 * 导出excel到前端页面下载,若是中文文件名,默认编码时要用gbk,解码时要用iso8859-1
	 * 
	 * @param <T> 泛型
	 * @param datas 导出的数据
	 * @param resp 响应
	 * @param excelName 文件名,可不带后缀,默认后缀为xls
	 * @param encode 文件名编码的字符集
	 * @param decode 文件名解压的字符集
	 * @param sheetMax 每个sheet页的最大写入行数,默认65535
	 * @param subject 是否添加标题,默认true添加false不添加,真实数据从第2行开始写入
	 */
	default <T> void exportExcel(List<T> datas, HttpServletResponse resp, String excelName, String encode,
			String decode, int sheetMax, boolean subject) {
		resp.setContentType("application/download");
		try (OutputStream os = resp.getOutputStream();) {
			// 处理文件名后缀
			String fileExtension = FileTool.getFileExtension(excelName);
			if (StrTool.isBlank(fileExtension)) {
				excelName += "." + ConstOffice.EXCEL_DEFAULT_FILE_NAME_SUFFIX;
			}
			// 处理文件编码
			resp.setHeader("Content-Disposition",
					"attchament;filename=" + new String(excelName.getBytes(encode), Charset.forName(decode)));
			// 处理每个sheet页最大行数据
			sheetMax = sheetMax >= ConstOffice.EXCEL_SHEET_MAX_ROW ? ConstOffice.EXCEL_SHEET_MAX_ROW : sheetMax;
			long sheetNum = Math.round(NumberTool.div(datas.size(), sheetMax).floatValue());
			for (int i = 1; i <= sheetNum; i++) {
				handleSheet(i, datas, os, sheetMax, subject);
			}
		} catch (IOException e) {
			throw new ResultException("导出失败", e);
		}
	}

	/**
	 * 根据文件后缀名生成相应的Workbook实例,将数据写入到excel中使用
	 * 
	 * @param path 文件路径
	 * @return Workbook实例
	 */
	static Workbook generateWorkbook(String path) {
		if (StrTool.isBlank(path)) {
			throw new ResultException(TipFormatEnum.TIP_LOG_ERROR.getMsg("文件路径错误"));
		}
		if (Objects.equals("xlsx", FileTool.getFileExtension(path))) {
			return new XSSFWorkbook();
		} else {
			return new HSSFWorkbook();
		}
	}

	/**
	 * 根据文件后缀名生成相应的Workbook实例,读取excel文件中的数据时使用
	 * 
	 * @param path 文件路径
	 * @return Workbook实例
	 */
	static Workbook generateReadWorkbook(String path) {
		File file = new File(path);
		if (!file.exists()) {
			throw new ResultException("文件不存在");
		}
		Workbook workbook = null;
		try {
			if (path.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(new FileInputStream(file));
			} else {
				workbook = new HSSFWorkbook(new FileInputStream(file));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return workbook;
	}

	/**
	 * 获得单元格值
	 * 
	 * @param cell 单元格
	 * @return 单元格值
	 */
	public static Object getCellValue(Cell cell) {
		if (Objects.isNull(cell)) {
			return "";
		}
		switch (cell.getCellType()) {
		case BOOLEAN:
			return cell.getBooleanCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			}
			return cell.getNumericCellValue();
		case STRING:
			return cell.getStringCellValue();
		case FORMULA:
			if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
				return cell.getNumericCellValue();
			} else if (cell.getCachedFormulaResultType() == CellType.STRING) {
				return cell.getRichStringCellValue().getString();
			} else {
				return cell.getCellFormula();
			}
		default:
			return cell.getErrorCellValue();
		}
	}

	/**
	 * 文件导出到输入流中生成excel文件
	 * 
	 * @param <T> 泛型
	 * @param index sheet页下标,从1开始
	 * @param datas 实体类数据源集合或Map数据源集合
	 * @param os 输入流
	 * @param sheetMax 每个sheet页的最大写入行数,默认65535
	 * @param subject 是否添加标题,默认true添加false不添加,真实数据从第2行开始写入
	 */
	<T> void handleSheet(int index, List<T> datas, OutputStream os, int sheetMax, boolean subject);

	/**
	 * 处理每一个单元格
	 * 
	 * @param <T> 泛型
	 * @param cell 单元格
	 * @param t 需要写入到单元格的数据
	 * @param field 当前字段
	 */
	default <T> void handleCell(Cell cell, T t, Field field) {
	}

	/**
	 * 读取excel表格数据,默认表格中第一行是key值,且可以使用,不计入数据,从第2行第1列读取数据
	 * 
	 * @param path 需要读取的excel文件路径
	 * @return 结果集
	 */
	public static List<Map<String, Object>> readExcel(String path) {
		return readExcel(path, true, null);
	}

	/**
	 * 读取excel中的数据.第一行不计入数据,从2行第1列开始读取数据
	 * 
	 * @param path 文件地址
	 * @param firstUse 每个sheet中第一行数据是否可作为字段使用,true可,false不可
	 * @param titles 当firstUse为true时,该值不使用.若是false,则该值为字段名或key值,但是第一行数据仍不使用
	 * @return list集合
	 */
	public static List<Map<String, Object>> readExcel(String path, boolean firstUse, List<String> titles) {
		return readExcel(path, firstUse, titles, 0);
	}

	/**
	 * 读取excel中的数据.excel的第一行作为key值,不计入数据,从第beginRow+2行第1列开始读取数据
	 * 
	 * @param path 文件地址
	 * @param firstUse 每个sheet中第一行数据是否可作为字段使用,true可,false不可
	 * @param titles 当firstUse为true时,该值不使用.若是false,则该值为字段名或key值,但是第一行数据仍不使用
	 * @param beginRow 从第beginRow+2行开始读取数据
	 * @return list集合
	 */
	public static List<Map<String, Object>> readExcel(String path, boolean firstUse, List<String> titles,
			int beginRow) {
		return readExcel(path, firstUse, titles, beginRow, 0);
	}

	/**
	 * 读取excel中的数据.excel的第一行作为key值,不计入数据,从第beginRow+2行第beginCol+1列开始读取数据
	 * 
	 * @param path 需要读取的excel路径
	 * @param firstUse 每个sheet中第一行数据是否可作为字段使用,true可,false不可
	 * @param titles 当firstUse为true时,该值不使用.若是false,则该值为字段名或key值,但是第一行数据仍不使用
	 * @param beginRow 从第beginRow+2行开始读取数据
	 * @param beginCol 从第beginCol+1列开始读取excel
	 * @return 结果集
	 */
	public static List<Map<String, Object>> readExcel(String path, boolean firstUse, List<String> titles, int beginRow,
			int beginCol) {
		return null;
	}

	/**
	 * 读取excel表格数据,默认表格中第一行是key值,且可以使用,不计入数据,从第2行第1列读取数据
	 * 
	 * @param is 输入流
	 * @return 结果集
	 */
	default List<Map<String, Object>> readExcel(InputStream is) {
		return readExcel(is, true, null);
	}

	/**
	 * 读取excel中的数据.第一行不计入数据,从2行第1列开始读取数据
	 * 
	 * @param is 输入流
	 * @param firstUse 每个sheet中第一行数据是否可作为字段使用,true可,false不可
	 * @param titles 当firstUse为true时,该值不使用.若是false,则该值为字段名或key值的集合,但是第一行数据仍不使用
	 * @return 结果集
	 */
	default List<Map<String, Object>> readExcel(InputStream is, boolean firstUse, List<String> titles) {
		return readExcel(is, firstUse, titles, 0);
	}

	/**
	 * 读取excel中的数据.excel的第一行作为key值,不计入数据,从第beginRow+2行第1列开始读取数据
	 * 
	 * @param is 输入流
	 * @param firstUse 每个sheet中第一行数据是否可作为字段使用,true可,false不可
	 * @param titles 当firstUse为true时,该值不使用.若是false,则该值为字段名或key值,但是第一行数据仍不使用
	 * @param beginRow 从第beginRow+1行开始读取excel
	 * @return 结果集
	 */
	default List<Map<String, Object>> readExcel(InputStream is, boolean firstUse, List<String> titles, int beginRow) {
		return readExcel(is, firstUse, titles, beginRow, 0);
	}

	/**
	 * 读取excel中的数据.excel的第一行作为key值,不计入数据,从第beginRow+2行第beginCol+1列开始读取数据
	 * 
	 * @param is 输入流
	 * @param firstUse 每个sheet中第一行数据是否可作为字段使用,true可,false不可
	 * @param titles 当firstUse为true时,该值不使用.若是false,则该值为字段名或key值,但是第一行数据仍不使用
	 * @param beginRow 从第beginRow+1行开始读取excel数据
	 * @param beginCol 从第beginCol+1列开始读取excel
	 * @return 结果集
	 */
	default List<Map<String, Object>> readExcel(InputStream is, boolean firstUse, List<String> titles, int beginRow,
			int beginCol) {
		return null;
	}

	/**
	 * 设置单元格格式以及值
	 * 
	 * @param cell 单元格
	 * @param value 单元格值
	 */
	static void setCellValue(Cell cell, Object value) {
		Class<? extends Object> clazz = value.getClass();
		if (clazz == Boolean.class || clazz == boolean.class) {
			cell.setCellValue(Boolean.valueOf(value.toString()));
		} else if (NumberTool.isNumber(value.toString())) {
			cell.setCellValue(Double.valueOf(value.toString()));
		} else if (clazz == Date.class) {
			cell.setCellValue((Date) value);
		} else {
			cell.setCellValue(value.toString());
		}
	}

	/**
	 * 根据值创建不同类型的Cell并设置值
	 * 
	 * @param row 行
	 * @param index 行中的第几个cell
	 * @param value 值
	 */
	static void setCellValue(Row row, int index, Object value) {
		if (Objects.isNull(row)) {
			return;
		}
		if (Objects.isNull(value)) {
			row.createCell(index).setCellValue("");
			return;
		}
		Class<? extends Object> clazz = value.getClass();
		if (clazz == Boolean.class || clazz == boolean.class) {
			row.createCell(index, CellType.BOOLEAN).setCellValue(Boolean.valueOf(value.toString()));
		} else if (value instanceof Number) {
			row.createCell(index, CellType.NUMERIC).setCellValue(Double.valueOf(value.toString()));
		} else if (value instanceof Date) {
			row.createCell(index).setCellValue(DateTool.formatDateTime((Date) value));
		} else {
			row.createCell(index, CellType.STRING).setCellValue(String.valueOf(value));
		}
	}

	/**
	 * 将数据写入到excel文件中:<br>
	 * 1.若不指定文件后缀,则默认文件后缀为.xls<br>
	 * 2.若文件不存在,则创建<br>
	 * 3.若文件已经存在,则从第一行开始写数据,且第一行为标题,真实数据从第2行开始<br>
	 * 4.每个sheet页写入的最大行数默认为65535
	 * 
	 * @param <T> 泛型数据类型
	 * @param datas 实体类数据源集合或Map数据源集合
	 * @param path 文件路径,若文件路径不带后缀,则默认后缀为.xls
	 */
	default <T> void write(List<T> datas, String path) throws IOException {
		write(datas, path, ConstOffice.EXCEL_SHEET_MAX_ROW);
	}

	default <T> void write(List<T> datas, File file) throws IOException {
		write(datas, file, ConstOffice.EXCEL_SHEET_MAX_ROW);
	}

	/**
	 * 将数据写入到excel文件中:<br>
	 * 1.若不指定文件后缀,则默认文件后缀为.xls<br>
	 * 2.若文件不存在,则创建<br>
	 * 3.若文件已经存在,则从第一行开始写数据,且第一行为标题,真实数据从第2行开始<br>
	 * 
	 * @param <T> 泛型数据类型
	 * @param datas 实体类数据源集合或Map数据源集合
	 * @param path 文件路径,若文件路径不带后缀,则默认后缀为.xls
	 * @param sheetMax 每个sheet页的最大写入行数,默认65535
	 */
	default <T> void write(List<T> datas, String path, int sheetMax) throws IOException {
		write(datas, path, sheetMax, true);
	}

	default <T> void write(List<T> datas, File file, int sheetMax) throws IOException {
		write(datas, file, sheetMax, true);
	}

	/**
	 * 将数据写入到excel文件中:<br>
	 * 1.若不指定文件后缀,则默认文件后缀为.xls<br>
	 * 2.若文件不存在,则创建<br>
	 * 
	 * @param <T> 泛型数据类型
	 * @param datas 实体类数据源集合或Map数据源集合
	 * @param path 文件路径,若文件路径不带后缀,则默认后缀为.xls
	 * @param sheetMax 每个sheet页的最大写入行数,默认65535
	 * @param subject 是否添加标题,默认true添加false不添加,真实数据从第2行开始写入
	 */
	default <T> void write(List<T> datas, String path, int sheetMax, boolean subject) throws IOException {
		writeSheet(datas, path, sheetMax, subject);
	}

	default <T> void write(List<T> datas, File file, int sheetMax, boolean subject) throws IOException {
		writeSheet(datas, file, sheetMax, subject);
	}

	/**
	 * 将数据写入一个excel表中,表以低版本为主,即以xls结尾,默认无字段栏
	 * 
	 * @param excel 数据源
	 * @param path 写入文件路径
	 */
	default boolean writeExcel(Workbook wb, List<List<List<Object>>> datas, String path) {
		return writeExcel(wb, datas, path, null);
	}

	default boolean writeExcel(Workbook wb, List<List<List<Object>>> excel, String path, CellStyle cellStyle) {
		return false;
	}

	/**
	 * 根据excel文件的结尾来自动判断生成那种版本的excel,若传的文件没有指定类型,自动归结为低版本excel
	 * 
	 * @param path 以.xls或xlsx结尾的文件路径
	 * @param list 数据源
	 */
	default boolean writeExcelAuto(String path, List<List<Object>> datas) {
		List<List<List<Object>>> excel = new ArrayList<>();
		excel.add(datas);
		return writeExcelAuto(excel, path);
	}

	/**
	 * 根据excel文件的结尾来自动判断生成那种版本的excel,若传的文件没有指定类型,自动归结为低版本excel
	 * 
	 * @param path 以.xls或xlsx结尾的文件路径
	 * @param list 数据源
	 */
	default boolean writeExcelAuto(List<List<List<Object>>> excel, String path) {
		Workbook workbook = generateWorkbook(path);
		return writeExcel(workbook, excel, path);
	}

	/**
	 * 参数检查,sheet最大数量检查
	 * 
	 * @param <T> 泛型数据类型
	 * @param datas 实体类数据源集合或Map数据源集合
	 * @param path 文件路径,若文件路径不带后缀,则默认后缀为.xls
	 * @param sheetMax 每个sheet页的最大写入行数,默认65535
	 * @param subject 是否添加标题,默认true添加false不添加,真实数据从第2行开始写入
	 * @throws IOException
	 */
	default <T> void writeSheet(List<T> datas, String path, int sheetMax, boolean subject) throws IOException {
		if (StrTool.isBlank(path)) {
			throw new ResultException(TipFormatEnum.TIP_LOG_ERROR.getMsg("excel写入文件路径不存在"));
		}
		writeSheet(datas, new File(path), sheetMax, subject);
	}

	default <T> void writeSheet(List<T> datas, File file, int sheetMax, boolean subject) throws IOException {
		if (ListTool.isEmpty(datas)) {
			throw new ResultException(TipFormatEnum.TIP_LOG_INFO.getMsg("excel写入文件数据源为空"));
		}
		if (Objects.isNull(file)) {
			throw new ResultException(TipFormatEnum.TIP_LOG_ERROR.getMsg("excel写入文件不能为空"));
		}
		FileTool.createFile(file, true);
		sheetMax = sheetMax >= ConstOffice.EXCEL_SHEET_MAX_ROW ? ConstOffice.EXCEL_SHEET_MAX_ROW : sheetMax;
		long sheetNum = Math.round(NumberTool.div(datas.size(), sheetMax).floatValue());
		for (int i = 1; i <= sheetNum; i++) {
			writeSheet(i, datas, file, sheetMax, subject);
		}
	}

	/**
	 * 处理每一个sheet页
	 *
	 * @param <T> 泛型
	 * @param index sheet页下标,从1开始
	 * @param datas 实体类数据源集合或Map数据源集合
	 * @param path 文件路径,若文件路径不带后缀,则默认后缀为.xls
	 * @param sheetMax 每个sheet页的最大写入行数,默认65535
	 * @param subject 是否添加标题,默认true添加false不添加,真实数据从第2行开始写入
	 */
	default <T> void writeSheet(int index, List<T> datas, String path, int sheetMax, boolean subject) {
		writeSheet(index, datas, new File(path), sheetMax, subject);
	}

	default <T> void writeSheet(int index, List<T> datas, File file, int sheetMax, boolean subject) {
		try (OutputStream fos = new FileOutputStream(file);) {
			handleSheet(index, datas, fos, sheetMax, subject);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResultException("文件输出流初始化失败");
		}
	}

	/**
	 * 根据excel模版来写入数据,需要先从一个excel中读取格式等信息 不同的版本需要不同的模版文件 FIXME
	 */
	public static boolean writeTemp() {
		return false;
	}

	/**
	 * 写入一个xls结尾的excel文件,低版本的excel,Excel2003以前(包括2003)的版本
	 * 
	 * @param excel 数据源
	 * @param path 写入文件路径
	 */
	default boolean writeXLS(String path, List<List<Object>> datas) {
		List<List<List<Object>>> excel = new ArrayList<>();
		excel.add(datas);
		return writeXLS(excel, path);
	}

	/**
	 * 写入一个xls结尾的excel文件,低版本的excel,Excel2003以前(包括2003)的版本
	 * 
	 * @param excel 数据源
	 * @param path 写入文件路径
	 */
	default boolean writeXLS(List<List<List<Object>>> excel, String path) {
		return writeExcel(new HSSFWorkbook(), excel, path);
	}

	/**
	 * 写入一个xlsx结尾的excel文件,高版本的excel,Excel2007的版本
	 * 
	 * @param excel 数据源
	 * @param path 写入文件路径
	 */
	default boolean writeXLSX(String path, List<List<Object>> datas) {
		List<List<List<Object>>> excel = new ArrayList<>();
		excel.add(datas);
		return writeXLSX(excel, path);
	}

	/**
	 * 写入一个xlsx结尾的excel文件,高版本的excel,Excel2007的版本
	 * 
	 * @param excel 数据源
	 * @param path 写入文件路径
	 */
	default boolean writeXLSX(List<List<List<Object>>> excel, String path) {
		return writeExcel(new XSSFWorkbook(), excel, path);
	}

	/**
	 * 指定某列为下拉框
	 * 
	 * @param sheet sheet页
	 * @param datas 下拉框数据
	 * @param col 第col列下拉框,从0开始
	 */
	default void writeSelect(Sheet sheet, String[] datas, int col) {
		writeSelect(sheet, datas, 0, 65535, col, col);
	}

	/**
	 * 指定某列为下拉框
	 * 
	 * @param sheet sheet页
	 * @param datas 下拉框数据
	 * @param beginRow 开始行,从0开始
	 * @param endRow 结束行,从0开始.若不确定结束行,可写65535
	 * @param beginCol 开始列,从0开始
	 * @param endCol 结束列,从0开始.若只有一列下拉框,开始和结束列相同
	 */
	default void writeSelect(Sheet sheet, String[] datas, int beginRow, int endRow, int beginCol, int endCol) {
		// 开始行,结束行,开始列,结束列,都是从0开始
		CellRangeAddressList addressList = new CellRangeAddressList(beginRow, endRow, beginCol, endCol);
		DataValidationHelper helper = sheet.getDataValidationHelper();
		DataValidationConstraint constraint = helper.createExplicitListConstraint(datas);
		DataValidation dataValidation = helper.createValidation(constraint, addressList);
		sheet.addValidationData(dataValidation);
	}
}