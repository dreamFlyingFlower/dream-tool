package com.wy.user.service;

import com.wy.core.base.BaseService;
import com.wy.user.entity.UserRole;
import com.wy.user.entity.dto.UserRoleDTO;
import com.wy.user.query.UserRoleQuery;

/**
 * 账号-角色关系业务层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface UserRoleService extends BaseService<UserRole, UserRoleDTO, UserRoleQuery> {

}