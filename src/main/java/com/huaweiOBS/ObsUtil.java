package com.huaweiOBS;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.CompleteMultipartUploadRequest;
import com.obs.services.model.DeleteObjectsRequest;
import com.obs.services.model.DeleteObjectsResult;
import com.obs.services.model.InitiateMultipartUploadRequest;
import com.obs.services.model.InitiateMultipartUploadResult;
import com.obs.services.model.KeyAndVersion;
import com.obs.services.model.ListObjectsRequest;
import com.obs.services.model.ListPartsRequest;
import com.obs.services.model.ListPartsResult;
import com.obs.services.model.Multipart;
import com.obs.services.model.ObjectListing;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.PartEtag;
import com.obs.services.model.S3Object;
import com.obs.services.model.UploadPartRequest;
import com.obs.services.model.UploadPartResult;
import com.sun.media.sound.SoftTuning;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ObsUtil {

	private static Logger logger = LoggerFactory.getLogger(glodon.gcj.member.center.utils.util.ObsUtil.class);

	private static String endPoint = "obs.cn-north-1.myhwclouds.com";
	private static String ak = "6GBKY5WYZZLZTQZSOHR2";
	private static String sk = "0BRmyWXDdFv0iO7Bs4KYQjST4vrspmJJ0dm3Shr3";

	private static String bucketName = "dws-behaviour-data-operation";
	private static Long partSize = 5 * 1024 * 1024L;

	public static void main(String[] args) throws IOException {
//		upload(new File("D:\\IdeaSource\\Others\\springbatchtest\\src\\main\\resources\\jdbc\\user.txt"), "user.txt");
//		System.out.println(new File("G:\\test-obs.zip").length());
//		isExist();

//		List<String> list = listAllKeys("membercenter/dwsBehaviourData/2018/20180122");
//		list.sort(new Comparator<String>() {
//			@Override
//			public int compare(String o1, String o2) {
//				String[] o1s = o1.split("/");
//				String[] o2s = o2.split("/");
//				int date = NumberUtils.toInt(o1s[3]) - NumberUtils.toInt(o2s[3]);
//				int fncode = NumberUtils.toInt(o1s[4].split("-")[0]) - NumberUtils.toInt(o2s[4].split("-")[0]);
//				int machineNum = NumberUtils.toInt(o1s[4].split("-")[1].substring(5, 7)) - NumberUtils.toInt(o2s[4].split("-")[1].substring(5, 7));
//
//				return date != 0 ? date : (fncode != 0 ? fncode : machineNum);
//			}
//		});
//
//		System.out.println(StringUtils.join(list, "\n"));

		// 从华为云下载
		System.out.println(listAllKeys("membercenter/dwsBehaviourData-Error/").size());
		listAllKeys("membercenter/dwsBehaviourData-Error/").stream().filter(key -> key.contains("Distinct")).forEach(key -> {
			try {
				load(key, new File("E:/test2/" + key.replace("membercenter/dwsBehaviourData-Error/", "")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		// 删除存在的异常数据
		deleteAllKeys(listAllKeys("membercenter/dwsBehaviourData-Error/"));

		// 将错误日志合并
		Map<String, Map<String, String>> fncodeMap = new HashMap<>();
		for (File file : FileUtils.listFiles(new File("E:\\test2"), new String[]{"log"}, true)) {
			String fncode = file.getName().split("-")[1];
			fncodeMap.computeIfAbsent(fncode, k -> new HashMap<>());

			String ss = StringUtils.join(FileUtils.readLines(file, "utf-8"), System.getProperty("line.separator"));
			for (String s : ss.split("----------------------------------------------------------------------------------------------------" + System.getProperty("line.separator"))) {
				String aa[] = s.split(System.getProperty("line.separator"));
				if (aa.length > 1) {
					fncodeMap.get(fncode).put(aa[1], s);
				}
			}
		}
		for (String fncode : fncodeMap.keySet()) {
			for (String s : fncodeMap.get(fncode).keySet()) {
				FileUtils.write(new File("E:\\test2\\combine\\" + fncode + ".log"), System.getProperty("line.separator") + fncodeMap.get(fncode).get(s), "utf-8", true);
			}
		}
	}

	public static void deleteAllKeys(List<String> objectKeyList) {
		ObsClient obsClient = new ObsClient(ak, sk, endPoint);
		DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName);
		// 设置为verbose模式
		request.setQuiet(false);

		List<KeyAndVersion> toDelete = new ArrayList<>();
		for (int i = 0; i < objectKeyList.size(); i++) {
			toDelete.add(new KeyAndVersion(objectKeyList.get(i)));
			if ((i + 1) % 1000 == 0 || i == (objectKeyList.size() - 1)) {
				request.setKeyAndVersions(toDelete.toArray(new KeyAndVersion[toDelete.size()]));

				DeleteObjectsResult result = obsClient.deleteObjects(request);
				System.out.println(result.getDeletedObjectResults());
				System.out.println(result.getErrorResults());
				toDelete.clear();
			}
		}

		try {
			obsClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String> listAllKeys(String prefix) throws IOException {
		List<String> keyList = new ArrayList<>();
		ObsClient obsClient = new ObsClient(ak, sk, endPoint);
		ListObjectsRequest request = new ListObjectsRequest(bucketName);
		request.setPrefix(prefix);
		request.setMaxKeys(1000);
		ObjectListing result;
		do {
			result = obsClient.listObjects(request);
			for (S3Object s3Object : result.getObjectSummaries()) {
				keyList.add(s3Object.getObjectKey());
			}
			request.setMarker(result.getNextMarker());
		} while (result.isTruncated());
		obsClient.close();
		return keyList;
	}

	public static boolean isExist() {
		ObsClient obsClient = new ObsClient(ak, sk, endPoint);
		try {
			S3Object s3Object = obsClient.getObject(bucketName, "user.txt");
			return s3Object != null;
		} catch (ObsException e) {
			return false;
		}
	}

	public static boolean load(String objectKey, File file) throws IOException {
//		String endPoint = ResourceBundle.getBundle("property/init").getString("obs.endPoint");
//		String ak = ResourceBundle.getBundle("property/init").getString("obs.ak");
//		String sk = ResourceBundle.getBundle("property/init").getString("obs.sk");
//		String bucketName = ResourceBundle.getBundle("property/init").getString("obs.bucketName");

		return load(endPoint, ak, sk, bucketName, objectKey, file);
	}

	public static boolean load(String endPoint, String ak, String sk, String bucketName, String objectKey, File file) throws IOException {
		try {
			ObsClient obsClient = new ObsClient(ak, sk, endPoint);
			S3Object s3Object = obsClient.getObject(bucketName, objectKey);
			long beginTime = System.currentTimeMillis();
			logger.info("Start file loading. Begin at {}", DateFormatUtils.format(beginTime, "yyyy-MM-dd HH:mm:ss"));
			BufferedReader br = new BufferedReader(new InputStreamReader(s3Object.getObjectContent(), "UTF-8"));

			if (file.isFile()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
			}
			int count = 0;
			if (!file.exists() || file.delete()) {
				boolean firstLine = true;
				String line;
				while ((line = br.readLine()) != null) {
					FileUtils.write(file, (firstLine ? "" : System.getProperty("line.separator")) + line, "utf-8", true);
					firstLine = false;

					// 提示文件下载进度
					if (count % 200 == 0) {
						System.out.print(".");
					}
					count++;
					if (count % 20000 == 0) {
						System.out.println();
					}
				}
				System.out.println();
			}
			br.close();
			obsClient.close();
			logger.info("File load successfully! Line count {}. File size {}.", count, file.length());
			long endTime = System.currentTimeMillis();
			logger.info("End file loading. End at {}. Total time {}.", DateFormatUtils.format(endTime, "yyyy-MM-dd HH:mm:ss"), DurationFormatUtils.formatDuration(endTime - beginTime, "yyyy-MM-dd HH:mm:ss"));
			return true;
		} catch (ObsException e) {
			logger.info("请求OBS对象失败 errorCode:{}", e.getErrorCode());
			return false;
		}
	}

	public static void delete(String objectKey) throws IOException {
		ObsClient obsClient = new ObsClient(ak, sk, endPoint);
		obsClient.deleteObject(bucketName, objectKey);
		obsClient.close();
	}

	public static String upload(File file, String objectKey) throws IOException {
		Long fileLength = file.length();

		// 创建ObsClient实例
		ObsClient obsClient = new ObsClient(ak, sk, endPoint);

		InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, objectKey);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.addUserMetadata("property", "property-value");
		metadata.setContentType("text/plain");
		request.setMetadata(metadata);
		InitiateMultipartUploadResult result = obsClient.initiateMultipartUpload(request);
		String uploadId = result.getUploadId();

		List<PartEtag> partEtags = new ArrayList<PartEtag>();

		long partCount = fileLength % partSize == 0 ? fileLength / partSize : fileLength / partSize + 1;
		if (partCount > 10000) {
			throw new RuntimeException("Total parts count should not exceed 10000");
		}

		UploadPartRequest requestU;
		for (int i = 0; i < partCount; i++) {
			System.out.print("Uploading part " + (i + 1) + "/" + partCount);
			long offset = i * partSize;
			long currPartSize = (i + 1 == partCount) ? fileLength - offset : partSize;

			requestU = new UploadPartRequest(bucketName, objectKey);
			requestU.setUploadId(uploadId);
			requestU.setPartNumber(i + 1);
			requestU.setFile(file);
			requestU.setOffset(offset);
			requestU.setPartSize(currPartSize);
			UploadPartResult resultU = obsClient.uploadPart(requestU);
			partEtags.add(new PartEtag(resultU.getEtag(), resultU.getPartNumber()));
			System.out.println("   Done.");
		}

		if (partEtags.size() != partCount) {
			throw new IllegalStateException("Upload multiparts fail due to some parts are not finished yet");
		} else {
			System.out.println("Succeed to complete multiparts into an object named " + objectKey + "\n");
		}

		completeMultipartUpload(obsClient, bucketName, objectKey, partEtags, uploadId);
		obsClient.close();
		return uploadId;
	}

	private static void completeMultipartUpload(ObsClient obsClient, String bucketName, String objectKey, List<PartEtag> partEtags, String uploadId) throws ObsException {
		Collections.sort(partEtags, new Comparator<PartEtag>() {
			@Override
			public int compare(PartEtag o1, PartEtag o2) {
				return o1.getPartNumber() - o2.getPartNumber();
			}
		});

		System.out.println("Completing to upload multiparts\n");
		CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName,
				objectKey, uploadId, partEtags);
		obsClient.completeMultipartUpload(completeMultipartUploadRequest);
	}

	private static void listAllParts(ObsClient obsClient, String bucketName, String objectKey, String uploadId) throws ObsException {
		System.out.println("Listing all parts......");
		ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, objectKey, uploadId);
		ListPartsResult partListing = obsClient.listParts(listPartsRequest);

		for (Multipart part : partListing.getMultipartList()) {
			System.out.println("\tPart#" + part.getPartNumber() + ", ETag=" + part.getEtag());
		}
		System.out.println();
	}

}
