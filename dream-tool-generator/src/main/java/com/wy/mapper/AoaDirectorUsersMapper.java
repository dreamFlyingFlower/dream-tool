package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaDirectorUsers;
import com.wy.model.AoaDirectorUsersExample;
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
public interface AoaDirectorUsersMapper extends BaseMapper<AoaDirectorUsers, Long> {

	long countByExample(AoaDirectorUsersExample example);

	int deleteByExample(AoaDirectorUsersExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaDirectorUsers record);

	int insertSelective(AoaDirectorUsers record);

	List<AoaDirectorUsers> selectByExample(AoaDirectorUsersExample example);

	AoaDirectorUsers selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaDirectorUsers record,
			@Param("example") AoaDirectorUsersExample example);

	int updateByExample(@Param("record") AoaDirectorUsers record, @Param("example") AoaDirectorUsersExample example);

	int updateByPrimaryKeySelective(AoaDirectorUsers record);

	int updateByPrimaryKey(AoaDirectorUsers record);
}