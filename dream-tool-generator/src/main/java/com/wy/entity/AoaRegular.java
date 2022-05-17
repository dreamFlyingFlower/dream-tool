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
 *  aoa_regular
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_regular")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaRegular extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long regularId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String advice;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String deficiency;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String dobetter;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String experience;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String personnelAdvice;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String pullulate;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String understand;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long proId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String managerAdvice;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer days;

	/** 非数据库字段 */
}