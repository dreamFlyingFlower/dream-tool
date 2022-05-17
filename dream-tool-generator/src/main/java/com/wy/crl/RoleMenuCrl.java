package com.wy.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.RoleMenu;

import io.swagger.annotations.Api;

/**
 * 角色菜单权限表API
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "角色菜单权限表API")
@RestController
@RequestMapping("roleMenu")
public class RoleMenuCrl extends AbstractCrl<RoleMenu, Long> {

}