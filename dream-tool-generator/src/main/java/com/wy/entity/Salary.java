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
 * 工资表 ts_salary
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "工资表 ts_salary")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Salary extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 工资编号
	 */
	@ApiModelProperty("工资编号")
	private Long salaryId;

	/**
	 * 用户编号
	 */
	@ApiModelProperty("用户编号")
	private Long userId;

	/**
	 * 用户姓名
	 */
	@ApiModelProperty("用户姓名")
	private String username;

	/**
	 * 基本工资,默认0
	 */
	@ApiModelProperty("基本工资,默认0")
	private Double basic;

	/**
	 * 审核人编号
	 */
	@ApiModelProperty("审核人编号")
	private Long checkId;

	/**
	 * 审核人姓名
	 */
	@ApiModelProperty("审核人姓名")
	private String checkName;

	/**
	 * 工资年月,格式为yyyy-MM
	 */
	@ApiModelProperty("工资年月,格式为yyyy-MM")
	private String month;

	/**
	 * 其他工资,默认0
	 */
	@ApiModelProperty("其他工资,默认0")
	private Double other;

	/**
	 * 总工资,默认0
	 */
	@ApiModelProperty("总工资,默认0")
	private Double salary;

	/**
	 * 工资发放时间
	 */
	@ApiModelProperty("工资发放时间")
	private Date craetetime;

	/** 非数据库字段 */
}