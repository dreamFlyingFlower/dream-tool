package com.wy.qrcode;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.wy.digest.DigestTool;
import com.wy.io.file.FileTool;
import com.wy.lang.StrTool;

/**
 * 二维码生成 利用google的zxing.jar包生成,
 * jar包下载地址:https://oss.sonatype.org/content/repositories/releases/com/google/zxing/core/3.2.1/
 * githup地址:https://github.com/zxing/zxing/tree/zxing-3.0.0
 */
public class QREncode {

	private static final int WHITE = 0xFF000000;

	private static final int BLACK = 0xFFFFFFFF;

	private static final String IMG_SUFFIX = "JPG";// 二维码图片后缀

	private static final String IMG_DES = "%s.jpg";// 生成的二维码图片名字

	private static final int QRCODE_SIZE = 300; // 二维码尺寸

	private static final int WIDTH = 60; // logo宽度

	private static final int HEIGHT = 60; // logo高度

	// FRONT_COLOR:二维码前景色,0x000000 表示黑色
	private static final int FRONT_COLOR = 0x000000;

	// BACKGROUND_COLOR:二维码背景色,0xFFFFFF 表示白色
	// 演示用 16 进制表示,和前端页面 CSS 的取色是一样的,注意前后景颜色应该对比明显,如常见的黑白
	private static final int BACKGROUND_COLOR = 0xFFFFFF;

	/**
	 * 生成二维码
	 * 
	 * @param content:二维码内容
	 * @param desPath:生成的二维码存放地址
	 */
	public static void encode(String content, String desPath) {
		encode(content, desPath, false);
	}

	public static void encode(String content, String desPath, boolean isCompress) {
		encode(content, desPath, null, isCompress);
	}

	public static void encode(String content, String desPath, String imgPath) {
		encode(content, desPath, imgPath, false);
	}

