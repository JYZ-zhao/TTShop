package com.taotao.portal.pojo;

import com.taotao.pojo.TbItem;

/**
 * 商品展示的pojo，由于从数据库中取到的图片不止一张，所以需要子类继承TbItem
 * 
 * 并添加一个获取图片链接数组的get方法
 * 
 * @author lenovo
 *
 */
public class ItemInfo extends TbItem {

	public String[] getImages() {
		String image = getImage();
		if (image != null) {
			String[] images = image.split(",");
			return images;
		}
		return null;
	}
}
