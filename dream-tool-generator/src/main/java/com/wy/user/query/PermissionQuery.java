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
 * 权限查询参数
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "权限查询参数")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionQuery extends AbstractQuery {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty("主键")
	private Long id;

	/**
	 * 权限编码
	 */
	@ApiModelProperty("权限编码")
	private String permissionCode;

	/**
	 * 权限名称
	 */
	@ApiModelProperty("权限名称")
	private String permissionName;

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