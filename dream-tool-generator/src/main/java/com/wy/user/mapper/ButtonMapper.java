package com.wy.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.core.base.BaseMappers;
import com.wy.user.entity.Button;
import com.wy.user.query.ButtonQuery;

/**
 * 按钮表数据层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface ButtonMapper extends BaseMappers<Button> {

	List<Button> list(Page<Button> page, @Param("query") ButtonQuery query);

	List<Button> list(@Param("query") ButtonQuery query);
}