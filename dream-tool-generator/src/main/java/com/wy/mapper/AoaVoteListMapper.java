package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaVoteList;
import com.wy.model.AoaVoteListExample;
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
public interface AoaVoteListMapper extends BaseMapper<AoaVoteList, Long> {

	long countByExample(AoaVoteListExample example);

	int deleteByExample(AoaVoteListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaVoteList record);

	int insertSelective(AoaVoteList record);

	List<AoaVoteList> selectByExample(AoaVoteListExample example);

	AoaVoteList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaVoteList record,
			@Param("example") AoaVoteListExample example);

	int updateByExample(@Param("record") AoaVoteList record, @Param("example") AoaVoteListExample example);

	int updateByPrimaryKeySelective(AoaVoteList record);

	int updateByPrimaryKey(AoaVoteList record);
}