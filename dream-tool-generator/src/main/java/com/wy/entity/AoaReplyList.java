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
 *  aoa_reply_list
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_reply_list")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaReplyList extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long replyId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String content;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date replayTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long discussId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long replyUserId;

	/** 非数据库字段 */
}