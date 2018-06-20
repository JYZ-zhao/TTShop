package com.taotao.portal.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;

/**
 * 购物车商品controller
 * 
 * @author lenovo
 *
 */

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	/**
	 * 增加或者减少购物车数量
	 * 
	 * 防止添加成功后页面刷新页面导致继续添加数量
	 * 
	 * 使用转发的方式，地址栏不变
	 * 
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/add/{itemId}")
	public String addCartItem(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		cartService.addCartItem(itemId, num, request, response);
		return "redirect:/cart/success.html";
	}

	@RequestMapping("/success")
	public String showSuccess() {
		return "cartSuccess";
	}

	/**
	 * 展示购物车列表
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/cart")
	public String showCart(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<CartItem> list = cartService.getCartItemList(request, response);
		model.addAttribute("cartList", list);
		return "cart";
	}

	/**
	 * 修改购物车数量
	 * 
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/update/{itemId}/{num}")
	public String updateCart(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		cartService.updateCart(itemId, num, request, response);
		return "cart";
	}

	/**
	 * 删除单个商品
	 * 
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		cartService.deleteCart(itemId, request, response);
		// 重定向到展示购物车的controller
		return "redirect:/cart/cart.html";
	}

	/**
	 * 删除多个商品
	 * 
	 * @param itemId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/deleteItems")
	public String deleteCarts(@RequestParam("delIds") Long[] itemId, HttpServletRequest request,
			HttpServletResponse response) {
		cartService.deleteCarts(itemId, request, response);
		// 重定向到展示购物车的controller
		return "redirect:/cart/cart.html";
	}

}
