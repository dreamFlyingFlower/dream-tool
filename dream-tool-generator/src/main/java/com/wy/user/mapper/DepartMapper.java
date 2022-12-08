package com.wy.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.core.base.BaseMappers;
import com.wy.user.entity.Depart;
import com.wy.user.query.DepartQuery;

/**
 * 部门表数据层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface DepartMapper extends BaseMappers<Depart> {

	List<Depart> list(Page<Depart> page, @Param("query") DepartQuery query);

	List<Depart> list(@Param("query") DepartQuery query);
}