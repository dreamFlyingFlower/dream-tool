package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaNotepaper;
import com.wy.model.AoaNotepaperExample;
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
public interface AoaNotepaperMapper extends BaseMapper<AoaNotepaper, Long> {

	long countByExample(AoaNotepaperExample example);

	int deleteByExample(AoaNotepaperExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaNotepaper record);

	int insertSelective(AoaNotepaper record);

	List<AoaNotepaper> selectByExample(AoaNotepaperExample example);

	AoaNotepaper selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaNotepaper record,
			@Param("example") AoaNotepaperExample example);

	int updateByExample(@Param("record") AoaNotepaper record, @Param("example") AoaNotepaperExample example);

	int updateByPrimaryKeySelective(AoaNotepaper record);

	int updateByPrimaryKey(AoaNotepaper record);
}