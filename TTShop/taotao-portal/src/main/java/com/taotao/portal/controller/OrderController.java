package com.taotao.portal.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.pojo.TbUser;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.pojo.Order;
import com.taotao.portal.service.CartService;
import com.taotao.portal.service.OrderService;

/**
 * 跳转到订单确认页面
 * 
 * @author lenovo
 *
 */
@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;

	// 展示order-cart.jsp
	@RequestMapping("/order-cart")
	public String showOrderCart(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<CartItem> itemList = cartService.getCartItemList(request, response);
		model.addAttribute("cartList", itemList);
		return "order-cart";
	}

	/**
	 * 接收页面提交表单内容
	 * 
	 * 返回成功页面
	 */
	@RequestMapping("/create")
	public String createOrder(Order order, Model model, HttpServletRequest request) {

		try {
			// 从request中取User对象
			TbUser user = (TbUser) request.getAttribute("user");
			// 在order对象中补全用户信息
			order.setUserId(user.getId());
			order.setBuyerNick(user.getUsername());
			// 调用服务
			String orderId = orderService.createOrder(order);
			// 向页面返回orderId
			model.addAttribute("orderId", orderId);

			// 向页面返回总金额 传来的pojo中存在
			model.addAttribute("payment", order.getPayment());

			// 返回预计送达时间 创建三天后，可以写一个方法，根据快递，距离，时间去计算送达时间
			model.addAttribute("date", new DateTime().plusDays(3).toString("yyyy-MM-dd"));
			return "success";
		} catch (Exception e) {
			e.printStackTrace();

			// 创建订单出错后，跳转到错误页面，返回错误信息
			model.addAttribute("message", "生成订单出错，请稍候重试");

			// 告知开发人员，系统出错 可以发送邮件
			return "error/exception";
		}
	}
}
