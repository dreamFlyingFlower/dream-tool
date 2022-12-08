package com.wy.user.service.impl;

import org.springframework.stereotype.Service;

import com.wy.core.base.AbstractServiceImpl;
import com.wy.user.convert.RoleResourceConvert;
import com.wy.user.entity.RoleResource;
import com.wy.user.entity.dto.RoleResourceDTO;
import com.wy.user.mapper.RoleResourceMapper;
import com.wy.user.query.RoleResourceQuery;
import com.wy.user.service.RoleResourceService;

/**
 * 角色-资源关系业务层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service("roleResourceService")
public class RoleResourceServiceImpl extends AbstractServiceImpl<RoleResource, RoleResourceDTO, RoleResourceQuery, RoleResourceConvert, RoleResourceMapper> implements RoleResourceService {

}