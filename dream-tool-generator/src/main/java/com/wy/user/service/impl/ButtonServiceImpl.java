package com.wy.user.service.impl;

import org.springframework.stereotype.Service;

import com.wy.core.base.AbstractServiceImpl;
import com.wy.user.convert.ButtonConvert;
import com.wy.user.entity.Button;
import com.wy.user.entity.dto.ButtonDTO;
import com.wy.user.mapper.ButtonMapper;
import com.wy.user.query.ButtonQuery;
import com.wy.user.service.ButtonService;

/**
 * 按钮表业务层
 * 
 * @author 飞花梦影
 * @date 2022-12-08 16:53:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service("buttonService")
public class ButtonServiceImpl extends AbstractServiceImpl<Button, ButtonDTO, ButtonQuery, ButtonConvert, ButtonMapper> implements ButtonService {

}