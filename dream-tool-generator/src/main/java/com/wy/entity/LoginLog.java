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
 * 用户登录日志 tb_login_log
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "用户登录日志 tb_login_log")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginLog extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 登录日志编号
	 */
	@ApiModelProperty("登录日志编号")
	private Long loginId;

	/**
	 * 登录用户编号
	 */
	@ApiModelProperty("登录用户编号")
	private Long userId;

	/**
	 * 登录用户名
	 */
	@ApiModelProperty("登录用户名")
	private String username;

	/**
	 * 登录用户ip
	 */
	@ApiModelProperty("登录用户ip")
	private String loginIp;

	/**
	 * 登录时间,格式为yyyy-MM-dd HH:mm:ss
	 */
	@ApiModelProperty("登录时间,格式为yyyy-MM-dd HH:mm:ss")
	private Date loginDate;

	/**
	 * 登录地点
	 */
	@ApiModelProperty("登录地点")
	private String loginPlace;

	/**
	 * 浏览器
	 */
	@ApiModelProperty("浏览器")
	private String browser;

	/**
	 * 操作系统
	 */
	@ApiModelProperty("操作系统")
	private String os;

	/**
	 * 登录状态:0失败;默认1成功
	 */
	@ApiModelProperty("登录状态:0失败;默认1成功")
	private Integer state;

	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;

	/** 非数据库字段 */
}