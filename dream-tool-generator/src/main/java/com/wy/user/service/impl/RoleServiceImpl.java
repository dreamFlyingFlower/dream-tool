package com.wy.user.service.impl;

import org.springframework.stereotype.Service;

import com.wy.core.base.AbstractServiceImpl;
import com.wy.user.convert.RoleConvert;
import com.wy.user.entity.Role;
import com.wy.user.entity.dto.RoleDTO;
import com.wy.user.mapper.RoleMapper;
import com.wy.user.query.RoleQuery;
import com.wy.user.service.RoleService;

/**
 * 角色信息业务层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service("roleService")
public class RoleServiceImpl extends AbstractServiceImpl<Role, RoleDTO, RoleQuery, RoleConvert, RoleMapper> implements RoleService {

}