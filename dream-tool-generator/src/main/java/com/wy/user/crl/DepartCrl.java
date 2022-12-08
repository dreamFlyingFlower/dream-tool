package com.wy.user.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.core.base.AbstractCrl;
import com.wy.user.entity.Depart;
import com.wy.user.entity.dto.DepartDTO;
import com.wy.user.query.DepartQuery;

import io.swagger.annotations.Api;

/**
 * 部门表API
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "部门表API")
@RestController
@RequestMapping("depart")
public class DepartCrl extends AbstractCrl<Depart, DepartDTO, DepartQuery> {

}