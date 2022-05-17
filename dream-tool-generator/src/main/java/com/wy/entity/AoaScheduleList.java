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
 *  aoa_schedule_list
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_schedule_list")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaScheduleList extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long rcId;

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
	private String filedescribe;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer isRemind;

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
	private Long userId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String miaoshu;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer isreminded;

	/** 非数据库字段 */
}