package com.wy.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.AoaNotepaper;

import io.swagger.annotations.Api;

/**
 * API
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "API")
@RestController
@RequestMapping("aoaNotepaper")
public class AoaNotepaperCrl extends AbstractCrl<AoaNotepaper, Long> {

}