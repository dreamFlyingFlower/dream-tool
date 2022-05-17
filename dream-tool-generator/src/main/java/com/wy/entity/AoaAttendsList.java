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
 *  aoa_attends_list
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_attends_list")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaAttendsList extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long attendsId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String attendsIp;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String attendsRemark;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date attendsTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long statusId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long typeId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long attendsUserId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String attendHmtime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String weekOfday;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Double holidayDays;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date holidayStart;

	/** 非数据库字段 */
}