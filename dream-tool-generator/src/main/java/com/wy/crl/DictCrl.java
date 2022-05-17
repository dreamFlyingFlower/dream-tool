package com.wy.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.Dict;

import io.swagger.annotations.Api;

/**
 * 系统字典类API
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "系统字典类API")
@RestController
@RequestMapping("dict")
public class DictCrl extends AbstractCrl<Dict, Long> {

}