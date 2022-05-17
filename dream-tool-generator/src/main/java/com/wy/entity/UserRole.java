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
 * 用户角色中间表 tr_user_role
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "用户角色中间表 tr_user_role")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户编号
	 */
	@ApiModelProperty("用户编号")
	private Long userId;

	/**
	 * 角色编号
	 */
	@ApiModelProperty("角色编号")
	private Long roleId;

	/**
	 * 多角色时,每次登陆默认登录的角色,默认1有效
	 */
	@ApiModelProperty("多角色时,每次登陆默认登录的角色,默认1有效")
	private Integer defaultRole;

	/** 非数据库字段 */
}