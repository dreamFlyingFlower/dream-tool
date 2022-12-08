package com.wy.user.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.core.base.AbstractCrl;
import com.wy.user.entity.Dict;
import com.wy.user.entity.dto.DictDTO;
import com.wy.user.query.DictQuery;

import io.swagger.annotations.Api;

/**
 * 字典表API
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "字典表API")
@RestController
@RequestMapping("dict")
public class DictCrl extends AbstractCrl<Dict, DictDTO, DictQuery> {

}