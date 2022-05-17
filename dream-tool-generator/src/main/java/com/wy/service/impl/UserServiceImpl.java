package com.wy.service.impl;

import org.springframework.stereotype.Service;

import com.wy.base.AbstractService;
import com.wy.model.User;
import com.wy.service.UserService;

/**
 * 用户表
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service("userService")
public class UserServiceImpl extends AbstractService<User, Long> implements UserService {

}