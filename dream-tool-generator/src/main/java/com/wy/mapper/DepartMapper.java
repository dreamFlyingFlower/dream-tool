package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.Depart;
import com.wy.model.DepartExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 部门表
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface DepartMapper extends BaseMapper<Depart, Long> {

	long countByExample(DepartExample example);

	int deleteByExample(DepartExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Depart record);

	int insertSelective(Depart record);

	List<Depart> selectByExample(DepartExample example);

	Depart selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Depart record,
			@Param("example") DepartExample example);

	int updateByExample(@Param("record") Depart record, @Param("example") DepartExample example);

	int updateByPrimaryKeySelective(Depart record);

	int updateByPrimaryKey(Depart record);
}