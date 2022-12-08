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
 * 菜单表 ts_menu
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "菜单表 ts_menu")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ts_menu")
public class Menu extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty("主键")
	private Long id;

	/**
	 * 菜单标题
	 */
	@ApiModelProperty("菜单标题")
	private String menuName;

	/**
	 * 父id
	 */
	@ApiModelProperty("父id")
	private Long pid;

	/**
	 * 层级,以-拼接
	 */
	@ApiModelProperty("层级,以-拼接")
	private String level;

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