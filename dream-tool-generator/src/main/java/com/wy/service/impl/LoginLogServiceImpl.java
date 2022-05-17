package com.wy.service.impl;

import org.springframework.stereotype.Service;

import com.wy.base.AbstractService;
import com.wy.model.LoginLog;
import com.wy.service.LoginLogService;

/**
 * 用户登录日志
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service("loginLogService")
public class LoginLogServiceImpl extends AbstractService<LoginLog, Long> implements LoginLogService {

}