package com.wy.model;

import com.wy.base.AbstractModel;
import com.wy.database.annotation.Sort;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 部门表 ts_depart
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "部门表 ts_depart")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Depart extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 部门编号
	 */
	@ApiModelProperty("部门编号")
	private Long departId;

	/**
	 * 部门名称
	 */
	@ApiModelProperty("部门名称")
	private String departName;

	/**
	 * 上级部门编号
	 */
	@ApiModelProperty("上级部门编号")
	private Long pid;

	/**
	 * 上级部门名称
	 */
	@ApiModelProperty("上级部门名称")
	private String pname;

	/**
	 * 部门描述
	 */
	@ApiModelProperty("部门描述")
	private String departDesc;

	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	@Sort
	private Integer sortIndex;

	/** 非数据库字段 */
}