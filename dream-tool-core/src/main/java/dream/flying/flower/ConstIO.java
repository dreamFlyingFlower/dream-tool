package dream.flying.flower;

import java.io.File;

/**
 * IO流以及File,Path常量
 *
 * @author 飞花梦影
 * @date 2022-05-15 15:04:27
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface ConstIO {

	/** 流结束 */
	int EOF = -1;

	/** 根据系统自动调整的目录分隔符 */
	char DIR_SEPARATOR = File.separatorChar;

	/** unix系统目录分隔符 */
	char DIR_SEPARATOR_UNIX = '/';

	/** windows系统目录分隔符 */
	char DIR_SEPARATOR_WINDOWS = '\\';

	/** 默认字节缓冲大小 */
	int DEFAULT_BUFFER_SIZE = 8192;

	/** 直接内存读取流时的缓存大小,8M */
	int DEFAULT_BUFFER_SIZE_DIRECT = 8 << 20;

	/** 默认字节缓冲数组,8k */
	byte[] DEFAULT_BUFFER_BYTE = new byte[DEFAULT_BUFFER_SIZE];

	/** 临时文件名默认前缀 */
	String FILE_TEMP_PREFIX = "paradise";

	/** 根据系统自动调整的换行符 */
	String LINE_SEPERATOR = System.lineSeparator();

	/** unix换行符 */
	String LINE_SEPARATOR_UNIX = "\n";

	/** windows换行符 */
	String LINE_SEPARATOR_WINDOWS = "\r\n";

	/** 1kb == 1024b */
	long ONE_KB = 1024;

	/** 默认字节缓冲数组 {@link #skip(InputStream, long)} */
	byte[] SKIP_BYTE_BUFFER = new byte[DEFAULT_BUFFER_SIZE];

	/** 文件为null时的提示信息 */
	String TOAST_FILE_NULL = "the file can't be null";

	/** 文件为null时的提示信息 */
	String TOAST_FILE_NULL_FORMAT = "the %s can't be null";

	/** 目录为null时的提示信息 */
	String TOAST_DIR_NULL = "the directory can't be null";

	/** 目录为null时的提示信息 */
	String TOAST_DIR_NULL_FORMAT = "the %s can't be null";
}