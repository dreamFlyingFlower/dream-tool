package com.wy.user.service;

import com.wy.core.base.BaseService;
import com.wy.user.entity.User;
import com.wy.user.entity.dto.UserDTO;
import com.wy.user.query.UserQuery;

/**
 * 用户表业务层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface UserService extends BaseService<User, UserDTO, UserQuery> {

}