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
 *  aoa_task_logger
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_task_logger")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaTaskLogger extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long loggerId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date createTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String loggerTicking;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long taskId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String username;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer loggerStatusid;

	/** 非数据库字段 */
}