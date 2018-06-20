package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ContentService;
import com.taotao.utils.JsonUtils;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	// 注入redis
	@Autowired
	private JedisClient jedisClient;

	@Value("${INDEX_CONTENT_REDIS_KEY}")
	private String INDEX_CONTENT_REDIS_KEY;

	/**
	 * 服务层查询广告的内容信息
	 */
	@Override
	public TaotaoResult getContentList(long contentCategoryId) {
		// 从缓存中取内容
		try {
			String result = jedisClient.hget(INDEX_CONTENT_REDIS_KEY, contentCategoryId + "");

			// 如果取出来的不是空
			if (!StringUtils.isBlank(result)) {
				// 把字符串转成list
				System.out.println("从缓存中取数据");
				List<TbContent> list = JsonUtils.jsonToList(result, TbContent.class);

				return TaotaoResult.ok(list);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		// 根据分类ID查询内容列表
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(contentCategoryId);
		List<TbContent> list = contentMapper.selectByExample(example);

		// 向缓存中添加内容
		try {
			// 把list转换成字符串
			String cacheString = JsonUtils.objectToJson(list);
			jedisClient.hset(INDEX_CONTENT_REDIS_KEY, contentCategoryId + "", cacheString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.ok(list);
	}

}
