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
 *  aoa_director
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_director")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaDirector extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long directorId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String address;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String companyNumber;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String email;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer imagePath;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String phoneNumber;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String pinyin;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String remark;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String sex;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String userName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long userId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String companyname;

	/** 非数据库字段 */
}