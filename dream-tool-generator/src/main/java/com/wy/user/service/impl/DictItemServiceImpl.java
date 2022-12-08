package com.wy.user.service.impl;

import org.springframework.stereotype.Service;

import com.wy.core.base.AbstractServiceImpl;
import com.wy.user.convert.DictItemConvert;
import com.wy.user.entity.DictItem;
import com.wy.user.entity.dto.DictItemDTO;
import com.wy.user.mapper.DictItemMapper;
import com.wy.user.query.DictItemQuery;
import com.wy.user.service.DictItemService;

/**
 * 字典项业务层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service("dictItemService")
public class DictItemServiceImpl extends AbstractServiceImpl<DictItem, DictItemDTO, DictItemQuery, DictItemConvert, DictItemMapper> implements DictItemService {

}