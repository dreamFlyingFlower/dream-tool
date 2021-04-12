package com.wy.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.wy.util.CharsetTool;

/**
 * 字符串压缩
 * 
 * @author 飞花梦影
 * @date 2021-01-07 14:05:09
 * @git {@link https://github.com/mygodness100}
 */
public class ZipTool {

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
		return gzip(str, CharsetTool.defaultCharset());
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
		return gzip(str.getBytes(CharsetTool.defaultCharset(charset)));
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
			int offset = -1;
			while ((offset = ginzip.read(buffer)) != -1) {
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
		return zip(str.getBytes(CharsetTool.defaultCharset()));
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
		return zip(str.getBytes(CharsetTool.defaultCharset(charset)));
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
		byte[] compressed = Base64.getDecoder().decode(compressedStr.getBytes(CharsetTool.defaultCharset()));
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
		byte[] compressed = Base64.getDecoder().decode(compressedStr.getBytes(CharsetTool.defaultCharset(charset)));
		return unzip(compressed);
	}
}