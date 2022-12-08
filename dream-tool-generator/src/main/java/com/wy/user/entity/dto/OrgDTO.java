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
 * 组织机构表DTO
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "组织机构表DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgDTO {

	/**
	 * 主键ID
	 */
	@ApiModelProperty("主键ID")
	@NotNull(groups = ValidEdit.class)
	private Long id;

	/**
	 * 组织机构编码
	 */
	@ApiModelProperty("组织机构编码")
	private String orgCode;

	/**
	 * 组织机构简称
	 */
	@ApiModelProperty("组织机构简称")
	private String orgName;

	/**
	 * 上级组织机构,顶层组织机构为0
	 */
	@ApiModelProperty("上级组织机构,顶层组织机构为0")
	private Long pid;

	/**
	 * 组织机构层级,由高到低,以-相连
	 */
	@ApiModelProperty("组织机构层级,由高到低,以-相连")
	private String orgLevel;

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