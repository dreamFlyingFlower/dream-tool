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
 *  aoa_file_list
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_file_list")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaFileList extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long fileId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String fileName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String filePath;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String fileShuffix;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String contentType;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String model;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long pathId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long size;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date uploadTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long fileUserId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long fileIstrash;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long fileIsshare;

	/** 非数据库字段 */
}