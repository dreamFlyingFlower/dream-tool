package com.wy.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Random;

import javax.imageio.ImageIO;

import com.wy.result.ResultException;

/**
 * Image工具类 FIXME
 * 
 * @author 飞花梦影
 * @date 2021-03-08 09:02:51
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ImageTool {

	/**
	 * 生成缩略图
	 * 
	 * @param originalFile 源文件
	 * @param thumbnailFile 缩略图文件
	 * @param thumbWidth 缩略图宽度
	 * @param thumbHeight 缩略图高度
	 */
	public static void transform(String originalFile, String thumbnailFile, int thumbWidth, int thumbHeight) {
		Image image;
		try {
			image = ImageIO.read(new File(originalFile));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResultException(e);
		}
		double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;
		if (thumbRatio < imageRatio) {
			thumbHeight = (int) (thumbWidth / imageRatio);
		} else {
			thumbWidth = (int) (thumbHeight * imageRatio);
		}

		if (imageWidth < thumbWidth && imageHeight < thumbHeight) {
			thumbWidth = imageWidth;
			thumbHeight = imageHeight;
		} else if (imageWidth < thumbWidth) {
			thumbWidth = imageWidth;
		} else if (imageHeight < thumbHeight) {
			thumbHeight = imageHeight;
		}

		BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setBackground(Color.WHITE);
		graphics2D.setPaint(Color.WHITE);
		graphics2D.fillRect(0, 0, thumbWidth, thumbHeight);
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
		try {
			ImageIO.write(thumbImage, "JPG", new File(thumbnailFile));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResultException(e);
		}
	}

	/**
	 * 将图片文件转成base64
	 */
	public static String getBase64Image(String path) {
		byte[] b = null;
		try (InputStream is = new FileInputStream(path);) {
			b = new byte[is.available()];
			is.read(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Base64.getEncoder().encodeToString(b);
	}

	/**
	 * 对字节数组字符串进行Base64解码并生成图片
	 */
	public static boolean generateImage(String imgStr, String imgFilePath) {
		if (imgStr == null) {
			return false;
		}
		try (OutputStream out = new FileOutputStream(imgFilePath);) {
			// Base64解码
			byte[] bytes = Base64.getDecoder().decode(imgStr);
			for (int i = 0; i < bytes.length; ++i) {
				// 调整异常数据
				if (bytes[i] < 0) {
					bytes[i] += 256;
				}
			}
			out.write(bytes);
			out.flush();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	

	/**
	 * @param waterFile 水印文件路径,可是图片,文字
	 * @param srcImg 需要进行水印处理的图片
	 * @param desImg 处理完成后生成的图片地址
	 * @param x 水印在目标图片的坐标x
	 * @param y 水印在目标图片的坐标y
	 * @return 处理后图片
	 */
	public static boolean waterMark(String waterFile, String srcImg, String desImg, int x, int y) {
		String lowerImage = waterFile.toLowerCase();
		if (lowerImage.endsWith(".png") || lowerImage.endsWith(".jpg") || lowerImage.endsWith(".jpeg")) {
			try (FileOutputStream fos = new FileOutputStream(desImg);){
				// 转换成图片对象
				Image image = ImageIO.read(new File(waterFile));
				// 获得图片对象的宽高
				int width = image.getWidth(null);
				int height = image.getHeight(null);
				// 加载目标文件
				File srcFile = new File(srcImg);
				Image target = ImageIO.read(srcFile);
				int targetWidth = target.getWidth(null);
				int targetHeight = target.getHeight(null);
				// 创建一块画板,画板的宽高,三原色
				BufferedImage bi = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
				// 创建画笔
				Graphics2D g2d = bi.createGraphics();
				// 绘制底图,底图,从x,y坐标是开始画,宽高
				g2d.drawImage(target, 0, 0, targetWidth, targetHeight, null);
				// 绘制水印
				g2d.drawImage(image, x, y, width, height, null);
				// 结束绘制
				g2d.dispose();
				// 输出新图片,将生成的图片缓冲流放入到文件输出流中
				ImageIO.write(bi, "jpeg", fos);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return false;
		}
		return false;
	}

	public static BufferedImage obtainImage(int width, int height, String code) {
		width = width <= 0 ? 120 : width;
		height = height <= 0 ? 35 : height;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		for (int i = 0; i < code.length(); i++) {
			char rand = code.charAt(i);
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(String.valueOf(rand), width * i / code.length() + 6, height * 3 / 4);
		}
		g.dispose();
		return image;
	}

	/**
	 * 生成验证码
	 * 
	 * @param os 验证码生成时输出的流
	 * @param width 验证码生成的宽度,默认120px
	 * @param height 验证码生成的高度,默认35px
	 * @param length 验证码的长度,默认4位
	 * @return 生成的验证码,若返回null表示验证码生成失败
	 */
	public static String obtainImage(OutputStream os, int width, int height, String code) {
		BufferedImage image = obtainImage(width, height, code);
		try {
			ImageIO.write(image, "jpeg", os);
			return code;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成随机背景条纹
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	private static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}