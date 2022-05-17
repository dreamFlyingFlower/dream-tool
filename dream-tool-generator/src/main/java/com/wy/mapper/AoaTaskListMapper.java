package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaTaskList;
import com.wy.model.AoaTaskListExample;
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
public interface AoaTaskListMapper extends BaseMapper<AoaTaskList, Long> {

	long countByExample(AoaTaskListExample example);

	int deleteByExample(AoaTaskListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaTaskList record);

	int insertSelective(AoaTaskList record);

	List<AoaTaskList> selectByExample(AoaTaskListExample example);

	AoaTaskList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaTaskList record,
			@Param("example") AoaTaskListExample example);

	int updateByExample(@Param("record") AoaTaskList record, @Param("example") AoaTaskListExample example);

	int updateByPrimaryKeySelective(AoaTaskList record);

	int updateByPrimaryKey(AoaTaskList record);
}