	/**
	 * 生成二维码
	 * 
	 * @param content:二维码内容
	 * @param desPath:二维码图片生成地址
	 * @param imgPath:需要插入到二维码图片中的图片地址
	 * @param isCompress:是否需要压缩,true需要
	 */
	public static void encode(String content, String desPath, String imgPath, boolean isCompress) {
		try {
			BufferedImage image = createImage(content, imgPath, isCompress);
			File file = new File(desPath);
			FileTool.checkDirectory(file);
			ImageIO.write(image, IMG_SUFFIX,
					new File(desPath + File.separator + String.format(IMG_DES, DigestTool.uuid())));
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public static void encodeIO(String content, OutputStream output) {
		encodeIO(content, output, false);
	}

	public static void encodeIO(String content, OutputStream output, boolean isCompress) {
		encodeIO(content, null, output, isCompress);
	}

	public static void encodeIO(String content, String imgPath, OutputStream output) {
		encodeIO(content, imgPath, output, false);
	}

	public static void encodeIO(String content, String imgPath, OutputStream output, boolean isCompress) {
		try {
			BufferedImage image = createImage(content, imgPath, isCompress);
			ImageIO.write(image, IMG_SUFFIX, output);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * 生成二维码图片的流
	 */
	public static BufferedImage createImage(String content, String imgPath, boolean isCompress) {
		try {
			Map<EncodeHintType, Object> map = new HashMap<>();
			map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			// 设置字符集
			map.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.toString());
			// 设置边框
			map.put(EncodeHintType.MARGIN, 1);
			BitMatrix bitMatrix =
					new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, map);
			int width = bitMatrix.getWidth();
			int height = bitMatrix.getHeight();
			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					bi.setRGB(x, y, bitMatrix.get(x, y) ? WHITE : BLACK);
				}
			}
			// 是否插入图片插入图片
			if (StrTool.isBlank(imgPath)) {
				return bi;
			} else {
				insertImage(bi, imgPath, isCompress);
			}
			return bi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void createCodeToFile(String content, File codeImgFileSaveDir, String fileName) {
		try {
			if (StrTool.isBlank(content) || StrTool.isBlank(fileName)) {
				return;
			}
			content = content.trim();
			if (codeImgFileSaveDir == null || codeImgFileSaveDir.isFile()) {
				// 二维码图片存在目录为空,默认放在桌面...
				codeImgFileSaveDir = FileSystemView.getFileSystemView().getHomeDirectory();
			}
			if (!codeImgFileSaveDir.exists()) {
				// 二维码图片存在目录不存在,开始创建...
				codeImgFileSaveDir.mkdirs();
			}

			// 核心代码-生成二维码
			BufferedImage bufferedImage = getBufferedImage(content);

			File codeImgFile = new File(codeImgFileSaveDir, fileName);
			ImageIO.write(bufferedImage, "png", codeImgFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成二维码并输出到输出流, 通常用于输出到网页上进行显示，输出到网页与输出到磁盘上的文件中，区别在于最后一句 ImageIO.write
	 * write(RenderedImage im,String formatName,File output)：写到文件中
	 * write(RenderedImage im,String formatName,OutputStream output)：输出到输出流中
	 * 
	 * @param content ：二维码内容
	 * @param outputStream ：输出流，比如 HttpServletResponse 的 getOutputStream
	 */
	public static void createCodeToOutputStream(String content, OutputStream outputStream) {
		try {
			if (StrTool.isBlank(content)) {
				return;
			}
			content = content.trim();
			// 核心代码-生成二维码
			BufferedImage bufferedImage = getBufferedImage(content);

			// 区别就是这一句，输出到输出流中，如果第三个参数是 File，则输出到文件中
			ImageIO.write(bufferedImage, "png", outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成二维码
	 * 
	 * @param content
	 * @return
	 * @throws WriterException
	 */
	private static BufferedImage getBufferedImage(String content) throws WriterException {
		// com.google.zxing.EncodeHintType：编码提示类型,枚举类型
		Map<EncodeHintType, Object> hints = new HashMap<>();

		// EncodeHintType.CHARACTER_SET：设置字符编码类型
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		// EncodeHintType.ERROR_CORRECTION：设置误差校正
		// ErrorCorrectionLevel：误差校正等级，L = ~7% correction、M = ~15% correction、Q = ~25%
		// correction、H = ~30% correction
		// 不设置时，默认为 L 等级，等级不一样，生成的图案不同，但扫描的结果是一样的
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

		// EncodeHintType.MARGIN：设置二维码边距，单位像素，值越小，二维码距离四周越近
		hints.put(EncodeHintType.MARGIN, 1);

		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
		BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? FRONT_COLOR : BACKGROUND_COLOR);
			}
		}
		return bufferedImage;
	}

	/**
	 * 在生成的二维码上插入图片
	 */
	public static void insertImage(BufferedImage bi, String imgPath, boolean isCompress) {
		try {
			File file = new File(imgPath);
			if (!file.exists()) {
				throw new Exception(imgPath + "该文件不存在");
			}
			Image src = ImageIO.read(file);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			if (isCompress) {
				if (width > WIDTH) {
					width = WIDTH;
				}
				if (height > HEIGHT) {
					height = HEIGHT;
				}
				Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
				BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(image, 0, 0, null); // 绘制缩小后的图
				g.dispose();
				src = image;
			}
			// 插入LOGO
			Graphics2D graph = bi.createGraphics();
			int x = (QRCODE_SIZE - width) / 2;
			int y = (QRCODE_SIZE - height) / 2;
			graph.drawImage(src, x, y, width, height, null);
			Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
			graph.setStroke(new BasicStroke(3f));
			graph.draw(shape);
			graph.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String decode(String path) {
		return decode(new File(path));
	}

	public static String decode(File file) {
		try {
			BufferedImage image = ImageIO.read(file);
			if (image == null) {
				return null;
			}
			QRDecode source = new QRDecode(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Map<DecodeHintType, String> hints = new HashMap<>();
			hints.put(DecodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.toString());
			Result result = new MultiFormatReader().decode(bitmap, hints);
			return result.getText();
		} catch (Exception e) {
			e.getMessage();
		}
		return null;
	}
}
