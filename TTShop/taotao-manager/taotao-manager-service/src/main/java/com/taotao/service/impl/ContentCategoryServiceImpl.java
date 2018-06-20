package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.EUITreeMsg;
import com.taotao.pojo.PageBean;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.service.ContentCategoryService;
import com.taotao.pojo.TbContentExample;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	@Autowired
	private TbContentMapper contentMapper;
	/**
	 * 后台管理页面显示内容的那个Tree的方法
	 */
	@Override
	public List<EUITreeMsg> getTreeList(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<EUITreeMsg> resultList = new ArrayList<EUITreeMsg>();
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		for (TbContentCategory tbContentCategory : list) {
			EUITreeMsg euiTreeMsg = new EUITreeMsg();
			euiTreeMsg.setId(tbContentCategory.getId());
			euiTreeMsg.setText(tbContentCategory.getName());
			euiTreeMsg.setState(tbContentCategory.getIsParent()?"closed":"open");
			resultList.add(euiTreeMsg);
		}
		return resultList;
	}
	/**
	 * 新增加一个树节点
	 */
	@Override
	public TaotaoResult saveContentCategory(long parentId, String name) {
		TbContentCategory category = new TbContentCategory();
		category.setCreated(new Date());
		category.setIsParent(false);
		category.setName(name);
		category.setSortOrder(1);
		category.setStatus(1);
		category.setUpdated(new Date());
		category.setParentId(parentId);
		contentCategoryMapper.insert(category);
		//查看父节点的isparent是否是true如果不是true改成true
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!contentCategory.getIsParent()){
			//更新父节点
			contentCategory.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(contentCategory);
		}
		return TaotaoResult.ok(category);
	}
	/**
	 * 删除对应的节点。
	 */
	@Override
	public TaotaoResult deleteContentCategory(long id) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		long parentId_flag = contentCategory.getParentId(); 
		if(!contentCategory.getIsParent()){
			contentCategoryMapper.deleteByPrimaryKey(id);
			TbContentCategoryExample example3 = new TbContentCategoryExample();
			Criteria criteria3 = example3.createCriteria();
			criteria3.andParentIdEqualTo(parentId_flag);
			List<TbContentCategory> list = contentCategoryMapper.selectByExample(example3);
			if(list.size() == 0 || list == null){
				long id_flag = parentId_flag;
				TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(id_flag);
				category.setIsParent(false);
				contentCategoryMapper.updateByPrimaryKey(category);
			}
			return TaotaoResult.ok();
		}else{
			long parentId = contentCategory.getId();
			TbContentCategoryExample example2 = new TbContentCategoryExample();
			Criteria criteria2 = example2.createCriteria();
			criteria2.andParentIdEqualTo(parentId);
			contentCategoryMapper.deleteByExample(example2);
			contentCategoryMapper.deleteByPrimaryKey(id);
			return TaotaoResult.ok();
		}
	}
	/**
	 * 对一个节点进行重命名
	 */
	@Override
	public TaotaoResult updateContentCategory(long id, String name) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		contentCategory.setName(name);
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
		return TaotaoResult.ok();
	}
}










