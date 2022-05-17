package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaVoteTitleUser;
import com.wy.model.AoaVoteTitleUserExample;
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
public interface AoaVoteTitleUserMapper extends BaseMapper<AoaVoteTitleUser, Long> {

	long countByExample(AoaVoteTitleUserExample example);

	int deleteByExample(AoaVoteTitleUserExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaVoteTitleUser record);

	int insertSelective(AoaVoteTitleUser record);

	List<AoaVoteTitleUser> selectByExample(AoaVoteTitleUserExample example);

	AoaVoteTitleUser selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaVoteTitleUser record,
			@Param("example") AoaVoteTitleUserExample example);

	int updateByExample(@Param("record") AoaVoteTitleUser record, @Param("example") AoaVoteTitleUserExample example);

	int updateByPrimaryKeySelective(AoaVoteTitleUser record);

	int updateByPrimaryKey(AoaVoteTitleUser record);
}