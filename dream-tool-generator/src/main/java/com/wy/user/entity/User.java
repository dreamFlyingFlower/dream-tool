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
 * 用户表 ts_user
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "用户表 ts_user")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ts_user")
public class User extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@ApiModelProperty("用户ID")
	private Long id;

	/**
	 * 用户名,唯一
	 */
	@ApiModelProperty("用户名,唯一")
	@Unique
	private String username;

	/**
	 * 密码
	 */
	@ApiModelProperty("密码")
	private String password;

	/**
	 * 盐
	 */
	@ApiModelProperty("盐")
	private String salt;

	/**
	 * 昵称
	 */
	@ApiModelProperty("昵称")
	private String nickname;

	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	private String mobile;

	/**
	 * 部门ID
	 */
	@ApiModelProperty("部门ID")
	private Long departId;

	/**
	 * 组织机构ID
	 */
	@ApiModelProperty("组织机构ID")
	private Long orgId;

	/**
	 * 用户状态,见字典user_status
	 */
	@ApiModelProperty("用户状态,见字典user_status")
	private Integer status;

	/** 非数据库字段 */
}