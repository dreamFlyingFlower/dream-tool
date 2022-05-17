package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaVoteTitles;
import com.wy.model.AoaVoteTitlesExample;
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
public interface AoaVoteTitlesMapper extends BaseMapper<AoaVoteTitles, Long> {

	long countByExample(AoaVoteTitlesExample example);

	int deleteByExample(AoaVoteTitlesExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaVoteTitles record);

	int insertSelective(AoaVoteTitles record);

	List<AoaVoteTitles> selectByExample(AoaVoteTitlesExample example);

	AoaVoteTitles selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaVoteTitles record,
			@Param("example") AoaVoteTitlesExample example);

	int updateByExample(@Param("record") AoaVoteTitles record, @Param("example") AoaVoteTitlesExample example);

	int updateByPrimaryKeySelective(AoaVoteTitles record);

	int updateByPrimaryKey(AoaVoteTitles record);
}