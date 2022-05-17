package com.wy.model;

import com.wy.base.AbstractModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户和公告关联表 tr_user_notice
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "用户和公告关联表 tr_user_notice")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserNotice extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户编号
	 */
	@ApiModelProperty("用户编号")
	private Long userId;

	/**
	 * 公告编号
	 */
	@ApiModelProperty("公告编号")
	private Long noticeId;

	/**
	 * 公告是否已读:默认0未读;1已读
	 */
	@ApiModelProperty("公告是否已读:默认0未读;1已读")
	private Integer read;

	/** 非数据库字段 */
}