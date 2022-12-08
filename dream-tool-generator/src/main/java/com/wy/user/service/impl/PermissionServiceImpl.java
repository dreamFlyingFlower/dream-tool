package com.wy.user.service.impl;

import org.springframework.stereotype.Service;

import com.wy.core.base.AbstractServiceImpl;
import com.wy.user.convert.PermissionConvert;
import com.wy.user.entity.Permission;
import com.wy.user.entity.dto.PermissionDTO;
import com.wy.user.mapper.PermissionMapper;
import com.wy.user.query.PermissionQuery;
import com.wy.user.service.PermissionService;

/**
 * 权限业务层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service("permissionService")
public class PermissionServiceImpl extends AbstractServiceImpl<Permission, PermissionDTO, PermissionQuery, PermissionConvert, PermissionMapper> implements PermissionService {

}