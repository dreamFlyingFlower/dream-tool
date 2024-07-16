package dream.flying.flower.helper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Random;

import javax.imageio.ImageIO;

import dream.flying.flower.binary.Base64Helper;
import dream.flying.flower.io.file.FileNameHelper;
import dream.flying.flower.result.ResultException;

/**
 * Image工具类 FIXME
 * 
 * @author 飞花梦影
 * @date 2021-03-08 09:02:51
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class ImageHelper {

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

	public static String encodeImage(BufferedImage bufferedImage) {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", stream);
			String b64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(stream.toByteArray());
			stream.close();
			return b64Image;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String encodeImage(byte[] byteImage) {
		return "data:image/png;base64," + Base64.getEncoder().encodeToString(byteImage);
	}

	/**
	 * 将图片文件转成base64
	 * 
	 * @param path 文件路径
	 * @return Base64字符串
	 */
	public static String fileToBase64(String path) {
		try (InputStream is = new FileInputStream(path);) {
			byte[] b = new byte[is.available()];
			is.read(b);
			return Base64.getEncoder().encodeToString(b);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResultException("生成Base64图片失败:" + e.getMessage());
		}
	}

	/**
	 * base64 Code decode String save to targetPath.
	 * 
	 * @param base64Code String
	 * @param targetPath String
	 * @throws Exception e
	 */
	public static void decodeBase64ToFile(String base64Code, String targetPath) throws Exception {
		byte[] buffer = Base64Helper.decode(base64Code);
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
	}

	/**
	 * base64 code save to file.
	 * 
	 * @param base64Code String
	 * @param targetPath String
	 * @throws Exception e
	 */
	public static void base64ToFile(String base64Code, String targetPath) throws Exception {
		byte[] buffer = base64Code.getBytes();
		FileOutputStream out = new FileOutputStream(targetPath);
		out.write(buffer);
		out.close();
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
	 * 给图片加水印
	 * 
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
			try (FileOutputStream fos = new FileOutputStream(desImg);) {
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

	/**
	 * 生成指定大小的图片,并将指定内容写到图片中
	 * 
	 * @param width 图片的宽
	 * @param height 图片的高
	 * @param code 图片内容
	 * @return 图片
	 */
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

	/**
	 * 转换图片操作
	 * 
	 * @param outputFile 输出文件
	 * @param inputFile 输入文件
	 * @param width 要压缩的宽度
	 * @param height 要压缩的高度
	 * @param proportion 是否进行等比例压缩
	 * @throws IOException
	 */
	public static String compressImage(File outputFile, File inputFile, int width, int height, boolean proportion) {
		try (InputStream is = new FileInputStream(inputFile); OutputStream os = new FileOutputStream(outputFile);) {
			compressImage(os, is, width, height, proportion);
			return outputFile.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 转换图片操作
	 * 
	 * @param outputFile 输出文件
	 * @param is 输入流
	 * @param width 要压缩的宽度
	 * @param height 要压缩的高度
	 * @param proportion 是否进行等比例压缩
	 * @throws IOException
	 */
	public static String compressImage(File outputFile, InputStream is, int width, int height, boolean proportion) {
		try (OutputStream os = new FileOutputStream(outputFile);) {
			compressImage(os, is, width, height, proportion);
			return outputFile.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 转换图片操作,需要调用者手动关闭流
	 * 
	 * @param os 要转换图片的输出流
	 * @param is 要转换图片的输入流
	 * @param width 要压缩的宽度
	 * @param height 要压缩的高度
	 * @param proportion 是否进行等比例压缩
	 * @throws IOException
	 */
	public static void compressImage(OutputStream os, InputStream is, int width, int height, boolean proportion) {
		compressImage(os, is, width, height, proportion, false);
	}

	/**
	 * 转换图片操作,需要调用者手动关闭流
	 * 
	 * @param os 要转换图片的输出流
	 * @param is 要转换图片的输入流
	 * @param width 要压缩的宽度
	 * @param height 要压缩的高度
	 * @param proportion 是否进行等比例压缩
	 * @param magnify 是否进行放大
	 * @throws IOException
	 */
	public static void compressImage(OutputStream os, InputStream is, int width, int height, boolean proportion,
			boolean magnify) {
		try {
			BufferedImage img = ImageIO.read(is);
			int newWidth;
			int newHeight;
			int oldWidth = img.getWidth(null);
			int oldHeight = img.getHeight(null);
			boolean isWrite = false;
			if (!magnify) {
				boolean iw = width >= height ? true : false;
				if (iw) {
					if (width > oldWidth)
						isWrite = true;
				} else {
					if (height > oldHeight)
						isWrite = true;
				}
				if (isWrite) {
					ImageIO.write(img, "jpg", os);
					os.flush();
				}
			}
			if (!isWrite) {
				// 判断是否是等比缩放
				if (proportion) {
					// 为等比缩放计算输出的图片宽度及高度
					double rate1 = ((double) oldWidth) / (double) width + 0.1;
					double rate2 = ((double) oldHeight) / (double) height + 0.1;
					System.out.println((rate1 + "," + rate2));
					// 根据缩放比率大的进行缩放控制
					double rate = rate1 < rate2 ? rate1 : rate2;
					newWidth = (int) (((double) img.getWidth(null)) / rate);
					newHeight = (int) (((double) img.getHeight(null)) / rate);
				} else {
					newWidth = width;
					newHeight = height;
				}
				BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);
				tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
				tag.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 转换图片操作
	 * 
	 * @param oPath 输出路径
	 * @param is 输入流
	 * @param width 要压缩的宽度
	 * @param height 要压缩的高度
	 * @param proportion 是否进行等比例压缩
	 * @throws IOException
	 */
	public static String compressImage(String oPath, InputStream is, int width, int height, boolean proportion) {
		try (OutputStream os = new FileOutputStream(oPath);) {
			compressImage(os, is, width, height, proportion);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return oPath;
	}

	/**
	 * 压缩图片
	 * 
	 * @param filePath 源图片地址
	 * @param destPath 压缩后的图片地址
	 * @param proportion 是否等比压缩.true->是,false->否
	 * @return 压缩是否成功.true->是,false->否
	 */
	public static boolean compressImage(String filePath, String destPath, boolean proportion) {
		// 获得源文件
		File file = new File(filePath);
		if (!file.exists()) {
			throw new ResultException("the file [%s] is not exist", filePath);
		}
		try {
			Image img = ImageIO.read(file);
			// 判断图片格式是否正确
			if (img.getWidth(null) == -1) {
				throw new ResultException("the format can't read,retry!");
			} else {
				int newWidth;
				int newHeight;
				// 判断是否是等比缩放
				if (proportion) {
					// 为等比缩放计算输出的图片宽度及高度
					double rate1 = ((double) img.getWidth(null)) / (double) 100 + 0.1;
					double rate2 = ((double) img.getHeight(null)) / (double) 100 + 0.1;
					// 根据缩放比率大的进行缩放控制
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (((double) img.getWidth(null)) / rate);
					newHeight = (int) (((double) img.getHeight(null)) / rate);
				} else {
					// 默认输出的图片宽高
					newWidth = 100;
					newHeight = 100;
				}
				BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);
				// Image.SCALE_SMOOTH缩略算法,生成缩略图片的平滑度的优先级比速度高,生成的图片质量比较好,但速度慢
				tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
				tag.flush();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return true;
	}

	/**
	 * 转换图片操作
	 * 
	 * @param oPath 输出路径
	 * @param iPath 输入路径
	 * @param width 要压缩的宽度
	 * @param height 要压缩的高度
	 * @param proportion 是否进行等比例压缩
	 * @throws IOException
	 */
	public static String compressImage(String oPath, String iPath, int width, int height, boolean proportion) {
		try (InputStream is = new FileInputStream(iPath); OutputStream os = new FileOutputStream(oPath);) {
			compressImage(os, is, width, height, proportion);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return oPath;
	}

	/**
	 * 切割图片,需要调用者手动关闭流
	 * 
	 * @param os 切割后的输出流
	 * @param is 输入流
	 * @param type 文件的图片类型
	 * @param x x坐标
	 * @param y y坐标
	 * @param width 宽度
	 * @param height 高度
	 */
	public void cropImg(OutputStream os, InputStream is, String type, int x, int y, int width, int height) {
		try {
			BufferedImage imgBuf = ImageIO.read(is);
			ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
			Image img =
					Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(imgBuf.getSource(), cropFilter));
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(img, 0, 0, null);
			ImageIO.write(tag, type, os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 切割图片
	 * 
	 * @param os 切割后的输出流
	 * @param is 输入流
	 * @param type 文件的图片类型
	 * @param x x坐标
	 * @param y y坐标
	 * @param width 宽度
	 * @param height 高度
	 */
	public String cropImg(String oPath, InputStream is, int x, int y, int width, int height) {
		try (OutputStream os = new FileOutputStream(oPath);) {
			cropImg(os, is, FileNameHelper.getExtension(oPath), x, y, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 切割图片自动根据输入的文件名转换为xxx_small.type
	 * 
	 * @param filename 输入文件名
	 * @param type 文件的图片类型
	 * @param x x坐标
	 * @param y y坐标
	 * @param width 宽度
	 * @param height 高度
	 */
	public String cropImg(String filename, int x, int y, int width, int height) {
		String sname = generatorSmallFileName(filename);
		try (InputStream is = new FileInputStream(filename); OutputStream os = new FileOutputStream(sname);) {
			cropImg(os, is, FileNameHelper.getExtension(filename), x, y, width, height);
			return sname;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 切割图片
	 * 
	 * @param oPath 输出文件
	 * @param iPath 输入文件名
	 * @param type 文件的图片类型
	 * @param x x坐标
	 * @param y y坐标
	 * @param width 宽度
	 * @param height 高度
	 */
	public String cropImg(String oPath, String iPath, int x, int y, int width, int height) {
		try (InputStream is = new FileInputStream(iPath); OutputStream os = new FileOutputStream(oPath);) {
			cropImg(os, is, FileNameHelper.getExtension(iPath), x, y, width, height);
			return oPath;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String generatorSmallFileName(String name) {
		String fn = FileNameHelper.getBaseName(name);
		return name.replace(fn, fn + "_small");
	}

	/**
	 * 获得图片的高
	 * 
	 * @param file 图片文件
	 * @return 高,为0表示获取图片失败
	 */
	public static int getHeight(File file) {
		try (InputStream is = new FileInputStream(file);) {
			return getHeight(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获得图片的高
	 * 
	 * @param is 图片输入流
	 * @return 高,为0表示获取图片失败
	 */
	public static int getHeight(InputStream is) {
		try {
			return ImageIO.read(is).getHeight(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获得图片的高
	 * 
	 * @param path 图片地址
	 * @return 高,为0表示获取图片失败
	 */
	public static int getHeight(String path) {
		try (InputStream is = new FileInputStream(path);) {
			return getHeight(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获得图片的宽
	 * 
	 * @param path 图片文件
	 * @return 高,为0表示获取图片失败
	 */
	public static int getWidth(File file) {
		try (InputStream is = new FileInputStream(file);) {
			return getWidth(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获得图片的宽
	 * 
	 * @param is 图片输入流
	 * @return 高,为0表示获取图片失败
	 */
	public static int getWidth(InputStream is) {
		try {
			return ImageIO.read(is).getWidth(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获得图片的宽
	 * 
	 * @param path 图片地址
	 * @return 高,为0表示获取图片失败
	 */
	public static int getWidth(String path) {
		try (InputStream is = new FileInputStream(path);) {
			return getWidth(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获得一张图片的宽高
	 * 
	 * @param file 图片文件
	 * @return 返回一个数组,第一个值是宽,第二个值是高
	 * @throws IOException
	 */
	public static int[] getWidthHeight(File file) {
		try (InputStream is = new FileInputStream(file);) {
			return getWidthHeight(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得一张图片的宽高
	 * 
	 * @param is 图片输入流
	 * @return 返回一个数组,第一个值是宽,第二个值是高
	 * @throws IOException
	 */
	public static int[] getWidthHeight(InputStream is) {
		try {
			Image img = ImageIO.read(is);
			return new int[] { img.getWidth(null), img.getHeight(null) };
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new int[0];
	}

	/**
	 * 获得一张图片的宽高
	 * 
	 * @param path 图片路径
	 * @return 返回一个数组,第一个值是宽,第二个值是高
	 * @throws IOException
	 */
	public static int[] getWidthHeight(String path) {
		try (InputStream is = new FileInputStream(path);) {
			return getWidthHeight(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}