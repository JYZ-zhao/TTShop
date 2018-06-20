package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.PageBean;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.service.ContentService;
import com.taotao.utils.HttpClientUtil;

@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper;

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;

	@Value("${REST_CONTENT_SYNC_URL}")
	private String REST_CONTENT_SYNC_URL;

	/**
	 * 点击内容管理显示节点对应的内容。
	 */
	@Override
	public PageBean showContentList(int page, int rows, long categoryId) {
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		// 准备分页
		PageHelper.startPage(page, rows);
		List<TbContent> list = contentMapper.selectByExample(example);
		PageBean pageBean = new PageBean();
		pageBean.setRows(list);
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
		long total = pageInfo.getTotal();
		pageBean.setTotal(total);
		return pageBean;
	}

	/**
	 * 新增内容。
	 */
	@Override
	public TaotaoResult saveContent(TbContent tbContent) {
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		contentMapper.insert(tbContent);

		// 同步缓存
		try {
			HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + tbContent.getCategoryId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok();
	}

	/**
	 * 删除子节点的内容！！
	 */
	@Override
	public TaotaoResult deleteContent(long[] contentIds) {
		//取对应分类ID 用于同步缓存
		long id =contentIds[0];
		TbContent content = contentMapper.selectByPrimaryKey(id);
		long cid = content.getCategoryId();
		for (long contentId : contentIds) {
			contentMapper.deleteByPrimaryKey(contentId);
		}
		// 同步缓存
		try {
			HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL +cid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok();
	}

	/**
	 * 修改内容
	 */
	@Override
	public TaotaoResult updateContent(long contentId, TbContent tbContent) {
		tbContent.setUpdated(new Date());
		tbContent.setCreated(new Date());
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(contentId);
		contentMapper.updateByExampleWithBLOBs(tbContent, example);
		// 同步缓存
		try {
			HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + tbContent.getCategoryId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok();
	}

}
