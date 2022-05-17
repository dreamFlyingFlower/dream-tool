package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaFileList;
import com.wy.model.AoaFileListExample;
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
public interface AoaFileListMapper extends BaseMapper<AoaFileList, Long> {

	long countByExample(AoaFileListExample example);

	int deleteByExample(AoaFileListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaFileList record);

	int insertSelective(AoaFileList record);

	List<AoaFileList> selectByExample(AoaFileListExample example);

	AoaFileList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaFileList record,
			@Param("example") AoaFileListExample example);

	int updateByExample(@Param("record") AoaFileList record, @Param("example") AoaFileListExample example);

	int updateByPrimaryKeySelective(AoaFileList record);

	int updateByPrimaryKey(AoaFileList record);
}