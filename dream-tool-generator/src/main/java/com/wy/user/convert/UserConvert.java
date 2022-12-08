package com.wy.user.convert;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.wy.core.base.BaseConvert;
import com.wy.user.entity.User;
import com.wy.user.entity.dto.UserDTO;

/**
 * 用户表数据库实体与DTO互转
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserConvert extends BaseConvert<User, UserDTO> {

	UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

}