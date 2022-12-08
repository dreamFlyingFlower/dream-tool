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
 * 账号-角色关系 ts_user_role
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "账号-角色关系 ts_user_role")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ts_user_role")
public class UserRole extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty("主键")
	private Long id;

	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	private Long userId;

	/**
	 * 角色编码
	 */
	@ApiModelProperty("角色编码")
	private Long roleId;

	/** 非数据库字段 */
}