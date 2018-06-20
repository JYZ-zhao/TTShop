package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.ast.expr.SQLCaseExpr.Item;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.IDUtils;
import com.taotao.pojo.PageBean;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemDescExample;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.service.ItemService;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private TbItemParamItemMapper tbItemParamItemMapper;
	@Override 
	public TbItem getItemById(Long itemId){
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = tbItemMapper.selectByExample(example);
		if(list.size()>0 && list != null){
			TbItem item = list.get(0);
			return item;
		}
		return null;
	}
	
	@Override
	public PageBean getItemList(int page, int rows) {
		TbItemExample example = new TbItemExample();
		//使用分页插件完成分页操作
		PageHelper.startPage(page, rows);
		//查询所有商品
		List<TbItem> list = tbItemMapper.selectByExample(example);
		
		PageBean pageBean = new PageBean();
		
		pageBean.setRows(list);
		
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		
		pageBean.setTotal(pageInfo.getTotal());
		
		return pageBean;
	}
	/**
	 * 完成添加商品的功能
	 */
	@Override
	public TaotaoResult insertItem(TbItem item,String desc,String itemParam) {
		//先补全item中的属性。使用IDUtils完成随机主键的生成策略。
		long itemId = IDUtils.genItemId();
		item.setId(itemId);
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//调用dao中的添加方法完成商品的上传。
		tbItemMapper.insert(item);
		//保存商品的描述到数据库中
		insertItemDesc(itemId, desc);
		//保存商品的规格参数到数据库中
		saveItemParams(item,itemParam);
		return TaotaoResult.ok();
	}
	/**
	 * 保存商品的规格参数到数据库中。
	 * @param id
	 * @param itemParam
	 */
	private TaotaoResult saveItemParams(TbItem item, String itemParam) {
		TbItemParamItem tbItemParamItem = new TbItemParamItem();
		tbItemParamItem.setCreated(new Date());
		tbItemParamItem.setItemId(item.getId());
		tbItemParamItem.setParamData(itemParam);
		tbItemParamItem.setUpdated(new Date());
		tbItemParamItemMapper.insert(tbItemParamItem);
		return TaotaoResult.ok();
	}

	/**
	 * 将商品的描述保存到数据库中
	 */
	public TaotaoResult insertItemDesc(long itemId,String desc){
		TbItemDesc tbItemDesc= new TbItemDesc();
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setUpdated(new Date());
		tbItemDescMapper.insert(tbItemDesc);
		return TaotaoResult.ok();
	}
	/**
	 * 修改时更新数据库中商品的信息
	 */
	
	@Override
	public TaotaoResult updateItem(TbItem item,String desc,String itemParams) {
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		TbItemExample example = new TbItemExample();
		Long id  = item.getId();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		tbItemMapper.updateByExample(item, example);
		//更新数据库中商品信息
		upDateItemDesc(item,desc);
		//同时更新商品的规格参数到数据库中
		updateItemParams(item,itemParams);
		return TaotaoResult.ok();
	}
	//更新商品的规格参数信息
	private TaotaoResult updateItemParams(TbItem item, String itemParams) {
		TbItemParamItem tbItemParamItem = new TbItemParamItem();
		tbItemParamItem.setId(IDUtils.genItemId());
		tbItemParamItem.setItemId(item.getId());
		tbItemParamItem.setCreated(new Date());
		tbItemParamItem.setUpdated(new Date());
		tbItemParamItem.setParamData(itemParams);
		TbItemParamItemExample example = new TbItemParamItemExample();
		com.taotao.pojo.TbItemParamItemExample.Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(item.getId());
		tbItemParamItemMapper.updateByExampleWithBLOBs(tbItemParamItem, example);
		return TaotaoResult.ok();
	}

	//更新数据库中商品信息
	public TaotaoResult upDateItemDesc(TbItem item,String desc){
		TbItemDesc tbItemDesc= new TbItemDesc();
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		tbItemDesc.setItemId(item.getId());
		TbItemDescExample descExample = new TbItemDescExample();
		//同时更新商品描述。
		com.taotao.pojo.TbItemDescExample.Criteria criteria = descExample.createCriteria();
		criteria.andItemIdEqualTo(item.getId());
		tbItemDescMapper.updateByExampleSelective(tbItemDesc, descExample);
	
		
		return TaotaoResult.ok();
	}
	/**
	 * 加载商品描述
	 */
	@Override
	public TaotaoResult loadDesc(long itemId) {
		TbItemDescExample example = new TbItemDescExample();
		com.taotao.pojo.TbItemDescExample.Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemDesc> list = tbItemDescMapper.selectByExampleWithBLOBs(example);
		return TaotaoResult.ok(list.get(0));
	}
	/**
	 * 删除商品 
	 */
	@Override
	public TaotaoResult deleteItem(long[] items) {
		for (int i = 0; i < items.length; i++) {
			TbItemExample example = new TbItemExample();
			Criteria criteria = example.createCriteria();
			criteria.andIdEqualTo(items[i]);
			tbItemMapper.deleteByExample(example);
		}
		return TaotaoResult.ok();
	}
	/**
	 * 下架商品，将商品的status修改成2
	 */
	@Override
	public TaotaoResult downItem(long[] items) {
		for (int i = 0; i < items.length; i++) {
			TbItemExample example = new TbItemExample();
			Criteria criteria = example.createCriteria();
			criteria.andIdEqualTo(items[i]);
			List<TbItem> itemList = tbItemMapper.selectByExample(example);
			TbItem item = itemList.get(0);
			item.setUpdated(new Date());
			item.setStatus((byte)2);
			tbItemMapper.updateByExample(item, example);
		}
		return TaotaoResult.ok();
	}
	/**
	 * 上架商品，将商品的status修改成2
	 */
	@Override
	public TaotaoResult upItem(long[] items) {
		for (int i = 0; i < items.length; i++) {
			TbItemExample example = new TbItemExample();
			Criteria criteria = example.createCriteria();
			criteria.andIdEqualTo(items[i]);
			List<TbItem> itemList = tbItemMapper.selectByExample(example);
			TbItem item = itemList.get(0);
			item.setUpdated(new Date());
			item.setStatus((byte)1);
			tbItemMapper.updateByExample(item, example);
		}
		return TaotaoResult.ok();
	}
	//加载商品的参数到编辑页面上去
	@Override
	public TaotaoResult loadItemParams(long itemId) {
		TbItemParamItemExample example = new TbItemParamItemExample();
		com.taotao.pojo.TbItemParamItemExample.Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
		if(list !=null && list.size() > 0){
			return TaotaoResult.ok(list.get(0));
		}
		return TaotaoResult.ok();
	}
}






















