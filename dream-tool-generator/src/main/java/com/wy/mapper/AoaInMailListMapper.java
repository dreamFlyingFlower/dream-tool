package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaInMailList;
import com.wy.model.AoaInMailListExample;
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
public interface AoaInMailListMapper extends BaseMapper<AoaInMailList, Long> {

	long countByExample(AoaInMailListExample example);

	int deleteByExample(AoaInMailListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaInMailList record);

	int insertSelective(AoaInMailList record);

	List<AoaInMailList> selectByExample(AoaInMailListExample example);

	AoaInMailList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaInMailList record,
			@Param("example") AoaInMailListExample example);

	int updateByExample(@Param("record") AoaInMailList record, @Param("example") AoaInMailListExample example);

	int updateByPrimaryKeySelective(AoaInMailList record);

	int updateByPrimaryKey(AoaInMailList record);
}