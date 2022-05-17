package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaHoliday;
import com.wy.model.AoaHolidayExample;
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
public interface AoaHolidayMapper extends BaseMapper<AoaHoliday, Long> {

	long countByExample(AoaHolidayExample example);

	int deleteByExample(AoaHolidayExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaHoliday record);

	int insertSelective(AoaHoliday record);

	List<AoaHoliday> selectByExample(AoaHolidayExample example);

	AoaHoliday selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaHoliday record,
			@Param("example") AoaHolidayExample example);

	int updateByExample(@Param("record") AoaHoliday record, @Param("example") AoaHolidayExample example);

	int updateByPrimaryKeySelective(AoaHoliday record);

	int updateByPrimaryKey(AoaHoliday record);
}