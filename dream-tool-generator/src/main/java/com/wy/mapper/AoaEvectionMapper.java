package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaEvection;
import com.wy.model.AoaEvectionExample;
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
public interface AoaEvectionMapper extends BaseMapper<AoaEvection, Long> {

	long countByExample(AoaEvectionExample example);

	int deleteByExample(AoaEvectionExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaEvection record);

	int insertSelective(AoaEvection record);

	List<AoaEvection> selectByExample(AoaEvectionExample example);

	AoaEvection selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaEvection record,
			@Param("example") AoaEvectionExample example);

	int updateByExample(@Param("record") AoaEvection record, @Param("example") AoaEvectionExample example);

	int updateByPrimaryKeySelective(AoaEvection record);

	int updateByPrimaryKey(AoaEvection record);
}