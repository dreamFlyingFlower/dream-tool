package com.wy;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Word,Excel等Office办公软件工具类
 * 
 * {@link Workbook}:操作Excel的主要接口<br>
 * {@link HSSFWorkbook}:Workbook的实现,只能操作Excel2003以前的版本,文件后缀为xls
 * {@link XSSFWorkbook}:Workbook的实现,只能操作Excel2007以后的版本,文件后缀是xlsx
 * {@link SXSSFWorkbook}:该类在数据量极大时使用,主要用来写数据,只能操作Excel2007之后的版本,因Excel2003版本每页最多65535行.
 * 该类只能操作流,不能使用模版,只能用空的Excel文件承载数据.对内存消耗过大,需要在每次写数据时睡10毫秒,允许jvm垃圾回收.
 * 
 * @author 飞花梦影
 * @date 2021-03-30 14:36:21
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ConstOffice {

	/** xls每页sheet中的最大行数 */
	int EXCEL_SHEET_MAX_ROW = 65535;

	/** xls每页sheet中的最大列数 */
	int EXCEL_SHEET_MAX_COL = 256;

	/** xlsx每个sheet页中的最大行 */
	int EXCEL_SHEET_MAX_ROW_XLSX = 1048576;

	/** xlsx每个sheet页中的最大列数 */
	int EXCEL_SHEET_MAX_COL_XLSX = 16384;

	/** 默认导出文件的名称 */
	String EXCEL_DEFAULT_FILE_NAME = "数据导出";

	/** excel文件的默认后缀,为了兼容低版本,使用xls */
	String EXCEL_DEFAULT_FILE_NAME_SUFFIX = "xls";

	/** xls文件后缀 */
	String EXCEL_SUFFIX_XLS = "xls";

	/** xlsx文件后缀 */
	String EXCEL_SUFFIX_XLSX = "xlsx";
}