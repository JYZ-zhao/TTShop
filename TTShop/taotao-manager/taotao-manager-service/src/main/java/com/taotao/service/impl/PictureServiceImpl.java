package com.taotao.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.service.PictureService;
import com.taotao.utils.FastDFSClient;
import com.taotao.utils.PictureResult;

@Service
public class PictureServiceImpl implements PictureService {
	
	@Value("${UPLOAD_BASE_PATH}")
	private String UPLOAD_BASE_PATH;
	
	@Override
	public PictureResult upLoadPic(MultipartFile picFile) {
		PictureResult result = new PictureResult();
		/*
		 * 判断图片是否为空
		 */
		if(picFile == null){
			result.setError(1);
			result.setMessage("图片上传失败啦！！！！");
		}
		/**
		 * 上传图片
		 */
		/*
		 * 拿到文件的扩展名
		 */
		String originalName = picFile.getOriginalFilename();
		String extName = originalName.substring(originalName.lastIndexOf(".")+1);
		try {
			FastDFSClient client = new FastDFSClient("classpath:resource/client.conf");
			String url = client.uploadFile(picFile.getBytes(), extName);
			System.out.println(url);
			url = UPLOAD_BASE_PATH + url;
			System.out.println(url);
			result.setError(0);
			result.setMessage("图片上传成功");
			result.setUrl(url);
		} catch (Exception e) {
			result.setError(1);
			result.setMessage("图片上传失败");
		}
		return result;
	}


}
