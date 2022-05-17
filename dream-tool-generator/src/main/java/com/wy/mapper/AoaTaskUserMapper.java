package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaTaskUser;
import com.wy.model.AoaTaskUserExample;
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
public interface AoaTaskUserMapper extends BaseMapper<AoaTaskUser, Long> {

	long countByExample(AoaTaskUserExample example);

	int deleteByExample(AoaTaskUserExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaTaskUser record);

	int insertSelective(AoaTaskUser record);

	List<AoaTaskUser> selectByExample(AoaTaskUserExample example);

	AoaTaskUser selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaTaskUser record,
			@Param("example") AoaTaskUserExample example);

	int updateByExample(@Param("record") AoaTaskUser record, @Param("example") AoaTaskUserExample example);

	int updateByPrimaryKeySelective(AoaTaskUser record);

	int updateByPrimaryKey(AoaTaskUser record);
}