package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.AoaSysMenu;
import com.wy.model.AoaSysMenuExample;
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
public interface AoaSysMenuMapper extends BaseMapper<AoaSysMenu, Long> {

	long countByExample(AoaSysMenuExample example);

	int deleteByExample(AoaSysMenuExample example);

	int deleteByPrimaryKey(Long id);

	int insert(AoaSysMenu record);

	int insertSelective(AoaSysMenu record);

	List<AoaSysMenu> selectByExample(AoaSysMenuExample example);

	AoaSysMenu selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") AoaSysMenu record,
			@Param("example") AoaSysMenuExample example);

	int updateByExample(@Param("record") AoaSysMenu record, @Param("example") AoaSysMenuExample example);

	int updateByPrimaryKeySelective(AoaSysMenu record);

	int updateByPrimaryKey(AoaSysMenu record);
}