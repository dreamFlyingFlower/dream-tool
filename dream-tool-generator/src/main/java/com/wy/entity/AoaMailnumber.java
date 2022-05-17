package com.wy.model;

import com.wy.base.AbstractModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *  aoa_mailnumber
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_mailnumber")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaMailnumber extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long mailNumberId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String mailAccount;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date mailCreateTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String mailDes;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long mailType;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String mailUserName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String password;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long status;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long mailNumUserId;

	/** 非数据库字段 */
}