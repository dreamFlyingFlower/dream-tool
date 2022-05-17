package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.UserNotice;
import com.wy.model.UserNoticeExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户和公告关联表
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface UserNoticeMapper extends BaseMapper<UserNotice, Long> {

	long countByExample(UserNoticeExample example);

	int deleteByExample(UserNoticeExample example);

	int deleteByPrimaryKey(Long id);

	int insert(UserNotice record);

	int insertSelective(UserNotice record);

	List<UserNotice> selectByExample(UserNoticeExample example);

	UserNotice selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") UserNotice record,
			@Param("example") UserNoticeExample example);

	int updateByExample(@Param("record") UserNotice record, @Param("example") UserNoticeExample example);

	int updateByPrimaryKeySelective(UserNotice record);

	int updateByPrimaryKey(UserNotice record);
}