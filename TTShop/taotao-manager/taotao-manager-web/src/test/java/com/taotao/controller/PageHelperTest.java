package com.taotao.controller;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

public class PageHelperTest {
	@Test
	public void testPageHelper(){
		//加载spring容器
		ApplicationContext  applicationContext = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext-*.xml");
		//得到mapper的代理对象
	    TbItemMapper itemMapper = applicationContext.getBean(TbItemMapper.class);
	    //执行查询并且分页
	    TbItemExample example  = new TbItemExample();
	    PageHelper.startPage(1, 20);
	    List<TbItem> list = itemMapper.selectByExample(example);
	    //遍历取出商品的列表
	    for(TbItem itemList:list){
	    	System.out.println(itemList.getTitle());
	    }
	    //取出分页的信息
	    PageInfo<TbItem> pageInfo  = new PageInfo<TbItem>(list);
	    System.out.println("商品的总数"+pageInfo.getTotal());
	}
}
