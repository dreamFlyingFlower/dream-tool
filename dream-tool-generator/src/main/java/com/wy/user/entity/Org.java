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
 * 组织机构表 ts_org
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "组织机构表 ts_org")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ts_org")
public class Org extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@ApiModelProperty("主键ID")
	private Long id;

	/**
	 * 组织机构编码
	 */
	@ApiModelProperty("组织机构编码")
	@Unique
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

	/** 非数据库字段 */
}