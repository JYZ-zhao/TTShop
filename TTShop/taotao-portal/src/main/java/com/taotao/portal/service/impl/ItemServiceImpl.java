package com.taotao.portal.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.portal.pojo.ItemInfo;
import com.taotao.portal.service.ItemService;
import com.taotao.utils.HttpClientUtil;
import com.taotao.utils.JsonUtils;

/**
 * 商品展示的service
 * 
 * 调用rest服务层，获得数据
 * 
 * 不需要dao层
 * 
 * @author lenovo
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	// 不需要dao层

	// rest服务层url
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;

	// 商品基本信息url
	@Value("${ITEM_INFO_URL}")
	private String ITEM_INFO_URL;

	// 商品描述信息url
	@Value("${ITEM_DESC_URL}")
	private String ITEM_DESC_URL;

	// 商品描述信息url
	@Value("${ITEM_PARAM_URL}")
	private String ITEM_PARAM_URL;

	/**
	 * 取商品基本信息
	 */
	@Override
	public ItemInfo geTbItemById(Long itemId) {

		try {
			// 调用rest服务查询商品基本信息
			String string = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
			// 如果查询出来的结果有值
			if (!StringUtils.isBlank(string)) {
				// 将查询出来的json对象转换成pojo

				/*
				 * ************************************************
				 * 
				 * 需要返回ItemInfo TbItem的子类 添加一个获取图片数组的方法
				 * 
				 * ************************************************
				 */

				// TaotaoResult taotaoResult = TaotaoResult.formatToPojo(string,
				// TbItem.class);

				TaotaoResult taotaoResult = TaotaoResult.formatToPojo(string, ItemInfo.class);
				// 如果转换成功
				if (taotaoResult.getStatus() == 200) {
					// 将对象取出
					// TbItem tbItem = (TbItem) taotaoResult.getData();

					// 返回对象
					// return tbItem;
					ItemInfo item = (ItemInfo) taotaoResult.getData();
					return item;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 取商品描述
	 */
	@Override
	public String getItemDescById(Long itemId) {
		try {
			// 查询商品描述
			String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_DESC_URL + itemId);

			// 转换成pojo
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItemDesc.class);

			if (taotaoResult.getStatus() == 200) {
				// 取商品描述pojo
				TbItemDesc desc = (TbItemDesc) taotaoResult.getData();
				// 取商品描述信息
				String result = desc.getItemDesc();

				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取商品参数通过商品id
	 */
	@Override
	public String getItemParamById(Long itemId) {
		try {
			String string = HttpClientUtil.doGet(REST_BASE_URL + ITEM_PARAM_URL + itemId);
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(string, TbItemParamItem.class);
			if (taotaoResult.getStatus() == 200) {
				// 取值
				TbItemParamItem itemParamItem = (TbItemParamItem) taotaoResult.getData();
				String paramData = itemParamItem.getParamData();
				// json转换成list
				List<Map> jsonList = JsonUtils.jsonToList(paramData, Map.class);

				// 创建一个stringbuffer
				StringBuffer sb = new StringBuffer();
				sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\">\n");
				sb.append("    <tbody>\n");
				for (Map m1 : jsonList) {
					sb.append("        <tr>\n");
					sb.append("            <th class=\"tdTitle\" colspan=\"2\">" + m1.get("group") + "</th>\n");
					sb.append("        </tr>\n");
					List<Map> list2 = (List<Map>) m1.get("params");
					for (Map m2 : list2) {
						sb.append("        <tr>\n");
						sb.append("            <td class=\"tdTitle\">" + m2.get("k") + "</td>\n");
						sb.append("            <td>" + m2.get("v") + "</td>\n");
						sb.append("        </tr>\n");
					}
				}
				sb.append("    </tbody>\n");
				sb.append("</table>");
				// 返回html片段
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
