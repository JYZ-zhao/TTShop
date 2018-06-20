package com.taotao.portal.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
/**
 * 商品搜索controller
 * @author lenovo
 *
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;

/**
 * 搜索服务controller
 * 
 * @author lenovo
 *
 */
@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;

	/**
	 * 调用service从solr服务器查询列表，
	 * 
	 * 参数是搜索条件和当前页码
	 * 
	 * 将返回的Item对象，回显的搜索条件，总页数，当前页渲染给视图
	 * 
	 * @param queryString
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/search")
	public String search(@RequestParam("q") String queryString, @RequestParam(defaultValue = "1") Integer page,
			Model model) {

		// 乱码解决问题
		if (queryString != null) {
			try {
				queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		SearchResult searchResult = searchService.search(queryString, page);
		// 向页面传递参数
		model.addAttribute("query", queryString);
		model.addAttribute("totalPages", searchResult.getPageCount());
		model.addAttribute("itemList", searchResult.getItemList());
		model.addAttribute("page", page);
		return "search";

	}
}
