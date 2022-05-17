package com.wy.model;

import com.wy.base.AbstractModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *  aoa_sys_menu
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_sys_menu")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaSysMenu extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long menuId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer isShow;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer menuGrade;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String menuIcon;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String menuName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String menuUrl;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long parentId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer sortId;

	/** 非数据库字段 */
}