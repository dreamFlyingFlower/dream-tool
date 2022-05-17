package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaDiscussList;
import com.wy.model.AoaDiscussListExample;
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
public interface AoaDiscussListMapper extends BaseMapper<AoaDiscussList, Long> {

	long countByExample(AoaDiscussListExample example);

	int deleteByExample(AoaDiscussListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaDiscussList record);

	int insertSelective(AoaDiscussList record);

	List<AoaDiscussList> selectByExample(AoaDiscussListExample example);

	AoaDiscussList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaDiscussList record,
			@Param("example") AoaDiscussListExample example);

	int updateByExample(@Param("record") AoaDiscussList record, @Param("example") AoaDiscussListExample example);

	int updateByPrimaryKeySelective(AoaDiscussList record);

	int updateByPrimaryKey(AoaDiscussList record);
}