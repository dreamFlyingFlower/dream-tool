package dream.flying.flower.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import dream.flying.flower.helper.CharsetHelper;
import dream.flying.flower.io.file.FileHelper;
import dream.flying.flower.io.file.FileNameHelper;
import dream.flying.flower.lang.AssertHelper;

/**
 * 字符串压缩
 * 
 * @author 飞花梦影
 * @date 2021-01-07 14:05:09
 * @git {@link https://github.com/mygodness100}
 */
public class ZipHelper {

	/**
	 * gzip压缩
	 * 
	 * @param bytes 需要压缩的字节数组
	 * @return 压缩后的字节数组
	 * @throws IOException
	 */
	public static byte[] gzip(byte[] bytes) throws IOException {
		if (null == bytes || bytes.length == 0) {
			return null;
		}
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				GZIPOutputStream gzip = new GZIPOutputStream(out);) {
			gzip.write(bytes);
			out.flush();
			return out.toByteArray();
		}
	}

	/**
	 * gzip压缩
	 * 
	 * @param str 需要压缩的字符串
	 * @return 压缩后的字节数组
	 * @throws IOException
	 */
	public static byte[] gzip(String str) throws IOException {
		return gzip(str, CharsetHelper.defaultCharset());
	}

	/**
	 * gzip压缩
	 * 
	 * @param str 需要压缩的字符串
	 * @param charset 字符编码
	 * @return 压缩后的字节数组
	 * @throws IOException
	 */
	public static byte[] gzip(String str, Charset charset) throws IOException {
		if (str == null || str.length() == 0) {
			return null;
		}
		return gzip(str.getBytes(CharsetHelper.defaultCharset(charset)));
	}

	/**
	 * gzip解压缩
	 *
	 * @param bytes 压缩后的字节数组
	 * @return 解压缩后的字节数组
	 * @throws IOException
	 */
	public static byte[] gunzip(byte[] bytes) throws IOException {
		if (null == bytes) {
			return null;
		}
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				ByteArrayInputStream in = new ByteArrayInputStream(bytes);
				GZIPInputStream ginzip = new GZIPInputStream(in);) {
			byte[] buffer = new byte[1024];
			int offset = 0;
			while ((offset = ginzip.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, offset);
			}
			return out.toByteArray();
		}
	}

	/**
	 * zip压缩
	 *
	 * @param bytes 压缩前的字节数组
	 * @return 压缩后的字节数组
	 * @throws IOException
	 */
	public static final byte[] zip(byte[] bytes) throws IOException {
		if (null == bytes) {
			return null;
		}
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				ZipOutputStream zout = new ZipOutputStream(out);) {
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(bytes);
			zout.closeEntry();
			return out.toByteArray();
		}
	}

	/**
	 * ZIP压缩文件或目录,默认会在同级目录下生成同名文件
	 * 
	 * @param source 待压缩文件
	 */
	public static void zip(File source) {
		AssertHelper.notNull(source);
		zip(source, source.getParentFile());
	}

	/**
	 * ZIP压缩文件或目录
	 * 
	 * @param source 待压缩文件
	 * @param target 压缩后文件.若是目录,则压缩后文件放入该目录下;若是文件,取文件父目录以及压缩文件名加zip
	 */
	public static void zip(File source, File target) {
		AssertHelper.notNull(source);
		if (source.isFile()) {
			zipFile(source, target);
			return;
		}
		File zipFile = new File(
				null == target ? source.getParentFile() : target.isDirectory() ? target : target.getParentFile(),
				FileNameHelper.getBaseName(source.getName()) + ".zip");
		try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));) {
			InputStream input = null;
			File[] files = source.listFiles();
			for (int i = 0; i < files.length; ++i) {
				input = new FileInputStream(files[i]);
				zipOut.putNextEntry(new ZipEntry(source.getName() + File.separator + files[i].getName()));
				int temp = 0;
				while ((temp = input.read()) != -1) {
					zipOut.write(temp);
				}
				input.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * zip压缩
	 *
	 * @param str 压缩前的文本
	 * @return 压缩后的字节数组
	 * @throws IOException
	 */
	public static final byte[] zip(String str) throws IOException {
		if (str == null) {
			return null;
		}
		return zip(str.getBytes(CharsetHelper.defaultCharset()));
	}

	/**
	 * zip压缩
	 *
	 * @param str 压缩前的文本
	 * @param charset 字符编码
	 * @return 压缩后的字节数组
	 * @throws IOException
	 */
	public static final byte[] zip(String str, Charset charset) throws IOException {
		if (str == null) {
			return null;
		}
		return zip(str.getBytes(CharsetHelper.defaultCharset(charset)));
	}

	/**
	 * ZIP压缩单个文件,默认会在同级目录下生成同名文件,注意,只压缩文件
	 * 
	 * @param source 需要压缩的文件
	 */
	public static void zipFile(File source) {
		AssertHelper.notNull(source);
		zipFile(source, source.getParentFile());
	}

	/**
	 * ZIP压缩文件到指定目录或指定名称,注意,只压缩文件
	 * 
	 * @param source 待压缩文件
	 * @param target 压缩后文件.若是目录,则压缩后文件放入该目录下;若是文件,取文件父目录以及压缩文件名加zip
	 */
	public static void zipFile(File source, File target) {
		FileHelper.checkFile(source);
		File zipFile = new File(
				null == target ? source.getParentFile() : target.isDirectory() ? target : target.getParentFile(),
				FileNameHelper.getBaseName(source.getName()) + ".zip");
		try (InputStream input = new FileInputStream(source);
				ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));) {
			zipOut.putNextEntry(new ZipEntry(source.getName()));
			zipOut.setComment(source.getName());
			int temp = 0;
			while ((temp = input.read()) != -1) {
				zipOut.write(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * zip解压缩
	 *
	 * @param bytes 压缩后的文本字节数组
	 * @return 解压后的字节数组
	 * @throws IOException
	 */
	public static final byte[] unzip(byte[] bytes) throws IOException {
		if (null == bytes || bytes.length == 0) {
			return null;
		}
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				ByteArrayInputStream in = new ByteArrayInputStream(bytes);
				ZipInputStream zin = new ZipInputStream(in);) {
			zin.getNextEntry();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			return out.toByteArray();
		}
	}

	/**
	 * 解压缩文件
	 * 
	 * @param file 待解压缩文件
	 */
	public static void unzip(File file) {
		FileHelper.checkFile(file);
		File outFile = null;
		try (ZipFile zipFile = new ZipFile(file);
				ZipInputStream zipInput = new ZipInputStream(new FileInputStream(file));) {
			ZipEntry entry = null;
			InputStream input = null;
			OutputStream output = null;
			while ((entry = zipInput.getNextEntry()) != null) {
				outFile = new File(file.getParentFile(), entry.getName());
				if (!outFile.getParentFile().exists()) {
					outFile.getParentFile().mkdirs();
				}
				if (!outFile.exists()) {
					outFile.createNewFile();
				}
				input = zipFile.getInputStream(entry);
				output = new FileOutputStream(outFile);
				int temp = 0;
				while ((temp = input.read()) != -1) {
					output.write(temp);
				}
				input.close();
				output.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * zip解压缩
	 *
	 * @param compressedStr 压缩后的文本
	 * @return 解压后的字节数组
	 * @throws IOException
	 */
	public static final byte[] unzip(String compressedStr) throws IOException {
		if (null == compressedStr) {
			return null;
		}
		byte[] compressed = Base64.getDecoder().decode(compressedStr.getBytes(CharsetHelper.defaultCharset()));
		return unzip(compressed);
	}

	/**
	 * zip解压缩
	 *
	 * @param compressedStr 压缩后的文本
	 * @param charset 字符编码
	 * @return 解压后的字节数组
	 * @throws IOException
	 */
	public static final byte[] unzip(String compressedStr, Charset charset) throws IOException {
		if (null == compressedStr) {
			return null;
		}
		byte[] compressed = Base64.getDecoder().decode(compressedStr.getBytes(CharsetHelper.defaultCharset(charset)));
		return unzip(compressed);
	}
}