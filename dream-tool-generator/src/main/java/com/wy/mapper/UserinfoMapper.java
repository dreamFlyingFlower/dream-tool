package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.Userinfo;
import com.wy.model.UserinfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户信息扩展表
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface UserinfoMapper extends BaseMapper<Userinfo, Long> {

	long countByExample(UserinfoExample example);

	int deleteByExample(UserinfoExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Userinfo record);

	int insertSelective(Userinfo record);

	List<Userinfo> selectByExample(UserinfoExample example);

	Userinfo selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Userinfo record,
			@Param("example") UserinfoExample example);

	int updateByExample(@Param("record") Userinfo record, @Param("example") UserinfoExample example);

	int updateByPrimaryKeySelective(Userinfo record);

	int updateByPrimaryKey(Userinfo record);
}