package com.wy.excel;

import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * CSV工具类
 *
 * @author 飞花梦影
 * @date 2022-08-14 17:33:54
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class CsvTools {

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 使用csv文件导出百万数据
	 * 
	 * @param response
	 * @throws Exception
	 */
	public void downLoadCSV(HttpServletResponse response) throws Exception {
		ServletOutputStream outputStream = response.getOutputStream();
		String filename = "百万用户数据的导出.csv";
		response.setHeader("content-disposition",
		        "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));
		response.setContentType("text/csv");
		CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outputStream, "utf-8"));
		// 写入了小标题数据
		String[] titles = new String[] { "编号", "姓名", "手机号", "入职日期", "现住址" };
		csvWriter.writeNext(titles);

		while (true) {
			// 该数据从数据库从取
			List<Map<String, Object>> datas = new ArrayList<>();
			if (CollectionUtils.isEmpty(datas)) {
				break;
			}
			for (Map<String, Object> data : datas) {
				csvWriter.writeNext(new String[] { data.get("userId").toString(), data.get("username").toString(),
				        data.get("phone").toString(), simpleDateFormat.format(data.get("hireDate").toString()),
				        data.get("address").toString() });
			}
			csvWriter.flush();
		}
		csvWriter.close();
	}

	/**
	 * 读取百万级数据的csv文件
	 * 
	 * @param filePath CSV文件地址
	 * @throws Exception
	 */
	public static void read(String filePath) throws Exception {
		CSVReader csvReader = new CSVReader(new FileReader(filePath));
		// 读取到第一行 是小标题
		String[] titles = csvReader.readNext();
		System.out.println(titles);
		// "编号","姓名","手机号","入职日期","现住址"
		while (true) {
			String[] content = csvReader.readNext();
			if (content == null) {
				break;
			}
			System.out.println(Long.parseLong(content[0]));
			System.out.println(content[1]);
			System.out.println(content[2]);
			System.out.println(simpleDateFormat.parse(content[3]));
			System.out.println(content[4]);
		}
	}
}