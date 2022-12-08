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
 * 菜单表查询参数
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "菜单表查询参数")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MenuQuery extends AbstractQuery {

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