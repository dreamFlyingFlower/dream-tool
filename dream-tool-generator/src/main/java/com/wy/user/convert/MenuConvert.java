package com.wy.user.convert;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.wy.core.base.BaseConvert;
import com.wy.user.entity.Menu;
import com.wy.user.entity.dto.MenuDTO;

/**
 * 菜单表数据库实体与DTO互转
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MenuConvert extends BaseConvert<Menu, MenuDTO> {

	MenuConvert INSTANCE = Mappers.getMapper(MenuConvert.class);

}