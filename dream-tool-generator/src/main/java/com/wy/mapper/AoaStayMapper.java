package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaStay;
import com.wy.model.AoaStayExample;
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
public interface AoaStayMapper extends BaseMapper<AoaStay, Long> {

	long countByExample(AoaStayExample example);

	int deleteByExample(AoaStayExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaStay record);

	int insertSelective(AoaStay record);

	List<AoaStay> selectByExample(AoaStayExample example);

	AoaStay selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaStay record,
			@Param("example") AoaStayExample example);

	int updateByExample(@Param("record") AoaStay record, @Param("example") AoaStayExample example);

	int updateByPrimaryKeySelective(AoaStay record);

	int updateByPrimaryKey(AoaStay record);
}