package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaPlanList;
import com.wy.model.AoaPlanListExample;
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
public interface AoaPlanListMapper extends BaseMapper<AoaPlanList, Long> {

	long countByExample(AoaPlanListExample example);

	int deleteByExample(AoaPlanListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaPlanList record);

	int insertSelective(AoaPlanList record);

	List<AoaPlanList> selectByExample(AoaPlanListExample example);

	AoaPlanList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaPlanList record,
			@Param("example") AoaPlanListExample example);

	int updateByExample(@Param("record") AoaPlanList record, @Param("example") AoaPlanListExample example);

	int updateByPrimaryKeySelective(AoaPlanList record);

	int updateByPrimaryKey(AoaPlanList record);
}