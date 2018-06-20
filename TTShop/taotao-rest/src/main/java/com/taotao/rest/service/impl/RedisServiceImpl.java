package com.taotao.rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.pojo.TaotaoResult;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.RedisService;
import com.taotao.utils.ExceptionUtilsMsg;

@Service
public class RedisServiceImpl implements RedisService {

	@Autowired
	private JedisClient jedisClient;

	@Value("${INDEX_CONTENT_REDIS_KEY}")
	private String INDEX_CONTENT_REDIS_KEY;
	
	@Value("${INDEX_ITEMCAT_REDIS_KEY}")
	private String INDEX_ITEMCAT_REDIS_KEY;

	//删除缓存，根据对应的内容分类id
	@Override
	public TaotaoResult syncContent(Long contentCid) {
		try {
			jedisClient.hdel(INDEX_CONTENT_REDIS_KEY, contentCid + "");
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtilsMsg.getStackTrace(e));
		}
		return TaotaoResult.ok();
	}

	
	@Override
	public TaotaoResult syncItemCategory() {
		try {
			jedisClient.del(INDEX_ITEMCAT_REDIS_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtilsMsg.getStackTrace(e));
		}
		return TaotaoResult.ok();
	}

}
