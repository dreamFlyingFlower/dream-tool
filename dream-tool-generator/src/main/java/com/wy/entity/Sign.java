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
 * 签到表 tb_sign
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "签到表 tb_sign")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Sign extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 签到编号
	 */
	@ApiModelProperty("签到编号")
	private Long signId;

	/**
	 * 用户编号
	 */
	@ApiModelProperty("用户编号")
	private Long userId;

	/**
	 * 签到开始时间
	 */
	@ApiModelProperty("签到开始时间")
	private Date starttime;

	/**
	 * 签到结束时间
	 */
	@ApiModelProperty("签到结束时间")
	private Date finishtime;

	/**
	 * 是否请假:默认0不是;1是
	 */
	@ApiModelProperty("是否请假:默认0不是;1是")
	private Integer leave;

	/**
	 * 是否迟到:默认0不是;1是
	 */
	@ApiModelProperty("是否迟到:默认0不是;1是")
	private Integer late;

	/**
	 * 是否早退:默认0不是;1是
	 */
	@ApiModelProperty("是否早退:默认0不是;1是")
	private Integer early;

	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;

	/** 非数据库字段 */
}