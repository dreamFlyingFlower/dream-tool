package com.wy.mapper;

import com.wy.base.BaseMapper;
import com.wy.model.Fileinfo;
import com.wy.model.FileinfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文件表
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface FileinfoMapper extends BaseMapper<Fileinfo, Long> {

	long countByExample(FileinfoExample example);

	int deleteByExample(FileinfoExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Fileinfo record);

	int insertSelective(Fileinfo record);

	List<Fileinfo> selectByExample(FileinfoExample example);

	Fileinfo selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Fileinfo record,
			@Param("example") FileinfoExample example);

	int updateByExample(@Param("record") Fileinfo record, @Param("example") FileinfoExample example);

	int updateByPrimaryKeySelective(Fileinfo record);

	int updateByPrimaryKey(Fileinfo record);
}