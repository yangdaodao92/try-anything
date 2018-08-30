package com.pinche;

import lombok.Data;

import java.util.Date;

/**
 * @author yangnx
 * @date 2018/6/13
 */
@Data
public class PincheInfo {
	// 昵称
	private String nickName;
	// 聊天时间
	private Date msgDate;
	// 车类型：私家车
	private String carType;
	// 方式：车找人、人找车
	private String msgType;
	// 发车日期
	private Date startDate;
	// 发车时段：上午 中午 下午
	private String startInDay;
	// 发车地点：北京顺义/后沙峪地铁站
	private String startLocation;
	// 目的地点：柏乡，隆尧固城附近
	private String endLocation;
	// 电话
	private String phone;
	// 备注
	private String carRemark;
	// 原文
	private String oriMsg;
	// 是否过期
	private int isValid;
}
