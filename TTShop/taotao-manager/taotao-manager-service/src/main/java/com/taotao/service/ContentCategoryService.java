package com.taotao.service;

import java.util.List;

import com.taotao.pojo.EUITreeMsg;
import com.taotao.pojo.TaotaoResult;

public interface ContentCategoryService {
	List<EUITreeMsg> getTreeList(long parentId);
	TaotaoResult saveContentCategory(long parentId,String name);
	TaotaoResult deleteContentCategory(long id);
	TaotaoResult updateContentCategory(long id,String name);
	
}
