package com.wy.user.service.impl;

import org.springframework.stereotype.Service;

import com.wy.core.base.AbstractServiceImpl;
import com.wy.user.convert.DictConvert;
import com.wy.user.entity.Dict;
import com.wy.user.entity.dto.DictDTO;
import com.wy.user.mapper.DictMapper;
import com.wy.user.query.DictQuery;
import com.wy.user.service.DictService;

/**
 * 字典表业务层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service("dictService")
public class DictServiceImpl extends AbstractServiceImpl<Dict, DictDTO, DictQuery, DictConvert, DictMapper> implements DictService {

}