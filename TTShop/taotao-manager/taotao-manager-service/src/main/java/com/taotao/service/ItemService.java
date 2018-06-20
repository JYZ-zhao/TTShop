package com.taotao.service;



import com.taotao.pojo.PageBean;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface ItemService {
	//根据商品的id俩查找
	 TbItem getItemById(Long itemId);
	//返回分页数据
	 PageBean getItemList(int page,int rows);
	//添加商品
	TaotaoResult insertItem(TbItem item,String desc,String itemParam);
	//修改的时候更新商品的信息
	TaotaoResult updateItem(TbItem item,String desc,String itemParams);
	//加载商品描述
	TaotaoResult loadDesc(long itemId);
	//删除商品
	TaotaoResult deleteItem(long[] items);
	//下架商品
	TaotaoResult downItem(long[] items);
	//上架商品
	TaotaoResult upItem(long[] items);
	//添加商品描述到数据库中
	TaotaoResult insertItemDesc(long itemId,String desc);
	//加载商品的参数到编辑页面上
	TaotaoResult loadItemParams(long itemId);
}
