package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.utils.ExceptionUtilsMsg;

/**
 * 用户Controller
 * 
 * @author lenovo
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable String param, @PathVariable Integer type, String callback) {

		TaotaoResult result = null;

		// 参数有效性校验
		if (StringUtils.isBlank(param)) {
			result = TaotaoResult.build(400, "校验内容不能为空");
		}
		if (type == null) {
			result = TaotaoResult.build(400, "校验内容类型不能为空");
		}
		if (type != 1 && type != 2 && type != 3) {
			result = TaotaoResult.build(400, "校验内容类型错误");
		}
		// 校验出错调用jsonp
		if (null != result) {
			if (null != callback) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			} else {
				return result;
			}
		}
		// 调用服务
		try {
			result = userService.checkDate(param, type);

		} catch (Exception e) {
			result = TaotaoResult.build(500, ExceptionUtilsMsg.getStackTrace(e));
		}

		// 校验成功调用jsonp
		if (null != callback) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {
			return result;
		}
	}

	/**
	 * 注册
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult creatUser(TbUser user) {
		try {
			TaotaoResult result = userService.creatUser(user);
			return result;

		} catch (Exception e) {
			return TaotaoResult.build(500, ExceptionUtilsMsg.getStackTrace(e));
		}
	}

	/**
	 * 登录
	 * 
	 * @param String
	 *            username,String password
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult userLogin(String username, String password, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			TaotaoResult result = userService.userLogin(username, password, request, response);
			return result;

		} catch (Exception e) {
			return TaotaoResult.build(500, ExceptionUtilsMsg.getStackTrace(e));
		}
	}

	/**
	 * 从redis中获取用户登录信息
	 * 
	 * 检查用户是否登录
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback) {

		TaotaoResult result = null;
		try {
			// 根据token从redis中取值
			result = userService.getUserByToken(token);
			// 执行后面的判断，判断是否有回调函数
		} catch (Exception e) {
			return TaotaoResult.build(500, ExceptionUtilsMsg.getStackTrace(e));
		}

		// 判断是否为jsonp
		if (StringUtils.isBlank(callback)) {
			return result;
		} else {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
	}

	/**
	 * 登出
	 * 
	 * @param token
	 * @param callback
	 * @return
	 */
	@RequestMapping(value = "/logout/{token}")
	@ResponseBody
	public Object logout(@PathVariable String token, String callback) {

		TaotaoResult result = null;
		try {
			result = userService.logout(token);

		} catch (Exception e) {
			return TaotaoResult.build(500, ExceptionUtilsMsg.getStackTrace(e));
		}

		// 判断是否为jsonp
		if (StringUtils.isBlank(callback)) {
			return result;
		} else {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
	}

}
