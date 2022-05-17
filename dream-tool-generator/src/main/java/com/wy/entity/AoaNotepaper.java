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
 *  aoa_notepaper
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_notepaper")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaNotepaper extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long notepaperId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String concent;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date createTime;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String title;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long notepaperUserId;

	/** 非数据库字段 */
}