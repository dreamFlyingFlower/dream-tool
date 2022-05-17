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
 *  aoa_director_users
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = " aoa_director_users")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AoaDirectorUsers extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long directorUsersId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private String catelogName;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Integer isHandle;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long directorId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long userId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Long shareUserId;

	/**
	 * 
	 */
	@ApiModelProperty("")
	private Date sharetime;

	/** 非数据库字段 */
}