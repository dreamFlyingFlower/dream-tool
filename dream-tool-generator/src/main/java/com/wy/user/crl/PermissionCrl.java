package com.wy.user.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.core.base.AbstractCrl;
import com.wy.user.entity.Permission;
import com.wy.user.entity.dto.PermissionDTO;
import com.wy.user.query.PermissionQuery;

import io.swagger.annotations.Api;

/**
 * 权限API
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "权限API")
@RestController
@RequestMapping("permission")
public class PermissionCrl extends AbstractCrl<Permission, PermissionDTO, PermissionQuery> {

}