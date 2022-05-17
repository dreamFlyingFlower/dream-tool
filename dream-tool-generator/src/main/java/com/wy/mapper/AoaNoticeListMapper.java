package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaNoticeList;
import com.wy.model.AoaNoticeListExample;
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
public interface AoaNoticeListMapper extends BaseMapper<AoaNoticeList, Long> {

	long countByExample(AoaNoticeListExample example);

	int deleteByExample(AoaNoticeListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaNoticeList record);

	int insertSelective(AoaNoticeList record);

	List<AoaNoticeList> selectByExample(AoaNoticeListExample example);

	AoaNoticeList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaNoticeList record,
			@Param("example") AoaNoticeListExample example);

	int updateByExample(@Param("record") AoaNoticeList record, @Param("example") AoaNoticeListExample example);

	int updateByPrimaryKeySelective(AoaNoticeList record);

	int updateByPrimaryKey(AoaNoticeList record);
}