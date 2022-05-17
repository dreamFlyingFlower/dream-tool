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
 * 角色菜单权限表 tr_role_menu
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "角色菜单权限表 tr_role_menu")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenu extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色编号
	 */
	@ApiModelProperty("角色编号")
	private Long roleId;

	/**
	 * 菜单编号
	 */
	@ApiModelProperty("菜单编号")
	private Long menuId;

	/**
	 * 权限,见ts_dict表PERMISSION,多个用逗号隔开
	 */
	@ApiModelProperty("权限,见ts_dict表PERMISSION,多个用逗号隔开")
	private String permissions;

	/**
	 * 菜单状态:默认1全选,2半选
	 */
	@ApiModelProperty("菜单状态:默认1全选,2半选")
	private Integer menuState;

	/** 非数据库字段 */
}