package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.sql.dialect.oracle.ast.clause.ModelClause.ReturnRowsClause;
import com.taotao.pojo.EUITreeMsg;
import com.taotao.pojo.TaotaoResult;
import com.taotao.service.ContentCategoryService;

@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService categoryService;
	/**
	 * 后台管理系统显示内容的Tree的方法
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EUITreeMsg> getTreeList(@RequestParam(value="id",defaultValue="0") long parentId){
		List<EUITreeMsg> list = categoryService.getTreeList(parentId);
		return list;
	}
	/**
	 * 新增一个树节点
	 * @param parentId
	 * @param name
	 * @return
	 */
	@RequestMapping("/content/category/create")
	@ResponseBody
	public TaotaoResult saveContentCategory(long parentId,String name){
		TaotaoResult result = categoryService.saveContentCategory(parentId, name);
		return result;
	}
	/**
	 * 删除一个树节点。
	 * @param id
	 * @return
	 */
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public TaotaoResult deleteContentCategory(long id){
		System.out.println("================="+id);
		TaotaoResult result = categoryService.deleteContentCategory(id);
		return result;
	}
	/**
	 * 修改也就是更新一个树节点
	 */
	@RequestMapping("/content/category/update")
	@ResponseBody
	public TaotaoResult updateContentCategory(long id,String name){
		TaotaoResult result = categoryService.updateContentCategory(id, name);
		return result;
	}
}
