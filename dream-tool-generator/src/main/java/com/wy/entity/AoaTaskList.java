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
 *  aoa_task_list
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_task_list")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaTaskList extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long taskId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String comment;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date endTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer isCancel;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer isTop;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date modifyTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date publishTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date starTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer statusId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String taskDescribe;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String ticking;

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
	private Long taskPushUserId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String reciverlist;

	/** 非数据库字段 */
}