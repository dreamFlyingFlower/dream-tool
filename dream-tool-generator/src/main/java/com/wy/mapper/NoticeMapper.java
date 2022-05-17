package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.Notice;
import com.wy.model.NoticeExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 公告表
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice, Long> {

	long countByExample(NoticeExample example);

	int deleteByExample(NoticeExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Notice record);

	int insertSelective(Notice record);

	List<Notice> selectByExample(NoticeExample example);

	Notice selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Notice record,
			@Param("example") NoticeExample example);

	int updateByExample(@Param("record") Notice record, @Param("example") NoticeExample example);

	int updateByPrimaryKeySelective(Notice record);

	int updateByPrimaryKey(Notice record);
}