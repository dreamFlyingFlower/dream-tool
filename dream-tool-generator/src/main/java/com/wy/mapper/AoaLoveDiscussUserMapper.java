package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaLoveDiscussUser;
import com.wy.model.AoaLoveDiscussUserExample;
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
public interface AoaLoveDiscussUserMapper extends BaseMapper<AoaLoveDiscussUser, Long> {

	long countByExample(AoaLoveDiscussUserExample example);

	int deleteByExample(AoaLoveDiscussUserExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaLoveDiscussUser record);

	int insertSelective(AoaLoveDiscussUser record);

	List<AoaLoveDiscussUser> selectByExample(AoaLoveDiscussUserExample example);

	AoaLoveDiscussUser selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaLoveDiscussUser record,
			@Param("example") AoaLoveDiscussUserExample example);

	int updateByExample(@Param("record") AoaLoveDiscussUser record, @Param("example") AoaLoveDiscussUserExample example);

	int updateByPrimaryKeySelective(AoaLoveDiscussUser record);

	int updateByPrimaryKey(AoaLoveDiscussUser record);
}