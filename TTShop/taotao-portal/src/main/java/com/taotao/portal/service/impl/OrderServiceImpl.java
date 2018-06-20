package com.taotao.portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.pojo.TaotaoResult;
import com.taotao.portal.pojo.Order;
import com.taotao.portal.service.OrderService;
import com.taotao.utils.HttpClientUtil;
import com.taotao.utils.JsonUtils;

/**
 * 订单处理service
 * 
 * @author lenovo
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Value("${ORDER_BASE_URL}")
	private String ORDER_BASE_URL;
	@Value("${ORDER_CREATE_URL}")
	private String ORDER_CREATE_URL;

	/**
	 * 创建订单
	 */
	@Override
	public String createOrder(Order order) {

		// 创建订单服务之前补全用户信息 拦截器已经获得，可以在拦截器中把取到的用户信息放到reuqest对象中

		// 在controller中取出，省去一次调用服务的过程

		// 从cookie中获取TT_TOKEN,根据token换取用户信息

		// 调用taotao-order的服务

		// 调用taotao-order服务创建订单
		String json = HttpClientUtil.doPostJson(ORDER_BASE_URL + ORDER_CREATE_URL, JsonUtils.objectToJson(order));
		// 需要把json转换成taotaoresult
		TaotaoResult taotaoResult = TaotaoResult.format(json);
		if (taotaoResult.getStatus() == 200) {
			// 从taotaoResult中将数据取出
			Object orderById = taotaoResult.getData();
			return orderById.toString();
		}
		// 如果status不为200，则返回空
		return "";
	}

}
