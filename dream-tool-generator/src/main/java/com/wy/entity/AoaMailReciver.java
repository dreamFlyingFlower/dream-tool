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
 *  aoa_mail_reciver
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_mail_reciver")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaMailReciver extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long pkId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer isRead;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long mailId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long mailReciverId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer isStar;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer isDel;

	/** 非数据库字段 */
}