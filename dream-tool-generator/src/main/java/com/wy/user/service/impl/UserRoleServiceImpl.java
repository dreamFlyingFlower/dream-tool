package com.wy.user.service.impl;

import org.springframework.stereotype.Service;

import com.wy.core.base.AbstractServiceImpl;
import com.wy.user.convert.UserRoleConvert;
import com.wy.user.entity.UserRole;
import com.wy.user.entity.dto.UserRoleDTO;
import com.wy.user.mapper.UserRoleMapper;
import com.wy.user.query.UserRoleQuery;
import com.wy.user.service.UserRoleService;

/**
 * 账号-角色关系业务层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends AbstractServiceImpl<UserRole, UserRoleDTO, UserRoleQuery, UserRoleConvert, UserRoleMapper> implements UserRoleService {

}