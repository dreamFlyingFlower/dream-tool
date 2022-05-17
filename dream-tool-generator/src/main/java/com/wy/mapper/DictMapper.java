package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.Dict;
import com.wy.model.DictExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统字典类
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface DictMapper extends BaseMapper<Dict, Long> {

	long countByExample(DictExample example);

	int deleteByExample(DictExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Dict record);

	int insertSelective(Dict record);

	List<Dict> selectByExample(DictExample example);

	Dict selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Dict record,
			@Param("example") DictExample example);

	int updateByExample(@Param("record") Dict record, @Param("example") DictExample example);

	int updateByPrimaryKeySelective(Dict record);

	int updateByPrimaryKey(Dict record);
}