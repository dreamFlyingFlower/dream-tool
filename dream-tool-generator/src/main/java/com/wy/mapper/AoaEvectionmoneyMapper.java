package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaEvectionmoney;
import com.wy.model.AoaEvectionmoneyExample;
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
public interface AoaEvectionmoneyMapper extends BaseMapper<AoaEvectionmoney, Long> {

	long countByExample(AoaEvectionmoneyExample example);

	int deleteByExample(AoaEvectionmoneyExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaEvectionmoney record);

	int insertSelective(AoaEvectionmoney record);

	List<AoaEvectionmoney> selectByExample(AoaEvectionmoneyExample example);

	AoaEvectionmoney selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaEvectionmoney record,
			@Param("example") AoaEvectionmoneyExample example);

	int updateByExample(@Param("record") AoaEvectionmoney record, @Param("example") AoaEvectionmoneyExample example);

	int updateByPrimaryKeySelective(AoaEvectionmoney record);

	int updateByPrimaryKey(AoaEvectionmoney record);
}