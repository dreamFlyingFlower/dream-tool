package com.wy.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.core.base.BaseMappers;
import com.wy.user.entity.RoleResource;
import com.wy.user.query.RoleResourceQuery;

/**
 * 角色-资源关系数据层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface RoleResourceMapper extends BaseMappers<RoleResource> {

	List<RoleResource> list(Page<RoleResource> page, @Param("query") RoleResourceQuery query);

	List<RoleResource> list(@Param("query") RoleResourceQuery query);
}