package com.wy.user.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.core.base.AbstractCrl;
import com.wy.user.entity.Org;
import com.wy.user.entity.dto.OrgDTO;
import com.wy.user.query.OrgQuery;

import io.swagger.annotations.Api;

/**
 * 组织机构表API
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "组织机构表API")
@RestController
@RequestMapping("org")
public class OrgCrl extends AbstractCrl<Org, OrgDTO, OrgQuery> {

}