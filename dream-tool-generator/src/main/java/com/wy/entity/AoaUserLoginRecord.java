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
 *  aoa_user_login_record
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_user_login_record")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaUserLoginRecord extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long recordId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String browser;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String ipAddr;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date loginTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String sessionId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long userId;

	/** 非数据库字段 */
}