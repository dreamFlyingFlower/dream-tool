package com.wy.user.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.core.base.AbstractCrl;
import com.wy.user.entity.DictItem;
import com.wy.user.entity.dto.DictItemDTO;
import com.wy.user.query.DictItemQuery;

import io.swagger.annotations.Api;

/**
 * 字典项API
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Api(tags = "字典项API")
@RestController
@RequestMapping("dictItem")
public class DictItemCrl extends AbstractCrl<DictItem, DictItemDTO, DictItemQuery> {

}