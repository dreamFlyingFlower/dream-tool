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
 *  aoa_dept
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_dept")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaDept extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long deptId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String deptAddr;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String deptFax;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String deptName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String deptTel;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String email;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long deptmanager;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date endTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date startTime;

	/** 非数据库字段 */
}