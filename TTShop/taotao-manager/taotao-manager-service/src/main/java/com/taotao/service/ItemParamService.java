package com.taotao.service;

import com.taotao.pojo.PageBean;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;

public interface ItemParamService {
	/**
	 * 商品规格参数列表的展示。
	 * @param page
	 * @param rows
	 * @return
	 */
	PageBean showItemParam(Integer page,Integer rows);
	/**
	 * 商品参数的添加
	 */
	TaotaoResult getItemParamCid(long cid);
	/**
	 * 商品规格参数的保存
	 */
	TaotaoResult saveItemParam(long cid,String paramData);
	/**
	 * 删除商品规格参数的模板
	 */
	TaotaoResult deleteItemParam(long[] itemsId);
}
