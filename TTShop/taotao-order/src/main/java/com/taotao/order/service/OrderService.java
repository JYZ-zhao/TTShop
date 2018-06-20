package com.taotao.order.service;

import java.util.List;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

public interface OrderService {

	TaotaoResult creatOrder(TbOrder order, List<TbOrderItem> itemList, TbOrderShipping orderShipping);

	TaotaoResult getOrdetByOrderId(Long orderId);

	TaotaoResult getOrderByUserId(Long userId ,Integer page ,Integer count);
	
	TaotaoResult updateOrderStatus(Long orderid, Integer status, String paymentTime);
}
