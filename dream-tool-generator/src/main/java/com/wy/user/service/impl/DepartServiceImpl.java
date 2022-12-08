package com.wy.user.service.impl;

import org.springframework.stereotype.Service;

import com.wy.core.base.AbstractServiceImpl;
import com.wy.user.convert.DepartConvert;
import com.wy.user.entity.Depart;
import com.wy.user.entity.dto.DepartDTO;
import com.wy.user.mapper.DepartMapper;
import com.wy.user.query.DepartQuery;
import com.wy.user.service.DepartService;

/**
 * 部门表业务层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service("departService")
public class DepartServiceImpl extends AbstractServiceImpl<Depart, DepartDTO, DepartQuery, DepartConvert, DepartMapper> implements DepartService {

}