package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaDept;
import com.wy.model.AoaDeptExample;
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
public interface AoaDeptMapper extends BaseMapper<AoaDept, Long> {

	long countByExample(AoaDeptExample example);

	int deleteByExample(AoaDeptExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaDept record);

	int insertSelective(AoaDept record);

	List<AoaDept> selectByExample(AoaDeptExample example);

	AoaDept selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaDept record,
			@Param("example") AoaDeptExample example);

	int updateByExample(@Param("record") AoaDept record, @Param("example") AoaDeptExample example);

	int updateByPrimaryKeySelective(AoaDept record);

	int updateByPrimaryKey(AoaDept record);
}