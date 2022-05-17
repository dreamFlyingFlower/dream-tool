package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.Salary;
import com.wy.model.SalaryExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 工资表
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface SalaryMapper extends BaseMapper<Salary, Long> {

	long countByExample(SalaryExample example);

	int deleteByExample(SalaryExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Salary record);

	int insertSelective(Salary record);

	List<Salary> selectByExample(SalaryExample example);

	Salary selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Salary record,
			@Param("example") SalaryExample example);

	int updateByExample(@Param("record") Salary record, @Param("example") SalaryExample example);

	int updateByPrimaryKeySelective(Salary record);

	int updateByPrimaryKey(Salary record);
}