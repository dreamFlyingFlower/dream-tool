package com.wy.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.core.base.BaseMappers;
import com.wy.user.entity.Role;
import com.wy.user.query.RoleQuery;

/**
 * 角色信息数据层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface RoleMapper extends BaseMappers<Role> {

	List<Role> list(Page<Role> page, @Param("query") RoleQuery query);

	List<Role> list(@Param("query") RoleQuery query);
}