package com.wy.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.core.base.BaseMappers;
import com.wy.user.entity.UserRole;
import com.wy.user.query.UserRoleQuery;

/**
 * 账号-角色关系数据层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface UserRoleMapper extends BaseMappers<UserRole> {

	List<UserRole> list(Page<UserRole> page, @Param("query") UserRoleQuery query);

	List<UserRole> list(@Param("query") UserRoleQuery query);
}