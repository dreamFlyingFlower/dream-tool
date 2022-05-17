package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaProcessList;
import com.wy.model.AoaProcessListExample;
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
public interface AoaProcessListMapper extends BaseMapper<AoaProcessList, Long> {

	long countByExample(AoaProcessListExample example);

	int deleteByExample(AoaProcessListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaProcessList record);

	int insertSelective(AoaProcessList record);

	List<AoaProcessList> selectByExample(AoaProcessListExample example);

	AoaProcessList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaProcessList record,
			@Param("example") AoaProcessListExample example);

	int updateByExample(@Param("record") AoaProcessList record, @Param("example") AoaProcessListExample example);

	int updateByPrimaryKeySelective(AoaProcessList record);

	int updateByPrimaryKey(AoaProcessList record);
}