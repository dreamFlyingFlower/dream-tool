package com.wy.user.service.impl;

import org.springframework.stereotype.Service;

import com.wy.core.base.AbstractServiceImpl;
import com.wy.user.convert.MenuConvert;
import com.wy.user.entity.Menu;
import com.wy.user.entity.dto.MenuDTO;
import com.wy.user.mapper.MenuMapper;
import com.wy.user.query.MenuQuery;
import com.wy.user.service.MenuService;

/**
 * 菜单表业务层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service("menuService")
public class MenuServiceImpl extends AbstractServiceImpl<Menu, MenuDTO, MenuQuery, MenuConvert, MenuMapper> implements MenuService {

}