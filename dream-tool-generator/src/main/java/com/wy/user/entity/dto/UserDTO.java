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
 * 用户表DTO
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "用户表DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	/**
	 * 用户ID
	 */
	@ApiModelProperty("用户ID")
	@NotNull(groups = ValidEdit.class)
	private Long id;

	/**
	 * 用户名,唯一
	 */
	@ApiModelProperty("用户名,唯一")
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