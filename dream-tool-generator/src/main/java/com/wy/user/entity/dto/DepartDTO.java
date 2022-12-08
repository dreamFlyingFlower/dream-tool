package com.wy.user.entity.dto;

import javax.validation.constraints.NotNull;

import com.wy.core.valid.ValidEdit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门表DTO
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "部门表DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartDTO {

	/**
	 * 主键ID
	 */
	@ApiModelProperty("主键ID")
	@NotNull(groups = ValidEdit.class)
	private Long id;

	/**
	 * 部门编码
	 */
	@ApiModelProperty("部门编码")
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
	 * 最新一次更新时间
	 */
	@ApiModelProperty("最新一次更新时间")
	private LocalDateTime updateTime;

	/**
	 * 最近一次更新用户ID
	 */
	@ApiModelProperty("最近一次更新用户ID")
	private Long updateUser;
}