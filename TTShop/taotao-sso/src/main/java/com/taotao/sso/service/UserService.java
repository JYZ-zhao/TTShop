package com.taotao.sso.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {

	//验证注册信息
	TaotaoResult checkDate(String content,Integer type);
	
	//注册
	TaotaoResult creatUser(TbUser user);
	
	//登录
	TaotaoResult userLogin(String username,String password, HttpServletRequest request, HttpServletResponse response );
	
	//判断是否登录
	TaotaoResult getUserByToken(String token);
	
	//退出登录
	TaotaoResult logout(String token);
}
