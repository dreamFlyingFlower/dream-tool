package com.wy.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.core.base.BaseMappers;
import com.wy.user.entity.Permission;
import com.wy.user.query.PermissionQuery;

/**
 * 权限数据层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface PermissionMapper extends BaseMappers<Permission> {

	List<Permission> list(Page<Permission> page, @Param("query") PermissionQuery query);

	List<Permission> list(@Param("query") PermissionQuery query);
}