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
 *  aoa_resign
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_resign")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaResign extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long resignId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String financialAdvice;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Boolean isFinish;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String nofinish;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String personnelAdvice;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String suggest;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long handUser;

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

	/** 非数据库字段 */
}