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
 *  aoa_stay
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_stay")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaStay extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long stayId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer day;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String hotelName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date leaveTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String stayCity;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Double stayMoney;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date stayTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long evemoneyId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long userName;

	/** 非数据库字段 */
}