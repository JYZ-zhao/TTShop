package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 跳转到后台管理的首页
 * @author Administrator
 *
 */
@Controller
public class PageController {
	
	@RequestMapping("/")
	public String showIndex(){
		return "index";
	}
	
	/**
	 * 实现页面跳转
	 * @param page
	 * @return
	 */
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		return page;
	}
}
