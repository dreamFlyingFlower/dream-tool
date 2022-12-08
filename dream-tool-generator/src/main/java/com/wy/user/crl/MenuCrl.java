package com.wy.user.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.core.base.AbstractCrl;
import com.wy.user.entity.Menu;
import com.wy.user.entity.dto.MenuDTO;
import com.wy.user.query.MenuQuery;

import io.swagger.annotations.Api;

/**
 * 菜单表API
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "菜单表API")
@RestController
@RequestMapping("menu")
public class MenuCrl extends AbstractCrl<Menu, MenuDTO, MenuQuery> {

}