package com.wy.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wy.core.base.BaseMappers;
import com.wy.user.entity.Org;
import com.wy.user.query.OrgQuery;

/**
 * 组织机构表数据层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface OrgMapper extends BaseMappers<Org> {

	List<Org> list(Page<Org> page, @Param("query") OrgQuery query);

	List<Org> list(@Param("query") OrgQuery query);
}