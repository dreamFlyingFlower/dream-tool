package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaTaskLogger;
import com.wy.model.AoaTaskLoggerExample;
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
public interface AoaTaskLoggerMapper extends BaseMapper<AoaTaskLogger, Long> {

	long countByExample(AoaTaskLoggerExample example);

	int deleteByExample(AoaTaskLoggerExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaTaskLogger record);

	int insertSelective(AoaTaskLogger record);

	List<AoaTaskLogger> selectByExample(AoaTaskLoggerExample example);

	AoaTaskLogger selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaTaskLogger record,
			@Param("example") AoaTaskLoggerExample example);

	int updateByExample(@Param("record") AoaTaskLogger record, @Param("example") AoaTaskLoggerExample example);

	int updateByPrimaryKeySelective(AoaTaskLogger record);

	int updateByPrimaryKey(AoaTaskLogger record);
}