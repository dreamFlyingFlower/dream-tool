package com.simple.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.mybatis.generator.api.ShellRunner;

import com.simple.model.Tableinfo;
import com.simple.properties.ConfigProperties;

import dream.flying.flower.io.IOHelper;
import dream.flying.flower.io.file.FileHelper;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.result.ResultException;
import lombok.extern.slf4j.Slf4j;

/**
 * @apiNote
 * @author ParadiseWY
 * @date 2020年12月12日 下午6:37:35
 */
/**
 * Velocity模板工具类
 * 
 * @author ParadiseWY
 * @date 2020-12-14 10:44:32
 * @git {@link https://github.com/mygodness100}
 */
@Slf4j
public class VelocityUtils {

	private static ConfigProperties config = SpringContextUtils.getBean(ConfigProperties.class);

	static {
		// 设置velocity资源加载器
		Properties prop = new Properties();
		prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(prop);
	}

	/**
	 * 生成MyBatis生成mapper.xml和example的generatorConfig.xml文件
	 * 
	 * @param <K> 泛型
	 * @param <V> 泛型
	 * @param data 数据
	 * @param localOrRemote 本地生成或远程下载,true本地,false远程打包成zip
	 */
	public static <K, V> void generateFiles(Map<K, V> data, boolean localOrRemote) {
		if (!config.getCommon().isGenerateMybatisConfig()) {
			return;
		}
		VelocityContext context = new VelocityContext(data);
		// 获取模板列表
		try (StringWriter sw = new StringWriter();) {
			// 渲染模板
			Template tpl = Velocity.getTemplate(config.getTemplate().getGeneratorConfig(),
					StandardCharsets.UTF_8.displayName());
			File file = new File("src/main/resources/generator/generatorConfig.xml");
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (file.exists()) {
				file.delete();
			}
			BufferedWriter bufferedWriter = FileHelper.newBufferedWriter(file, StandardCharsets.UTF_8);
			tpl.merge(context, sw);
			IOHelper.write(sw.toString(), bufferedWriter);
			bufferedWriter.flush();
			ShellRunner.main(new String[] { "-configfile", "src\\main\\resources\\generator\\generatorConfig.xml",
					"-overwrite" });
		} catch (IOException e) {
			throw new ResultException("Mybatis GeneratorConfig.xml渲染模板失败", e);
		}
	}

	public static <K, V> byte[] generateFiles(Map<K, V> data, Tableinfo tableinfo, List<String> templates,
			boolean localOrRemote) {
		VelocityContext context = new VelocityContext(data);
		for (String template : templates) {
			// 渲染模板
			Template tpl = Velocity.getTemplate(template, StandardCharsets.UTF_8.displayName());
			if (localOrRemote) {
				try (StringWriter sw = new StringWriter();) {
					String fileName = GenerateUtils.getFileName(config, template, tableinfo.getClassName());
					if (StrHelper.isBlank(fileName)) {
						log.error("unknown template :" + template);
						continue;
					}
					File file = new File(fileName);
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					BufferedWriter generatorFile = FileHelper.newBufferedWriter(file, StandardCharsets.UTF_8);
					tpl.merge(context, sw);
					IOHelper.write(sw.toString(), generatorFile);
					generatorFile.flush();
				} catch (IOException e) {
					throw new ResultException("渲染模板失败,表名:" + tableinfo.getTableName(), e);
				}
			} else {
				try (StringWriter sw = new StringWriter();
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						ZipOutputStream zip = new ZipOutputStream(outputStream);) {
					tpl.merge(context, sw);
					// 添加到zip
					zip.putNextEntry(
							new ZipEntry(GenerateUtils.getFileName(config, template, tableinfo.getClassName())));
					IOHelper.write(sw.toString(), zip, StandardCharsets.UTF_8.displayName());
					zip.closeEntry();
					return outputStream.toByteArray();
				} catch (IOException e) {
					throw new ResultException("渲染模板失败,表名:" + tableinfo.getTableName(), e);
				}
			}
		}
		return null;
	}
}