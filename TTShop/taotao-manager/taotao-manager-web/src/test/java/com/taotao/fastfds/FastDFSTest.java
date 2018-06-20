package com.taotao.fastfds;

import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class FastDFSTest {
	/**
	 * 测试使用图片服务器上传图片。
	 * @throws MyException 
	 * @throws IOException 
	 */
	/*@Test
	public void testFastDFSTest() throws Exception{
		//1,把FastDFS提供的jar包提供到工程里面
		//2,初始化全局变量加载一个配置文件
		ClientGlobal.init("");
		//3，创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//4，创建一个TrackerServler对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//5，声明一个StorageClient对象那个
		StorageServer storageServer = null;
		//6,通过TrackerServer对象来获得StorageClient对象
		StorageClient storageClient = new StorageClient();
		//7,直接调用StorageClient对象实现图片的上传。
		String[] strings = storageClient.upload_file("C:\\Users\\Administrator\\Desktop\\splash1.bmp", "bmp",null);
		for(String string:strings){
			System.out.println(string);
		}
	}*/
}
