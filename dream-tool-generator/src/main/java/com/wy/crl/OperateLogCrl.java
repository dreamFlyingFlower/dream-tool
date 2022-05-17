package com.wy.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.OperateLog;

import io.swagger.annotations.Api;

/**
 * 操作日志表API
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "操作日志表API")
@RestController
@RequestMapping("operateLog")
public class OperateLogCrl extends AbstractCrl<OperateLog, Long> {

}