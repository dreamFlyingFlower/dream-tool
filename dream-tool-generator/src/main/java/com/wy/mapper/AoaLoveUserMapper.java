package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaLoveUser;
import com.wy.model.AoaLoveUserExample;
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
public interface AoaLoveUserMapper extends BaseMapper<AoaLoveUser, Long> {

	long countByExample(AoaLoveUserExample example);

	int deleteByExample(AoaLoveUserExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaLoveUser record);

	int insertSelective(AoaLoveUser record);

	List<AoaLoveUser> selectByExample(AoaLoveUserExample example);

	AoaLoveUser selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaLoveUser record,
			@Param("example") AoaLoveUserExample example);

	int updateByExample(@Param("record") AoaLoveUser record, @Param("example") AoaLoveUserExample example);

	int updateByPrimaryKeySelective(AoaLoveUser record);

	int updateByPrimaryKey(AoaLoveUser record);
}