package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaMailReciver;
import com.wy.model.AoaMailReciverExample;
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
public interface AoaMailReciverMapper extends BaseMapper<AoaMailReciver, Long> {

	long countByExample(AoaMailReciverExample example);

	int deleteByExample(AoaMailReciverExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaMailReciver record);

	int insertSelective(AoaMailReciver record);

	List<AoaMailReciver> selectByExample(AoaMailReciverExample example);

	AoaMailReciver selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaMailReciver record,
			@Param("example") AoaMailReciverExample example);

	int updateByExample(@Param("record") AoaMailReciver record, @Param("example") AoaMailReciverExample example);

	int updateByPrimaryKeySelective(AoaMailReciver record);

	int updateByPrimaryKey(AoaMailReciver record);
}