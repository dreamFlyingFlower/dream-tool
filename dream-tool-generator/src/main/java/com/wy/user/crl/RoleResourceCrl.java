package com.wy.user.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.core.base.AbstractCrl;
import com.wy.user.entity.RoleResource;
import com.wy.user.entity.dto.RoleResourceDTO;
import com.wy.user.query.RoleResourceQuery;

import io.swagger.annotations.Api;

/**
 * 角色-资源关系API
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "角色-资源关系API")
@RestController
@RequestMapping("roleResource")
public class RoleResourceCrl extends AbstractCrl<RoleResource, RoleResourceDTO, RoleResourceQuery> {

}