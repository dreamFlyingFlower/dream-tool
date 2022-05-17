package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaTypeList;
import com.wy.model.AoaTypeListExample;
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
public interface AoaTypeListMapper extends BaseMapper<AoaTypeList, Long> {

	long countByExample(AoaTypeListExample example);

	int deleteByExample(AoaTypeListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaTypeList record);

	int insertSelective(AoaTypeList record);

	List<AoaTypeList> selectByExample(AoaTypeListExample example);

	AoaTypeList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaTypeList record,
			@Param("example") AoaTypeListExample example);

	int updateByExample(@Param("record") AoaTypeList record, @Param("example") AoaTypeListExample example);

	int updateByPrimaryKeySelective(AoaTypeList record);

	int updateByPrimaryKey(AoaTypeList record);
}