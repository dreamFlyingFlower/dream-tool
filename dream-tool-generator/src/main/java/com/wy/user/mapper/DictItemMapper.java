package com.wy.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.core.base.BaseMappers;
import com.wy.user.entity.DictItem;
import com.wy.user.query.DictItemQuery;

/**
 * 字典项数据层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface DictItemMapper extends BaseMappers<DictItem> {

	List<DictItem> list(Page<DictItem> page, @Param("query") DictItemQuery query);

	List<DictItem> list(@Param("query") DictItemQuery query);
}