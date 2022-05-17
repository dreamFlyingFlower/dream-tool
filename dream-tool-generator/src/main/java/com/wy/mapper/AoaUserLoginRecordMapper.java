package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaUserLoginRecord;
import com.wy.model.AoaUserLoginRecordExample;
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
public interface AoaUserLoginRecordMapper extends BaseMapper<AoaUserLoginRecord, Long> {

	long countByExample(AoaUserLoginRecordExample example);

	int deleteByExample(AoaUserLoginRecordExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaUserLoginRecord record);

	int insertSelective(AoaUserLoginRecord record);

	List<AoaUserLoginRecord> selectByExample(AoaUserLoginRecordExample example);

	AoaUserLoginRecord selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaUserLoginRecord record,
			@Param("example") AoaUserLoginRecordExample example);

	int updateByExample(@Param("record") AoaUserLoginRecord record, @Param("example") AoaUserLoginRecordExample example);

	int updateByPrimaryKeySelective(AoaUserLoginRecord record);

	int updateByPrimaryKey(AoaUserLoginRecord record);
}