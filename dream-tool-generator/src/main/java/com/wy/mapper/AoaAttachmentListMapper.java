package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaAttachmentList;
import com.wy.model.AoaAttachmentListExample;
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
public interface AoaAttachmentListMapper extends BaseMapper<AoaAttachmentList, Long> {

	long countByExample(AoaAttachmentListExample example);

	int deleteByExample(AoaAttachmentListExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaAttachmentList record);

	int insertSelective(AoaAttachmentList record);

	List<AoaAttachmentList> selectByExample(AoaAttachmentListExample example);

	AoaAttachmentList selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaAttachmentList record,
			@Param("example") AoaAttachmentListExample example);

	int updateByExample(@Param("record") AoaAttachmentList record, @Param("example") AoaAttachmentListExample example);

	int updateByPrimaryKeySelective(AoaAttachmentList record);

	int updateByPrimaryKey(AoaAttachmentList record);
}