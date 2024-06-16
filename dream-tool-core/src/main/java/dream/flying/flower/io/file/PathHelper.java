package dream.flying.flower.io.file;

import static java.nio.file.FileVisitOption.FOLLOW_LINKS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dream.flying.flower.ConstIO;
import dream.flying.flower.helper.CharsetHelper;
import dream.flying.flower.lang.AssertHelper;

/**
 * Path工具类,File的进阶版 {@link Path},{@link Files} FIXME
 * 
 * @author 飞花梦影
 * @date 2021-03-08 15:04:04
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class PathHelper {

	/** 空 {@link LinkOption} 数组,用于复制或剪切 */
	public static final LinkOption[] EMPTY_LINK_OPTION_ARRAY = new LinkOption[0];

	/** 空 {@link OpenOption} 数组,用于打开文件 */
	public static final OpenOption[] EMPTY_OPEN_OPTION_ARRAY = new OpenOption[0];

	/**
	 * 判断path存在且是一个文件
	 * 
	 * @param path 路径path
	 * @throws IOException
	 */
	public static void checkFile(Path path) throws IOException {
		AssertHelper.notNull(path);
		if (!isFile(path, false)) {
			throw new IOException("the path is not a file");
		}
	}

	/**
	 * 判断path存在且是一个目录
	 * 
	 * @param path 路径path
	 * @throws IOException
	 */
	public static void checkDir(Path path) throws IOException {
		AssertHelper.notNull(path);
		if (!isDir(path, false)) {
			throw new IOException("the path is not a directory");
		}
	}

	/**
	 * 递归复制文件或目录,不支持软连接
	 * 
	 * @param source 源目录
	 * @param target 目的目录
	 * @throws IOException in the case of I/O errors
	 */
	public static void copy(Path source, Path target) throws IOException {
		AssertHelper.notNull(source, "Source Path must not be null");
		AssertHelper.notNull(target, "Target Path must not be null");
		BasicFileAttributes srcAttr = Files.readAttributes(source, BasicFileAttributes.class);
		if (srcAttr.isDirectory()) {
			Files.walkFileTree(source, EnumSet.of(FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					Files.createDirectories(target.resolve(source.relativize(dir)));
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
					return FileVisitResult.CONTINUE;
				}
			});
		} else if (srcAttr.isRegularFile()) {
			Files.copy(source, target);
		} else {
			throw new IllegalArgumentException("Source File must denote a directory or file");
		}
	}

	/**
	 * 文件复制
	 * 
	 * <pre>
	 * 1.source为目录,target也为目录或不存在,则拷贝整个source目录到target目录下
	 * 2.source为文件,target为目录或不存在,则拷贝文件到目标目录下
	 * 3.source为文件,target也为文件,若target文件存在,默认覆盖
	 * </pre>
	 *
	 * @param source 源文件路径,如果为目录会在目标中创建新目录
	 * @param target 目标文件或目录,如果为目录使用与源文件相同的文件名
	 * @param options 复制模式,{@link StandardCopyOption},{@link LinkOption}
	 * @return Path
	 * @throws IOException
	 */
	public static Path copy(Path source, Path target, CopyOption... options) throws IOException {
		if (isDir(source)) {
			return copyDir(source, target.resolve(source.getFileName()), options);
		}
		return copyFile(source, target, options);
	}

	/**
	 * 拷贝目录下的所有文件或目录到目标目录中,此方法不支持文件对文件的拷贝
	 * 
	 * <pre>
	 * 1.source为目录,target也为目录或不存在,则拷贝source目录下所有文件和目录到target目录下
	 * 2.source为文件,target为目录或不存在,则拷贝文件到target目录下
	 * </pre>
	 *
	 * @param source 源文件路径,如果为目录只在目标中创建新目录
	 * @param target 目标目录,如果为目录使用与源文件相同的文件名
	 * @param options 复制模式,{@link StandardCopyOption},{@link LinkOption}
	 * @return Path
	 * @throws IOException
	 */
	public static Path copyDir(Path source, Path target, CopyOption... options) throws IOException {
		Files.walkFileTree(source, new CopyVisitor(source, target));
		return target;
	}

	/**
	 * 文件复制,不支持递归拷贝目录,如果source是目录,只会在目标目录中创建空目录
	 * 
	 * <pre>
	 * 1.默认情况下,若目标文件已经存在或是软连接,复制失败,除非是同文件
	 * 2.当source是目录时,只复制空目录,不递归复制目录中的内容
	 * 3.当source是软连接时,判断options是否支持软连接,支持则复制最终目标
	 * </pre>
	 * 
	 * @param source 源文件路径,如果为目录只在目标中创建新目录
	 * @param target 目标文件或目录,如果为目录使用与源文件相同的文件名
	 * @param options {@link StandardCopyOption}
	 * @return Path
	 * @throws IOException
	 * @see {@link Files#copy(Path, Path, CopyOption...)}
	 */
	public static Path copyFile(Path source, Path target, CopyOption... options) throws IOException {
		AssertHelper.notNull(source, "The source file can't be null !");
		AssertHelper.notNull(target, "The destination file or directiory can't be null !");
		return Files.copy(source, target, options);
	}

	/**
	 * 读取URL资源到文件中
	 *
	 * @param source URL资源
	 * @param target 目标文件
	 * @return 目标文件
	 * @throws IOException if an I/O error occurs
	 * @see Files#copy(InputStream, Path, CopyOption...)
	 */
	public static Path copyFile(final URL source, final Path target) throws IOException {
		try (final InputStream inputStream = source.openStream()) {
			Files.copy(inputStream, target);
			return target;
		}
	}

	/**
	 * 读取URL资源到文件中
	 *
	 * @param source URL资源
	 * @param target 目标文件
	 * @param options 复制模式,{@link StandardCopyOption}
	 * @return 目标文件
	 * @throws IOException if an I/O error occurs
	 * @see Files#copy(InputStream, Path, CopyOption...)
	 */
	public static Path copyFile(final URL source, final Path target, final CopyOption... options) throws IOException {
		try (final InputStream inputStream = source.openStream()) {
			Files.copy(inputStream, target, options);
			return target;
		}
	}

	/**
	 * 将文件复制到另外的目录中
	 *
	 * @param source 文件
	 * @param target 目标目录
	 * @return 目标文件
	 * @throws IOException if an I/O error occurs
	 * @see Files#copy(Path, Path, CopyOption...)
	 */
	public static Path copyFileToDirectory(final Path source, final Path target) throws IOException {
		return Files.copy(source, target.resolve(source.getFileName()));
	}

	/**
	 * 将文件复制到另外的目录中
	 *
	 * @param source 文件
	 * @param target 目标目录
	 * @param options 复制模式,{@link StandardCopyOption}
	 * @return 目标文件
	 * @throws IOException if an I/O error occurs
	 * @see Files#copy(Path, Path, CopyOption...)
	 */
	public static Path copyFileToDirectory(final Path source, final Path target, final CopyOption... options)
			throws IOException {
		return Files.copy(source, target.resolve(source.getFileName()), options);
	}

	/**
	 * 将URL资源复制到目录中
	 *
	 * @param source URL资源
	 * @param target 目标目录
	 * @return 目标目录Path
	 * @throws IOException if an I/O error occurs
	 * @see Files#copy(InputStream, Path, CopyOption...)
	 */
	public static Path copyFileToDirectory(final URL source, final Path target) throws IOException {
		try (final InputStream inputStream = source.openStream()) {
			Files.copy(inputStream, target.resolve(source.getFile()));
			return target;
		}
	}

	/**
	 * 将URL资源复制到目录中
	 *
	 * @param source URL资源
	 * @param target 目标目录
	 * @param options 复制模式,{@link StandardCopyOption}
	 * @return 目标目录Path
	 * @throws IOException if an I/O error occurs
	 * @see Files#copy(InputStream, Path, CopyOption...)
	 */
	public static Path copyFileToDirectory(final URL source, final Path target, final CopyOption... options)
			throws IOException {
		try (final InputStream inputStream = source.openStream()) {
			Files.copy(inputStream, target.resolve(source.getFile()), options);
			return target;
		}
	}

	/**
	 * 删除文件或者目录,不支持软连接.目录会递归删除
	 *
	 * @param path 文件对象
	 * @return 成功与否
	 */
	public static boolean delete(Path path) throws IOException {
		if (Files.notExists(path)) {
			return true;
		}
		return isDir(path) ? deleteRecursive(path) : Files.deleteIfExists(path);
	}

	/**
	 * 删除文件.若被删除的是目录,且目录中有文件,抛异常
	 * 
	 * @param path 文件路径
	 * @return true->删除成功
	 * @throws IOException
	 * @see {@link Files#deleteIfExists(Path)}
	 */
	public static boolean deleteIfExists(Path path) throws IOException {
		return Files.deleteIfExists(path);
	}

	/**
	 * 删除文件,如果是目录,递归删除目录中的目录和文件,目录本身也会删除
	 * 
	 * @param root 需要删除的文件Path
	 * @return true当文件被删除或文件本身不存在
	 * @throws IOException in the case of I/O errors
	 */
	public static boolean deleteRecursive(Path root) throws IOException {
		if (root == null || !Files.exists(root)) {
			return true;
		}
		Files.walkFileTree(root, new DeleteVisitor());
		return true;
	}

	/**
	 * 判断文件或目录是否存在,不支持软连接
	 *
	 * @param path 文件
	 * @return 是否存在
	 */
	public static boolean exists(Path path) {
		return exists(path, false);
	}

	/**
	 * 判断文件或目录是否存在
	 *
	 * @param path 文件
	 * @param followLink 是否跟踪软链,快捷方式,true->是,false->不是
	 * @return 是否存在
	 */
	public static boolean exists(Path path, boolean followLink) {
		final LinkOption[] options =
				followLink ? EMPTY_LINK_OPTION_ARRAY : new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
		return Files.exists(path, options);
	}

	/**
	 * 从文件属性读取访问控制列表
	 *
	 * @param source 源文件Path
	 * @return List<AclEntry>
	 * @throws IOException if an I/O error occurs.
	 */
	public static List<AclEntry> getAclEntrys(final Path source) throws IOException {
		final AclFileAttributeView fileAttributeView = Files.getFileAttributeView(source, AclFileAttributeView.class);
		return fileAttributeView == null ? Collections.emptyList() : fileAttributeView.getAcl();
	}

	/**
	 * 获取文件属性
	 *
	 * @param path 文件路径{@link Path}
	 * @param followLink 是否跟踪到软链对应的真实路径
	 * @return {@link BasicFileAttributes}
	 * @throws IOException
	 */
	public static BasicFileAttributes getAttributes(Path path, boolean followLink) throws IOException {
		if (null == path) {
			return null;
		}
		final LinkOption[] options =
				followLink ? EMPTY_LINK_OPTION_ARRAY : new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
		return Files.readAttributes(path, BasicFileAttributes.class, options);
	}

	/**
	 * 获得文件名的后缀,不带点
	 * 
	 * @param path Path
	 * @return 文件名后缀,不带点,可能为""
	 */
	public static String getFileExtension(Path path) {
		AssertHelper.notNull(path, "The path of the file can't be null");
		return FileHelper.getFileExtension(path.toFile());
	}

	/**
	 * 递归遍历目录以及子目录中的所有文件,如果path为文件,直接返回过滤结果
	 *
	 * @param path 当前遍历文件或目录
	 * @param maxDepth 遍历最大深度,负数和0表示遍历到没有目录为止
	 * @param fileFilter 文件过滤规则对象,选择要保留的文件,只对文件有效,不过滤目录,null表示接收全部文件
	 * @return 文件列表
	 * @throws IOException
	 */
	public static List<File> getFiles(Path path, FileFilter fileFilter) throws IOException {
		if (null == path || !Files.exists(path)) {
			return Collections.emptyList();
		}
		final List<File> result = new ArrayList<>();
		if (isFile(path)) {
			final File file = path.toFile();
			if (null == fileFilter || fileFilter.accept(file)) {
				result.add(file);
			}
			return result;
		}
		walkFileTree(path, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
				final File file = path.toFile();
				if (null == fileFilter || fileFilter.accept(file)) {
					result.add(file);
				}
				return FileVisitResult.CONTINUE;
			}
		});
		return result;
	}

	/**
	 * 获得文件的MimeType,文件内容类型
	 *
	 * @param file 文件
	 * @return MimeType
	 * @throws IOException
	 * @see Files#probeContentType(Path)
	 */
	public static String getMimeType(Path file) throws IOException {
		return Files.probeContentType(file);
	}

	/**
	 * 获得不带点和后缀的文件名
	 * 
	 * @param path Path
	 * @return 不带点和后缀的文件名
	 */
	public static String getNameNoExtension(Path path) {
		AssertHelper.notNull(path, "The path of the file can't be null");
		return FileHelper.getNameNoExtension(path.toFile());
	}

	/**
	 * 获得当前路径的父路径,若当前路径是根目录,返回null
	 * 
	 * @param path 路径
	 * @return 父路径或null
	 */
	public static Path getParent(Path path) {
		return path.getNameCount() == 0 ? null : path.getName(path.getNameCount() - 1);
	}

	/**
	 * 获得当前路径从起始路径到index索引的父路径,index为普通索引用法,从0开始
	 *
	 * @param path 路径
	 * @param index 路径节点位置,小于0默认为0,大于路径长度,默认最大路径长度
	 * @return 父路径
	 */
	public static Path getParent(Path path, int index) {
		int count = path.getNameCount();
		if (count == 0) {
			return null;
		}
		return path.getName(index <= 0 ? 0 : index >= count ? index = count - 1 : index);
	}

	/**
	 * 判断是否为目录,如果path为null返回false,软连接返回false
	 *
	 * @param path {@link Path}
	 * @return 如果为目录true
	 */
	public static boolean isDir(Path path) {
		return isDir(path, false);
	}

	/**
	 * 判断是否为目录,如果file为path返回false
	 *
	 * @param path {@link Path}
	 * @param followLink 是否追踪到软链对应的真实地址,true->是,false->否
	 * @return true->是目录,false->否
	 */
	public static boolean isDir(Path path, boolean followLink) {
		if (null == path) {
			return false;
		}
		final LinkOption[] options =
				followLink ? EMPTY_LINK_OPTION_ARRAY : new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
		return Files.isDirectory(path, options);
	}

	/**
	 * 判断目录是空的,文件中没有内容
	 *
	 * @param path 文件
	 * @return true->是,false->否
	 * @throws IOException
	 */
	public static boolean isEmpty(final Path path) throws IOException {
		return Files.isDirectory(path) ? isEmptyDirectory(path) : isEmptyFile(path);
	}

	/**
	 * 判断目录为空
	 *
	 * @param path 目录
	 * @return true->是,false->否
	 * @throws IOException
	 */
	public static boolean isEmptyDirectory(Path path) throws IOException {
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {
			return !dirStream.iterator().hasNext();
		}
	}

	/**
	 * 判断文件为空,实际是判断文件中的字节数为0
	 * 
	 * @param file 文件
	 * @return true->是,false->否
	 * @throws IOException
	 */
	public static boolean isEmptyFile(final Path file) throws IOException {
		return Files.size(file) <= 0;
	}

	/**
	 * 判断是否为文件,如果path为null返回false,软连接返回false
	 *
	 * @param path 文件
	 * @return true->是文件,false->不是文件
	 */
	public static boolean isFile(Path path) {
		return isFile(path, false);
	}

	/**
	 * 判断是否为文件,如果path为null返回false
	 *
	 * @param path 文件
	 * @param followLink 是否支持软连接,true->支持,false->不支持
	 * @return true->是文件,false->不是文件
	 * @see Files#isRegularFile(Path, LinkOption...)
	 */
	public static boolean isFile(Path path, boolean followLink) {
		if (null == path) {
			return false;
		}
		final LinkOption[] options =
				followLink ? EMPTY_LINK_OPTION_ARRAY : new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
		return Files.isRegularFile(path, options);
	}

	/**
	 * 检查两个文件是否是同一个文件
	 *
	 * @param file1 文件1
	 * @param file2 文件2
	 * @return 是否相同,true->相同,false->不同
	 * @throws IOException
	 */
	public static boolean isSameFile(Path file1, Path file2) throws IOException {
		return Files.isSameFile(file1, file2);
	}

	/**
	 * 判断路径是一个链接,快捷方式
	 * 
	 * @param path 需要判断的path
	 * @return true->是一个链接,快捷方式
	 */
	public static boolean isSymlink(final Path path) {
		AssertHelper.notNull(path);
		return Files.isSymbolicLink(path);
	}

	/**
	 * 创建所给目录及其父目录
	 *
	 * @param dir 目录
	 * @return 目录
	 * @throws IOException
	 */
	public static Path mkdirs(Path dir) throws IOException {
		if (null != dir && !false == exists(dir, false)) {
			Files.createDirectories(dir);
		}
		return dir;
	}

	/**
	 * 创建所给文件或目录的父目录
	 *
	 * @param path 文件或目录
	 * @return 父目录
	 * @throws IOException
	 */
	public static Path mkParentDirs(Path path) throws IOException {
		return mkdirs(path.getParent());
	}

	/**
	 * 移动或重命名文件
	 * 
	 * @param source 源文件
	 * @param target 目标文件
	 * @throws IOException
	 * @see {@link java.nio.file.Path} {@link java.nio.file.Files#move}
	 */
	public static void move(Path source, Path target) throws IOException {
		move(source, target, true);
	}

	/**
	 * 移动文件或目录,当目标是目录时,会将源文件或文件夹整体移动至目标目录下
	 *
	 * @param source 源文件或目录路径
	 * @param target 目标路径,如果为目录,则移动到此目录下
	 * @param cover 是否覆盖目标文件,true->覆盖,false->不覆盖
	 * @return 目标文件Path
	 * @throws IOException
	 */
	public static Path move(Path source, Path target, boolean cover) throws IOException {
		AssertHelper.notNull(source, "Source path must be not null !");
		AssertHelper.notNull(target, "Target path must be not null !");
		final CopyOption[] options =
				cover ? new CopyOption[] { StandardCopyOption.REPLACE_EXISTING } : new CopyOption[] {};
		if (isDir(target)) {
			target = target.resolve(source.getFileName());
		}
		mkParentDirs(target);
		return Files.move(source, target, options);
	}

	/**
	 * 获得字符输入流
	 *
	 * @param path 文件Path
	 * @return BufferedReader
	 * @throws IOException
	 */
	public static BufferedReader newBufferedReader(Path path) throws IOException {
		return Files.newBufferedReader(path, CharsetHelper.defaultCharset());
	}

	/**
	 * 获得字符输入流
	 *
	 * @param path 文件Path
	 * @return BufferedReader
	 * @throws IOException
	 */
	public static BufferedReader newBufferedReader(Path path, Charset charset) throws IOException {
		return Files.newBufferedReader(path, CharsetHelper.defaultCharset(charset));
	}

	/**
	 * 从文件中读取字节输入流
	 *
	 * @param path 需要打开的文件路径
	 * @param options 文件操作模式,{@link StandardOpenOption}
	 * @return 输入流
	 * @throws IOException if an I/O error occurs
	 * @see {@link Files#newInputStream(Path, OpenOption...)}
	 */
	public static InputStream newInputStream(Path path, OpenOption... options) throws IOException {
		AssertHelper.notNull(path, "the path can't be null");
		return Files.newInputStream(path, options);
	}

	/**
	 * 打开或创建一个字节输出流,线程安全
	 *
	 * <pre>
	 * 1.CREATE,TRUNCATE_EXISTING,WRITE:默认.文件存在,删除后创建;不存在则直接创建;追加写入
	 *	2.APPEND:将数据追加写入到一个已经存在的文件,若文件不存在,报错
	 *	3.APPEND,CREATE:若文件不存在则创建,存在则直接追加写入数据
	 *	4.CREATE_NEW:总是创建新文件,报错如果文件已经存在
	 * </pre>
	 *
	 * @param path 需要开启或创建的path
	 * @param options 文件操作模式,{@link StandardOpenOption}
	 * @return 字节输出流
	 * @throws IOException if an I/O error occurs
	 */
	public static OutputStream newOutputStream(Path path, OpenOption... options) throws IOException {
		return Files.newOutputStream(path, options);
	}

	/**
	 * 判断文件不存在,不支持软连接
	 * 
	 * @param path 文件Path
	 * @return true->文件不存在,false->文件存在
	 */
	public static boolean notExist(Path path) {
		return notExist(path, false);
	}

	/**
	 * 判断文件不存在,不支持软连接
	 * 
	 * @param path 文件Path
	 * @param followLink 是否支持软连接,true->支持,false->不支持
	 * @return true->文件不存在,false->文件存在
	 */
	public static boolean notExist(Path path, boolean followLink) {
		final LinkOption[] options =
				followLink ? EMPTY_LINK_OPTION_ARRAY : new LinkOption[] { LinkOption.NOFOLLOW_LINKS };
		return Files.notExists(path, options);
	}

	/**
	 * 读取文件的所有内容为byte数组
	 *
	 * @param path 文件
	 * @return byte数组
	 * @throws IOException
	 */
	public static byte[] readBytes(Path path) throws IOException {
		return Files.readAllBytes(path);
	}

	/**
	 * 按行读取文件中所有的内容,若path为null,返回空集合
	 * 
	 * @param path 文件
	 * @return 文件所有的行
	 * @throws IOException
	 */
	public static List<String> readLines(Path path) throws IOException {
		return null == path ? Collections.emptyList() : Files.readAllLines(path, CharsetHelper.defaultCharset());
	}

	/**
	 * 以指定字符编码按行读取文件中所有的内容,若path为null,返回空集合
	 * 
	 * @param path 文件
	 * @param charset 字符编码
	 * @return 文件所有的行
	 * @throws IOException
	 */
	public static List<String> readLines(Path path, Charset charset) throws IOException {
		return null == path ? Collections.emptyList() : Files.readAllLines(path, CharsetHelper.defaultCharset(charset));
	}

	/**
	 * 修改文件或目录的文件名,不变更路径,只是简单修改文件名
	 *
	 * @param path 被修改的文件
	 * @param newName 新的文件名,包括扩展名
	 * @param cover 是否覆盖目标文件,true->覆盖,false->不覆盖
	 * @return 目标文件Path
	 * @throws IOException
	 */
	public static Path rename(Path path, String newName, boolean cover) throws IOException {
		return move(path, path.resolveSibling(newName), cover);
	}

	/**
	 * 路径解析,合并2个路径
	 * 
	 * <pre>
	 * 1.当path2是绝对路径时,直接返回path2,忽略path1
	 * 2.当path2非绝对路径,也不是\\或/开头时,返回path1拼接path2的新path
	 * 3.当path2以\\或/开头时,若path1是绝对路径,返回path1的根路径拼接path2;否则返回path2
	 * </pre>
	 * 
	 * <pre>
	 * Paths.get("e:\\test\\text.txt").resolve(Paths.get("f:\\tect\\tttt"))	=f:\tect\tttt
	 * Paths.get("e:\\test\\text.txt").resolve(Paths.get("tect\\tttt"))			=e:\test\text.txt\tect\tttt
	 * Paths.get("test\\text.txt").resolve(Paths.get("\\tect\\tttt"))			=\tect\tttt
	 * Paths.get("e:\\test\\text.txt").resolve(Paths.get("\\tect\\tttt"))		=e:\tect\tttt
	 * </pre>
	 * 
	 * @param path1 路径1
	 * @param path2 路径1
	 * @return 新的Path
	 * @see {@link Path#resolve(Path)}
	 */
	public static Path resolve(Path path1, Path path2) {
		if (null == path1) {
			return path2;
		}
		if (null == path2) {
			return path1;
		}
		return path1.resolve(path2);
	}

	/**
	 * 判断sub目录是否为parent起始部分
	 *
	 * @param parent 父目录
	 * @param sub 子目录
	 * @return 子目录是否为父目录的起始部分
	 */
	public static boolean startsWith(Path parent, Path sub) {
		return toAbsoluteNormal(sub).startsWith(toAbsoluteNormal(parent));
	}

	/**
	 * 设置文件为只读,根据OS不同行为可能不同
	 *
	 * @param path 文件
	 * @param readOnly true->设置只读,false->非只读
	 * @param options 是否支持软连接
	 * @return 文件path
	 * @throws IOException
	 */
	public static Path setReadOnly(final Path path, final boolean readOnly, final LinkOption... options)
			throws IOException {
		final DosFileAttributeView fileAttributeView =
				Files.getFileAttributeView(path, DosFileAttributeView.class, options);
		if (fileAttributeView != null) {
			fileAttributeView.setReadOnly(readOnly);
			return path;
		}
		final PosixFileAttributeView posixFileAttributeView =
				Files.getFileAttributeView(path, PosixFileAttributeView.class, options);
		if (posixFileAttributeView != null) {
			// 只作用于Windows,不能作用于Linux
			// Files.setAttribute(path, "unix:readonly", readOnly, options);
			final PosixFileAttributes readAttributes = posixFileAttributeView.readAttributes();
			final Set<PosixFilePermission> permissions = readAttributes.permissions();
			permissions.remove(PosixFilePermission.OWNER_WRITE);
			permissions.remove(PosixFilePermission.GROUP_WRITE);
			permissions.remove(PosixFilePermission.OTHERS_WRITE);
			return Files.setPosixFilePermissions(path, permissions);
		}
		throw new IOException("No DosFileAttributeView or PosixFileAttributeView for " + path);
	}

	/**
	 * 获取指定位置的子路径部分,支持负数,例如起始为-1表示从后数第一个节点位置
	 *
	 * @param path 路径
	 * @param start 起始路径索引
	 * @param end 结束路径索引
	 * @return 获取的子路径
	 */
	public static Path subPath(Path path, int start, int end) {
		if (null == path) {
			return null;
		}
		final int len = path.getNameCount();
		if (start < 0) {
			start = len + start;
			if (start < 0) {
				start = 0;
			}
		} else if (start > len) {
			start = len;
		}
		if (end < 0) {
			end = len + end;
			if (end < 0) {
				end = len;
			}
		} else if (end > len) {
			end = len;
		}
		if (end < start) {
			int tmp = start;
			start = end;
			end = tmp;
		}
		if (start == end) {
			return null;
		}
		return path.subpath(start, end);
	}

	/**
	 * 将Path路径转换为标准的绝对路径
	 *
	 * @param path 文件或目录Path
	 * @return 转换后的Path
	 */
	public static Path toAbsoluteNormal(Path path) {
		AssertHelper.notNull(path);
		return path.toAbsolutePath().normalize();
	}

	/**
	 * 将{@link FileVisitOption}数组转换为 {@link Set}
	 * 
	 * @param fileVisitOptions 数组
	 * @return Set
	 */
	public static Set<FileVisitOption> toFileVisitOptionSet(final FileVisitOption... fileVisitOptions) {
		return fileVisitOptions == null ? EnumSet.noneOf(FileVisitOption.class)
				: Arrays.stream(fileVisitOptions).collect(Collectors.toSet());
	}

	/**
	 * 遍历指定path下的文件并做处理
	 *
	 * @param start 起始路径,必须为目录
	 * @param visitor 自定义在访问文件时,访问目录前后时做的操作
	 * @throws IOException
	 * @see Files#walkFileTree(Path, java.util.Set, int, FileVisitor)
	 */
	public static void walkFileTree(Path start, FileVisitor<? super Path> visitor) throws IOException {
		walkFileTree(start, -1, visitor);
	}

	/**
	 * 遍历指定path下的文件并做处理
	 *
	 * @param start 起始路径,必须为目录
	 * @param maxDepth 最大遍历深度,-1表示不限制深度
	 * @param visitor 自定义在访问文件时,访问目录前后时做的操作
	 * @throws IOException
	 * @see Files#walkFileTree(Path, java.util.Set, int, FileVisitor)
	 */
	public static void walkFileTree(Path start, int maxDepth, FileVisitor<? super Path> visitor) throws IOException {
		maxDepth = maxDepth <= 0 ? Integer.MAX_VALUE : maxDepth;
		Files.walkFileTree(start, EnumSet.noneOf(FileVisitOption.class), maxDepth, visitor);
	}

	/**
	 * 遍历指定path下的文件并做处理
	 *
	 * @param start 起始路径,必须为目录
	 * @param maxDepth 最大遍历深度,-1表示不限制深度
	 * @param visitor 自定义在访问文件时,访问目录前后时做的操作
	 * @param fileVisitOptions 文件模式,普通文件或软连接
	 * @throws IOException
	 * @see Files#walkFileTree(Path, java.util.Set, int, FileVisitor)
	 */
	public static void walkFileTree(Path start, int maxDepth, FileVisitor<? super Path> visitor,
			FileVisitOption... fileVisitOptions) throws IOException {
		maxDepth = maxDepth <= 0 ? Integer.MAX_VALUE : maxDepth;
		Files.walkFileTree(start, toFileVisitOptionSet(fileVisitOptions), maxDepth, visitor);
	}

	/**
	 * 向文件中写入字节数组
	 * 
	 * @param path 文件Path
	 * @param bytes 字节数组
	 * @throws IOException
	 */
	public static void write(Path path, byte[] bytes) throws IOException {
		write(path, bytes, new OpenOption[0]);
	}

	/**
	 * 向文件中写入字节数组
	 * 
	 * @param path 文件Path
	 * @param bytes 字节数组
	 * @param options 文件打开模式
	 * @throws IOException
	 */
	public static void write(Path path, byte[] bytes, OpenOption... options) throws IOException {
		AssertHelper.notNull(path, ConstIO.TOAST_FILE_NULL);
		Files.write(path, bytes, null == options ? new OpenOption[0] : options);
	}

	/**
	 * 向文件中以指定字符编码写入一行数据
	 * 
	 * @param path 文件Path
	 * @param str 写入文件的数据
	 * @param options 文件打开模式
	 * @throws IOException
	 */
	public static void write(Path path, String str, OpenOption... options) throws IOException {
		write(path, Arrays.asList(str), CharsetHelper.defaultCharset(), options);
	}

	/**
	 * 向文件中写入多行数据
	 * 
	 * @param path 文件Path
	 * @param lines 写入文件的数据
	 * @param options 文件打开模式
	 * @throws IOException
	 */
	public static void write(Path path, List<String> lines, OpenOption... options) throws IOException {
		write(path, lines, Charset.defaultCharset(), options);
	}

	/**
	 * 向文件中以指定字符编码写入多行数据
	 * 
	 * @param path 文件Path
	 * @param lines 写入文件的数据
	 * @param charset 字符编码
	 * @param options 文件打开模式
	 * @throws IOException
	 */
	public static void write(Path path, List<String> lines, Charset charset, OpenOption... options) throws IOException {
		AssertHelper.notNull(path, ConstIO.TOAST_FILE_NULL);
		Files.write(path, lines, CharsetHelper.defaultCharset(charset), options);
	}

	/**
	 * 向文件中以指定字符编码写入多行数据
	 * 
	 * @param path 文件Path
	 * @param lines 写入文件的数据
	 * @param charsetName 字符编码名称
	 * @param options 文件打开模式
	 * @throws IOException
	 */
	public static void write(Path path, List<String> lines, String charsetName, OpenOption... options)
			throws IOException {
		write(path, lines, CharsetHelper.defaultCharset(charsetName), options);
	}
}