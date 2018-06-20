package com.taotao.order.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.dao.JedisClient;
import com.taotao.order.pojo.Order;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderExample;
import com.taotao.pojo.TbOrderExample.Criteria;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderItemExample;
import com.taotao.pojo.TbOrderShipping;
import com.taotao.pojo.TbOrderShippingExample;

/**
 * 订单管理service
 * 
 * @author lenovo
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private JedisClient jedisClient;

	@Value("${ORDER_GEN_KEY}")
	private String ORDER_GEN_KEY;
	@Value("${ORDER_INIT_ID}")
	private String ORDER_INIT_ID;
	@Value("${ORDER_DETAIL_GEN_KEY}")
	private String ORDER_DETAIL_GEN_KEY;

	/**
	 * 创建订单
	 */
	@Override
	public TaotaoResult creatOrder(TbOrder order, List<TbOrderItem> itemList, TbOrderShipping orderShipping) {
		// 向订单表中插入记录

		// 获得订单编号 使用redis中的incr命令 自增长 从自定义的100544开始
		String string = jedisClient.get(ORDER_GEN_KEY);

		// 判断并设置初始值
		if (StringUtils.isBlank(string)) {
			jedisClient.set(ORDER_GEN_KEY, ORDER_INIT_ID);
		}
		long orderId = jedisClient.incr(ORDER_GEN_KEY);
		// 补全pojo内容
		order.setOrderId(orderId + "");
		// 状态：1、未付款 2、已付款 3、未发货 4、已发货 5、交易成功 6、交易关闭
		order.setStatus(1);
		Date date = new Date();
		order.setCreateTime(date);
		order.setUpdateTime(date);
		// 是否已评价 0 未评价 1已评价
		order.setBuyerRate(0);

		// 向订单表插入数据
		orderMapper.insert(order);
		// 插入订单明细
		for (TbOrderItem tbOrderItem : itemList) {
			// 补全订单明细
			// 取订单明细id
			long orderDetailId = jedisClient.incr(ORDER_DETAIL_GEN_KEY);
			tbOrderItem.setId(orderDetailId + "");
			tbOrderItem.setOrderId(orderId + "");
			// 向订单明细插入记录
			orderItemMapper.insert(tbOrderItem);
		}
		// 插入物流表数据
		// 补全物流表的属性
		orderShipping.setOrderId(orderId + "");
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		// 插入数据
		orderShippingMapper.insert(orderShipping);

		// 返回status: 200 //200 成功 msg: "OK" // 返回信息消息 data: 100544// 返回订单号
		return TaotaoResult.ok(orderId);
	}

	/**
	 * 根据订单id查询订单
	 */
	@Override
	public TaotaoResult getOrdetByOrderId(Long orderId) {
		// 创建Tborder的子类 对应 所需订单数据，查询三个表，将数据写入此对象
		Order order = new Order();
		// 执行查询
		TbOrder tbOrder = orderMapper.selectByPrimaryKey(orderId.toString());

		// 将查到的tbOrder 传入Order
		order.setOrderId(tbOrder.getOrderId());
		order.setPayment(tbOrder.getPayment());
		order.setPaymentType(tbOrder.getPaymentType());
		order.setStatus(tbOrder.getStatus());
		order.setCreateTime(tbOrder.getCreateTime());
		order.setPostFee(tbOrder.getPostFee());
		order.setUserId(tbOrder.getUserId());
		order.setBuyerMessage(tbOrder.getBuyerMessage());
		order.setBuyerNick(tbOrder.getBuyerNick());

		// 创建TbOrderItem的查询条件
		TbOrderItemExample orderItemExample = new TbOrderItemExample();
		TbOrderItemExample.Criteria criteria2 = orderItemExample.createCriteria();
		criteria2.andOrderIdEqualTo(orderId.toString());

		// 执行查询
		List<TbOrderItem> tbOrderItemList = orderItemMapper.selectByExample(orderItemExample);
		// 赋值 取出的是一次订单中的多个商品信息
		order.setOrderItems(tbOrderItemList);

		// order是主键，直接查询
		TbOrderShipping tbOrderShipping = orderShippingMapper.selectByPrimaryKey(orderId.toString());

		// 将查询的对象赋值
		order.setOrderShipping(tbOrderShipping);

		return TaotaoResult.ok(order);
	}

	/**
	 * 根据userid查询订单TbOrder
	 */
	@Override
	public TaotaoResult getOrderByUserId(Long userId, Integer page, Integer count) {
		// 创建查询条件
		TbOrderExample example = new TbOrderExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		// 使用分页插件完成分页操作
		PageHelper.startPage(page, count);
		// 执行查询
		List<TbOrder> orderList = orderMapper.selectByExample(example);

		// 返回订单列表
		return TaotaoResult.ok(orderList);
	}

	/**
	 * 修改订单状态
	 * 
	 * 状态：1、未付款 2、已付款 3、未发货 4、已发货 5、交易成功 6、交易关闭
	 */
	@Override
	public TaotaoResult updateOrderStatus(Long orderid, Integer status, String paymentTime) {
		// 执行查询
		TbOrder tbOrder = orderMapper.selectByPrimaryKey(orderid.toString());
		// 修改订单状态
		tbOrder.setStatus(status);

		// 如果修改状态码为2，则需要添加支付时间
		if (status.equals(2)) {
			tbOrder.setPaymentTime(new Date());
		}

		// 返回成功
		return TaotaoResult.ok();
	}

}
