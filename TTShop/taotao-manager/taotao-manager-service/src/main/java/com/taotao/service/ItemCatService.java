package com.taotao.service;

import java.util.List;

import com.taotao.pojo.EUITreeMsg;

/**
 * 增加商品的时候显示Tree结构。
 * @author Administrator
 *
 */
public interface ItemCatService {
	
	List<EUITreeMsg> getItemCatList(long parentId);
}
