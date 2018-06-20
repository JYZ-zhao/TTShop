package com.taotao.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.portal.service.ContentService;

/**
 * 展示首页Controller
 * 
 * @author lenovo
 *
 */
@Controller
public class PageIndexController {

	@Autowired
	private ContentService contentService;

	/*
	 * @RequestMapping("/index")
	 * 
	 * public String showIndexPage(){
	 * 
	 * return "index";
	 * 
	 * }
	 */

	/**
	 * 展示首页，调用 ContentService将查询到的广告信息渲染到主页面
	 * 
	 * 展示打广告信息，是直接渲染过去的，不是通过ajax方式获取首页广告信息
	 * 
	 * web.xml中配置好了index的默认打开页面 地址栏直接输入即可，不需要加index
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/index")
	public String showContent(Model model) {
		String contentString = contentService.showContent();
		model.addAttribute("ad1", contentString);
		return "index";
	}
}
