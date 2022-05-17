package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.UserRole;
import com.wy.model.UserRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户角色中间表
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole, Long> {

	long countByExample(UserRoleExample example);

	int deleteByExample(UserRoleExample example);

	int deleteByPrimaryKey(Long id);

	int insert(UserRole record);

	int insertSelective(UserRole record);

	List<UserRole> selectByExample(UserRoleExample example);

	UserRole selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") UserRole record,
			@Param("example") UserRoleExample example);

	int updateByExample(@Param("record") UserRole record, @Param("example") UserRoleExample example);

	int updateByPrimaryKeySelective(UserRole record);

	int updateByPrimaryKey(UserRole record);
}