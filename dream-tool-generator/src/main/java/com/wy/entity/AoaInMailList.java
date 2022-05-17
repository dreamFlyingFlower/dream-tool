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
 *  aoa_in_mail_list
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_in_mail_list")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaInMailList extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long mailId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String mailContent;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date mailCreateTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long mailFileId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String mailTitle;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long mailType;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long mailInPushUserId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String inReceiver;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long mailStatusId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long mailNumberId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer mailDel;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer mailPush;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer mailStar;

	/** 非数据库字段 */
}