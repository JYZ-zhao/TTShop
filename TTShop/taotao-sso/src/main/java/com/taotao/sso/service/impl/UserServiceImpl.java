package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.dao.JedisClient;
import com.taotao.sso.service.UserService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;

/**
 * 登录信息校验
 * 
 * @author lenovo
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_USER_SEASSION_KEY}")
	private String REDIS_USER_SEASSION_KEY;

	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;

	@Override
	public TaotaoResult checkDate(String content, Integer type) {
		// 创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();

		// 对数据进行校验:1，2，3分别代表username，phone email

		// 用户名校验
		if (1 == type) {
			criteria.andUsernameEqualTo(content);
			// 电话校验
		} else if (2 == type) {
			criteria.andPhoneEqualTo(content);
			// 邮箱校验
		} else {
			criteria.andEmailEqualTo(content);
		}

		// 执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return TaotaoResult.ok(true);
		}
		return TaotaoResult.ok(false);
	}

	/**
	 * 注册用户
	 */
	@Override
	public TaotaoResult creatUser(TbUser user) {

		user.setCreated(new Date());
		user.setUpdated(new Date());
		// md5加密
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		userMapper.insert(user);
		return TaotaoResult.ok();
	}

	/**
	 * 用户登录
	 */
	@Override
	public TaotaoResult userLogin(String username, String password, HttpServletRequest request,
			HttpServletResponse response) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		// 执行查询
		List<TbUser> list = userMapper.selectByExample(example);

		// 如果查出来的list为空，则登录失败
		if (list == null || list.size() == 0) {
			return TaotaoResult.build(400, "用户名或密码错误");
		}

		TbUser user = list.get(0);
		// 对比密码
		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return TaotaoResult.build(400, "用户名或密码错误");
		}

		// 生成token 一个不重复的字符串，用户的唯一标识
		String token = UUID.randomUUID().toString();

		// 保存用户之前把对象中的密码清掉
		user.setPassword(null);

		// 把用户信息写入redis
		jedisClient.set(REDIS_USER_SEASSION_KEY + ":" + token, JsonUtils.objectToJson(user));
		// 设置session的过期世间
		jedisClient.expire(REDIS_USER_SEASSION_KEY + ":" + token, SSO_SESSION_EXPIRE);

		// 添加Cookie的逻辑,cookie的有效期是关闭浏览器就失效
		CookieUtils.setCookie(request, response, "TT_TOKEN", token);

		// 返回token
		return TaotaoResult.ok(token);
	}

	/**
	 * 根据token查user
	 */
	@Override
	public TaotaoResult getUserByToken(String token) {

		// 根据token从redis中查user
		String json = jedisClient.get(REDIS_USER_SEASSION_KEY + ":" + token);
		// 判断是否为空
		if (StringUtils.isBlank(json)) {
			// session已经过期
			return TaotaoResult.build(400, "用户登录信息已过期，请重新登录");
		}
		// 更新过期时间
		jedisClient.expire(REDIS_USER_SEASSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		// 返回用户信息
		return TaotaoResult.ok(json);
	}

	/**
	 * 退出登录
	 */
	@Override
	public TaotaoResult logout(String token) {
		// 删除用户登录信息
		jedisClient.del(REDIS_USER_SEASSION_KEY + ":" + token);
		return TaotaoResult.ok();
	}

}
