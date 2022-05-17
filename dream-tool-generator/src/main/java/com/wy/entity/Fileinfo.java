package com.wy.model;

import com.wy.base.AbstractModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 文件表 ts_fileinfo
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "文件表 ts_fileinfo")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Fileinfo extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 文件编号
	 */
	@ApiModelProperty("文件编号")
	private Long fileId;

	/**
	 * 存储在本地的名称,规则是32uuid_yyyyMMdd.文件后缀
	 */
	@ApiModelProperty("存储在本地的名称,规则是32uuid_yyyyMMdd.文件后缀")
	private String localName;

	/**
	 * 文件本来的名字
	 */
	@ApiModelProperty("文件本来的名字")
	private String originalName;

	/**
	 * 文件类型:默认0其他;1图片;2音频;3视频;4文本;5压缩文件;
	 */
	@ApiModelProperty("文件类型:默认0其他;1图片;2音频;3视频;4文本;5压缩文件;")
	private Integer fileType;

	/**
	 * 文件大小,单位M
	 */
	@ApiModelProperty("文件大小,单位M")
	private Double fileSize;

	/**
	 * 音视频文件时长,格式为HH:mm:ss
	 */
	@ApiModelProperty("音视频文件时长,格式为HH:mm:ss")
	private String fileTime;

	/**
	 * 文件后缀,不需要点
	 */
	@ApiModelProperty("文件后缀,不需要点")
	private String fileSuffix;

	/**
	 * 文件远程访问地址
	 */
	@ApiModelProperty("文件远程访问地址")
	private String fileUrl;

	/**
	 * 上传时间
	 */
	@ApiModelProperty("上传时间")
	private Date createtime;

	/** 非数据库字段 */
}