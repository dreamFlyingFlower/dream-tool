package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaScheduleUser;
import com.wy.model.AoaScheduleUserExample;
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
public interface AoaScheduleUserMapper extends BaseMapper<AoaScheduleUser, Long> {

	long countByExample(AoaScheduleUserExample example);

	int deleteByExample(AoaScheduleUserExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaScheduleUser record);

	int insertSelective(AoaScheduleUser record);

	List<AoaScheduleUser> selectByExample(AoaScheduleUserExample example);

	AoaScheduleUser selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaScheduleUser record,
			@Param("example") AoaScheduleUserExample example);

	int updateByExample(@Param("record") AoaScheduleUser record, @Param("example") AoaScheduleUserExample example);

	int updateByPrimaryKeySelective(AoaScheduleUser record);

	int updateByPrimaryKey(AoaScheduleUser record);
}