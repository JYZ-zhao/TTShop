package com.taotao.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.TaotaoResult;
import com.taotao.rest.service.ContentService;
import com.taotao.utils.ExceptionUtilsMsg;

@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/list/{contentCategoryId}")
	@ResponseBody
	public TaotaoResult getContentList(@PathVariable long contentCategoryId){
		try {
			TaotaoResult result = contentService.getContentList(contentCategoryId);
			return result;
		} catch (Exception e) {
			e.getStackTrace();
			return TaotaoResult.build(500,ExceptionUtilsMsg.getStackTrace(e));
		}
	}
}
