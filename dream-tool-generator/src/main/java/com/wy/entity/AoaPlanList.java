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
 *  aoa_plan_list
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_plan_list")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaPlanList extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long planId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date createTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date endTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String label;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String planComment;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String planContent;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String planSummary;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date startTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long statusId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String title;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long typeId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long planUserId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long attachId;

	/** 非数据库字段 */
}