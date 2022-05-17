package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaReceiverNote;
import com.wy.model.AoaReceiverNoteExample;
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
public interface AoaReceiverNoteMapper extends BaseMapper<AoaReceiverNote, Long> {

	long countByExample(AoaReceiverNoteExample example);

	int deleteByExample(AoaReceiverNoteExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaReceiverNote record);

	int insertSelective(AoaReceiverNote record);

	List<AoaReceiverNote> selectByExample(AoaReceiverNoteExample example);

	AoaReceiverNote selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaReceiverNote record,
			@Param("example") AoaReceiverNoteExample example);

	int updateByExample(@Param("record") AoaReceiverNote record, @Param("example") AoaReceiverNoteExample example);

	int updateByPrimaryKeySelective(AoaReceiverNote record);

	int updateByPrimaryKey(AoaReceiverNote record);
}