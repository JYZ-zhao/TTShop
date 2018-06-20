package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.portal.service.ContentService;
import com.taotao.utils.HttpClientUtil;
import com.taotao.utils.JsonUtils;

/**
 * 首页显示，大广告的展示service
 * 
 * @author lenovo
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${REST_INDEX_DGG_URL}")
	private String REST_INDEX_DGG_URL;

	/**
	 * 展示首页大广告
	 */
	@Override
	public String showContent() {
		// 使用httpclient来调用rest服务返回的是一个json格式的字符串
		String result = HttpClientUtil.doGet(REST_BASE_URL + REST_INDEX_DGG_URL);
		// 把json格式的字符串转化成TaoTaoResult
		TaotaoResult taotaoResult = TaotaoResult.formatToList(result, TbContent.class);

		@SuppressWarnings("unchecked")
		List<TbContent> list = (List<TbContent>) taotaoResult.getData();

		@SuppressWarnings("rawtypes")
		List<Map> resultList = new ArrayList<>();
		for (TbContent tbContent : list) {
			Map<Object, Object> map = new HashMap<>();
			map.put("srcB", tbContent.getPic2());
			map.put("height", 240);
			map.put("alt", tbContent.getSubTitle());
			map.put("width", 670);
			map.put("src", tbContent.getPic());
			map.put("widthB", 550);
			map.put("href", tbContent.getUrl());
			map.put("heightB", 240);
			resultList.add(map);
		}

		String contentJson = JsonUtils.objectToJson(resultList);
		return contentJson;
	}
}
