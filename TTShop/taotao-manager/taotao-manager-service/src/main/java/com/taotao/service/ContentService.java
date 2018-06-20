package com.taotao.service;

import com.taotao.pojo.PageBean;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	PageBean showContentList(int page,int rows,long categoryId);
	TaotaoResult saveContent(TbContent tbContent);
	TaotaoResult deleteContent(long[] contentIds);
	TaotaoResult updateContent(long contentId,TbContent tbContent);
}
