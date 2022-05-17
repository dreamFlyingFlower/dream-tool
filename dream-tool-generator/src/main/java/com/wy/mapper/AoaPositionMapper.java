package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaPosition;
import com.wy.model.AoaPositionExample;
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
public interface AoaPositionMapper extends BaseMapper<AoaPosition, Long> {

	long countByExample(AoaPositionExample example);

	int deleteByExample(AoaPositionExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaPosition record);

	int insertSelective(AoaPosition record);

	List<AoaPosition> selectByExample(AoaPositionExample example);

	AoaPosition selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaPosition record,
			@Param("example") AoaPositionExample example);

	int updateByExample(@Param("record") AoaPosition record, @Param("example") AoaPositionExample example);

	int updateByPrimaryKeySelective(AoaPosition record);

	int updateByPrimaryKey(AoaPosition record);
}