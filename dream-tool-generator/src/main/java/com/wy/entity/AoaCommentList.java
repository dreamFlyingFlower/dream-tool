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
 *  aoa_comment_list
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_comment_list")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaCommentList extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long commentId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String comment;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date time;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long commentUserId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long replyId;

	/** 非数据库字段 */
}