package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaStatusList;
import com.wy.model.AoaStatusListExample;
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
public interface AoaStatusListMapper extends BaseMapper<AoaStatusList, Long> {

	long countByExample(AoaStatusListExample example);

	int deleteByExample(AoaStatusListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaStatusList record);

	int insertSelective(AoaStatusList record);

	List<AoaStatusList> selectByExample(AoaStatusListExample example);

	AoaStatusList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaStatusList record,
			@Param("example") AoaStatusListExample example);

	int updateByExample(@Param("record") AoaStatusList record, @Param("example") AoaStatusListExample example);

	int updateByPrimaryKeySelective(AoaStatusList record);

	int updateByPrimaryKey(AoaStatusList record);
}