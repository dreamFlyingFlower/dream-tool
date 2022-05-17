package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.Sign;
import com.wy.model.SignExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 签到表
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface SignMapper extends BaseMapper<Sign, Long> {

	long countByExample(SignExample example);

	int deleteByExample(SignExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Sign record);

	int insertSelective(Sign record);

	List<Sign> selectByExample(SignExample example);

	Sign selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Sign record,
			@Param("example") SignExample example);

	int updateByExample(@Param("record") Sign record, @Param("example") SignExample example);

	int updateByPrimaryKeySelective(Sign record);

	int updateByPrimaryKey(Sign record);
}