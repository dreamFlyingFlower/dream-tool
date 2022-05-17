package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaRole_;
import com.wy.model.AoaRole_Example;
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
public interface AoaRole_Mapper extends BaseMapper<AoaRole_, Long> {

	long countByExample(AoaRole_Example example);

	int deleteByExample(AoaRole_Example example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaRole_ record);

	int insertSelective(AoaRole_ record);

	List<AoaRole_> selectByExample(AoaRole_Example example);

	AoaRole_ selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaRole_ record,
			@Param("example") AoaRole_Example example);

	int updateByExample(@Param("record") AoaRole_ record, @Param("example") AoaRole_Example example);

	int updateByPrimaryKeySelective(AoaRole_ record);

	int updateByPrimaryKey(AoaRole_ record);
}