package com.taotao.search.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.TaotaoResult;
import com.taotao.search.pojo.SearchResult;
import com.taotao.search.service.SearchService;
import com.taotao.utils.ExceptionUtilsMsg;

/**
 * 搜索功能的实现
 * 
 * @author lenovo
 *
 */
@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;

	/**
	 * 通过条件查询 solr服务器
	 * 
	 * http://localhost:8083/search/query?q=一加5t
	 * 
	 * q为查询条件
	 * 
	 * 当前页默认为1
	 * 
	 * 每页展示的条数默认为60
	 * 
	 * @param queryString
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult search(@RequestParam("q") String queryString, @RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "60") Integer rows) {
		// 查询条件不能为空
		if (StringUtils.isBlank(queryString)) {
			return TaotaoResult.build(400, "查询条件不能为空");
		}
		// 声明返回值 try块中声明的变量只对try块可见，所以要先在try块外声明变量，便于块外使用
		SearchResult searchResult = null;
		try {
			// 解决get请求参数乱码 重新设置编码
			queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
			// 通过查询条件执行查询
			searchResult = searchService.search(queryString, page, rows);
		} catch (Exception e) {
			e.printStackTrace();
			// 如果查询出错，向页面返回错误信息
			return TaotaoResult.build(500, ExceptionUtilsMsg.getStackTrace(e));
		}

		// 如果try块中没有错误则向页面返回查询结果
		// 向页面返回结果，json数据格式的返回值
		return TaotaoResult.ok(searchResult);

	}
}
