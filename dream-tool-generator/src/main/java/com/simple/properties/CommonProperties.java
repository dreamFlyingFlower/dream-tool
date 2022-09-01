package com.simple.properties;

import java.io.File;

import lombok.Getter;
import lombok.Setter;

/**
 * 通用属性
 * 
 * @author 飞花梦影
 * @date 2020-12-13 22:39:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
public class CommonProperties {

	/** 作者 */
	private String author = "飞花梦影";

	/** git地址 */
	private String git = "{@link https://github.com/dreamFlyingFlower}";

	/** Java资源文件根目录 */
	private String pathMain = "src" + File.separator + "main" + File.separator + "java" + File.separator;

	/** 静态资源根目录 */
	private String pathResources = "src" + File.separator + "main" + File.separator + "resources" + File.separator;

	/** Java文件根目录 */
	private String pathPackageRoot = "com.wy.user";

	private String pathPackageEntity = pathPackageRoot + ".entity";

	private String pathPackageEntityDTO = pathPackageEntity + ".dto";

	private String pathPackageConvert = pathPackageRoot + ".convert";

	private String pathPackageQuery = pathPackageRoot + ".query";

	private String pathPackageMapper = pathPackageRoot + ".mapper";

	private String pathPackageService = pathPackageRoot + ".service";

	private String pathPackageServiceImpl = pathPackageRoot + ".service.impl";

	private String pathPackageCrl = pathPackageRoot + ".crl";

	private String pathPackageController = pathPackageRoot + ".controller";

	/** 基础包根目录全路径 */
	private String pathPackageBase = "com.wy.core.base";

	/** 基础类 */
	private String baseEntity = "AbstractEntity";

	private String baseEntityPath = pathPackageBase + "." + baseEntity;

	private String baseDto = "AbstractDTO";

	private String baseDtoPath = pathPackageBase + "." + baseDto;

	private String baseQuery = "AbstractQuery";

	private String baseQueryPath = pathPackageBase + "." + baseQuery;

	private String baseConvert = "BaseConvert";

	private String baseConvertPath = pathPackageBase + "." + baseConvert;

	private String baseMapper = "BaseMappers";

	private String baseMapperPath = pathPackageBase + "." + baseMapper;

	private String baseService = "BaseService";

	private String baseServicePath = pathPackageBase + "." + baseService;

	private String baseServiceImpl = "AbstractServiceImpl";

	private String baseServiceImplPath = pathPackageBase + "." + baseServiceImpl;

	private String baseController = "AbstractCrl";

	private String baseControllerPath = pathPackageBase + "." + baseController;

	/** mybatis xml文件在资源根目录下的目录 */
	private String pathResourcesMapper = "mappers";

	/** 是否执行生成mybatis生成model example和mapper.xml的方法 */
	private boolean generateMybatisConfig = true;

	/** 是否使用mybatis-generate生成实体类example,只有generateMybatisConfig为true时才有效,默认使用 */
	private boolean generateModelExample = true;

	/** 是否使用mybatis-generate生成Mapper接口,只有generateMybatisConfig为true时才有效,默认不使用 */
	private boolean generateMapper = false;

	/** 是否使用mybatis-generate生成Mapper对应的xml,只有generateMybatisConfig为true时才有效,默认使用 */
	private boolean generateMapperXml = true;

	/** 是否使用模板生成实体类,默认使用 */
	private boolean generateModel = true;

	/** 是否使用模板生成service接口,默认使用 */
	private boolean generateService = true;

	/** 是否使用模板生成service.impl业务实现类,默认使用 */
	private boolean generateServiceImpl = true;

	/** 是否使用模板生成controller类,默认使用 */
	private boolean generateCrl = true;
}