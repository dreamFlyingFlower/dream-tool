package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaBursement;
import com.wy.model.AoaBursementExample;
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
public interface AoaBursementMapper extends BaseMapper<AoaBursement, Long> {

	long countByExample(AoaBursementExample example);

	int deleteByExample(AoaBursementExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaBursement record);

	int insertSelective(AoaBursement record);

	List<AoaBursement> selectByExample(AoaBursementExample example);

	AoaBursement selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaBursement record,
			@Param("example") AoaBursementExample example);

	int updateByExample(@Param("record") AoaBursement record, @Param("example") AoaBursementExample example);

	int updateByPrimaryKeySelective(AoaBursement record);

	int updateByPrimaryKey(AoaBursement record);
}