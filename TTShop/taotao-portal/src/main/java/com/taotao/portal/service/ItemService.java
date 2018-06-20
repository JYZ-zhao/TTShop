package com.taotao.portal.service;

import com.taotao.portal.pojo.ItemInfo;

public interface ItemService {

	// 商品基本信息
	ItemInfo geTbItemById(Long itemId);

	// 商品描述
	String getItemDescById(Long itemId);

	// 商品参数
	String getItemParamById(Long itemId);
}
