package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaCatalog;
import com.wy.model.AoaCatalogExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface AoaCatalogMapper extends BaseMapper<AoaCatalog, Long> {

	long countByExample(AoaCatalogExample example);

	int deleteByExample(AoaCatalogExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaCatalog record);

	int insertSelective(AoaCatalog record);

	List<AoaCatalog> selectByExample(AoaCatalogExample example);

	AoaCatalog selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaCatalog record,
			@Param("example") AoaCatalogExample example);

	int updateByExample(@Param("record") AoaCatalog record, @Param("example") AoaCatalogExample example);

	int updateByPrimaryKeySelective(AoaCatalog record);

	int updateByPrimaryKey(AoaCatalog record);
}