package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaRolePowerList;
import com.wy.model.AoaRolePowerListExample;
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
public interface AoaRolePowerListMapper extends BaseMapper<AoaRolePowerList, Long> {

	long countByExample(AoaRolePowerListExample example);

	int deleteByExample(AoaRolePowerListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaRolePowerList record);

	int insertSelective(AoaRolePowerList record);

	List<AoaRolePowerList> selectByExample(AoaRolePowerListExample example);

	AoaRolePowerList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaRolePowerList record,
			@Param("example") AoaRolePowerListExample example);

	int updateByExample(@Param("record") AoaRolePowerList record, @Param("example") AoaRolePowerListExample example);

	int updateByPrimaryKeySelective(AoaRolePowerList record);

	int updateByPrimaryKey(AoaRolePowerList record);
}