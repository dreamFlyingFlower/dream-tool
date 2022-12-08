package com.wy.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wy.core.base.AbstractEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 部门表 ts_depart
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "部门表 ts_depart")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ts_depart")
public class Depart extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@ApiModelProperty("主键ID")
	private Long id;

	/**
	 * 部门编码
	 */
	@ApiModelProperty("部门编码")
	@Unique
	private String departCode;

	/**
	 * 部门名称
	 */
	@ApiModelProperty("部门名称")
	private String departName;

	/**
	 * 上级部门ID,顶层部门为0
	 */
	@ApiModelProperty("上级部门ID,顶层部门为0")
	private Long pid;

	/**
	 * 部门层级,由高到低,以-相连
	 */
	@ApiModelProperty("部门层级,由高到低,以-相连")
	private String departLevel;

	/** 非数据库字段 */
}