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
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping("/item/{itemId}")
	/**
	 * 返回对象，并且把对象转换成json数据。
	 * 
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	public TbItem getItemById(@PathVariable long itemId) throws Exception {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}

	@RequestMapping("/item/list")
	@ResponseBody
	/**
	 * 展示商品的首页。
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	public PageBean getItemList(Integer page, Integer rows) {
		PageBean pageBean = itemService.getItemList(page, rows);
		return pageBean;
	}

	/**
	 * 添加商品
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/item/save")
	@ResponseBody
	public TaotaoResult insertItem(TbItem tbItem, String desc, String itemParams) throws Exception {
		TaotaoResult result = itemService.insertItem(tbItem, desc, itemParams);
		return result;
	}

	/**
	 * 更新商品的信息
	 */
	@RequestMapping(value = "/rest/item/update", method = RequestMethod.POST)
	@ResponseBody()
	public TaotaoResult updateItem(TbItem item, String desc, String itemParams) {
		TaotaoResult result = itemService.updateItem(item, desc, itemParams);
		return result;
	}

	/**
	 * 加载商品的描述
	 */
	@RequestMapping(value = "/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public TaotaoResult loadDesc(@PathVariable Long itemId) {
		TaotaoResult result = itemService.loadDesc(itemId);
		return result;
	}

	/**
	 * 加载商品的规格参数显示到编辑页面上
	 */
	@RequestMapping("/rest/item/param/item/query/{itemId}")
	@ResponseBody
	public TaotaoResult loadItemParams(@PathVariable Long itemId) {
		TaotaoResult result = itemService.loadItemParams(itemId);
		return result;
	}

	/**
	 * 删除商品
	 */
	@RequestMapping(value = "/rest/item/delete")
	@ResponseBody
	public TaotaoResult deleteItem(@RequestParam("ids") long[] items) {
		TaotaoResult result = itemService.deleteItem(items);
		return result;
	}

	/**
	 * 商品下架
	 */
	@RequestMapping(value = "/rest/item/instock")
	@ResponseBody
	public TaotaoResult instockItems(@RequestParam("ids") long[] items) {
		TaotaoResult result = itemService.downItem(items);
		return result;
	}

	/**
	 * 商品上架
	 */
	@RequestMapping(value = "/rest/item/reshelf")
	@ResponseBody
	public TaotaoResult ipItems(@RequestParam("ids") long[] items) {
		TaotaoResult result = itemService.upItem(items);
		return result;
	}
}
