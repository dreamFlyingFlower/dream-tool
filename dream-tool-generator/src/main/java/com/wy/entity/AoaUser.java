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
 *  aoa_user
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_user")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaUser extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long userId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String address;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String bank;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date birth;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String eamil;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long fatherId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date hireTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String userIdcard;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String imgPath;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer isLock;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String lastLoginIp;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date lastLoginTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date modifyTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long modifyUserId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String password;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String realName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Float salary;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String userSchool;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String sex;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String themeSkin;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String userEdu;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String userName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String userSign;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String userTel;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long deptId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long positionId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long roleId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer superman;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer holiday;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String pinyin;

	/** 非数据库字段 */
}