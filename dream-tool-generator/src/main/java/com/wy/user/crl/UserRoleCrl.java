package com.wy.user.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.core.base.AbstractCrl;
import com.wy.user.entity.UserRole;
import com.wy.user.entity.dto.UserRoleDTO;
import com.wy.user.query.UserRoleQuery;

import io.swagger.annotations.Api;

/**
 * 账号-角色关系API
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "账号-角色关系API")
@RestController
@RequestMapping("userRole")
public class UserRoleCrl extends AbstractCrl<UserRole, UserRoleDTO, UserRoleQuery> {

}