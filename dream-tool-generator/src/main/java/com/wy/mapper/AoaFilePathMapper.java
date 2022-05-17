package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaFilePath;
import com.wy.model.AoaFilePathExample;
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
public interface AoaFilePathMapper extends BaseMapper<AoaFilePath, Long> {

	long countByExample(AoaFilePathExample example);

	int deleteByExample(AoaFilePathExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaFilePath record);

	int insertSelective(AoaFilePath record);

	List<AoaFilePath> selectByExample(AoaFilePathExample example);

	AoaFilePath selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaFilePath record,
			@Param("example") AoaFilePathExample example);

	int updateByExample(@Param("record") AoaFilePath record, @Param("example") AoaFilePathExample example);

	int updateByPrimaryKeySelective(AoaFilePath record);

	int updateByPrimaryKey(AoaFilePath record);
}