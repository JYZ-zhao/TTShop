package com.taotao.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
import com.taotao.utils.JsonUtils;

/**
 * 加载前台页面显示分类所需要的json数据
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;

	// 注入redis
	@Autowired
	private JedisClient jedisClient;

	// 注入常量
	@Value("${INDEX_ITEMCAT_REDIS_KEY}")
	private String INDEX_ITEMCAT_REDIS_KEY;

	/**
	 * 获取分类列表
	 */
	@Override
	public CatResult getCatResultList() {
		// 页面所需返回值pojo
		CatResult catResult = new CatResult();
		// 从缓存中查找数据
		try {
			String result = jedisClient.get(INDEX_ITEMCAT_REDIS_KEY);
			// 如果取出来的不是空
			if (!StringUtils.isBlank(result)) {
				catResult = JsonUtils.jsonToPojo(result, CatResult.class);
				return catResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 调用递归方法获得页面所需数据
		catResult.setData(getDataList(0));

		// 同步到缓存
		try {
			// 把list转换成字符串
			String cacheString = JsonUtils.objectToJson(catResult);
			jedisClient.set(INDEX_ITEMCAT_REDIS_KEY, cacheString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return catResult;
	}

	/**
	 * 递归从数据库中取数据
	 * 
	 * @param parentId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<?> getDataList(long parentId) {
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 返回值list
		List resultList = new ArrayList<>();
		// 根据parentId去查找list，默认值是0，大分类
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		int count = 0;
		// 向list中递归添加节点
		for (TbItemCat tbItemCat : list) {
			if (tbItemCat.getIsParent()) {
				CatNode catNode = new CatNode();
				// 判断是否为父节点
				if (tbItemCat.getParentId() == 0) {
					catNode.setName(
							"<a href='/products/" + tbItemCat.getId() + ".html'>" + tbItemCat.getName() + "</a>");
				} else {
					catNode.setName(tbItemCat.getName());
				}
				catNode.setUrl("/products/" + tbItemCat.getId() + ".html");
				// 递归
				catNode.setItem(getDataList(tbItemCat.getId()));
				resultList.add(catNode);
				count++;
				if (count >= 14) {
					break;
				}
				// 如果是子节点
			} else {
				resultList.add("products/" + tbItemCat.getId() + ".html|" + tbItemCat.getName());
			}
		}
		return resultList;
	}
}
