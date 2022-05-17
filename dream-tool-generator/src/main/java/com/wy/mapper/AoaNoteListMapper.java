package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaNoteList;
import com.wy.model.AoaNoteListExample;
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
public interface AoaNoteListMapper extends BaseMapper<AoaNoteList, Long> {

	long countByExample(AoaNoteListExample example);

	int deleteByExample(AoaNoteListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaNoteList record);

	int insertSelective(AoaNoteList record);

	List<AoaNoteList> selectByExample(AoaNoteListExample example);

	AoaNoteList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaNoteList record,
			@Param("example") AoaNoteListExample example);

	int updateByExample(@Param("record") AoaNoteList record, @Param("example") AoaNoteListExample example);

	int updateByPrimaryKeySelective(AoaNoteList record);

	int updateByPrimaryKey(AoaNoteList record);
}