package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaReplyList;
import com.wy.model.AoaReplyListExample;
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
public interface AoaReplyListMapper extends BaseMapper<AoaReplyList, Long> {

	long countByExample(AoaReplyListExample example);

	int deleteByExample(AoaReplyListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaReplyList record);

	int insertSelective(AoaReplyList record);

	List<AoaReplyList> selectByExample(AoaReplyListExample example);

	AoaReplyList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaReplyList record,
			@Param("example") AoaReplyListExample example);

	int updateByExample(@Param("record") AoaReplyList record, @Param("example") AoaReplyListExample example);

	int updateByPrimaryKeySelective(AoaReplyList record);

	int updateByPrimaryKey(AoaReplyList record);
}