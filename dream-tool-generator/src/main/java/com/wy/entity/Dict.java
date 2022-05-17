package com.wy.model;

import com.wy.base.AbstractModel;
import com.wy.database.annotation.Sort;
import com.wy.database.annotation.Unique;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统字典类 ts_dict
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "系统字典类 ts_dict")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Dict extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 字典编号
	 */
	@ApiModelProperty("字典编号")
	private Long dictId;

	/**
	 * 字典名
	 */
	@ApiModelProperty("字典名")
	private String dictName;

	/**
	 * 字典编码,唯一
	 */
	@ApiModelProperty("字典编码,唯一")
	@Unique
	private String dictCode;

	/**
	 * 字典值
	 */
	@ApiModelProperty("字典值")
	private Integer dictVal;

	/**
	 * 上级字典编号
	 */
	@ApiModelProperty("上级字典编号")
	private Long pid;

	/**
	 * 上级字典名
	 */
	@ApiModelProperty("上级字典名")
	private String pname;

	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	@Sort
	private Integer sortIndex;

	/** 非数据库字段 */
}