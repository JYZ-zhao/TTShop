package com.taotao.portal.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.pojo.TaotaoResult;
import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;
import com.taotao.utils.HttpClientUtil;

/**
 * 商品搜索service
 * 
 * @author lenovo
 *
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Value("${SEARCH_BASE_URL}")
	private String SEARCH_BASE_URL;

	/**
	 * 调用taotao-search服务
	 */
	@Override
	public SearchResult search(String queryString, int page) {
		// 查询参数
		Map<String, String> param = new HashMap<>();
		param.put("q", queryString);
		param.put("page", page + "");
		try {
			// 调用taotao-search服务
			String string = HttpClientUtil.doGet(SEARCH_BASE_URL, param);
			// 把字符串转换成java对象
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(string, SearchResult.class);
			if (taotaoResult.getStatus()==200) {
				SearchResult result =(SearchResult) taotaoResult.getData();
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
