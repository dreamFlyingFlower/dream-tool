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
 * 按钮表DTO
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "按钮表DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ButtonDTO {

	/**
	 * 主键
	 */
	@ApiModelProperty("主键")
	@NotNull(groups = ValidEdit.class)
	private Long id;

	/**
	 * 按钮标题
	 */
	@ApiModelProperty("按钮标题")
	private String buttonName;

	/**
	 * 所属菜单ID
	 */
	@ApiModelProperty("所属菜单ID")
	private Long menuId;

	/**
	 * 链接url
	 */
	@ApiModelProperty("链接url")
	private String url;

	/**
	 * 图标
	 */
	@ApiModelProperty("图标")
	private String icon;

	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	private Integer sort;

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