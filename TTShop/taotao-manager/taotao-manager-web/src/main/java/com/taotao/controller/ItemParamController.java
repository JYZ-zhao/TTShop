package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.PageBean;
import com.taotao.pojo.TaotaoResult;
import com.taotao.service.ItemParamService;

@Controller
public class ItemParamController {
	
	@Autowired
	private ItemParamService itemParamService;
	
	@RequestMapping("/item/param/list")
	@ResponseBody
	public PageBean showItemParam(Integer page,Integer rows){
		PageBean pageBean = itemParamService.showItemParam(page, rows);
//		System.out.println(pageBean.getTotal());
//		System.out.println(pageBean.getRows().get(0));
		return pageBean;
	}
	@RequestMapping("/item/param/query/itemcatid/{itemCatId}")
	@ResponseBody
	public TaotaoResult getItemParamId(@PathVariable long itemCatId){
		TaotaoResult result = itemParamService.getItemParamCid(itemCatId);
		return result;
	}
	/**
	 *商品规格阐述的保存
	 */
	@RequestMapping("/item/param/save/{cid}")
	@ResponseBody
	public TaotaoResult saveItemParam(@PathVariable long cid ,String paramData){
		TaotaoResult result = itemParamService.saveItemParam(cid,paramData);
		return result;
	}
	/**
	 * 删除模板
	 */
	@RequestMapping(value="/item/param/delete",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult deleteItemParam(@RequestParam("ids") long[] itemsId){
		TaotaoResult result = itemParamService.deleteItemParam(itemsId);
		return result;
	}
}
