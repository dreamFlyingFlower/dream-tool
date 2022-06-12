package com.wy.pdf;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * 使用JasperReport进行PDF操作,需要使用专用的工具进行PDF设计
 *
 * @author 飞花梦影
 * @date 2022-06-11 10:01:09
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class JasperReportTools {

	/**
	 * 将数据渲染到PDF输出到页面进行预览
	 * 
	 * @param request 请求
	 * @param response 响应,需要自行关闭流或刷新
	 * @param japserTemplatePath jasper模板地址
	 */
	public static void createPdf(HttpServletRequest request, HttpServletResponse response, String japserTemplatePath) {
		createPdf(request, response, japserTemplatePath, new HashMap<>());
	}

	/**
	 * 将Map参数数据渲染到PDF输出到页面进行预览
	 * 
	 * @param request 请求
	 * @param response 响应,需要自行关闭流或刷新
	 * @param japserTemplatePath jasper模板地址
	 * @param params Map参数
	 */
	public static void createPdf(HttpServletRequest request, HttpServletResponse response, String japserTemplatePath,
			Map<String, Object> params) {
		// 引入jasper文件,需要用Jaspersoft Studio进行设计
		try (FileInputStream fileInputStream = new FileInputStream(japserTemplatePath);) {
			// 创建JasperPrint,向jasper文件中填充数据.最后一个参数为数据源,即使没有也必须填,否则数据无法进行渲染
			// JREmptyDataSource:空数据源,即没有数据源.还有Map,Bean,数据库等
			JasperPrint jasperPrint = JasperFillManager.fillReport(fileInputStream, params, new JREmptyDataSource());
			// 将JasperPrint以PDF的形式输出
			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
		} catch (IOException | JRException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将实体类数据渲染到PDF输出到页面进行预览
	 * 
	 * @param request 请求
	 * @param response 响应,需要自行关闭流或刷新
	 * @param japserTemplatePath jasper模板地址
	 * @param entitys 实体类参数
	 */
	public static <T> void createPdf(HttpServletRequest request, HttpServletResponse response,
			String japserTemplatePath, List<T> entitys) {
		createPdf(request, response, japserTemplatePath, new HashMap<>(), entitys);
	}

	/**
	 * 将实体类以及map数据数据渲染到PDF输出到页面进行预览
	 * 
	 * @param request 请求
	 * @param response 响应,需要自行关闭流或刷新
	 * @param japserTemplatePath jasper模板地址
	 * @param params Map参数
	 * @param entitys 实体类参数
	 */
	public static <T> void createPdf(HttpServletRequest request, HttpServletResponse response,
			String japserTemplatePath, Map<String, Object> params, List<T> entitys) {
		// 引入jasper文件,需要用Jaspersoft Studio进行设计
		try (FileInputStream fileInputStream = new FileInputStream(japserTemplatePath);) {
			// 创建JasperPrint,向jasper文件中填充数据.最后一个参数为数据源,即使没有也必须填,否则数据无法进行渲染
			// JREmptyDataSource:空数据源,即没有数据源.还有Map,Bean,数据库等
			JasperPrint jasperPrint =
					JasperFillManager.fillReport(fileInputStream, params, new JRBeanCollectionDataSource(entitys));
			// 将JasperPrint以PDF的形式输出
			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
		} catch (IOException | JRException e) {
			e.printStackTrace();
		}
	}
}