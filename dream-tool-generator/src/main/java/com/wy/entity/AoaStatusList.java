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
 *  aoa_status_list
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_status_list")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaStatusList extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long statusId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String statusColor;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String statusModel;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String statusName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer sortValue;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String sortPrecent;

	/** 非数据库字段 */
}