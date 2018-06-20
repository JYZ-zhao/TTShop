package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.EUITreeMsg;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	//注入Mapper
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Override
	public List<EUITreeMsg> getItemCatList(long parentId) {
		//调用dao的方法进行查询
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		List<EUITreeMsg> resultList = new ArrayList<EUITreeMsg>();
		for(TbItemCat tbItemCat:list){
			EUITreeMsg node = new EUITreeMsg();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			resultList.add(node);
//			System.out.println(parentId);
//			System.out.println(tbItemCat.getName()+"-----------"+(tbItemCat.getIsParent()?"1":"0"));
		}
		return resultList;
	}
}
 