package com.wy.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.UserNotice;

import io.swagger.annotations.Api;

/**
 * 用户和公告关联表API
 * 
 * @author 飞花梦影
 * @date 2022-05-17 10:44:40
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "用户和公告关联表API")
@RestController
@RequestMapping("userNotice")
public class UserNoticeCrl extends AbstractCrl<UserNotice, Long> {

}