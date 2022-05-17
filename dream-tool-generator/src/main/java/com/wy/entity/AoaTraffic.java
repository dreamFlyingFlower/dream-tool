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
 *  aoa_traffic
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_traffic")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaTraffic extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long trafficId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String departName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date departTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String reachName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String seatType;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Double trafficMoney;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String trafficName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long evectionId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long userName;

	/** 非数据库字段 */
}