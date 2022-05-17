package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaDetailsburse;
import com.wy.model.AoaDetailsburseExample;
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
public interface AoaDetailsburseMapper extends BaseMapper<AoaDetailsburse, Long> {

	long countByExample(AoaDetailsburseExample example);

	int deleteByExample(AoaDetailsburseExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaDetailsburse record);

	int insertSelective(AoaDetailsburse record);

	List<AoaDetailsburse> selectByExample(AoaDetailsburseExample example);

	AoaDetailsburse selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaDetailsburse record,
			@Param("example") AoaDetailsburseExample example);

	int updateByExample(@Param("record") AoaDetailsburse record, @Param("example") AoaDetailsburseExample example);

	int updateByPrimaryKeySelective(AoaDetailsburse record);

	int updateByPrimaryKey(AoaDetailsburse record);
}