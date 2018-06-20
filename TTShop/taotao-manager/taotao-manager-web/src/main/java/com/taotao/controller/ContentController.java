package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.tools.internal.xjc.generator.bean.ImplStructureStrategy.Result;
import com.taotao.pojo.PageBean;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;

@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	/**
	 * 显示子节点下的内容列表并且进行分页。
	 * @param page
	 * @param rows
	 * @param categoryId
	 * @return
	 */
	@RequestMapping(value="/content/query/list",method=RequestMethod.GET)
	@ResponseBody
	public PageBean showContentList(Integer page,Integer rows,long categoryId){
		System.out.println(page+"--"+rows+"--"+categoryId);
		PageBean pageBean = contentService.showContentList(page, rows, categoryId);
		return pageBean;
	}
	/**
	 * 新增内容
	 * @param tbContent
	 * @return
	 */
	@RequestMapping("/content/save")
	@ResponseBody
	public TaotaoResult saveContent(TbContent tbContent){
		TaotaoResult result = contentService.saveContent(tbContent);
		return result;
	}
	/**
	 * 删除内容
	 * 
	 */
	@RequestMapping("/content/delete")
	@ResponseBody
	public TaotaoResult deleteContent(@RequestParam("ids") long[] contentIds){
		TaotaoResult result = contentService.deleteContent(contentIds);
		return result;
	}
	/**
	 * 更新内容
	 */
	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public TaotaoResult updateContent(@RequestParam("id") long contentId,TbContent tbContent){
		TaotaoResult result = contentService.updateContent(contentId, tbContent);
		return result;
	}
}













