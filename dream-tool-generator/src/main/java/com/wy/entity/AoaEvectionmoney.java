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
 *  aoa_evectionmoney
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_evectionmoney")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaEvectionmoney extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long evectionmoneyId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String financialAdvice;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String managerAdvice;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Double money;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String name;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long proId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer pro;

	/** 非数据库字段 */
}