package com.wy.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.core.base.BaseMappers;
import com.wy.user.entity.Dict;
import com.wy.user.query.DictQuery;

/**
 * 字典表数据层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface DictMapper extends BaseMappers<Dict> {

	List<Dict> list(Page<Dict> page, @Param("query") DictQuery query);

	List<Dict> list(@Param("query") DictQuery query);
}