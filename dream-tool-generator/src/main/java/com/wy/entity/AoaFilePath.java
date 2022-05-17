package com.wy.model;

import com.wy.base.AbstractModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *  aoa_file_path
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_file_path")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaFilePath extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long pathId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long parentId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String pathName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long pathUserId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long pathIstrash;

	/** 非数据库字段 */
}