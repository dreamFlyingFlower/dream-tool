package com.wy.user.service.impl;

import org.springframework.stereotype.Service;

import com.wy.core.base.AbstractServiceImpl;
import com.wy.user.convert.OrgConvert;
import com.wy.user.entity.Org;
import com.wy.user.entity.dto.OrgDTO;
import com.wy.user.mapper.OrgMapper;
import com.wy.user.query.OrgQuery;
import com.wy.user.service.OrgService;

/**
 * 组织机构表业务层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service("orgService")
public class OrgServiceImpl extends AbstractServiceImpl<Org, OrgDTO, OrgQuery, OrgConvert, OrgMapper> implements OrgService {

}