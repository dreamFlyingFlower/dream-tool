package com.wy.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.Notice;

import io.swagger.annotations.Api;

/**
 * 公告表API
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "公告表API")
@RestController
@RequestMapping("notice")
public class NoticeCrl extends AbstractCrl<Notice, Long> {

}