package com.simple.crl;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simple.common.BasePager;
import com.simple.service.GeneratorService;
import com.wy.io.IOTool;
import com.wy.result.Result;

/**
 * 在线代码生成器
 * 
 * @author ParadiseWY
 * @date 2020-12-11 15:43:31
 * @git {@link https://github.com/mygodness100}
 */
@Controller
@RequestMapping("generator")
public class GeneratorCrl {

	@Autowired
	private GeneratorService generatorService;

	/**
	 * 表列表
	 */
	@ResponseBody
	@GetMapping("getTableinfos")
	public Result<?> list(BasePager params) {
		return generatorService.queryList(params);
	}

	/**
	 * 生成代码
	 */
	@GetMapping("code")
	public void code(String tables, HttpServletResponse response) throws IOException {
		byte[] data = generatorService.generateCode(false, tables.split(","));
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"codes.zip\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOTool.write(data, response.getOutputStream());
	}

	/**
	 * 生成所有表代码
	 */
	@GetMapping("codeAll")
	public void codeAll(HttpServletResponse response) throws IOException {
		byte[] data = generatorService.generateCode(false);
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"codes.zip\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOTool.write(data, response.getOutputStream());
	}
}