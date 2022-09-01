package com.simple.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 字段属性
 * 
 * @author ParadiseWY
 * @date 2020-12-11 15:28:44
 * @git {@link https://github.com/mygodness100}
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Columninfo {

	/**
	 * 列名
	 */
	private String columnName;

	/**
	 * 列名类型
	 */
	private String dataType;

	/**
	 * 列名备注
	 */
	private String comments;

	/**
	 * 属性名称(第一个字母大写),如:user_name -> UserName
	 */
	private String attrNameUpper;

	/**
	 * 属性名称(第一个字母小写),如:user_name ->userName
	 */
	private String attrNameLower;

	/**
	 * 属性类型
	 */
	private String attrType;

	/**
	 * 是否主键,唯一键等
	 */
	private String columnKey;

	/**
	 * 是否可为null,true是,false否
	 */
	private boolean nullable;

	/**
	 * 是否是唯一字段,true是,false否
	 */
	private boolean unique;

	/**
	 * 是否为排序字段
	 */
	private boolean sortColumn;

	/**
	 * auto_increment
	 */
	private String extra;

	/**
	 * 字段是否在Entity中出现
	 */
	private boolean excludeEntity;

	/**
	 * 字段是否在EntityDTO中出现
	 */
	private boolean excludeEntityDTO;

	/**
	 * 字段是否在Query中出现
	 */
	private boolean excludeQuery;
}