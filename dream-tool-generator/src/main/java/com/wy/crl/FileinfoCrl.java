package com.wy.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.Fileinfo;

import io.swagger.annotations.Api;

/**
 * 文件表API
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "文件表API")
@RestController
@RequestMapping("fileinfo")
public class FileinfoCrl extends AbstractCrl<Fileinfo, Long> {

}