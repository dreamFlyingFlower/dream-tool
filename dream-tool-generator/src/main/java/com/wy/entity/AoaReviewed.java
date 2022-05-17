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
 *  aoa_reviewed
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_reviewed")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaReviewed extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long reviewedId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String advice;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date reviewedTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long statusId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long proId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long userId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer del;

	/** 非数据库字段 */
}