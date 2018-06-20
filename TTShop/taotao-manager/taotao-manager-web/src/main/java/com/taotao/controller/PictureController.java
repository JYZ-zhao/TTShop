package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.stat.TableStat.Mode;
import com.taotao.service.PictureService;
import com.taotao.utils.JsonUtils;
import com.taotao.utils.PictureResult;

@Controller
public class PictureController {
	
	@Autowired
	private PictureService  pictureService;
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile){
		//调用service的上传图片的方法完成上传
		PictureResult result = pictureService.upLoadPic(uploadFile);
		String json = JsonUtils.objectToJson(result);
		return json;
	}
}
