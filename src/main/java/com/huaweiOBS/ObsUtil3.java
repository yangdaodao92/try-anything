package com.huaweiOBS;

import com.obs.services.ObsClient;
import com.obs.services.model.AppendObjectRequest;
import com.obs.services.model.AppendObjectResult;
import com.obs.services.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.io.File;

public class ObsUtil3 {

	private static String endPoint = "obs.cn-north-1.myhwclouds.com";
	private static String ak = "6GBKY5WYZZLZTQZSOHR2";
	private static String sk = "0BRmyWXDdFv0iO7Bs4KYQjST4vrspmJJ0dm3Shr3";

	private static String bucketName = "dws-behaviour-data-operation";

	public static void main(String[] args) {
		appendUpload();
	}

	public static void appendUpload() {
		String objectKey = "membercenter_test/test.txt";

		// 创建ObsClient实例
		ObsClient obsClient = new ObsClient(ak, sk, endPoint);
		ObjectMetadata metadata = obsClient.getObjectMetadata(bucketName, objectKey);

		// 第一次追加上传
		AppendObjectRequest request = new AppendObjectRequest();
		request.setBucketName(bucketName);
		request.setObjectKey(objectKey);

		request.setPosition(metadata.getNextPosition());
		request.setFile(new File("G:\\new 5.txt"));
		AppendObjectResult result = obsClient.appendObject(request);

		// 第二次追加上传
		request.setPosition(result.getNextPosition());
		request.setInput(new ByteArrayInputStream("Hello OBS Again".getBytes()));
		result = obsClient.appendObject(request);

		System.out.println("NextPosition:" + result.getNextPosition());
		System.out.println("Etag:" + result.getEtag());
		// 通过获取对象属性接口获取下次追加上传的位置
		System.out.println("NextPosition from metadata:" + metadata.getNextPosition());
	}

}
