package com.taotao.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.order.pojo.Order;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TaotaoResult;
import com.taotao.utils.ExceptionUtilsMsg;

/**
 * 订单controller
 * 
 * @author lenovo
 *
 */
@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;

	/**
	 * 创建订单 使用@RequestBody 接收页面传来的订单json
	 * 
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult creatOrder(@RequestBody Order order) {
		try {
			TaotaoResult result = orderService.creatOrder(order, order.getOrderItems(), order.getOrderShipping());
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtilsMsg.getStackTrace(e));
		}

	}

	/**
	 * 根据订单id查询订单
	 * 
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/info/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult getOrdetByOrderId(@PathVariable Long orderId) {
		try {
			TaotaoResult result = orderService.getOrdetByOrderId(orderId);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtilsMsg.getStackTrace(e));
		}
	}

	/**
	 * 根据用户id分页查询订单
	 * 
	 * @param userID
	 * @param page
	 * @param count
	 * @return
	 */
	@RequestMapping("/list/{userID}/{page}/{count}")
	@ResponseBody
	public TaotaoResult getOrderByUserId(@PathVariable Long userID, @PathVariable Integer page,
			@PathVariable Integer count) {

		try {
			TaotaoResult result = orderService.getOrderByUserId(userID, page, count);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtilsMsg.getStackTrace(e));
		}

	}
	
	
}
