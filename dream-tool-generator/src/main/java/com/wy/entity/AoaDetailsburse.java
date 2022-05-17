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
 *  aoa_detailsburse
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_detailsburse")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaDetailsburse extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long detailsburseId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String descript;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Double detailmoney;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer invoices;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date produceTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String subject;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long bursmentId;

	/** 非数据库字段 */
}