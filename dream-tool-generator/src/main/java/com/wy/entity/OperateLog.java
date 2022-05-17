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
 * 操作日志表 tb_operate_log
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@ApiModel(description = "操作日志表 tb_operate_log")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OperateLog extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 操作编号
	 */
	@ApiModelProperty("操作编号")
	private Long operateId;

	/**
	 * 操作模块
	 */
	@ApiModelProperty("操作模块")
	private String title;

	/**
	 * 业务类型:默认0其他;1新增;2修改;3删除;4授权;5导出;6导入;7清空数据
	 */
	@ApiModelProperty("业务类型:默认0其他;1新增;2修改;3删除;4授权;5导出;6导入;7清空数据")
	private Integer businessType;

	/**
	 * 方法名称
	 */
	@ApiModelProperty("方法名称")
	private String method;

	/**
	 * 请求方式
	 */
	@ApiModelProperty("请求方式")
	private String requestMethod;

	/**
	 * 操作类别:0其它;默认1后台用户;2手机端用户;见ts_dict表OPERATIOIN_TYPE
	 */
	@ApiModelProperty("操作类别:0其它;默认1后台用户;2手机端用户;见ts_dict表OPERATIOIN_TYPE")
	private Integer operateType;

	/**
	 * 操作人员编号
	 */
	@ApiModelProperty("操作人员编号")
	private Long operaterId;

	/**
	 * 操作人员姓名
	 */
	@ApiModelProperty("操作人员姓名")
	private String operateName;

	/**
	 * 操作ip地址
	 */
	@ApiModelProperty("操作ip地址")
	private String operateIp;

	/**
	 * 操作url
	 */
	@ApiModelProperty("操作url")
	private String operateUrl;

	/**
	 * 操作地址
	 */
	@ApiModelProperty("操作地址")
	private String operatePlace;

	/**
	 * 操作参数
	 */
	@ApiModelProperty("操作参数")
	private String operateParam;

	/**
	 * 错误信息
	 */
	@ApiModelProperty("错误信息")
	private String errorMsg;

	/**
	 * 操作状态:0异常;默认1正常
	 */
	@ApiModelProperty("操作状态:0异常;默认1正常")
	private Integer state;

	/**
	 * 操作时间
	 */
	@ApiModelProperty("操作时间")
	private Date createtime;

	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;

	/** 非数据库字段 */
}