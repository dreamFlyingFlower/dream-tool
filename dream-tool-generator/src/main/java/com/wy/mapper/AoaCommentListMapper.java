package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaCommentList;
import com.wy.model.AoaCommentListExample;
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
public interface AoaCommentListMapper extends BaseMapper<AoaCommentList, Long> {

	long countByExample(AoaCommentListExample example);

	int deleteByExample(AoaCommentListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaCommentList record);

	int insertSelective(AoaCommentList record);

	List<AoaCommentList> selectByExample(AoaCommentListExample example);

	AoaCommentList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaCommentList record,
			@Param("example") AoaCommentListExample example);

	int updateByExample(@Param("record") AoaCommentList record, @Param("example") AoaCommentListExample example);

	int updateByPrimaryKeySelective(AoaCommentList record);

	int updateByPrimaryKey(AoaCommentList record);
}