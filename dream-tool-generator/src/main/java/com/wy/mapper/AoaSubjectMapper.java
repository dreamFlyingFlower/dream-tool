package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaSubject;
import com.wy.model.AoaSubjectExample;
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
public interface AoaSubjectMapper extends BaseMapper<AoaSubject, Long> {

	long countByExample(AoaSubjectExample example);

	int deleteByExample(AoaSubjectExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaSubject record);

	int insertSelective(AoaSubject record);

	List<AoaSubject> selectByExample(AoaSubjectExample example);

	AoaSubject selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaSubject record,
			@Param("example") AoaSubjectExample example);

	int updateByExample(@Param("record") AoaSubject record, @Param("example") AoaSubjectExample example);

	int updateByPrimaryKeySelective(AoaSubject record);

	int updateByPrimaryKey(AoaSubject record);
}