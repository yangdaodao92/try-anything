package com.pinche;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.util.ClipboardUtil;
import glodon.gcj.member.center.utils.util.commons.lang3.EnhancedDateFormatUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.io.Resources;

import glodon.gcj.member.center.utils.util.commons.lang3.EnhancedDateUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @author yangnx
 * @date 2018/6/13
 */
public class Example {
	private static Pattern pattern1 = Pattern.compile("(.*?) (\\d{4}/\\d{1,2}/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$)");
	private static Pattern pattern2 = Pattern.compile("^(\\d{4}/\\d{1,2}/\\d{1,2} )?\\d{1,2}:\\d{1,2}:\\d{1,2}$");
	private static String startDateStr = "";

	public static void main(String[] args) throws IOException {
		String targetStartDate = "2018-06-16";
		String targetStartInDay = "上午";


//		URL url = Resources.getResource("pinche/chat3.txt");
//		List<String> lines = Resources.readLines(url, Charset.forName("utf-8"));

		List<String> lines = Arrays.asList(StringUtils.split(ClipboardUtil.getContent(), "\n"));
		List<String> newLines = mark(lines);
		List<PincheInfo> pincheInfoList = process(newLines);
//		for (PincheInfo pincheInfo : pincheInfoList) {
//			if (targetStartDate.equals(EnhancedDateFormatUtils.format(pincheInfo.getStartDate(), "yyyy-MM-dd"))) {
//				if (targetStartInDay.equals(pincheInfo.getStartInDay())) {
//					print(pincheInfo);
//				}
//			}
//		}
	}

	public static List<String> mark(List<String> lines) {
		List<String> newLines = new ArrayList<>();
		for (String line : lines) {
			Matcher matcher1 = pattern1.matcher(line);
			Matcher matcher2 = pattern2.matcher(line);
			// 去除纯时间行
			if (!matcher2.find()) {
				// 名字 时间：做标记
				if (matcher1.find()) {
					newLines.add("%%_%%-%%" + matcher1.group());
				} else {
					newLines.add(line);
				}
			}
		}
		return newLines;
	}

	public static List<PincheInfo> process(List<String> newLines) {
		List<PincheInfo> pincheInfoList = new ArrayList<>();

		String newLinesStr = StringUtils.join(newLines, "\n");
		String[] chatStrs = StringUtils.split(newLinesStr, "%%_%%-%%");
		for (String chatStr : chatStrs) {
			String[] chats = StringUtils.split(chatStr, "\n", 2);
			if (chats.length > 1) {
				PincheInfo pincheInfo = new PincheInfo();
				// 提取标题
				Matcher matcher1 = pattern1.matcher(chats[0]);
				if (matcher1.find()) {
					String nickName = matcher1.group(1);
					Date msgDate = EnhancedDateUtils.parseDate(matcher1.group(2), "yyyy/M/dd H:mm:ss");
					pincheInfo.setNickName(nickName);
					pincheInfo.setMsgDate(msgDate);
				}
				// 提取正文
				extract(chats[1], pincheInfo);
				pincheInfo.setOriMsg(chats[1]);
				pincheInfoList.add(pincheInfo);
				print(pincheInfo);
			}
		}
		return pincheInfoList;
	}

	public static void extract(String msg, PincheInfo pincheInfo) {
		// 车类型：私家车
		Matcher carTypePattern = Pattern.compile("私家车").matcher(msg);
		if (carTypePattern.find()) {
			pincheInfo.setCarType("私家车");
			pincheInfo.setMsgType("车找人");
		}
		if (pincheInfo.getMsgType() == null) {
			// 方式：车找人、人找车
			Matcher msgTypeM = Pattern.compile("((车找人)|(人找车))").matcher(msg);
			if (msgTypeM.find()) {
				pincheInfo.setMsgType(msgTypeM.group());
			}
		}
		// 发车日期
		Matcher startDateM = Pattern.compile("((今天)|(明天)|(后天)|(今)|(明))((上午)|(早上)|(早上午)|(中午)|(下午)|(晚上)|(早)|(晚))").matcher(msg);
		// 发车时段：上午 中午 下午
		Matcher startInDayM = Pattern.compile("^").matcher(msg);
		if (startDateM.find()) {
			switch (startDateM.group(1)) {
				case "今天":
					pincheInfo.setStartDate(pincheInfo.getMsgDate());
					break;
				case "明天":
					pincheInfo.setStartDate(DateUtils.addDays(pincheInfo.getMsgDate(), 1));
					break;
				case "后天":
					pincheInfo.setStartDate(DateUtils.addDays(pincheInfo.getMsgDate(), 2));
					break;
			}
			pincheInfo.setStartInDay(startDateM.group(5));
		}
		if (pincheInfo.getStartDate() == null) {
			// 发车日期
			Matcher startDateM2 = Pattern.compile("(\\d{1,2})[号日].*?((上午)|(早上)|(早上午)|(中午)|(下午)|(晚上)|(早)|(晚))").matcher(msg);
			if (startDateM2.find()) {
				Integer day = NumberUtils.createInteger(startDateM2.group(1));
				if (day != null) {
					int curDay = new Date().getDay();
					if (day < curDay) {
						Date startDate = DateUtils.setDays(new Date(), day);
						pincheInfo.setStartDate(DateUtils.addMonths(startDate, 1));
					} else {
						pincheInfo.setStartDate(DateUtils.setDays(new Date(), day));
					}
				}
				pincheInfo.setStartInDay(startDateM2.group(2));
			}
		}


		// 发车地点：北京顺义/后沙峪地铁站
		msg = StringUtils.replacePattern(msg, "((今天)|(明天)|(后天))((上午)|(早上)|(早上午)|(中午)|(下午)|(晚上)|(早)|(晚))", "##_##");
		Matcher startLocationM = Pattern.compile("##_##(.*?)[回到去往](.*)[\\s,，]?.*?((电话)|(手机)|(联系方式))?.*(\\d{11})(.*)").matcher(msg);
		if (startLocationM.find()) {
			pincheInfo.setStartLocation(startLocationM.group(1));
			pincheInfo.setEndLocation(startLocationM.group(2));
			pincheInfo.setPhone(startLocationM.group(7));
			pincheInfo.setCarRemark(startLocationM.group(8));
		}
		// 目的地点：柏乡，隆尧固城附近
		Matcher endLocationM = Pattern.compile("^").matcher(msg);
		// 电话
		Matcher phoneM = Pattern.compile("^").matcher(msg);
		// 备注
		Matcher carRemarkM = Pattern.compile("^").matcher(msg);
	}

	public static String ifNull(String str) {
		if (StringUtils.isEmpty(str)) {
			return "***";
		}
		return str;
	}

	public static void print(PincheInfo pincheInfo) {
		System.out.println(StringUtils.trimToEmpty(pincheInfo.getCarType()) + ifNull(pincheInfo.getMsgType())
				+ ifNull(EnhancedDateFormatUtils.format(pincheInfo.getStartDate(), "MM-dd"))
				+ ifNull(pincheInfo.getStartInDay()) + ifNull(pincheInfo.getStartLocation()) + "  回  " + ifNull(pincheInfo.getEndLocation())
				+ ifNull(pincheInfo.getPhone()) + StringUtils.trimToEmpty(pincheInfo.getCarRemark()));
		System.out.println(pincheInfo.getOriMsg());
		System.out.println(StringUtils.repeat("-", 100));
	}

}
