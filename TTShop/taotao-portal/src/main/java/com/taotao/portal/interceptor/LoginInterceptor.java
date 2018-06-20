package com.taotao.portal.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.taotao.pojo.TbUser;
import com.taotao.portal.service.impl.UserServiceImpl;
import com.taotao.utils.CookieUtils;

/**
 * 用户登录拦截器
 * 
 * 判断用户是否登录
 * 
 * 从cookie中取token，用token从redis中换取用户数据
 * 
 * 如果取到信息
 * 
 * 将用户信息返回到controller
 * 
 * 
 * @author lenovo
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserServiceImpl userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 在Handler执行之前处理
		// 判断用户是否登录
		// 从cookie中取token
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		// 根据token换取用户信息，调用sso系统的接口。
		TbUser user = userService.getUserByToken(token);
		// 取不到用户信息
		if (null == user) {
			// 跳转到登录页面，把用户请求的url作为参数传递给登录页面。
			response.sendRedirect(
					userService.SSO_DOMAIN_BASE_URL + userService.SSO_PAGE_LOGIN + "?redirect=" + request.getRequestURL());
			// 返回false
			return false;
		}
		// 取到用户信息，放行

		// 把用户信息放入request域中 用于从页面取用户数据
		request.setAttribute("user", user);
		// 返回值决定handler是否执行。true：执行，false：不执行。
		return true;
	}
}
