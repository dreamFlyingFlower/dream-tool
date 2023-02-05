package com.simple.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.alibaba.fastjson.JSON;
import com.wy.enums.TipFormatEnum;
import com.wy.result.Result;
import com.wy.result.ResultException;

import lombok.extern.slf4j.Slf4j;

/**
 * 统一异常处理
 * 
 * @author ParadiseWY
 * @date 2020-12-11 17:18:23
 * @git {@link https://github.com/mygodness100}
 */
@RestControllerAdvice
@Slf4j
public class ExceptionFilter {

	/**
	 * 必传参数为空异常
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public Result<?> missingServletRequestParameterException(MissingServletRequestParameterException e) {
		return Result.error(-500, TipFormatEnum.TIP_PARAM_REQUIRED_IS_NULL.getMsg(e.getParameterName()));
	}

	@ExceptionHandler(value = BindException.class)
	public Result<?> bindException(BindException e) {
		StringBuilder sb = new StringBuilder();
		// 解析原错误信息,封装后返回,此处返回非法的字段名称,原始值,错误信息
		for (FieldError error : e.getFieldErrors()) {
			sb.append(error.getDefaultMessage() + ";");
		}
		return Result.error(-500, sb.toString());
	}

	/**
	 * 参数未通过验证异常
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result<?> methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
		StringBuilder sb = new StringBuilder();
		// 解析原错误信息,封装后返回,此处返回非法的字段名称,原始值,错误信息
		for (FieldError error : e.getBindingResult().getFieldErrors()) {
			sb.append("字段：" + error.getField() + "-" + error.getRejectedValue() + ";");
		}
		return Result.error(-500, sb.toString());
	}

	/**
	 * 无法解析参数异常
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Result<?> httpMessageNotReadableHandler(HttpServletRequest request,
			HttpMessageNotReadableException exception) {
		return Result.error(-500, "参数无法正常解析");
	}

	/**
	 * 数据库字段异常
	 */
	@ExceptionHandler(value = DuplicateKeyException.class)
	public Object DuplicateKeyExceptionHandler(DuplicateKeyException e) throws Exception {
		return Result.error(-500, "数据库异常");
	}

	/**
	 * 参数不合法异常
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public Result<?> illegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
		log.info(request.getRequestURL().toString(), e.getMessage());
		return Result.error(-501, e.getMessage());
	}

	/**
	 * 接口不存在异常
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Result<?> noHandlerFoundException(NoHandlerFoundException e) {
		return Result.error(-404, e.getMessage());
	}

	/**
	 * http请求方式不支持异常
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Result<?> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		return Result.error(-500, e.getMessage());
	}

	/**
	 * 服务器中断异常,当页面请求未返回数据而不停发送请求时,可能出现该异常
	 */
	@ExceptionHandler(ClientAbortException.class)
	public Result<?> clientAbortException(ClientAbortException e) {
		return Result.error(-500, e.getMessage());
	}

	/**
	 * 自定义异常
	 */
	@ExceptionHandler(ResultException.class)
	public ModelAndView resultException(HttpServletRequest request, HttpServletResponse response, ResultException e) {
		// 自定义异常界面
		// ModelAndView modelAndView = new ModelAndView();
		// modelAndView.setViewName("error.html");
		// modelAndView.addObject("msg", e.getMessage());
		// return modelAndView;
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		// 记录异常日志
		log.error(e.getMessage());
		String json = JSON.toJSONString(Result.error(e.getMessage()));
		try {
			response.getWriter().print(json);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		log.info(request.getRequestURL().toString(), e.getMessage());
		return new ModelAndView();
	}

	@ExceptionHandler(value = Exception.class)
	public Result<?> handleException(HttpServletRequest request, Exception e) {
		log.error(request.getRemoteHost(), request.getRequestURL(), e);
		return Result.error(-500, e.getMessage());
	}
}