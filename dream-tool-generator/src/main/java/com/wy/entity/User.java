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
 * 用户表 ts_user
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "用户表 ts_user")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户编号
	 */
	@ApiModelProperty("用户编号")
	private Long userId;

	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	private String username;

	/**
	 * 密码,md5加密
	 */
	@ApiModelProperty("密码,md5加密")
	private String password;

	/**
	 * 真实姓名
	 */
	@ApiModelProperty("真实姓名")
	private String realname;

	/**
	 * 部门编号
	 */
	@ApiModelProperty("部门编号")
	private Integer departId;

	/**
	 * 身份证号
	 */
	@ApiModelProperty("身份证号")
	private String idcard;

	/**
	 * 性别,见ts_dict表SEX
	 */
	@ApiModelProperty("性别,见ts_dict表SEX")
	private String sex;

	/**
	 * 年龄
	 */
	@ApiModelProperty("年龄")
	private Integer age;

	/**
	 * 住址
	 */
	@ApiModelProperty("住址")
	private String address;

	/**
	 * 出生日期
	 */
	@ApiModelProperty("出生日期")
	private Date birthday;

	/**
	 * 邮箱地址
	 */
	@ApiModelProperty("邮箱地址")
	private String email;

	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	private String mobile;

	/**
	 * 用户状态:0黑名单;默认1正常;2锁定;3休假;4离职中;5离职;6逻辑删除
	 */
	@ApiModelProperty("用户状态:0黑名单;默认1正常;2锁定;3休假;4离职中;5离职;6逻辑删除")
	private Integer state;

	/**
	 * 用户头像
	 */
	@ApiModelProperty("用户头像")
	private String avatar;

	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	private Date createtime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty("修改时间")
	private Date updatetime;

	/** 非数据库字段 */
}