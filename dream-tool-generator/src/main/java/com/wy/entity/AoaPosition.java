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
 *  aoa_position
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_position")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaPosition extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long positionId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer level;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String name;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String describtion;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long deptid;

	/** 非数据库字段 */
}