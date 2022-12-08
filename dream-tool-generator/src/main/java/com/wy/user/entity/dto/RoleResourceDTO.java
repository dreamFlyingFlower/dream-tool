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
 * 角色-资源关系DTO
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "角色-资源关系DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleResourceDTO {

	/**
	 * 主键
	 */
	@ApiModelProperty("主键")
	@NotNull(groups = ValidEdit.class)
	private Long id;

	/**
	 * 角色id
	 */
	@ApiModelProperty("角色id")
	private Long roleId;

	/**
	 * 资源id
	 */
	@ApiModelProperty("资源id")
	private Long resourceId;

	/**
	 * 资源类型:1-菜单;2-按钮;3数据
	 */
	@ApiModelProperty("资源类型:1-菜单;2-按钮;3数据")
	private Integer resourceType;

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