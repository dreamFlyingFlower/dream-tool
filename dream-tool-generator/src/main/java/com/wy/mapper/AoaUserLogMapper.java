package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaUserLog;
import com.wy.model.AoaUserLogExample;
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
public interface AoaUserLogMapper extends BaseMapper<AoaUserLog, Long> {

	long countByExample(AoaUserLogExample example);

	int deleteByExample(AoaUserLogExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaUserLog record);

	int insertSelective(AoaUserLog record);

	List<AoaUserLog> selectByExample(AoaUserLogExample example);

	AoaUserLog selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaUserLog record,
			@Param("example") AoaUserLogExample example);

	int updateByExample(@Param("record") AoaUserLog record, @Param("example") AoaUserLogExample example);

	int updateByPrimaryKeySelective(AoaUserLog record);

	int updateByPrimaryKey(AoaUserLog record);
}