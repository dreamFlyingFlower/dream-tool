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
 *  aoa_bursement
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_bursement")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaBursement extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long bursementId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Double allMoney;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer allinvoices;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date burseTime;

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
	private String name;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long typeId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long operationName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long proId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long userName;

	/** 非数据库字段 */
}