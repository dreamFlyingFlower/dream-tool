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
 *  aoa_process_list
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_process_list")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaProcessList extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long processId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date applyTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long deeplyId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date endTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String processDes;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String processName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer procseeDays;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer isChecked;

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
	private String typeName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long proFileId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long processUserId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String shenuser;

	/** 非数据库字段 */
}