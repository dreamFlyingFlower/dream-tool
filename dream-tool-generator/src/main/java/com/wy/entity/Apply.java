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
 * 请假表 tb_apply
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "请假表 tb_apply")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Apply extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 请假编号
	 */
	@ApiModelProperty("请假编号")
	private Long applyId;

	/**
	 * 请假申请日期,格式yyyy-MM-dd
	 */
	@ApiModelProperty("请假申请日期,格式yyyy-MM-dd")
	private Date applyDate;

	/**
	 * 请假开始时间,格式yyyy-MM-dd HH:mm:ss
	 */
	@ApiModelProperty("请假开始时间,格式yyyy-MM-dd HH:mm:ss")
	private Date starttime;

	/**
	 * 请假结束时间,格式yyyy-MM-dd HH:mm:ss
	 */
	@ApiModelProperty("请假结束时间,格式yyyy-MM-dd HH:mm:ss")
	private Date finishtime;

	/**
	 * 请假类型,见ts_dict表APPLY_TYPE
	 */
	@ApiModelProperty("请假类型,见ts_dict表APPLY_TYPE")
	private Integer applyType;

	/**
	 * 请假原因
	 */
	@ApiModelProperty("请假原因")
	private String reason;

	/**
	 * 请假状态:默认1正在请假中;2已销假
	 */
	@ApiModelProperty("请假状态:默认1正在请假中;2已销假")
	private Integer state;

	/**
	 * 请假人编号
	 */
	@ApiModelProperty("请假人编号")
	private Long userId;

	/**
	 * 请假人姓名
	 */
	@ApiModelProperty("请假人姓名")
	private String username;

	/**
	 * 批准人用户编号
	 */
	@ApiModelProperty("批准人用户编号")
	private Long approveId;

	/**
	 * 批准人姓名
	 */
	@ApiModelProperty("批准人姓名")
	private String approveName;

	/**
	 * 请假批准日期,格式yyyy-MM-dd
	 */
	@ApiModelProperty("请假批准日期,格式yyyy-MM-dd")
	private Date approveDate;

	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;

	/** 非数据库字段 */
}