package com.wy.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.LoginLog;

import io.swagger.annotations.Api;

/**
 * 用户登录日志API
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "用户登录日志API")
@RestController
@RequestMapping("loginLog")
public class LoginLogCrl extends AbstractCrl<LoginLog, Long> {

}