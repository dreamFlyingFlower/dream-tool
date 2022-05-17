package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaNoticeUserRelation;
import com.wy.model.AoaNoticeUserRelationExample;
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
public interface AoaNoticeUserRelationMapper extends BaseMapper<AoaNoticeUserRelation, Long> {

	long countByExample(AoaNoticeUserRelationExample example);

	int deleteByExample(AoaNoticeUserRelationExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaNoticeUserRelation record);

	int insertSelective(AoaNoticeUserRelation record);

	List<AoaNoticeUserRelation> selectByExample(AoaNoticeUserRelationExample example);

	AoaNoticeUserRelation selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaNoticeUserRelation record,
			@Param("example") AoaNoticeUserRelationExample example);

	int updateByExample(@Param("record") AoaNoticeUserRelation record, @Param("example") AoaNoticeUserRelationExample example);

	int updateByPrimaryKeySelective(AoaNoticeUserRelation record);

	int updateByPrimaryKey(AoaNoticeUserRelation record);
}