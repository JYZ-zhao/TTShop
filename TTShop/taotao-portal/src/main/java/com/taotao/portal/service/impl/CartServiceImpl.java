package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.HttpClientUtil;
import com.taotao.utils.JsonUtils;

@Service
public class CartServiceImpl implements CartService {

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${ITEM_INFO_URL}")
	private String ITEM_INFO_URL;

	/**
	 * 添加购物车商品
	 */
	@Override
	public TaotaoResult addCartItem(Long itemId, Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		CartItem cartItem = null;

		// 获取购物车商品列表
		List<CartItem> itemList = getCartItemList(request);
		// 判断商品列表是否存在商品信息
		for (CartItem cItem : itemList) {
			// 如果存在此商品
			if (cItem.getId().equals(itemId)) {
				// 增加商品数量
				cItem.setNum(cItem.getNum() + num);
				cartItem = cItem;
				break;
			}
		}

		if (cartItem == null) {
			cartItem = new CartItem();

			// 根据商品id查询商品基本信息
			String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);

			// 把json转换成java对象
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItem.class);
			if (taotaoResult.getStatus() == 200) {
				TbItem item = (TbItem) taotaoResult.getData();
				cartItem.setId(item.getId());
				cartItem.setTitle(item.getTitle());
				cartItem.setImage(item.getImage() == null ? "" : item.getImage().split(",")[0]);
				cartItem.setNum(num);
				cartItem.setPrice(item.getPrice());
			}
			// 添加到购物车列表
			itemList.add(cartItem);

		}

		// 把购物车列表写入cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(itemList), true);
		return TaotaoResult.ok();

	}

	/**
	 * 从cookie中取商品列表
	 * 
	 * @return
	 */
	private List<CartItem> getCartItemList(HttpServletRequest request) {
		// 从cookie中取商品列表
		String cartJson = CookieUtils.getCookieValue(request, "TT_CART", true);
		if (cartJson == null) {
			return new ArrayList<>();
		}
		// 把json转换成商品列表
		try {
			List<CartItem> list = JsonUtils.jsonToList(cartJson, CartItem.class);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	/**
	 * 获取购物车列表
	 */
	@Override
	public List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response) {
		List<CartItem> list = getCartItemList(request);
		return list;
	}

	/**
	 * 修改购物车数量
	 */
	@Override
	public TaotaoResult updateCart(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {

		// 获取购物车商品列表
		List<CartItem> itemList = getCartItemList(request);
		// 判断商品列表是否存在商品信息
		for (CartItem cItem : itemList) {
			// 如果存在此商品
			if (cItem.getId().equals(itemId)) {
				// 增加商品数量
				cItem.setNum(num);
				break;
			}
		}

		// 把购物车列表写入cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(itemList), true);
		return TaotaoResult.ok();

	}

	/**
	 * 删除购物车商品
	 */
	@Override
	public TaotaoResult deleteCart(Long itemId, HttpServletRequest request, HttpServletResponse response) {
		// 获取购物车商品列表
		List<CartItem> itemList = getCartItemList(request);
		// 判断商品列表是否存在商品信息

		for (CartItem cItem : itemList) {
			// 如果存在此商品
			if (cItem.getId().equals(itemId)) {
				itemList.remove(cItem);
				break;
			}
		}

		// 把购物车列表重新写入cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(itemList), true);
		return TaotaoResult.ok();
	}

	/**
	 * 删除购物车多个商品
	 */
	@Override
	public TaotaoResult deleteCarts(Long[] itemId, HttpServletRequest request, HttpServletResponse response) {
		// 获取购物车商品列表
		List<CartItem> itemList = getCartItemList(request);
		for (Long long1 : itemId) {
			// 判断商品列表是否存在商品信息
			for (CartItem cItem : itemList) {
				// 如果存在此商品
				if (cItem.getId().equals(long1)) {
					itemList.remove(cItem);
					break;
				}
			}
		}
		// 把购物车列表重新写入cookie
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(itemList), true);
		return TaotaoResult.ok();
	}

}
