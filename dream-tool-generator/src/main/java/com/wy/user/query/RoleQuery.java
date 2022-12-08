package com.wy.user.query;

import com.wy.core.base.AbstractQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 角色信息查询参数
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "角色信息查询参数")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RoleQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty("主键")
	private Long id;

	/**
	 * 角色名称
	 */
	@ApiModelProperty("角色名称")
	private String roleName;

	/**
	 * 角色编码
	 */
	@ApiModelProperty("角色编码")
	private String roleCode;

	/**
	 * 角色类型:1-超级管理员;2-普通角色
	 */
	@ApiModelProperty("角色类型:1-超级管理员;2-普通角色")
	private Integer roleType;

	/**
	 * 可拥有角色数量,默认1
	 */
	@ApiModelProperty("可拥有角色数量,默认1")
	private Integer roleLimit;

	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	private LocalDateTime createTime;

	/**
	 * 创建人ID
	 */
	@ApiModelProperty("创建人ID")
	private Long createUser;

	/**
	 * 更新时间
	 */
	@ApiModelProperty("更新时间")
	private LocalDateTime updateTime;

	/**
	 * 更新人ID
	 */
	@ApiModelProperty("更新人ID")
	private Long updateUser;
}