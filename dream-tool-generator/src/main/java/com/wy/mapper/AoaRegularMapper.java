package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaRegular;
import com.wy.model.AoaRegularExample;
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
public interface AoaRegularMapper extends BaseMapper<AoaRegular, Long> {

	long countByExample(AoaRegularExample example);

	int deleteByExample(AoaRegularExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaRegular record);

	int insertSelective(AoaRegular record);

	List<AoaRegular> selectByExample(AoaRegularExample example);

	AoaRegular selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaRegular record,
			@Param("example") AoaRegularExample example);

	int updateByExample(@Param("record") AoaRegular record, @Param("example") AoaRegularExample example);

	int updateByPrimaryKeySelective(AoaRegular record);

	int updateByPrimaryKey(AoaRegular record);
}