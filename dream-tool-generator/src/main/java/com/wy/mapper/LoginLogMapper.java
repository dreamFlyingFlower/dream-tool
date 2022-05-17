package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.LoginLog;
import com.wy.model.LoginLogExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户登录日志
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog, Long> {

	long countByExample(LoginLogExample example);

	int deleteByExample(LoginLogExample example);

	int deleteByPrimaryKey(Long id);

	int insert(LoginLog record);

	int insertSelective(LoginLog record);

	List<LoginLog> selectByExample(LoginLogExample example);

	LoginLog selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") LoginLog record,
			@Param("example") LoginLogExample example);

	int updateByExample(@Param("record") LoginLog record, @Param("example") LoginLogExample example);

	int updateByPrimaryKeySelective(LoginLog record);

	int updateByPrimaryKey(LoginLog record);
}