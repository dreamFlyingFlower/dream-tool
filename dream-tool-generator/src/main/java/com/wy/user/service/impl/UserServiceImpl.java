package com.wy.user.service.impl;

import org.springframework.stereotype.Service;

import com.wy.core.base.AbstractServiceImpl;
import com.wy.user.convert.UserConvert;
import com.wy.user.entity.User;
import com.wy.user.entity.dto.UserDTO;
import com.wy.user.mapper.UserMapper;
import com.wy.user.query.UserQuery;
import com.wy.user.service.UserService;

/**
 * 用户表业务层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service("userService")
public class UserServiceImpl extends AbstractServiceImpl<User, UserDTO, UserQuery, UserConvert, UserMapper> implements UserService {

}