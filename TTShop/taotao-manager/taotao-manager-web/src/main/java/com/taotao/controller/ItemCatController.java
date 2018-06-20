package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.EUITreeMsg;
import com.taotao.service.ItemCatService;

@Controller
public class ItemCatController {
	//注入Service
	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EUITreeMsg> categoryList(@RequestParam(value="id",defaultValue="0") Long parentId){
		List<EUITreeMsg> itemCatList = itemCatService.getItemCatList(parentId);
		return itemCatList;
	}
}
