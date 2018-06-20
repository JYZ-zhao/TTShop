package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.PageBean;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.pojo.TbItemParamExample.Criteria;
import com.taotao.service.ItemParamService;

@Service
public class ItemParamServiceImpl implements ItemParamService {
	
	@Autowired
	private TbItemParamMapper tbItemParamMapper;
	/**
	 * 展示商品的参数列表
	 */
	@Override
	public PageBean showItemParam(Integer page, Integer rows) {
		//查询所有商品参数
		TbItemParamExample example = new TbItemParamExample();
		//进行分页
		PageHelper.startPage(page, rows);
		
		List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
		
		PageBean pb = new PageBean();
		pb.setRows(list);
		PageInfo<TbItemParam> pi = new PageInfo<TbItemParam>(list);
		long total = pi.getTotal();
		pb.setTotal(total);
		return pb;
	}
	/**
	 * 商品参数类目的展示
	 */
	@Override
	public TaotaoResult getItemParamCid(long cid) {
		// TODO Auto-generated method stub
		TbItemParamExample example = new TbItemParamExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemCatIdEqualTo(cid);
		List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
		if(list != null && list.size() > 0){
			return TaotaoResult.ok(list.get(0));
		}
		return TaotaoResult.ok();
	}
	/**
	 * 商品规格参数保存
	 */
	@Override
	public TaotaoResult saveItemParam(long cid,String paramData) {
		TbItemParam tbItemParam = new TbItemParam();
		tbItemParam.setCreated(new Date());
		tbItemParam.setUpdated(new Date());
		tbItemParam.setItemCatId(cid);
		tbItemParam.setParamData(paramData);
		tbItemParamMapper.insert(tbItemParam);
		return TaotaoResult.ok();
	}
	/**
	 * 删除商品规格参数模板
	 */
	@Override
	public TaotaoResult deleteItemParam(long[] itemsId) {
		for (int i = 0; i < itemsId.length; i++) {
			TbItemParamExample example = new TbItemParamExample();
			Criteria criteria = example.createCriteria();
			criteria.andIdEqualTo(itemsId[i]);
			tbItemParamMapper.deleteByExample(example);
		}
		return TaotaoResult.ok();
	}
}
















