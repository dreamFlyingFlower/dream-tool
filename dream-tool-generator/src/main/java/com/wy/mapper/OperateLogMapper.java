package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.OperateLog;
import com.wy.model.OperateLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 操作日志表
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface OperateLogMapper extends BaseMapper<OperateLog, Long> {

	long countByExample(OperateLogExample example);

	int deleteByExample(OperateLogExample example);

	int deleteByPrimaryKey(Long id);

	int insert(OperateLog record);

	int insertSelective(OperateLog record);

	List<OperateLog> selectByExample(OperateLogExample example);

	OperateLog selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") OperateLog record,
			@Param("example") OperateLogExample example);

	int updateByExample(@Param("record") OperateLog record, @Param("example") OperateLogExample example);

	int updateByPrimaryKeySelective(OperateLog record);

	int updateByPrimaryKey(OperateLog record);
}