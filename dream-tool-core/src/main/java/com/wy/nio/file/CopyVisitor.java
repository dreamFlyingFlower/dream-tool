package com.wy.nio.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 文件复制的实现,用于递归遍历复制目录,会自动创建目标目录中不存在的上级目录 FIXME
 * 
 * @author 飞花梦影
 * @date 2021-03-09 14:13:58
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class CopyVisitor extends SimpleFileVisitor<Path> {

	private final Path source;

	private final Path target;

	public CopyVisitor(Path source, Path target) {
		if (PathTool.exists(target, false) && !PathTool.isDir(target)) {
			throw new IllegalArgumentException("Target must be a directory");
		}
		this.source = source;
		this.target = target;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		// 将当前目录相对于源路径转换为相对于目标路径
		final Path newTarget = target.resolve(source.relativize(dir));
		if (Files.notExists(newTarget)) {
			Files.createDirectories(newTarget);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		Files.copy(file, target.resolve(source.relativize(file)));
		return FileVisitResult.CONTINUE;
	}
}