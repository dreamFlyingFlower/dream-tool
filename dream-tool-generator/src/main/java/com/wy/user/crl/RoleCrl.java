package com.wy.user.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.core.base.AbstractCrl;
import com.wy.user.entity.Role;
import com.wy.user.entity.dto.RoleDTO;
import com.wy.user.query.RoleQuery;

import io.swagger.annotations.Api;

/**
 * 角色信息API
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "角色信息API")
@RestController
@RequestMapping("role")
public class RoleCrl extends AbstractCrl<Role, RoleDTO, RoleQuery> {

}