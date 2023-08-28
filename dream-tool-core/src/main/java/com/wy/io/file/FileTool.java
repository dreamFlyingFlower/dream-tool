package com.wy.io.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.wy.ConstArray;
import com.wy.ConstFile;
import com.wy.ConstIO;
import com.wy.ConstLang;
import com.wy.binary.HexTool;
import com.wy.io.IOTool;
import com.wy.lang.AssertTool;
import com.wy.lang.StrTool;
import com.wy.result.ResultException;
import com.wy.util.CharsetTool;
import com.wy.util.UrlTool;

/**
 * File工具类,所有需要用到字符串编码的地方,都使用UTF-8 FIXME
 * ,{@link org.springframework.util.FastByteArrayOutputStream}
 * 
 * @author 飞花梦影
 * @date 2021-02-28 20:10:31
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class FileTool {

	/**
	 * url去重 FIXME
	 * 
	 * @param args
	 */
	// public static void main(String[] args) {
	// BufferedReader br = null;
	// OutputStreamWriter osr = null;
	// try {
	// File src = new
	// File("D:\\Java\\responsity\\javas\\Utils\\src\\com\\wy\\utils\\kukudas1530372519755.txt");
	// InputStreamReader isr = new InputStreamReader(new FileInputStream(src));
	// br = new BufferedReader(isr);
	// String line = null;
	// Set<String> urls = new HashSet<>();
	// while ((line = br.readLine()) != null) {
	// urls.add(line);
	// }
	// osr = new OutputStreamWriter(new FileOutputStream("D:\\Java\\test.txt"));
	// for (String url : urls) {
	// osr.write(url + "\r\n");
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// close(br, osr);
	// }
	// }
	/**
	 * 判断文件存在且是一个文件,而不是目录
	 * 
	 * @param file 需要判断的文件
	 * @return 源文件
	 * @throws IllegalArgumentException
	 */
	public static File checkFile(final File file) {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		if (!file.exists()) {
			throw new IllegalArgumentException(file + " does not exist");
		}
		if (!file.isFile()) {
			throw new IllegalArgumentException(file + " is not a file");
		}
		return file;
	}

	/**
	 * 判断文件存在且是一个文件,而不是目录
	 * 
	 * @param fullPath 文件地址的绝对路径
	 * @return 源文件
	 * @throws IllegalArgumentException
	 */
	public static File checkFile(final String fullPath) {
		AssertTool.notBlank(fullPath, "the file can't be blank");
		File file = new File(fullPath);
		if (!file.exists()) {
			throw new IllegalArgumentException(file + " does not exist");
		}
		if (!file.isFile()) {
			throw new IllegalArgumentException(file + " is not a file");
		}
		return file;
	}

	/**
	 * 判断文件存在且是一个目录
	 *
	 * @param directory 需要判断的文件
	 * @return 源文件
	 * @throws IllegalArgumentException
	 */
	public static File checkDirectory(final File directory) {
		AssertTool.notNull(directory, ConstIO.TOAST_DIR_NULL);
		if (!directory.exists()) {
			throw new IllegalArgumentException(directory + " does not exist");
		}
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException(directory + " is not a directory");
		}
		return directory;
	}

	/**
	 * 使用指定的校验和对象计算文件的校验和
	 *
	 * @param file 需要进行校验的文件
	 * @param checksum 校验方式
	 * @return 校验对象,可使用getValue()获得该值
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException if the file is a directory
	 * @throws IOException if an IO error occurs reading the file
	 */
	public static Checksum checksum(final File file, final Checksum checksum)
			throws FileNotFoundException, IOException {
		if (file.isDirectory()) {
			throw new IllegalArgumentException("Checksums can't be computed on directories");
		}
		try (InputStream in = new CheckedInputStream(new FileInputStream(file), checksum)) {
			IOTool.consume(in);
		}
		return checksum;
	}

	/**
	 * 使用CRC32的方式校验文件
	 *
	 * @param file 需要进行校验的文件
	 * @return 校验值
	 * @throws NullPointerException if the file or checksum is {@code null}
	 * @throws IllegalArgumentException if the file is a directory
	 * @throws IOException if an IO error occurs reading the file
	 */
	public static long checksumCRC32(final File file) throws IOException {
		return checksum(file, new CRC32()).getValue();
	}

	/**
	 * 批量修改文件名,自用 FIXME
	 */
	public static boolean testModifyFilesBatch(String parent) {
		if (StrTool.isBlank(parent)) {
			return false;
		}
		File parentFile = new File(parent);
		if (parentFile.exists()) {
			File[] listFiles = parentFile.listFiles();
			if (listFiles != null && listFiles.length > 0) {
				for (File childFile : listFiles) {
					if (childFile.isFile()) {
						String fileName = childFile.getParent() + File.separator
								+ childFile.getName().replace("[YYDM-11FANS][Gundam_Seed-Destiny-HD-ReMaster]", "")
										.replace("[BDRIP][X264-10bit_AAC][720P]", "");
						childFile.renameTo(new File(fileName));
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 文件分块,将大文件分成多个小块 FIXME
	 * 
	 * @param fileSrc 需要分块的文件地址
	 * @param chunkFolder 分块之后的文件存放目录
	 * @throws IOException
	 */
	public void testChunk(String fileSrc, String chunkFolder) throws IOException {
		File sourceFile = new File(fileSrc);
		if (sourceFile == null || !sourceFile.exists()) {
			throw new ResultException("文件不存在");
		}
		// 块文件大小,1M
		long chunkFileSize = 1 * 1024 * 1024;
		// 块数
		long chunkFileNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkFileSize);
		try (RandomAccessFile rafRead = new RandomAccessFile(sourceFile, "r");) {
			byte[] b = new byte[1024];
			for (int i = 0; i < chunkFileNum; i++) {
				File chunkFile = new File(chunkFolder + i);
				// 创建向块文件的写对象
				RandomAccessFile rafWrite = new RandomAccessFile(chunkFile, "rw");
				int len = -1;
				while ((len = rafRead.read(b)) != -1) {
					rafWrite.write(b, 0, len);
					// 如果块文件的大小达到1M开始写下一块儿
					if (chunkFile.length() >= chunkFileSize) {
						break;
					}
				}
				rafWrite.close();
			}
		}
	}

	/**
	 * 文件合并,文件必须是按顺序切割的文件列表,文件名必须是纯数字或数字是接在下划线之后,如filename_01,filename_02...
	 * 
	 * 文件名若是带下划线,则只能有一个下划线,否则同样会出问题
	 * 
	 * @param chunkFolder 块文件目录 FIXME
	 * @param desFile 合成后的文件地址,是一个文件
	 * @throws IOException
	 */
	public void testMergeFile(String chunkFolder, String desFile) throws IOException {
		File chunkFile = new File(chunkFolder);
		if (chunkFile == null || !chunkFile.exists() || !chunkFile.isDirectory()) {
			throw new ResultException("文件不存在或文件不是一个目录");
		}
		// 块文件列表
		File[] files = chunkFile.listFiles();
		// 将块文件排序,按名称升序
		List<File> fileList = Arrays.asList(files);
		Collections.sort(fileList, (file1, file2) -> {
			if (Integer.parseInt(
					file1.getName().indexOf("_") > -1 ? file1.getName().substring(file1.getName().indexOf("_") + 1)
							: file1.getName()) > Integer
									.parseInt(file2.getName().indexOf("_") > -1
											? file2.getName().substring(file2.getName().indexOf("_") + 1)
											: file2.getName())) {
				return 1;
			}
			return -1;
		});
		File mergeFile = new File(desFile);
		try (RandomAccessFile rafWrite = new RandomAccessFile(mergeFile, "rw");) {
			byte[] b = new byte[1024];
			RandomAccessFile rafRead = null;
			for (File file : fileList) {
				rafRead = new RandomAccessFile(file, "r");
				int len = -1;
				while ((len = rafRead.read(b)) != -1) {
					rafWrite.write(b, 0, len);
				}
				if (rafRead != null) {
					rafRead.close();
				}
			}
		}
	}

	/**
	 * 解析路径字符串,优化其中的..,.
	 * 
	 * @param path 原始路径
	 * @return 格式化后的路径
	 */
	public static String cleanPath(String path) {
		if (StrTool.isBlank(path)) {
			return path;
		}

		String normalizedPath = StrTool.replace(path, ConstFile.WINDOWS_FOLDER_SEPARATOR, ConstFile.FOLDER_SEPARATOR);
		String pathToUse = normalizedPath;

		// Shortcut if there is no work to do
		if (pathToUse.indexOf('.') == -1) {
			return pathToUse;
		}

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(':');
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			if (prefix.contains(ConstFile.FOLDER_SEPARATOR)) {
				prefix = "";
			} else {
				pathToUse = pathToUse.substring(prefixIndex + 1);
			}
		}
		if (pathToUse.startsWith(ConstFile.FOLDER_SEPARATOR)) {
			prefix = prefix + ConstFile.FOLDER_SEPARATOR;
			pathToUse = pathToUse.substring(1);
		}

		String[] pathArray = StrTool.split(pathToUse, ConstFile.FOLDER_SEPARATOR);
		// we never require more elements than pathArray and in the common case the same
		// number
		Deque<String> pathElements = new ArrayDeque<>(pathArray.length);
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			String element = pathArray[i];
			if (ConstFile.CURRENT_PATH.equals(element)) {
				// Points to current directory - drop it.
			} else if (ConstFile.TOP_PATH.equals(element)) {
				// Registering top path found.
				tops++;
			} else {
				if (tops > 0) {
					// Merging path element with element corresponding to top path.
					tops--;
				} else {
					// Normal path element found.
					pathElements.addFirst(element);
				}
			}
		}

		// All path elements stayed the same - shortcut
		if (pathArray.length == pathElements.size()) {
			return normalizedPath;
		}
		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.addFirst(ConstFile.TOP_PATH);
		}
		// If nothing else left, at least explicitly point to current path.
		if (pathElements.size() == 1 && pathElements.getLast().isEmpty()
				&& !prefix.endsWith(ConstFile.FOLDER_SEPARATOR)) {
			pathElements.addFirst(ConstFile.CURRENT_PATH);
		}

		final String joined =
				String.join(ConstFile.FOLDER_SEPARATOR, pathElements.toArray(new String[pathElements.size()]));
		// avoid string concatenation with empty prefix
		return prefix.isEmpty() ? joined : prefix + joined;
	}

	/**
	 * 判断2个文件中的内容是否相同
	 *
	 * @param file1 文件1
	 * @param file2 文件2
	 * @return true->当文件内容相同,false->文件内容不相同
	 * @throws IOException in case of an I/O error
	 */
	public static boolean contentEquals(final File file1, final File file2) throws IOException {
		if (file1 == null && file2 == null) {
			return true;
		}
		if (file1 == null ^ file2 == null) {
			return false;
		}
		final boolean file1Exists = file1.exists();
		if (file1Exists != file2.exists()) {
			return false;
		}
		if (!file1Exists) {
			// two not existing files are equal
			return true;
		}
		if (file1.isDirectory() || file2.isDirectory()) {
			// don't want to compare directory contents
			throw new IOException("Can't compare directories, only files");
		}
		if (file1.length() != file2.length()) {
			// lengths differ, cannot be equal
			return false;
		}
		if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
			// same file
			return true;
		}
		try (InputStream input1 = new FileInputStream(file1); InputStream input2 = new FileInputStream(file2)) {
			return IOTool.contentEquals(input1, input2);
		}
	}

	/**
	 * 判断2个文件中的内容是否相同
	 * 
	 * @param file1 文件1
	 * @param file2 文件2
	 * @param charsetName 字符编码名称
	 * @return true->当文件内容相同,false->文件内容不相同
	 * @throws IOException in case of an I/O error
	 */
	public static boolean contentEqualsIgnoreEOL(final File file1, final File file2, final String charsetName)
			throws IOException {
		if (file1 == null && file2 == null) {
			return true;
		}
		if (file1 == null ^ file2 == null) {
			return false;
		}
		final boolean file1Exists = file1.exists();
		if (file1Exists != file2.exists()) {
			return false;
		}
		if (!file1Exists) {
			// two not existing files are equal
			return true;
		}
		if (file1.isDirectory() || file2.isDirectory()) {
			// don't want to compare directory contents
			throw new IOException("Can't compare directories, only files");
		}
		if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
			// same file
			return true;
		}
		try (Reader input1 = new InputStreamReader(new FileInputStream(file1), CharsetTool.defaultCharset(charsetName));
				Reader input2 =
						new InputStreamReader(new FileInputStream(file2), CharsetTool.defaultCharset(charsetName))) {
			return IOTool.contentEqualsIgnoreEOL(input1, input2);
		}
	}

	/**
	 * 拷贝字节数组内容到指定文件中
	 * 
	 * @param in 字节数组
	 * @param out 目标文件
	 * @throws IOException in the case of I/O errors
	 */
	public static void copy(byte[] in, File out) throws IOException {
		AssertTool.notNull(in, "No input byte array specified");
		AssertTool.notNull(out, "No output File specified");
		IOTool.copy(new ByteArrayInputStream(in), Files.newOutputStream(out.toPath()));
	}

	/**
	 * 递归复制文件或目录
	 * 
	 * @param src 源目录
	 * @param dest 目的目录
	 * @throws IOException in the case of I/O errors
	 * @see PathTool#copyRecursive(Path, Path)
	 */
	public static void copy(final File src, final File dest) throws IOException {
		AssertTool.notNull(src, "Source File must not be null");
		AssertTool.notNull(dest, "Destination File must not be null");
		PathTool.copy(src.toPath(), dest.toPath());
	}

	/**
	 * 递归复制目录,{@link #copyRecursive(File, File)}
	 * 
	 * @param srcDir 源目录
	 * @param destDir 目标目录
	 * @throws IOException
	 */
	public static void copyDir(final File srcDir, final File destDir) throws IOException {
		copyDir(srcDir, destDir, null);
	}

	/**
	 * 递归复制目录,若文件已经存在,替代
	 * 
	 * @param srcDir 源目录
	 * @param destDir 目标目录
	 * @param filter 文件过滤
	 * @throws IOException
	 */
	public static void copyDir(final File srcDir, final File destDir, final FileFilter filter) throws IOException {
		copyDir(srcDir, destDir, filter, StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * 递归复制目录
	 * 
	 * @param srcDir 源目录
	 * @param destDir 目标目录
	 * @param filter 文件过滤
	 * @param copyOptions 复制类型
	 * @throws IOException
	 */
	public static void copyDir(final File srcDir, final File destDir, final FileFilter filter,
			final CopyOption... copyOptions) throws IOException {
		if (!srcDir.isDirectory()) {
			throw new IOException("Source '" + srcDir + "' exists but is not a directory");
		}
		if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
			throw new IOException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
		}
		// Cater for destination being directory within the source directory (see
		// IO-141)
		List<String> exclusionList = null;
		if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
			final File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
			if (srcFiles != null && srcFiles.length > 0) {
				exclusionList = new ArrayList<>(srcFiles.length);
				for (final File srcFile : srcFiles) {
					final File copiedFile = new File(destDir, srcFile.getName());
					exclusionList.add(copiedFile.getCanonicalPath());
				}
			}
		}
		copyDir(srcDir, destDir, filter, exclusionList, copyOptions);
	}

	/**
	 * 递归复制目录
	 * 
	 * @param srcDir 源目录
	 * @param destDir 目标目录
	 * @param filter 文件过滤
	 * @param exclusionList 不需要复制的文件
	 * @param copyOptions 复制类型
	 * @throws IOException
	 */
	public static void copyDir(final File srcDir, final File destDir, final FileFilter filter,
			final List<String> exclusionList, final CopyOption... copyOptions) throws IOException {
		final File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
		if (srcFiles == null) {
			throw new IOException("Failed to list contents of " + srcDir);
		}
		if (destDir.exists()) {
			if (!destDir.isDirectory()) {
				throw new IOException("Destination '" + destDir + "' exists but is not a directory");
			}
		} else {
			if (!destDir.mkdirs() && !destDir.isDirectory()) {
				throw new IOException("Destination '" + destDir + "' directory cannot be created");
			}
		}
		if (!destDir.canWrite()) {
			throw new IOException("Destination '" + destDir + "' cannot be written to");
		}
		for (final File srcFile : srcFiles) {
			final File dstFile = new File(destDir, srcFile.getName());
			if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
				if (srcFile.isDirectory()) {
					copyDir(srcFile, dstFile, filter, exclusionList, copyOptions);
				} else {
					copyFile(srcFile, dstFile, copyOptions);
				}
			}
		}
	}

	/**
	 * 复制文件,若目标文件已经存在,替换
	 * 
	 * @param srcFile 源文件
	 * @param destFile 目标文件
	 * @throws IOException in case of I/O errors
	 */
	public static void copyFile(final File srcFile, final File destFile) throws IOException {
		copyFile(srcFile, destFile, StandardCopyOption.REPLACE_EXISTING);
	}

	/**
	 * 复制文件
	 * 
	 * @param srcFile 源文件
	 * @param destFile 目标文件
	 * @param copyOptions 复制参数,{@link StandardCopyOption}
	 * @throws IOException in case of I/O errors
	 */
	public static void copyFile(final File srcFile, final File destFile, final CopyOption... copyOptions)
			throws IOException {
		checkFile(srcFile);
		checkFile(destFile);
		if (srcFile.isDirectory()) {
			throw new IOException("Source '" + srcFile + "' exists but is a directory");
		}
		if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
			throw new IOException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
		}
		final File parentFile = destFile.getParentFile();
		if (parentFile != null) {
			if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
				throw new IOException("Destination '" + parentFile + "' directory cannot be created");
			}
		}
		if (destFile.exists() && destFile.canWrite() == false) {
			throw new IOException("Destination '" + destFile + "' exists but is read-only");
		}
		Files.copy(srcFile.toPath(), destFile.toPath(), copyOptions);
	}

	/**
	 * 将文件复制到输出流中
	 * 
	 * @param input 源文件
	 * @param output 输出流
	 * @return 复制的字节数
	 * @throws IOException in case of I/O errors
	 */
	public static long copyFile(final File input, final OutputStream output) throws IOException {
		checkFile(input);
		try (FileInputStream fis = new FileInputStream(input)) {
			return IOTool.copy(fis, output);
		}
	}

	/**
	 * 读取文件中的内容为字节数组
	 * 
	 * @param file 文件
	 * @return 字节数组
	 * @throws IOException in case of I/O errors
	 */
	public static byte[] copyToByteArray(File file) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return copyToByteArray(Files.newInputStream(file.toPath()));
	}

	/**
	 * 读取文件中的内容为字节数组
	 * 
	 * @param input 读取数据的字节输入流
	 * @return 字节数组
	 * @throws IOException in case of I/O errors
	 */
	public static byte[] copyToByteArray(InputStream input) throws IOException {
		if (input == null) {
			return ConstArray.EMPTY_BYTE;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream(ConstIO.DEFAULT_BUFFER_SIZE);
		IOTool.copy(input, out);
		return out.toByteArray();
	}

	/**
	 * 递归创建目录
	 * 
	 * @param fullPath 文件地址绝对路径
	 * @return true->新建目录成功,false->新建目录失败
	 */
	public static boolean createDir(String fullPath) {
		AssertTool.notBlank(fullPath);
		return createDir(new File(fullPath));
	}

	/**
	 * 递归创建目录
	 * 
	 * @param file 文件
	 * @return true->新建目录成功,false->新建目录失败
	 */
	public static boolean createDir(File file) {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		if (!file.exists() && file.isDirectory()) {
			return file.mkdirs();
		}
		return false;
	}

	/**
	 * 新建一个文件,若文件已经存在或异常,返回false
	 * 
	 * @param fullPath 需要新建的文件绝对路径
	 * @return 新建是否成功.true->成功
	 * @throws IOException
	 */
	public static boolean createFile(String fullPath) throws IOException {
		AssertTool.notBlank(fullPath);
		return createFile(new File(fullPath));
	}

	/**
	 * 新建一个文件,若文件已经存在,抛异常
	 * 
	 * @param file 需要新建的文件
	 * @return 新建是否成功.true->成功
	 * @throws IOException
	 */
	public static boolean createFile(File file) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return file.createNewFile();
	}

	/**
	 * 新建一个文件
	 * 
	 * @param file 需要新建的文件
	 * @param cover 是否强制覆盖:true->覆盖,false不覆盖
	 * @return 新建是否成功.true->成功
	 * @throws IOException
	 */
	public static boolean createFile(File file, boolean cover) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		if (cover && file.exists() && file.isFile()) {
			file.delete();
		} else {
			if (file.exists()) {
				throw new IOException("the file is existence");
			}
		}
		return file.createNewFile();
	}

	/**
	 * 创建文件的所有上级目录,根目录不可创建
	 *
	 * @throws IOException if an I/O error occurs
	 */
	public static void createParentDirs(File file) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		File parent = file.getCanonicalFile().getParentFile();
		if (parent == null) {
			return;
		}
		parent.mkdirs();
		if (!parent.isDirectory()) {
			throw new IOException("Unable to create parent directories of " + file);
		}
	}

	/**
	 * 创建临时文件,文件名为prefix[Randonm].tmp
	 *
	 * @param dir 临时文件创建的所在目录
	 * @return 临时文件
	 * @throws IOException
	 */
	public static File createTempFile(File dir) throws IOException {
		return createTempFile(ConstIO.FILE_TEMP_PREFIX, null, dir);
	}

	/**
	 * 创建临时文件,文件名为prefix[Randon].suffix
	 *
	 * @param prefix 前缀,至少3个字符
	 * @param suffix 后缀,如果null则使用默认.tmp
	 * @param dir 临时文件创建的所在目录
	 * @return 临时文件
	 * @throws IOException
	 */
	public static File createTempFile(String prefix, String suffix, File dir) throws IOException {
		createDir(dir);
		return File.createTempFile(prefix, suffix, dir).getCanonicalFile();
	}

	/**
	 * 调用{@link Files#deleteIfExists(Path)},删除文件
	 * 
	 * @param path 文件路径,若文件不存在,抛异常
	 * @throws IOException
	 */
	public static boolean delete(File file) throws IOException {
		if (null == file) {
			return true;
		}
		return Files.deleteIfExists(file.toPath());
	}

	/**
	 * 调用{@link Files#deleteIfExists(Path)},删除文件.若被删除的是目录,且目录中有文件,抛异常
	 * 
	 * @param fullPath 文件绝对路径
	 * @throws IOException
	 */
	public static boolean delete(String fullPath) throws IOException {
		AssertTool.notBlank(fullPath, "The fulPath of the file is not blank");
		return PathTool.delete(Paths.get(fullPath));
	}

	/**
	 * 调用{@link Files#deleteIfExists(Path)},删除文件.若被删除的是目录,且目录中有文件,抛异常
	 * 
	 * @param parentPath 父级文件绝对路径
	 * @param fileName 文件名
	 * @throws IOException
	 */
	public static boolean delete(String parentPath, String fileName) throws IOException {
		AssertTool.notBlank(fileName, "The fileName can't be blank");
		return PathTool.delete(Paths.get(parentPath, fileName));
	}

	/**
	 * 删除文件,如果是目录,递归删除目录中的目录和文件
	 * 
	 * @param root 需要删除的文件
	 * @return true当文件被删除或文件本身不存在
	 * @throws IOException
	 */
	public static boolean deleteRecursive(File root) throws IOException {
		if (root == null || !root.exists()) {
			return true;
		}
		return PathTool.deleteRecursive(root.toPath());
	}

	/**
	 * 删除文件,如果是目录,递归删除目录中的目录和文件
	 * 
	 * @param fullPath 需要删除的文件绝对路径
	 * @return true当文件被删除或文件本身不存在
	 * @throws IOException
	 */
	public static boolean deleteRecursive(String fullPath) throws IOException {
		if (StrTool.isBlank(fullPath)) {
			return true;
		}
		return PathTool.deleteRecursive(new File(fullPath).toPath());
	}

	/**
	 * 将字符串拼接成一个绝对路径
	 *
	 * @param directory 父目录
	 * @param names 父目录下的路径
	 * @return the file 文件
	 */
	public static File getFile(final File directory, final String... names) {
		AssertTool.notNull(directory, ConstIO.TOAST_DIR_NULL);
		AssertTool.notNull(names, "names");
		return Paths.get(directory.getAbsolutePath(), names).toFile();
	}

	/**
	 * 将字符串拼接成一个绝对路径
	 *
	 * @param directory 父目录地址绝对路径
	 * @param names 父目录下的路径
	 * @return the file 文件
	 */
	public static File getFile(final String directory, final String... names) {
		AssertTool.notNull(directory, ConstIO.TOAST_DIR_NULL);
		AssertTool.notNull(names, "names");
		return Paths.get(directory, names).toFile();
	}

	/**
	 * 获得文件的所有基础属性
	 * 
	 * @param file 文件
	 * @return 属性Map
	 * @throws IOException
	 */
	public static Map<String, Object> getFileAttr(File file) throws IOException {
		AssertTool.notNull(file);
		return getFileAttr(file.toPath());
	}

	/**
	 * 获得文件的所有基础属性
	 * 
	 * @param file 文件
	 * @param attr 文件属性,*表示所有属性,多个指定属性可用逗号隔开
	 * @return 属性Map
	 * @throws IOException
	 */
	public static Map<String, Object> getFileAttr(File file, String attr) throws IOException {
		AssertTool.notNull(file);
		return getFileAttr(file.toPath(), attr);
	}

	/**
	 * 获得文件的所有基础属性
	 * 
	 * @param path 文件路径
	 * @return 属性Map
	 * @throws IOException
	 */
	public static Map<String, Object> getFileAttr(Path path) throws IOException {
		return getFileAttr(path, "*");
	}

	/**
	 * 获得文件的指定属性
	 * 
	 * @param path 文件路径
	 * @param attr 文件属性,*表示所有属性,多个指定属性可用逗号隔开
	 * @return 属性Map
	 * @throws IOException
	 */
	public static Map<String, Object> getFileAttr(Path path, String attr) throws IOException {
		AssertTool.notNull(path);
		return Files.readAttributes(path, attr, LinkOption.NOFOLLOW_LINKS);
	}

	/**
	 * 获得文件的指定属性
	 * 
	 * @param path 文件路径
	 * @return 属性Map
	 * @throws IOException
	 */
	public static Map<String, Object> getFileAttr(String path) throws IOException {
		AssertTool.notBlank(path);
		return getFileAttr(Paths.get(path));
	}

	/**
	 * 获得文件的指定属性
	 * 
	 * @param path 文件路径
	 * @param attr 文件属性,*表示所有属性,多个指定属性可用逗号隔开
	 * @return 属性Map
	 * @throws IOException
	 */
	public static Map<String, Object> getFileAttr(String path, String attr) throws IOException {
		AssertTool.notBlank(path);
		return getFileAttr(Paths.get(path), attr);
	}

	/**
	 * 获得文件的md5值
	 * 
	 * @param file 文件
	 * @return 文件MD5值
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getFileMd5(File file) throws NoSuchAlgorithmException, IOException {
		AssertTool.notNull(file);
		return getFileMd5(new FileInputStream(file));
	}

	/**
	 * 获得输入流的MD5值
	 * 
	 * @param inputStream 输入流
	 * @return MD5值
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String getFileMd5(InputStream inputStream) throws NoSuchAlgorithmException, IOException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] buffer = new byte[8192];
		int length;
		while ((length = inputStream.read(buffer)) != -1) {
			md5.update(buffer, 0, length);
		}
		return new String(HexTool.encodeHex(md5.digest()));
	}

	/**
	 * 获得不带点和后缀的文件名
	 * 
	 * @param file 文件
	 * @return 不带点和后缀的文件名
	 */
	public static String getNameNoExtension(File file) {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		String fileName = file.getName();
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
	}

	/**
	 * 获得不带点和后缀的文件名
	 * 
	 * @param fullPath 文件绝对路径
	 * @return 不带点和后缀的文件名
	 */
	public static String getNameNoExtension(String fullPath) {
		AssertTool.notBlank(fullPath, "The fullPath of the file can't be blank");
		return getNameNoExtension(new File(fullPath));
	}

	/**
	 * 获得文件名的后缀,不带点
	 * 
	 * @param file 文件
	 * @return 文件名后缀,不带点,可能为""
	 */
	public static String getFileExtension(File file) {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		String fileName = file.getName();
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}

	/**
	 * 获得文件名的后缀,不带点
	 * 
	 * @param fullPath 文件全路径
	 * @return 文件名后缀,不带点,可能为""
	 */
	public static String getFileExtension(String fullPath) {
		AssertTool.notBlank(fullPath, "The fullPath of the file can't be blank");
		return getFileExtension(new File(fullPath));
	}

	/**
	 * 获得文件的父目录
	 * 
	 * @param file 文件或目录
	 * @return 父目录
	 */
	public static File getParent(File file) {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return file.getParentFile();
	}

	/**
	 * 获得文件的父目录
	 * 
	 * @param file 文件或目录
	 * @param index 第N层目录,index越大越靠近最下层,越小越靠近根目录
	 * @return 父目录
	 */
	public static File getParent(File file, int index) {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return PathTool.getParent(file.toPath(), index).toFile();
	}

	/**
	 * 判断文件的最近修改时间比当前时间更晚更新
	 *
	 * @param file 需要判断的文件
	 * @param chronoLocalDate 计时日期
	 * @return true->文件存在且比当前时间更晚更新
	 */
	public static boolean isFileNewer(final File file, final ChronoLocalDate chronoLocalDate) {
		return isFileNewer(file, chronoLocalDate, LocalTime.now());
	}

	/**
	 * 判断文件的最近修改时间比指定时间更晚更新
	 *
	 * @param file 需要判断的文件
	 * @param chronoLocalDate 计时日期
	 * @param localTime 需要判断的时间
	 * @return true->文件存在且比指定时间更晚更新
	 */
	public static boolean isFileNewer(final File file, final ChronoLocalDate chronoLocalDate,
			final LocalTime localTime) {
		AssertTool.notNull(chronoLocalDate);
		AssertTool.notNull(localTime);
		return isFileNewer(file, chronoLocalDate.atTime(localTime));
	}

	/**
	 * 判断文件的最近修改时间比指定时间更晚更新
	 *
	 * @param file 需要判断的文件
	 * @param chronoLocalDateTime 计时日期
	 * @return true->文件存在且比指定时间更晚更新
	 */
	public static boolean isFileNewer(final File file, final ChronoLocalDateTime<?> chronoLocalDateTime) {
		return isFileNewer(file, chronoLocalDateTime, ZoneId.systemDefault());
	}

	/**
	 * 判断文件的最近修改时间比指定时间更晚更新
	 *
	 * @param file 需要判断的文件
	 * @param chronoLocalDateTime 计时日期
	 * @param zoneId 指定时区
	 * @return true->文件存在且比指定时间更晚更新
	 */
	public static boolean isFileNewer(final File file, final ChronoLocalDateTime<?> chronoLocalDateTime,
			final ZoneId zoneId) {
		AssertTool.notNull(chronoLocalDateTime, "chronoLocalDateTime");
		AssertTool.notNull(zoneId, "zoneId");
		return isFileNewer(file, chronoLocalDateTime.atZone(zoneId));
	}

	/**
	 * 判断文件的最近修改时间比指定时间更晚更新
	 *
	 * @param file 需要判断的文件
	 * @param chronoZonedDateTime 计时日期
	 * @return true->文件存在且比指定时间更晚更新
	 */
	public static boolean isFileNewer(final File file, final ChronoZonedDateTime<?> chronoZonedDateTime) {
		AssertTool.notNull(chronoZonedDateTime, "chronoZonedDateTime");
		return isFileNewer(file, chronoZonedDateTime.toInstant());
	}

	/**
	 * 判断文件的最近修改时间比指定时间更晚更新
	 *
	 * @param file 需要判断的文件
	 * @param date 指定时间
	 * @return true->文件存在且比指定时间更晚更新
	 */
	public static boolean isFileNewer(final File file, final Date date) {
		AssertTool.notNull(date, "date");
		return isFileNewer(file, date.getTime());
	}

	/**
	 * 判断文件的最近修改时间比另外一个文件更晚更新
	 *
	 * @param file1 文件1
	 * @param file2 文件2
	 * @return true->file1存在且比file2的修改时间更晚更新
	 */
	public static boolean isFileNewer(final File file1, final File file2) {
		AssertTool.notNull(file2, "reference");
		if (!file2.exists()) {
			throw new IllegalArgumentException("The reference file '" + file2 + "' doesn't exist");
		}
		return isFileNewer(file1, file2.lastModified());
	}

	/**
	 * 判断文件的最近修改时间比指定时间更晚更新
	 *
	 * @param file 需要判断的文件
	 * @param instant 时间线
	 * @return true->文件存在且比指定时间更晚更新
	 */
	public static boolean isFileNewer(final File file, final Instant instant) {
		AssertTool.notNull(instant, "instant");
		return isFileNewer(file, instant.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
	}

	/**
	 * 判断文件的最近修改时间比指定时间更晚更新
	 *
	 * @param file 需要判断的文件
	 * @param timeMillis 比较时间,毫秒
	 * @return true->文件存在且比指定时间更晚更新
	 */
	public static boolean isFileNewer(final File file, final long timeMillis) {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		if (!file.exists()) {
			return false;
		}
		return file.lastModified() > timeMillis;
	}

	/**
	 * 判断文件的最近修改时间比指定时间更早更旧
	 *
	 * @param file 需要判断的文件
	 * @param chronoLocalDate 计时日期
	 * @return true->文件存在且比当前时间更早更旧
	 */
	public static boolean isFileOlder(final File file, final ChronoLocalDate chronoLocalDate) {
		return isFileOlder(file, chronoLocalDate, LocalTime.now());
	}

	/**
	 * 判断文件的最近修改时间比指定时间更早更旧
	 *
	 * @param file 需要判断的文件
	 * @param chronoLocalDate 计时日期
	 * @param localTime 需要判断的时间
	 * @return true->文件存在且比指定时间更早更旧
	 */
	public static boolean isFileOlder(final File file, final ChronoLocalDate chronoLocalDate,
			final LocalTime localTime) {
		AssertTool.notNull(chronoLocalDate, "chronoLocalDate");
		AssertTool.notNull(localTime, "localTime");
		return isFileOlder(file, chronoLocalDate.atTime(localTime));
	}

	/**
	 * 判断文件的最近修改时间比指定时间更早更旧
	 *
	 * @param file 需要判断的文件
	 * @param chronoLocalDateTime 计时日期
	 * @return true->文件存在且比指定时间更早更旧
	 */
	public static boolean isFileOlder(final File file, final ChronoLocalDateTime<?> chronoLocalDateTime) {
		return isFileOlder(file, chronoLocalDateTime, ZoneId.systemDefault());
	}

	/**
	 * 判断文件的最近修改时间比指定时间更早更旧
	 *
	 * @param file 需要判断的文件
	 * @param chronoLocalDateTime 计时日期
	 * @return true->文件存在且比指定时间更早更旧
	 */
	public static boolean isFileOlder(final File file, final ChronoLocalDateTime<?> chronoLocalDateTime,
			final ZoneId zoneId) {
		AssertTool.notNull(chronoLocalDateTime, "chronoLocalDateTime");
		AssertTool.notNull(zoneId, "zoneId");
		return isFileOlder(file, chronoLocalDateTime.atZone(zoneId));
	}

	/**
	 * 判断文件的最近修改时间比指定时间更早更旧
	 *
	 * @param file 需要判断的文件
	 * @param chronoZonedDateTime 计时日期
	 * @return true->文件存在且比指定时间更早更旧
	 */
	public static boolean isFileOlder(final File file, final ChronoZonedDateTime<?> chronoZonedDateTime) {
		AssertTool.notNull(chronoZonedDateTime, "chronoZonedDateTime");
		return isFileOlder(file, chronoZonedDateTime.toInstant());
	}

	/**
	 * 判断文件的最近修改时间比指定时间更早更旧
	 *
	 * @param file 需要判断的文件
	 * @param date 指定时间
	 * @return true->文件存在且比指定时间更早更旧
	 */
	public static boolean isFileOlder(final File file, final Date date) {
		AssertTool.notNull(date, "date");
		return isFileOlder(file, date.getTime());
	}

	/**
	 * 判断文件的最近修改时间比指定时间更早更旧
	 *
	 * @param file1 file1
	 * @param file2 文件2
	 * @return true->file1存在且比file2的修改时间更早更旧
	 */
	public static boolean isFileOlder(final File file1, final File file2) {
		checkFile(file1);
		checkFile(file2);
		return isFileOlder(file1, file2.lastModified());
	}

	/**
	 * 判断文件的最近修改时间比指定时间更早更旧
	 *
	 * @param file 需要判断的文件
	 * @param instant 时间线
	 * @return true->文件存在且比指定时间更早更旧
	 */
	public static boolean isFileOlder(final File file, final Instant instant) {
		AssertTool.notNull(instant, "instant");
		return isFileOlder(file, instant.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
	}

	/**
	 * 判断文件的最近修改时间比指定时间更早更旧
	 *
	 * @param file 需要判断的文件
	 * @param timeMillis 比较时间,毫秒
	 * @return true->文件存在且比指定时间更早更旧
	 */
	public static boolean isFileOlder(final File file, final long timeMillis) {
		checkFile(file);
		return file.lastModified() < timeMillis;
	}

	/**
	 * 判断文件是一个链接,快捷方式
	 * 
	 * @param file 需要判断的文件
	 * @return true->是一个链接,快捷方式
	 */
	public static boolean isSymlink(final File file) {
		checkFile(file);
		return Files.isSymbolicLink(file.toPath());
	}

	/**
	 * 将只读文件完全映射到内存中,慎用,可能造成内存溢出
	 *
	 * @param file 需要映射的文件
	 * @return 文件的buffer
	 * @throws FileNotFoundException if the {@code file} does not exist
	 * @throws IOException if an I/O error occurs
	 * @see FileChannel#map(MapMode, long, long)
	 */
	public static MappedByteBuffer map(File file) throws IOException {
		return map(file, MapMode.READ_ONLY);
	}

	/**
	 * 将只读文件完全映射到内存中,慎用,可能造成内存溢出
	 *
	 * @param file 需要映射的文件
	 * @param mode 映射文件时的模式
	 * @return 文件的buffer
	 * @throws FileNotFoundException if the {@code file} does not exist
	 * @throws IOException if an I/O error occurs
	 * @see FileChannel#map(MapMode, long, long)
	 */
	public static MappedByteBuffer map(File file, MapMode mode) throws IOException {
		return mapInternal(file, mode, -1);
	}

	/**
	 * 将只读文件完全映射到内存中,慎用,可能造成内存溢出
	 *
	 * @param file 需要映射的文件
	 * @param mode 映射文件时的模式
	 * @return 文件的buffer
	 * @throws IOException if an I/O error occurs
	 * @see FileChannel#map(MapMode, long, long)
	 */
	public static MappedByteBuffer map(File file, MapMode mode, long size) throws IOException {
		return mapInternal(file, mode, size);
	}

	/**
	 * 将只读文件完全映射到内存中,慎用,可能造成内存溢出
	 *
	 * @param file 需要映射的文件
	 * @param mode 映射文件时的模式
	 * @return 文件的buffer
	 * @throws IOException if an I/O error occurs
	 * @see FileChannel#map(MapMode, long, long)
	 */
	private static MappedByteBuffer mapInternal(File file, MapMode mode, long size) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		AssertTool.notNull(mode);
		AssertTool.isTrue(size >= 0, "size (%s) may not be negative", size);
		try (RandomAccessFile raf = new RandomAccessFile(file, mode == MapMode.READ_ONLY ? "r" : "rw");
				FileChannel channel = raf.getChannel();) {
			return channel.map(mode, 0, size == -1 ? channel.size() : size);
		}
	}

	/**
	 * 移动一个目录到另一个目录
	 *
	 * @param from 需要移动的目录
	 * @param to 目标目录
	 * @param create true->当目标目录不存在时,创建,false->当目标目录不存在时,不创建
	 * @throws FileExistsException if the directory exists in the destination
	 *         directory
	 * @throws IOException if source or destination is invalid
	 */
	public static void moveDirectory(final File from, final File to, final boolean create) throws IOException {
		if (!to.exists() && create) {
			if (!to.mkdirs()) {
				throw new IOException("Could not create destination directories '" + to + "'");
			}
		}
		if (!to.exists()) {
			throw new FileNotFoundException(
					"Destination directory '" + to + "' does not exist [createDestDir=" + create + "]");
		}
		if (!to.isDirectory()) {
			throw new IOException("Destination '" + to + "' is not a directory");
		}
		moveDirectory(from, new File(to, from.getName()), true);
	}

	/**
	 * 移动文件,若目标文件存在,抛异常
	 *
	 * @param from 源文件
	 * @param to 目标地址
	 * @throws IOException
	 */
	public static void moveFile(final File from, final File to) throws IOException {
		moveFile(from, to, false, false);
	}

	/**
	 * 移动或重命名文件,若文件存在,抛异常
	 * 
	 * @param source 源文件地址绝对路径
	 * @param target 目标文件地址绝对路径
	 * @throws IOException
	 */
	public static void moveFile(String source, String target) throws IOException {
		AssertTool.notBlank(source, "the source file can't be blank");
		AssertTool.notBlank(target, "the target file can't be blank");
		moveFile(Paths.get(source).toFile(), Paths.get(target).toFile(), false, true);
	}

	/**
	 * 复制后剪切文件,若目标文件存在,删除之后再复制或剪切
	 * 
	 * @param from 源文件
	 * @param to 目标地址
	 * @param copy true->复制,false->剪切
	 * @throws IOException
	 */
	public static void moveFile(File from, File to, boolean copy) throws IOException {
		moveFile(from, to, true, copy);
	}

	/**
	 * 复制或剪切文件
	 * 
	 * @param from 源文件
	 * @param to 目标地址
	 * @param replace 若目标文件存在,是否覆盖,true->覆盖,false->不覆盖,抛异常
	 * @param copy true->复制,false->剪切
	 * @throws IOException
	 */
	public static final void moveFile(File from, File to, boolean replace, boolean copy) throws IOException {
		checkFile(from);
		AssertTool.notNull(to);
		AssertTool.isTrue(to.isDirectory(), " the target file is a directory");
		// 目标文件存在时,若replace为true直接覆盖,若为false,抛异常
		if (to.exists() && !replace) {
			throw new IOException("The " + to.getAbsolutePath() + " exist");
		}
		if (copy) {
			// 复制
			copyFile(from, to);
		} else {
			// renameto在同盘内是改名,从A盘移到B盘,相当于剪切.不同文件系统下移动文件会失败
			// if (!from.renameTo(to)) {
			// }
			Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}

	/**
	 * 移动文件到一个目录
	 *
	 * @param from 需要移动的文件
	 * @param to 目标目录
	 * @param create true->当目标目录不存在时,创建,false->当目标目录不存在时,不创建
	 * @throws FileExistsException if the destination file exists
	 * @throws IOException if an IO error occurs moving the file
	 */
	public static void moveFileToDir(final File from, final File to, final boolean create) throws IOException {
		checkFile(from);
		AssertTool.notNull(to, "the destination directory can't be null");
		if (!to.exists() && create) {
			if (!to.mkdirs()) {
				throw new IOException("Could not create destination directories '" + to + "'");
			}
		}
		if (!to.exists()) {
			throw new FileNotFoundException(
					"Destination directory '" + to + "' does not exist [createDestDir=" + create + "]");
		}
		if (!to.isDirectory()) {
			throw new IOException("Destination '" + to + "' is not a directory");
		}
		moveFile(from, new File(to, from.getName()));
	}

	/**
	 * 移动文件或目录到另一个目录
	 *
	 * @param from 需要移动的文件或目录
	 * @param to 目标目录
	 * @param create true->当目标目录不存在时,创建,false->当目标目录不存在时,不创建
	 * @throws FileExistsException if the directory or file exists in the
	 *         destination directory
	 * @throws IOException if source or destination is invalid
	 */
	public static void moveToDirectory(final File from, final File to, final boolean create) throws IOException {
		AssertTool.notNull(from, "the original file or directory can't be null");
		AssertTool.notNull(to, "the destination directory can't be null");
		if (from.isDirectory()) {
			moveDirectory(from, to, create);
		} else {
			moveFileToDir(from, to, create);
		}
	}

	/**
	 * 将文件转换为字节缓冲输入流
	 * 
	 * @param file 文件
	 * @return BufferedInputStream
	 * @throws IOException
	 */
	public static BufferedInputStream newBufferedInputStream(File file) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return new BufferedInputStream(new FileInputStream(file));
	}

	/**
	 * 将文件转换为字节缓冲输入流
	 * 
	 * @param fullPath 文件地址绝对路径
	 * @return BufferedInputStream
	 * @throws IOException
	 */
	public static BufferedInputStream newBufferedInputStream(String fullPath) throws IOException {
		AssertTool.notBlank(fullPath, "the filepath can't be blank");
		return new BufferedInputStream(new FileInputStream(fullPath));
	}

	/**
	 * 将文件转换为字节缓冲输出流
	 * 
	 * @param fullPath 文件地址绝对路径
	 * @param append 文件操作模式,true->追加,false->覆盖
	 * @return BufferedOutputStream
	 * @throws IOException
	 */
	public static BufferedOutputStream newBufferedOutputStream(String fullPath, boolean append) throws IOException {
		AssertTool.notBlank(fullPath, "the filepath can't be blank");
		return new BufferedOutputStream(new FileOutputStream(fullPath, append));
	}

	/**
	 * 将文件转换为字节缓冲输出流
	 * 
	 * @param file 文件
	 * @param append 文件操作模式,true->追加,false->覆盖
	 * @return BufferedOutputStream
	 * @throws IOException
	 */
	public static BufferedOutputStream newBufferedOutputStream(File file, boolean append) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return new BufferedOutputStream(new FileOutputStream(file, append));
	}

	/**
	 * 将文件转换为字符缓冲输入流
	 *
	 * @param file 需要读取的文件
	 * @param charset 字符编码
	 * @return BufferedReader
	 * @throws FileNotFoundException
	 */
	public static BufferedReader newBufferedReader(File file, Charset charset) throws FileNotFoundException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
	}

	/**
	 * 将文件转换为字符缓冲输出流
	 *
	 * @param file 需要写入数据的文件
	 * @return BufferedWriter
	 */
	public static BufferedWriter newBufferedWriter(File file) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return Files.newBufferedWriter(file.toPath(), CharsetTool.defaultCharset());
	}

	/**
	 * 将文件转换为字符缓冲输出流
	 *
	 * @param file 需要写入数据的文件
	 * @param charset 字符编码
	 * @return BufferedWriter
	 */
	public static BufferedWriter newBufferedWriter(File file, Charset charset) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return Files.newBufferedWriter(file.toPath(), CharsetTool.defaultCharset(charset));
	}

	/**
	 * 将文件转换为字符缓冲输出流
	 *
	 * @param file 需要写入数据的文件
	 * @param charset 字符编码
	 * @param options 文件操作模式
	 * @return BufferedWriter
	 * @throws IOException
	 */
	public static BufferedWriter newBufferedWriter(File file, Charset charset, OpenOption... options)
			throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return Files.newBufferedWriter(file.toPath(), CharsetTool.defaultCharset(charset), options);
	}

	/**
	 * 将文件转换为字符缓冲输出流
	 *
	 * @param file 需要写入数据的文件
	 * @param charsetName 字符编码名称
	 * @param options 文件操作模式
	 * @return BufferedWriter
	 * @throws IOException
	 */
	public static BufferedWriter newBufferedWriter(File file, String charsetName, OpenOption... options)
			throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return Files.newBufferedWriter(file.toPath(), CharsetTool.defaultCharset(charsetName), options);
	}

	/**
	 * 将文件转换为字符缓冲输出流
	 * 
	 * @param file 需要写入数据的文件
	 * @param charset 字符编码
	 * @param append 文件操作模式,true->追加,false->覆盖
	 * @return BufferedWriter
	 * @throws IOException
	 */
	public static BufferedWriter newBufferedWriter(File file, Charset charset, boolean append) throws IOException {
		checkFile(file);
		return new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file, append), CharsetTool.defaultCharset(charset)));
	}

	/**
	 * 将文件转换为字符缓冲输出流
	 * 
	 * @param file 需要写入数据的文件
	 * @param charsetName 字符编码名称
	 * @param append 文件操作模式,true->追加,false->覆盖
	 * @return BufferedWriter
	 * @throws IOException
	 */
	public static BufferedWriter newBufferedWriter(File file, String charsetName, boolean append) throws IOException {
		checkFile(file);
		return new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file, append), CharsetTool.defaultCharset(charsetName)));
	}

	/**
	 * 将文件转换为字节输出流
	 * 
	 * @param file 文件
	 * @param append 文件操作模式,true->追加,false->覆盖
	 * @return FileOutputStream
	 * @throws IOException
	 */
	public static FileOutputStream newFileOutputStream(File file, boolean append) throws IOException {
		checkFile(file);
		return new FileOutputStream(file, append);
	}

	/**
	 * 将文件转换为字节输入流
	 * 
	 * @param file 文件
	 * @return 字节输入流
	 * @throws IOException
	 */
	public static InputStream newInputStream(File file) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return Files.newInputStream(file.toPath());
	}

	/**
	 * 将文件转换为字节输入流
	 * 
	 * @param file 文件
	 * @return 字节输入流
	 * @param options 文件操作模式
	 * @throws IOException
	 */
	public static InputStream newInputStream(File file, OpenOption... options) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return Files.newInputStream(file.toPath(), options);
	}

	/**
	 * 将文件转换为字节输出流
	 * 
	 * @param file 文件
	 * @return 字节输出流
	 * @throws IOException
	 */
	public static OutputStream newOutputStream(File file) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return Files.newOutputStream(file.toPath());
	}

	/**
	 * 将文件转换为字节输出流
	 * 
	 * @param file 文件
	 * @param options 文件操作模式
	 * @return 字节输出流
	 * @throws IOException
	 */
	public static OutputStream newOutputStream(File file, OpenOption... options) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return Files.newOutputStream(file.toPath(), options);
	}

	/**
	 * 新建一个字节字符包装输出流,默认覆盖写,需要调用者关闭流
	 * 
	 * @param file 文件
	 * @return OutputStreamWriter
	 * @throws IOException
	 */
	public static OutputStreamWriter newOutputStreamWriter(File file) throws IOException {
		return newOutputStreamWriter(file, CharsetTool.defaultCharset(), false);
	}

	/**
	 * 新建一个字节字符包装输出流,默认覆盖写,需要调用者关闭流
	 * 
	 * @param file 文件
	 * @param charset 字符编码
	 * @return OutputStreamWriter
	 * @throws IOException
	 */
	public static OutputStreamWriter newOutputStreamWriter(File file, Charset charset) throws IOException {
		return newOutputStreamWriter(file, charset, false);
	}

	/**
	 * 新建一个字节字符包装输出流,默认覆盖写,需要调用者关闭流
	 * 
	 * @param file 文件
	 * @param charsetName 字符编码名
	 * @return OutputStreamWriter
	 * @throws IOException
	 */
	public static OutputStreamWriter newOutputStreamWriter(File file, String charsetName) throws IOException {
		return newOutputStreamWriter(file, CharsetTool.defaultCharset(charsetName), false);
	}

	/**
	 * 新建一个字节字符包装输出流,需要调用者关闭流
	 * 
	 * @param file 文件
	 * @param charset 字符编码
	 * @param append 追加写入或覆盖写入,true->追加写入,false->覆盖写入
	 * @return OutputStreamWriter
	 * @throws IOException
	 */
	public static OutputStreamWriter newOutputStreamWriter(File file, Charset charset, boolean append)
			throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		return new OutputStreamWriter(new FileOutputStream(file, append), CharsetTool.defaultCharset(charset));
	}

	/**
	 * 读取文件中的内容输出为一行字符串,流会关闭
	 * 
	 * @param fullPath 文件地址的绝对路径
	 * @throws IOException
	 */
	public static String read(String fullPath) throws IOException {
		return read(new File(fullPath));
	}

	/**
	 * 读取文件中的内容输出为一行字符串,流会关闭
	 * 
	 * @param file 文件
	 * @return 文件内容
	 * @throws FileNotFoundException
	 * @throws IOException in case of I/O errors
	 */
	public static String read(final File file) throws IOException {
		return read(file, CharsetTool.defaultCharset());
	}

	/**
	 * 读取文件中的内容输出为一行字符串,流会关闭
	 *
	 * @param file 文件
	 * @param charset 字符编码集
	 * @return 文件内容
	 * @throws IOException
	 */
	public static String read(final File file, final Charset charset) throws IOException {
		try (InputStream in = newInputStream(file)) {
			return IOTool.copyToString(in, CharsetTool.defaultCharset(charset));
		}
	}

	/**
	 * 读取文件中的内容输出为一行字符串,流会关闭
	 *
	 * @param file 文件
	 * @param charsetName 字符编码名称
	 * @return 文件内容
	 * @throws IOException
	 */
	public static String read(final File file, final String charsetName) throws IOException {
		return read(file, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 读取文件中的内容输出为一行字符串,流已关闭
	 * 
	 * @param file 文件
	 * @return 文件中的字符串
	 * @throws IOException
	 */
	public static String readBuffer(File file) throws IOException {
		if (null == file) {
			return ConstLang.STR_EMPTY;
		}
		StringBuffer sb = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		}
	}

	/**
	 * 按行读取文件内容并输出为集合,流已关闭
	 * 
	 * @param file 文件
	 * @return 文件中的行集合
	 * @throws IOException
	 */
	public static List<String> readLines(File file) throws IOException {
		return readLines(file, ConstLang.DEFAULT_CHARSET);
	}

	/**
	 * 按行读取文件内容并输出为集合,流已关闭
	 *
	 * @param file 文件
	 * @param charset 字符串编码
	 * @return 文件中的行集合
	 * @throws IOException
	 */
	public static List<String> readLines(final File file, final Charset charset) throws IOException {
		if (null == file) {
			return Collections.emptyList();
		}
		try (InputStreamReader in = new InputStreamReader(newInputStream(file), CharsetTool.defaultCharset(charset))) {
			return IOTool.readLines(in);
		}
	}

	/**
	 * 按行读取文件内容并输出为集合
	 *
	 * @param file 文件
	 * @param charsetName 字符编码名称
	 * @return 文件中的行集合
	 * @throws IOException in case of an I/O error
	 * @throws java.nio.charset.UnsupportedCharsetException
	 */
	public static List<String> readLines(final File file, final String charsetName) throws IOException {
		return readLines(file, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 读取文件中的内容转换为byte[],文件流会关闭
	 *
	 * @param file 文件
	 * @return 字节数组
	 * @throws IOException in case of an I/O error
	 */
	public static byte[] readToByte(final File file) throws IOException {
		try (InputStream in = newInputStream(file);) {
			return IOTool.toByteArray(in);
		}
	}

	/**
	 * 利用直接内存读取文件转换为byte[],文件流会关闭
	 *
	 * @param file 文件
	 * @return 字节数组
	 * @throws IOException in case of an I/O error
	 */
	public static byte[] readToByteDirect(final File file) throws IOException {
		MappedByteBuffer mappedByteBuffer = null;
		try (RandomAccessFile tempRaf = new RandomAccessFile(file, "rw");) {
			byte[] dst = new byte[ConstIO.DEFAULT_BUFFER_SIZE_DIRECT];
			long length = file.length();
			mappedByteBuffer = tempRaf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, length);
			for (int offset = 0; offset < length; offset += ConstIO.DEFAULT_BUFFER_SIZE_DIRECT) {
				if (length - offset >= ConstIO.DEFAULT_BUFFER_SIZE_DIRECT) {
					for (int i = 0; i < ConstIO.DEFAULT_BUFFER_SIZE_DIRECT; i++)
						dst[i] = mappedByteBuffer.get(offset + i);
				} else {
					for (int i = 0; i < length - offset; i++)
						dst[i] = mappedByteBuffer.get(offset + i);
				}
			}
			return mappedByteBuffer.array();
		} finally {
			if (mappedByteBuffer != null) {
				mappedByteBuffer.clear();
			}
		}
	}

	/**
	 * 将file://开头的URL资源转换为文件,URL地址将会decode
	 *
	 * @param url URL资源地址,file://开头
	 * @return 文件
	 */
	public static File toFile(final URL url) {
		if (url == null || !"file".equalsIgnoreCase(url.getProtocol())) {
			return null;
		}
		String filename = url.getFile().replace('/', File.separatorChar);
		filename = UrlTool.decodeUrl(filename);
		return new File(filename);
	}

	/**
	 * 将file://开头的URL资源转换为文件,URL地址将会decode
	 *
	 * @param url URL资源地址,file://开头
	 * @return 文件
	 * @throws IllegalArgumentException
	 */
	public static File[] toFiles(final URL... urls) {
		if (urls == null || urls.length == 0) {
			return ConstArray.EMPTY_FILE;
		}
		final File[] files = new File[urls.length];
		for (int i = 0; i < urls.length; i++) {
			final URL url = urls[i];
			if (url != null) {
				if (url.getProtocol().equals("file") == false) {
					throw new IllegalArgumentException("URL could not be converted to a File: " + url);
				}
				files[i] = toFile(url);
			}
		}
		return files;
	}

	/**
	 * 将文件转换为file://开头的URL资源
	 *
	 * @param files 需要转换的文件列表
	 * @return URL资源地址集合
	 * @throws IOException if a file cannot be converted
	 */
	public static URL[] toURLs(final File... files) throws IOException {
		final URL[] urls = new URL[files.length];
		for (int i = 0; i < urls.length; i++) {
			urls[i] = files[i].toURI().toURL();
		}
		return urls;
	}

	/**
	 * 解压缩 FIXME
	 * 
	 * @param source 源数据,需要解压的数据
	 * @return 解压后的数据,恢复的数据
	 * @throws IOException
	 */
	public static byte[] unzip(byte[] source) throws IOException {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				ByteArrayInputStream in = new ByteArrayInputStream(source);
				GZIPInputStream zipIn = new GZIPInputStream(in);) {
			byte[] temp = new byte[1024];
			int length = 0;
			while ((length = zipIn.read(temp, 0, temp.length)) != -1) {
				out.write(temp, 0, length);
			}
			return out.toByteArray();
		}
	}

	/**
	 * 将字节数组中的内容写到文件中,覆盖写入
	 *
	 * @param file 写入数据的文件
	 * @param data 写入的数据
	 * @throws IOException in case of an I/O error
	 */
	public static void write(final File file, final byte[] data) throws IOException {
		write(file, data, false);
		// try {
		// IOTool.copy(new ByteArrayInputStream(data),
		// Files.newOutputStream(file.toPath()));
		// } catch (IOException e) {
		// ResultException.throwResultException(e);
		// }
	}

	/**
	 * 将字节数组中的内容写到文件中
	 *
	 * @param file 写入数据的文件
	 * @param data 写入的数据
	 * @param append 文件操作模式,true->追加写入,false->覆盖写入
	 * @throws IOException in case of an I/O error
	 */
	public static void write(final File file, final byte[] data, final boolean append) throws IOException {
		write(file, data, 0, data.length, append);
	}

	/**
	 * 将字节数组中的内容写到文件中,覆盖写入
	 *
	 * @param file 写入数据的文件
	 * @param data 写入的数据
	 * @param off 写入字节数组的起始索引
	 * @param len 写入字节数组的长度
	 * @throws IOException in case of an I/O error
	 */
	public static void write(final File file, final byte[] data, final int off, final int len) throws IOException {
		write(file, data, off, len, false);
	}

	/**
	 * 将字节数组中的内容写到文件中
	 *
	 * @param file 写入数据的文件
	 * @param data 写入的数据
	 * @param off 写入字节数组的起始索引
	 * @param len 写入字节数组的长度
	 * @param append 文件操作模式,true->追加写入,false->覆盖写入
	 * @throws IOException in case of an I/O error
	 */
	public static void write(final File file, final byte[] data, final int off, final int len, final boolean append)
			throws IOException {
		try (OutputStream out = newFileOutputStream(file, append)) {
			out.write(data, off, len);
		}
	}

	/**
	 * 将字符串追加写入到文件中
	 * 
	 * @param file 文件
	 * @param content 写入的字符串
	 * @throws IOException
	 */
	public static void write(File file, String content) throws IOException {
		write(file, content, true);
	}

	/**
	 * 将字符串写入到文件中
	 * 
	 * @param file 文件
	 * @param content 写入的字符串
	 * @param append true->追加写入,false->覆盖写入
	 * @throws IOException
	 */
	public static void write(File file, String content, boolean append) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		byte[] bytes = content.getBytes(ConstLang.DEFAULT_CHARSET);
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file, append));) {
			bos.write(bytes);
		}
	}

	/**
	 * 将字符串写入到文件中
	 * 
	 * @param file 文件
	 * @param content 写入的字符串
	 * @param charsetName 字符编码名称
	 * @param append true->追加写入,false->覆盖写入
	 * @throws IOException
	 */
	public static void write(File file, String content, String charsetName, boolean append) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		byte[] bytes = content.getBytes(CharsetTool.defaultCharset(charsetName));
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file, append));) {
			bos.write(bytes);
		}
	}

	/**
	 * 将字符串写入到文件中
	 * 
	 * @param file 文件
	 * @param content 写入的字符串
	 * @param charset 字符编码
	 * @param append true->追加写入,false->覆盖写入
	 * @throws IOException
	 */
	public static void write(File file, String content, Charset charset, boolean append) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		byte[] bytes = content.getBytes(CharsetTool.defaultCharset(charset));
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file, append));) {
			bos.write(bytes);
		}
	}

	/**
	 * 将字节数组追加写入到文件中
	 * 
	 * @param file 文件
	 * @param input 输入流
	 * @throws IOException
	 */
	public static void write(File file, InputStream input) throws IOException {
		write(file, input, true);
	}

	/**
	 * 将字节数组写入到文件中
	 * 
	 * @param file 文件
	 * @param input 输入流
	 * @param append true->追加写入,false->覆盖写入
	 * @throws IOException
	 */
	public static void write(File file, InputStream input, boolean append) throws IOException {
		AssertTool.notNull(file, ConstIO.TOAST_FILE_NULL);
		try (OutputStream bos = newFileOutputStream(file, append);) {
			IOTool.copy(input, bos);
		}
	}

	/**
	 * 读取URL中的资源
	 * 
	 * @param destFile 目标文件
	 * @param source URL源
	 * @throws IOException
	 */
	public static void write(final File destFile, final URL source) throws IOException {
		try (final InputStream stream = source.openStream()) {
			write(destFile, stream);
		}
	}

	/**
	 * 读取URL中的资源
	 * 
	 * @param destFile 目标文件
	 * @param source URL源
	 * @param connectionTimeout 连接超时时间
	 * @param readTimeout 读取超时时间
	 * @throws IOException
	 */
	public static void write(final File destFile, final URL source, final int connectionTimeout, final int readTimeout)
			throws IOException {
		final URLConnection connection = source.openConnection();
		connection.setConnectTimeout(connectionTimeout);
		connection.setReadTimeout(readTimeout);
		try (final InputStream stream = connection.getInputStream()) {
			write(destFile, stream);
		}
	}

	/**
	 * 将字符串追加写入到文件中
	 * 
	 * @param fullPath 文件地址绝对路径
	 * @param content 写入的字符串
	 * @throws IOException
	 */
	public static void write(String fullPath, String content) throws IOException {
		write(new File(fullPath), content);
	}

	/**
	 * 将集合中的数据通过toString()一行一行写入文件中,默认覆盖写入,UTF8编码
	 *
	 * @param file 写入数据的文件
	 * @param lines 写入的数据
	 * @throws IOException in case of an I/O error
	 */
	public static void writeLines(final File file, final Collection<?> lines) throws IOException {
		writeLines(file, null, lines, null, false);
	}

	/**
	 * 将集合中的数据通过toString()一行一行写入文件中,UTF8编码
	 *
	 * @param file 写入数据的文件
	 * @param lines 写入的数据
	 * @param append 文件操作模式,true->追加写入,false->覆盖写入
	 * @throws IOException in case of an I/O error
	 */
	public static void writeLines(final File file, final Collection<?> lines, final boolean append) throws IOException {
		writeLines(file, null, lines, null, append);
	}

	/**
	 * 将集合中的数据通过toString()一行一行写入文件中,覆盖写入,UTF8编码
	 * 
	 * @param file 写入数据的文件
	 * @param lines 写入的数据
	 * @param lineEnding 文件换行符
	 * @throws IOException in case of an I/O error
	 */
	public static void writeLines(final File file, final Collection<?> lines, final String lineEnding)
			throws IOException {
		writeLines(file, null, lines, lineEnding, false);
	}

	/**
	 * 将集合中的数据通过toString()一行一行写入文件中,UTF8编码
	 *
	 * @param file 写入数据的文件
	 * @param lines 写入的数据
	 * @param lineEnding 文件换行符
	 * @param append 文件操作模式,true->追加写入,false->覆盖写入
	 * @throws IOException in case of an I/O error
	 */
	public static void writeLines(final File file, final Collection<?> lines, final String lineEnding,
			final boolean append) throws IOException {
		writeLines(file, null, lines, lineEnding, append);
	}

	/**
	 * 将集合中的数据通过toString()一行一行写入文件中,覆盖写入
	 *
	 * @param file 写入数据的文件
	 * @param charset 字符编码
	 * @param lines 写入的数据
	 * @throws IOException in case of an I/O error
	 * @throws java.io.UnsupportedEncodingException if the encoding is not supported
	 *         by the VM
	 */
	public static void writeLines(final File file, final Charset charset, final Collection<?> lines)
			throws IOException {
		writeLines(file, charset, lines, null, false);
	}

	/**
	 * 将集合中的数据通过toString()一行一行写入文件中
	 *
	 * @param file 写入数据的文件
	 * @param charset 字符编码
	 * @param lines 写入的数据
	 * @param append 文件操作模式,true->追加写入,false->覆盖写入
	 * @throws IOException in case of an I/O error
	 * @throws java.io.UnsupportedEncodingException if the encoding is not supported
	 *         by the VM
	 */
	public static void writeLines(final File file, final Charset charset, final Collection<?> lines,
			final boolean append) throws IOException {
		writeLines(file, charset, lines, null, append);
	}

	/**
	 * 将集合中的数据通过toString()一行一行写入文件中.覆盖写入
	 * 
	 * @param file 写入数据的文件
	 * @param charsetName 字符编码名称
	 * @param lines 写入的数据
	 * @param lineEnding 文件换行符
	 * @throws IOException in case of an I/O error
	 * @throws java.io.UnsupportedEncodingException if the encoding is not supported
	 *         by the VM
	 */
	public static void writeLines(final File file, final String charsetName, final Collection<?> lines,
			final String lineEnding) throws IOException {
		writeLines(file, CharsetTool.defaultCharset(charsetName), lines, lineEnding, false);
	}

	/**
	 * 将集合中的数据通过toString()一行一行写入文件中.覆盖写入
	 * 
	 * @param file 写入数据的文件
	 * @param charset 字符编码
	 * @param lines 写入的数据
	 * @param lineEnding 文件换行符
	 * @throws IOException in case of an I/O error
	 * @throws java.io.UnsupportedEncodingException if the encoding is not supported
	 *         by the VM
	 */
	public static void writeLines(final File file, final Charset charset, final Collection<?> lines,
			final String lineEnding) throws IOException {
		writeLines(file, charset, lines, lineEnding, false);
	}

	/**
	 * 将集合中的数据通过toString()一行一行写入文件中
	 *
	 * @param file 写入数据的文件
	 * @param charset 字符编码
	 * @param lines 写入的数据
	 * @param lineEnding 文件换行符
	 * @param append 文件操作模式,true->追加写入,false->覆盖写入
	 * @throws IOException in case of an I/O error
	 * @throws java.io.UnsupportedEncodingException if the encoding is not supported
	 *         by the VM
	 */
	public static void writeLines(final File file, final Charset charset, final Collection<?> lines,
			final String lineEnding, final boolean append) throws IOException {
		try (OutputStream out = newBufferedOutputStream(file, append)) {
			IOTool.writeLines(lines, lineEnding, out, charset);
		}
	}

	/**
	 * 压缩 FIXME
	 * 
	 * @param source 源数据,需要压缩的数据
	 * @return 压缩后的数据
	 * @throws IOException
	 */
	public static byte[] zip(byte[] source) throws IOException {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				GZIPOutputStream zipOut = new GZIPOutputStream(out);) {
			// 将压缩信息写入到内存, 写入的过程会实现解压
			zipOut.write(source);
			zipOut.finish();
			return out.toByteArray();
		}
	}

	/**
	 * 文件压缩 FIXME
	 * 
	 * @param srcPath
	 * @param desPath
	 * @throws IOException
	 */
	public static void zipFile(String srcPath, String desPath) throws IOException {
		checkFile(srcPath);
		checkFile(desPath);
		File file = new File(desPath);
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
				InputStream is = new FileInputStream(srcPath);) {
			ZipEntry zipEntry = new ZipEntry(file.getName());
			zipEntry.setSize(is.available());
			zos.putNextEntry(zipEntry);
			int size = 0;
			while ((size = is.read()) != -1) {
				zos.write(size);
			}
		}
	}

	/**
	 * 使用文件channel压缩文件 FIXME
	 * 
	 * @param path 需要进行压缩的文件地址
	 * @throws IOException
	 */
	public static void zipChannel(String srcPath, String desPath) throws IOException {
		checkFile(srcPath);
		checkFile(desPath);
		File file = new File(srcPath);
		File desFile = new File(desPath);
		try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(desFile));
				WritableByteChannel channel = Channels.newChannel(zipOutputStream);
				FileInputStream fis = new FileInputStream(file);
				FileChannel fileChannel = fis.getChannel();) {
			zipOutputStream.putNextEntry(new ZipEntry("test"));
			fileChannel.transferTo(0, file.getTotalSpace(), channel);
		}
	}

	/**
	 * 使用Map映射文件压缩文件 FIXME
	 * 
	 * @throws IOException
	 */
	public static void zipMap(String srcPath, String desPath) throws IOException {
		File zipFile = new File(desPath);
		File file = new File(srcPath);
		try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
				WritableByteChannel writableByteChannel = Channels.newChannel(zipOut);
				RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");) {
			zipOut.putNextEntry(new ZipEntry(".zip"));
			// 内存中的映射文件
			MappedByteBuffer mappedByteBuffer =
					randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, 1024);
			writableByteChannel.write(mappedByteBuffer);
		}
	}

	/**
	 * 异步压缩文件 FIXME
	 * 
	 * @param srcPath
	 * @param desPath
	 * @throws IOException
	 */
	public static void zipPip(String srcPath, String desPath) throws IOException {
		try (WritableByteChannel out = Channels.newChannel(new FileOutputStream(desPath))) {
			Pipe pipe = Pipe.open();
			// 异步任务
			CompletableFuture.runAsync(() -> {
				try (ZipOutputStream zos = new ZipOutputStream(Channels.newOutputStream(pipe.sink()));
						WritableByteChannel innerOut = Channels.newChannel(zos);
						FileChannel jpgChannel = new FileInputStream(new File(srcPath)).getChannel();) {
					zos.putNextEntry(new ZipEntry(".zip"));
					jpgChannel.transferTo(0, new File(srcPath).getTotalSpace(), innerOut);
					jpgChannel.close();
				} catch (Exception e) {
					e.printStackTrace();
					throw new ResultException(e);
				}
			});
			// 获取读通道
			ReadableByteChannel readableByteChannel = pipe.source();
			ByteBuffer buffer = ByteBuffer.allocate(((int) new File(srcPath).getTotalSpace()) * 10);
			while (readableByteChannel.read(buffer) >= 0) {
				buffer.flip();
				out.write(buffer);
				buffer.clear();
			}
		}
	}
}

/**
 * 例子
 * 
 * FIXME 未测试
 * 
 * @author 飞花梦影
 * @date 2021-12-07 15:16:41
 * @git {@link https://github.com/dreamFlyingFlower }
 */
class Example {

	/**
	 * 将多个文件流合并到一个流中
	 * 
	 * @param files 文件列表
	 * @return 文件合并后的流
	 * @throws IOException
	 */
	public static byte[] combine(List<File> files) throws IOException {
		if (null == files || files.size() == 0) {
			return ConstArray.EMPTY_BYTE;
		}
		try (FileInputStream fis = new FileInputStream(files.get(0));) {
			if (files.size() == 1) {
				return IOTool.toByteArray(fis);
			}
			FileInputStream inputStream = new FileInputStream(files.get(1));
			SequenceInputStream sis = new SequenceInputStream(fis, inputStream);
			if (files.size() == 2) {
				return IOTool.toByteArray(sis);
			}
			for (int i = 2; i < files.size(); i++) {
				sis = new SequenceInputStream(sis, new FileInputStream(files.get(i)));
			}
			sis.close();
		}
		return null;
	}
}