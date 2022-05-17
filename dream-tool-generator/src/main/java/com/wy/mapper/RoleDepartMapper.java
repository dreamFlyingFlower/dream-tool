package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.RoleDepart;
import com.wy.model.RoleDepartExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色部门关联表
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface RoleDepartMapper extends BaseMapper<RoleDepart, Long> {

	long countByExample(RoleDepartExample example);

	int deleteByExample(RoleDepartExample example);

	int deleteByPrimaryKey(Long id);

	int insert(RoleDepart record);

	int insertSelective(RoleDepart record);

	List<RoleDepart> selectByExample(RoleDepartExample example);

	RoleDepart selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") RoleDepart record,
			@Param("example") RoleDepartExample example);

	int updateByExample(@Param("record") RoleDepart record, @Param("example") RoleDepartExample example);

	int updateByPrimaryKeySelective(RoleDepart record);

	int updateByPrimaryKey(RoleDepart record);
}