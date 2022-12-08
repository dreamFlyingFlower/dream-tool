package com.wy.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.core.base.BaseMappers;
import com.wy.user.entity.Menu;
import com.wy.user.query.MenuQuery;

/**
 * 菜单表数据层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface MenuMapper extends BaseMappers<Menu> {

	List<Menu> list(Page<Menu> page, @Param("query") MenuQuery query);

	List<Menu> list(@Param("query") MenuQuery query);
}