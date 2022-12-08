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
 * 按钮表 ts_button
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "按钮表 ts_button")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ts_button")
public class Button extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty("主键")
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

	/** 非数据库字段 */
}