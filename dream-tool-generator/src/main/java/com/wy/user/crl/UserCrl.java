package com.wy.user.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.core.base.AbstractCrl;
import com.wy.user.entity.User;
import com.wy.user.entity.dto.UserDTO;
import com.wy.user.query.UserQuery;

import io.swagger.annotations.Api;

/**
 * 用户表API
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "用户表API")
@RestController
@RequestMapping("user")
public class UserCrl extends AbstractCrl<User, UserDTO, UserQuery> {

}