package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaOvertime;
import com.wy.model.AoaOvertimeExample;
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
public interface AoaOvertimeMapper extends BaseMapper<AoaOvertime, Long> {

	long countByExample(AoaOvertimeExample example);

	int deleteByExample(AoaOvertimeExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaOvertime record);

	int insertSelective(AoaOvertime record);

	List<AoaOvertime> selectByExample(AoaOvertimeExample example);

	AoaOvertime selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaOvertime record,
			@Param("example") AoaOvertimeExample example);

	int updateByExample(@Param("record") AoaOvertime record, @Param("example") AoaOvertimeExample example);

	int updateByPrimaryKeySelective(AoaOvertime record);

	int updateByPrimaryKey(AoaOvertime record);
}