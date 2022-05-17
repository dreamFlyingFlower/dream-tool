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
 *  aoa_user_log
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_user_log")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaUserLog extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long logId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String ipAddr;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date logTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String title;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String url;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long userId;

	/** 非数据库字段 */
}