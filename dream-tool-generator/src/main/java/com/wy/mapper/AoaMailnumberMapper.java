package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaMailnumber;
import com.wy.model.AoaMailnumberExample;
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
public interface AoaMailnumberMapper extends BaseMapper<AoaMailnumber, Long> {

	long countByExample(AoaMailnumberExample example);

	int deleteByExample(AoaMailnumberExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaMailnumber record);

	int insertSelective(AoaMailnumber record);

	List<AoaMailnumber> selectByExample(AoaMailnumberExample example);

	AoaMailnumber selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaMailnumber record,
			@Param("example") AoaMailnumberExample example);

	int updateByExample(@Param("record") AoaMailnumber record, @Param("example") AoaMailnumberExample example);

	int updateByPrimaryKeySelective(AoaMailnumber record);

	int updateByPrimaryKey(AoaMailnumber record);
}