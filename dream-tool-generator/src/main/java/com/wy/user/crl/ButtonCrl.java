package com.wy.user.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.core.base.AbstractCrl;
import com.wy.user.entity.Button;
import com.wy.user.entity.dto.ButtonDTO;
import com.wy.user.query.ButtonQuery;

import io.swagger.annotations.Api;

/**
 * 按钮表API
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "按钮表API")
@RestController
@RequestMapping("button")
public class ButtonCrl extends AbstractCrl<Button, ButtonDTO, ButtonQuery> {

}