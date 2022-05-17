package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaDirector;
import com.wy.model.AoaDirectorExample;
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
public interface AoaDirectorMapper extends BaseMapper<AoaDirector, Long> {

	long countByExample(AoaDirectorExample example);

	int deleteByExample(AoaDirectorExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaDirector record);

	int insertSelective(AoaDirector record);

	List<AoaDirector> selectByExample(AoaDirectorExample example);

	AoaDirector selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaDirector record,
			@Param("example") AoaDirectorExample example);

	int updateByExample(@Param("record") AoaDirector record, @Param("example") AoaDirectorExample example);

	int updateByPrimaryKeySelective(AoaDirector record);

	int updateByPrimaryKey(AoaDirector record);
}