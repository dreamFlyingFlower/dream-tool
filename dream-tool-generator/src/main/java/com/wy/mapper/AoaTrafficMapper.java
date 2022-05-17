package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaTraffic;
import com.wy.model.AoaTrafficExample;
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
public interface AoaTrafficMapper extends BaseMapper<AoaTraffic, Long> {

	long countByExample(AoaTrafficExample example);

	int deleteByExample(AoaTrafficExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaTraffic record);

	int insertSelective(AoaTraffic record);

	List<AoaTraffic> selectByExample(AoaTrafficExample example);

	AoaTraffic selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaTraffic record,
			@Param("example") AoaTrafficExample example);

	int updateByExample(@Param("record") AoaTraffic record, @Param("example") AoaTrafficExample example);

	int updateByPrimaryKeySelective(AoaTraffic record);

	int updateByPrimaryKey(AoaTraffic record);
}