package com.wy.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.wy.bean.BeanTool;

/**
 * PDF工具类
 * 
 * @author 飞花梦影
 * @date 2021-02-22 16:28:59
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class PdfTools {
		// 标准A4的宽
		private static final Integer A4_WEIGHT = 595 - 60;

		// 标准A4的高
		private static final Integer A4_HEIGHT = 842 - 60;
	public <T> String exportPdf(T t, HttpServletResponse response) throws IOException, DocumentException {
		// 指定解析器
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
				"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
		String filename = "测试.pdf";
		String path = "e:/";
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(filename, "UTF-8"));
		PdfStamper ps = null;
		PdfReader reader = null;
		try (OutputStream os = response.getOutputStream();) {
			// 读入pdf表单
			reader = new PdfReader(path + File.separator + filename);
			// 根据表单生成一个新的pdf
			ps = new PdfStamper(reader, os);
			// 获取pdf表单
			AcroFields form = ps.getAcroFields();
			// 给表单添加中文字体,这里采用系统字体,不设置的话,中文可能无法显示
			BaseFont bf = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			form.addSubstitutionFont(bf);
			Map<String, Object> data = BeanTool.beanToMap(t);
			for (String key : data.keySet()) {
				form.setField(key, data.get(key).toString());
			}
			ps.setFormFlattening(true);
		} finally {
			if (null != ps) {
				ps.close();
			}
			if (null != reader) {
				reader.close();
			}
		}
		return null;
	}/**
	 * 将图片转成pdf,需要用到itextpdf.jar
	 * 
	 * @param filePath 当前文件路径
	 * @param fileName 文件名称
	 */
	public static final void img2Pdf(String filePath, String fileName) {
		File pdfFile = null;
		Document document = null;
		try {
			String pdfPath = filePath + File.separator + fileName + ".pdf";
			pdfFile = new File(pdfPath);
			if (pdfFile.exists()) {
				return;
			}
			// 第一步:创建一个document对象。
			document = new Document();
			document.setMargins(0, 0, 0, 0);
			// 第二步:创建一个PdfWriter实例
			PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
			// 第三步:打开文档。
			document.open();
			// 第四步:在文档中增加图片。
			File files = new File(filePath);
			String[] images = files.list();
			Arrays.sort(images);
			int len = images.length;
			for (int i = 0; i < len; i++) {
				if (images[i].toLowerCase().endsWith(".bmp") || images[i].toLowerCase().endsWith(".jpg")
						|| images[i].toLowerCase().endsWith(".jpeg") || images[i].toLowerCase().endsWith(".gif")
						|| images[i].toLowerCase().endsWith(".png")) {
					com.itextpdf.text.Image img = com.itextpdf.text.Image
							.getInstance(filePath + File.separator + images[i]);
					img.setAlignment(com.itextpdf.text.Image.ALIGN_CENTER);
					// 根据图片大小设置页面,一定要先设置页面,再newPage(),否则无效
					if (img.getWidth() > 1440) {
						document.setPageSize(new Rectangle(A4_WEIGHT, A4_HEIGHT));
					} else {
						document.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
					}
					document.newPage();
					document.add(img);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭文档
			if (document != null) {
				document.close();
			}
		}
	}
}