package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaAttendsList;
import com.wy.model.AoaAttendsListExample;
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
public interface AoaAttendsListMapper extends BaseMapper<AoaAttendsList, Long> {

	long countByExample(AoaAttendsListExample example);

	int deleteByExample(AoaAttendsListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaAttendsList record);

	int insertSelective(AoaAttendsList record);

	List<AoaAttendsList> selectByExample(AoaAttendsListExample example);

	AoaAttendsList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaAttendsList record,
			@Param("example") AoaAttendsListExample example);

	int updateByExample(@Param("record") AoaAttendsList record, @Param("example") AoaAttendsListExample example);

	int updateByPrimaryKeySelective(AoaAttendsList record);

	int updateByPrimaryKey(AoaAttendsList record);
}