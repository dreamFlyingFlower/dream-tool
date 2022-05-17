package com.wy.model;

import com.wy.base.AbstractModel;
import com.wy.database.annotation.Unique;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 角色表 ts_role
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "角色表 ts_role")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色编号
	 */
	@ApiModelProperty("角色编号")
	private Long roleId;

	/**
	 * 角色名
	 */
	@ApiModelProperty("角色名")
	private String roleName;

	/**
	 * 角色编码
	 */
	@ApiModelProperty("角色编码")
	@Unique
	private String roleCode;

	/**
	 * 角色类型:0不可见,只有超级管理员不可见;默认1可见
	 */
	@ApiModelProperty("角色类型:0不可见,只有超级管理员不可见;默认1可见")
	private Integer roleType;

	/**
	 * 角色状态:0停用;默认1正常;2逻辑删除
	 */
	@ApiModelProperty("角色状态:0停用;默认1正常;2逻辑删除")
	private Integer roleState;

	/**
	 * 角色描述
	 */
	@ApiModelProperty("角色描述")
	private String roleDesc;

	/** 非数据库字段 */
}