package com.taotao.service;

import org.springframework.web.multipart.MultipartFile;

import com.taotao.utils.PictureResult;

public interface PictureService {
	//图片上传
	PictureResult upLoadPic(MultipartFile picFile);
	
}
