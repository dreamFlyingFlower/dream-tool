package com.wy.word;

import java.awt.Font;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.wy.io.file.FileTool;
import com.wy.lang.AssertTool;
import com.wy.util.ArrayTool;

/**
 * Word生成器
 *
 * @author 飞花梦影
 * @date 2021-03-06 00:24:39
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class Word07Writer implements Closeable {

	private final XWPFDocument doc;

	/**
	 * 目标文件
	 */
	protected File destFile;

	/**
	 * 是否被关闭
	 */
	protected boolean isClosed;

	public Word07Writer() {
		this(new XWPFDocument());
	}

	/**
	 * 构造
	 *
	 * @param destFile 写出的文件
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public Word07Writer(File destFile) throws InvalidFormatException, IOException {
		this(DocTools.create(destFile), destFile);
	}

	/**
	 * 构造
	 *
	 * @param doc {@link XWPFDocument}
	 */
	public Word07Writer(XWPFDocument doc) {
		this(doc, null);
	}

	/**
	 * 构造
	 *
	 * @param doc {@link XWPFDocument}
	 * @param destFile 写出的文件
	 */
	public Word07Writer(XWPFDocument doc, File destFile) {
		this.doc = doc;
		this.destFile = destFile;
	}

	// -------------------------------------------------------------------------- Constructor end

	/**
	 * 获取{@link XWPFDocument}
	 *
	 * @return {@link XWPFDocument}
	 */
	public XWPFDocument getDoc() {
		return this.doc;
	}

	/**
	 * 设置写出的目标文件
	 *
	 * @param destFile 目标文件
	 * @return this
	 */
	public Word07Writer setDestFile(File destFile) {
		this.destFile = destFile;
		return this;
	}

	/**
	 * 增加一个段落
	 *
	 * @param font 字体信息{@link Font}
	 * @param texts 段落中的文本，支持多个文本作为一个段落
	 * @return this
	 */
	public Word07Writer addText(Font font, String... texts) {
		return addText(null, font, texts);
	}

	/**
	 * 增加一个段落
	 *
	 * @param align 段落对齐方式{@link ParagraphAlignment}
	 * @param font 字体信息{@link Font}
	 * @param texts 段落中的文本，支持多个文本作为一个段落
	 * @return this
	 */
	public Word07Writer addText(ParagraphAlignment align, Font font, String... texts) {
		final XWPFParagraph p = this.doc.createParagraph();
		if (null != align) {
			p.setAlignment(align);
		}
		if (ArrayTool.isNotEmpty(texts)) {
			XWPFRun run;
			for (String text : texts) {
				run = p.createRun();
				run.setText(text);
				if (null != font) {
					run.setFontFamily(font.getFamily());
					run.setFontSize(font.getSize());
					run.setBold(font.isBold());
					run.setItalic(font.isItalic());
				}
			}
		}
		return this;
	}

	/**
	 * 增加表格数据
	 *
	 * @param data 表格数据，多行数据。元素表示一行数据，当为集合或者数组时，为一行；当为Map或者Bean时key表示标题，values为数据
	 * @return this
	 * @since 4.5.16
	 */
	public Word07Writer addTable(Iterable<?> data) {
		TableTools.createTable(this.doc, data);
		return this;
	}

	/**
	 * 增加图片，单独成段落
	 *
	 * @param picFile 图片文件
	 * @param width 宽度
	 * @param height 高度
	 * @return this
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public Word07Writer addPicture(File picFile, int width, int height) throws InvalidFormatException, IOException {
		final String fileName = picFile.getName();
		final String extName = FileTool.getFileExtension(fileName).toUpperCase();
		PicType picType;
		try {
			picType = PicType.valueOf(extName);
		} catch (IllegalArgumentException e) {
			// 默认值
			picType = PicType.JPEG;
		}
		return addPicture(FileTool.newInputStream(picFile), picType, fileName, width, height);
	}

	/**
	 * 增加图片，单独成段落，增加后图片流关闭，默认居中对齐
	 *
	 * @param in 图片流
	 * @param picType 图片类型，见Document.PICTURE_TYPE_XXX
	 * @param fileName 文件名
	 * @param width 宽度
	 * @param height 高度
	 * @return this
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public Word07Writer addPicture(InputStream in, PicType picType, String fileName, int width, int height)
			throws InvalidFormatException, IOException {
		return addPicture(in, picType, fileName, width, height, ParagraphAlignment.CENTER);
	}

	/**
	 * 增加图片，单独成段落，增加后图片流关闭
	 *
	 * @param in 图片流
	 * @param picType 图片类型，见Document.PICTURE_TYPE_XXX
	 * @param fileName 文件名
	 * @param width 宽度
	 * @param height 高度
	 * @param align 图片的对齐方式
	 * @return this
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public Word07Writer addPicture(InputStream in, PicType picType, String fileName, int width, int height,
			ParagraphAlignment align) throws InvalidFormatException, IOException {
		final XWPFParagraph paragraph = doc.createParagraph();
		paragraph.setAlignment(align);
		final XWPFRun run = paragraph.createRun();
		run.addPicture(in, picType.getValue(), fileName, Units.toEMU(width), Units.toEMU(height));
		return this;
	}

	/**
	 * 将Excel Workbook刷出到预定义的文件<br>
	 * 如果用户未自定义输出的文件，将抛出{@link NullPointerException}<br>
	 * 预定义文件可以通过{@link #setDestFile(File)} 方法预定义，或者通过构造定义
	 *
	 * @return this
	 * @throws IOException
	 */
	public Word07Writer flush() throws IOException {
		return flush(this.destFile);
	}

	/**
	 * 将Excel Workbook刷出到文件<br>
	 * 如果用户未自定义输出的文件，将抛出{@link NullPointerException}
	 *
	 * @param destFile 写出到的文件
	 * @return this
	 * @throws IOException
	 */
	public Word07Writer flush(File destFile) throws IOException {
		AssertTool.notNull(destFile,
				"[destFile] is null, and you must call setDestFile(File) first or call flush(OutputStream).");
		return flush(FileTool.newOutputStream(destFile), true);
	}

	/**
	 * 将Word Workbook刷出到输出流
	 *
	 * @param out 输出流
	 * @return this
	 * @throws IOException
	 * @throws IORuntimeException IO异常
	 */
	public Word07Writer flush(OutputStream out) throws IOException {
		return flush(out, false);
	}

	/**
	 * 将Word Document刷出到输出流
	 *
	 * @param out 输出流
	 * @param isCloseOut 是否关闭输出流
	 * @return this
	 * @throws IOException
	 * @throws IORuntimeException IO异常
	 */
	public Word07Writer flush(OutputStream out, boolean isCloseOut) throws IOException {
		AssertTool.isTrue(!this.isClosed, "WordWriter has been closed!");
		this.doc.write(out);
		out.flush();
		return this;
	}

	/**
	 * 关闭Word文档<br>
	 * 如果用户设定了目标文件，先写出目标文件后给关闭工作簿
	 * 
	 * @throws IOException
	 */
	@Override
	public void close() throws IOException {
		if (null != this.destFile) {
			flush();
		}
		closeWithoutFlush();
	}

	/**
	 * 关闭Word文档但是不写出
	 * 
	 * @throws IOException
	 */
	protected void closeWithoutFlush() throws IOException {
		this.doc.close();
		this.isClosed = true;
	}
}