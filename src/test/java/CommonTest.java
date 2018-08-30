import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.google.gson.JsonParser;
import com.util.ClipboardUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import glodon.gcj.member.center.utils.pojo.CstmSyncBehaviourDayLog;
import glodon.gcj.member.center.utils.util.OperateLogUtils;

import javax.xml.transform.Source;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangnx
 * @date 2018/2/24
 */
public class CommonTest {

	@Test
	public void test3() {
		double growthRate = 0.15;
		double discountRate = 0.09;
		double sustainableGrowthRate = 0.05;

		double baseFund = 8.28;
		int startYear = 1988;
		int endYear = 1998;

		double finalFund = 0;
		for (int i = 0; i < endYear - startYear; i++) {
			finalFund += baseFund * Math.pow((1 + growthRate) / (1 + discountRate), i);
		}

		double tailBaseFund = baseFund * Math.pow((1 + growthRate) / (1 + discountRate), endYear - startYear) * (1 + sustainableGrowthRate);


		finalFund = finalFund + tailBaseFund / (1 - (1 + sustainableGrowthRate) / (1 + discountRate)) / (1 + discountRate);
		System.out.println(finalFund - baseFund);
	}

	@Test
	public void test5() {
//		List<? extends Number> foo1 = new ArrayList<Number>();  // Number "extends" Number
//		List<? extends Number> foo2 = new ArrayList<Integer>(); // Integer extends Number
//		List<? extends Number> foo3 = new ArrayList<Double>();  // Double extends Number
//
//		foo1.add(new Integer(1));

//		System.out.println(DateFormatUtils.format(new Date(1533571200000L), "yyyy-MM-dd"));
//		List<CstmSyncBehaviourDayLog> dayLogList = OperateLogUtils.listDayLogsByStatusList("1___2057", "2018____", "4,-14,-15,-16");
//		dayLogList = dayLogList.stream().sorted((o2, o1) -> {
//			int countDay = o1.getCountDay() - o2.getCountDay();
//			int type = o1.getType() - o2.getType();
//			int status = o1.getStatus() - o2.getStatus();
//			return countDay != 0 ? countDay : (type != 0 ? type : status);
//		}).collect(Collectors.toList());
//		System.out.println();
//		System.out.println(new File("G:\\ceshi2\\ceshi3").getParentFile().getAbsolutePath());
//		FileUtils.deleteQuietly(new File("G:\\test2"));

		String[] paths = "query -> search_condition -> pterms".split("\\s*->\\s*");
		System.out.println(111);

		System.out.println(StringUtils.strip(StringUtils.substring("11000012", 2), "0"));

	}

}

