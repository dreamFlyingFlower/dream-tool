package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaReviewed;
import com.wy.model.AoaReviewedExample;
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
public interface AoaReviewedMapper extends BaseMapper<AoaReviewed, Long> {

	long countByExample(AoaReviewedExample example);

	int deleteByExample(AoaReviewedExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaReviewed record);

	int insertSelective(AoaReviewed record);

	List<AoaReviewed> selectByExample(AoaReviewedExample example);

	AoaReviewed selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaReviewed record,
			@Param("example") AoaReviewedExample example);

	int updateByExample(@Param("record") AoaReviewed record, @Param("example") AoaReviewedExample example);

	int updateByPrimaryKeySelective(AoaReviewed record);

	int updateByPrimaryKey(AoaReviewed record);
}