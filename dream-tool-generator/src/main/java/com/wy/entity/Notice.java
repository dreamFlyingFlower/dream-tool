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
 * 公告表 ts_notice
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "公告表 ts_notice")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 公告编号
	 */
	@ApiModelProperty("公告编号")
	private Long noticeId;

	/**
	 * 用户编号
	 */
	@ApiModelProperty("用户编号")
	private Long userId;

	/**
	 * 公告标题
	 */
	@ApiModelProperty("公告标题")
	private String title;

	/**
	 * 公告内容,不超过2000字
	 */
	@ApiModelProperty("公告内容,不超过2000字")
	private String content;

	/**
	 * 是否置顶:默认0否;1是
	 */
	@ApiModelProperty("是否置顶:默认0否;1是")
	private Integer top;

	/**
	 * 公告类型:默认1通知,见ts_dict表NOTICE_TYPE
	 */
	@ApiModelProperty("公告类型:默认1通知,见ts_dict表NOTICE_TYPE")
	private Integer type;

	/**
	 * 公告状态:0关闭;默认1正常
	 */
	@ApiModelProperty("公告状态:0关闭;默认1正常")
	private Integer state;

	/**
	 * 公告时间
	 */
	@ApiModelProperty("公告时间")
	private Date createtime;

	/** 非数据库字段 */
}