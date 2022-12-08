package com.wy.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.core.base.BaseMappers;
import com.wy.user.entity.User;
import com.wy.user.query.UserQuery;

/**
 * 用户表数据层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface UserMapper extends BaseMappers<User> {

	List<User> list(Page<User> page, @Param("query") UserQuery query);

	List<User> list(@Param("query") UserQuery query);
}