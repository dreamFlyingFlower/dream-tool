package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaResign;
import com.wy.model.AoaResignExample;
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
public interface AoaResignMapper extends BaseMapper<AoaResign, Long> {

	long countByExample(AoaResignExample example);

	int deleteByExample(AoaResignExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaResign record);

	int insertSelective(AoaResign record);

	List<AoaResign> selectByExample(AoaResignExample example);

	AoaResign selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaResign record,
			@Param("example") AoaResignExample example);

	int updateByExample(@Param("record") AoaResign record, @Param("example") AoaResignExample example);

	int updateByPrimaryKeySelective(AoaResign record);

	int updateByPrimaryKey(AoaResign record);
}