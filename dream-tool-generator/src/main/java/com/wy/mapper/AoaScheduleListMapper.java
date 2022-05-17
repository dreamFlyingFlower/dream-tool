package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaScheduleList;
import com.wy.model.AoaScheduleListExample;
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
public interface AoaScheduleListMapper extends BaseMapper<AoaScheduleList, Long> {

	long countByExample(AoaScheduleListExample example);

	int deleteByExample(AoaScheduleListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaScheduleList record);

	int insertSelective(AoaScheduleList record);

	List<AoaScheduleList> selectByExample(AoaScheduleListExample example);

	AoaScheduleList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaScheduleList record,
			@Param("example") AoaScheduleListExample example);

	int updateByExample(@Param("record") AoaScheduleList record, @Param("example") AoaScheduleListExample example);

	int updateByPrimaryKeySelective(AoaScheduleList record);

	int updateByPrimaryKey(AoaScheduleList record);
}