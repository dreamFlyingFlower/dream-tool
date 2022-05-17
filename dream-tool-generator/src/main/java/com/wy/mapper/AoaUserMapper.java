package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaUser;
import com.wy.model.AoaUserExample;
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
public interface AoaUserMapper extends BaseMapper<AoaUser, Long> {

	long countByExample(AoaUserExample example);

	int deleteByExample(AoaUserExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaUser record);

	int insertSelective(AoaUser record);

	List<AoaUser> selectByExample(AoaUserExample example);

	AoaUser selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaUser record,
			@Param("example") AoaUserExample example);

	int updateByExample(@Param("record") AoaUser record, @Param("example") AoaUserExample example);

	int updateByPrimaryKeySelective(AoaUser record);

	int updateByPrimaryKey(AoaUser record);
}