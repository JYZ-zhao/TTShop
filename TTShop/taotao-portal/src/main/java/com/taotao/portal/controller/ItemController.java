package com.taotao.portal.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.TbItem;
import com.taotao.portal.service.ItemService;

/**
 * 商品信息展示Controller
 * 
 * @author lenovo
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;

	/**
	 * 展示基本信息
	 * 
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping("/item/{itemId}")
	public String showItem(@PathVariable Long itemId, Model model) {
		TbItem item = itemService.geTbItemById(itemId);
		// 向模型中传递对象
		model.addAttribute("item", item);

		// 页面跳转
		return "item";
	}

	/**
	 * 取商品描述
	 * 
	 * @param itemId
	 * @return
	 *      
	 */
	@RequestMapping(value="/item/desc/{itemId}",produces=MediaType.TEXT_HTML_VALUE+";charset=utf-8")
	@ResponseBody
	public String getItemDesc(@PathVariable Long itemId) {
		
		//描述乱码问题
		String string = itemService.getItemDescById(itemId);
		return string;

	}
	
	/**
	 * 取商品参数
	 * 
	 * @param itemId
	 * @return
	 *      
	 */
	@RequestMapping(value="/item/param/{itemId}",produces=MediaType.TEXT_HTML_VALUE+";charset=utf-8")
	@ResponseBody
	public String getItemParam(@PathVariable Long itemId) {
		String string = itemService.getItemParamById(itemId);
		return string;
		
	}
	
}
