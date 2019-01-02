import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.obs.services.ObsClient;
import com.obs.services.model.CopyObjectResult;
import com.util.ClipboardUtil;
import glodon.gcj.member.center.utils.pojo.CstmSyncBehaviourDayLog;
import glodon.gcj.member.center.utils.pojo.CstmSyncBehaviourLog;
import glodon.gcj.member.center.utils.util.ObsUtil;
import glodon.gcj.member.center.utils.util.OperateLogUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.helpers.FormattingTuple;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yangnx
 * @date 2018/2/24
 */
public class CommonTest {

	@Test
	public void test1() {
		String month = "20170516_20170615,20170616_20170715,20170716_20170815,20170816_20170915,20170916_20171015,20171016_20171115,20171116_20171215," +
				"20171216_20180115,20180116_20180215,20180216_20180315,20180316_20180415,20180416_20180515,20180516_20180615,20180616_20180715,20180716_20180815," +
				"20180816_20180915,20180916_20181015,20181016_20181115,20181116_20181215";

		String sql = "";
		for (String name : month.split(",")) {
			sql += "alter table behaviour_project_process.fncode107_data_month_"+name+" rename to fncode107_data_month_"+name+"_temp;\n" +
					"\n" +
					"CREATE TABLE behaviour_project_process.fncode107_data_month_"+name+" (\n" +
					"\t\"ymd\" int4,\n" +
					"\t\"group_code\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"random_uuid\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"dognum\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"dognum_original\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"purchase_gcxx\" bool,\n" +
					"\t\"ver\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"computerid\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"admcode\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"createdate\" timestamp(6),\n" +
					"\t\"sysver\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"fncode\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"utype\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"mac\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"pcode\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"disknum\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"admcode2\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"area_code\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"project_type\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"project_spec_id\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"entire_project_name\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"project_name\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"gbq_id\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"clientip\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"type\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"norm_res_budget_price\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"norm_res_quantity\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"norm_res_code\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"norm_res_price\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"norm_res_unit\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"norm_res_name\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"project_identification\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"uid\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"gid\" int8,\n" +
					"\t\"norm_budget_tax_price\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"norm_dbid\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"tax_method\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"norm_res_spec\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"norm_market_tax_price\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"project_address\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"norm_market_tax_exclude_price\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"norm_res_tax_rate\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"norm_budget_tax_exclude_price\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"supply_material_flag\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"price_source\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"keyword\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"logindognum\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"logid\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"res_id\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"query_type\" int4,\n" +
					"\t\"term\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"res_type_name\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"package_type\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"rcjm\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"p_si\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"t_m\" int4,\n" +
					"\t\"p_t_i_d\" int4,\n" +
					"\t\"p_n\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"d_b_m_s\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"is_change_pirce\" bool,\n" +
					"\t\"complete_entire_project_info\" text COLLATE \"pg_catalog\".\"default\",\n" +
					"\t\"data_machine_num\" int4,\n" +
					"\t\"data_created_at\" timestamp(6)\n" +
					")\n" +
					"WITH (ORIENTATION = ROW,COMPRESSION=NO)\n" +
					"DISTRIBUTE BY HASH (createdate);\n" +
					"\n" +
					"CREATE INDEX \"idx_random_uuid_"+name+"\" ON \"behaviour_project_process\".\"fncode107_data_month_"+name+"\" USING btree (\"random_uuid\");\n" +
					"\n" +
					"INSERT INTO behaviour_project_process.fncode107_data_month_"+name+" SELECT * FROM behaviour_project_process.fncode107_data_month_"+name+"_temp;\n" +
					"\n" +
					"drop TABLE behaviour_project_process.fncode107_data_month_"+name+"_temp;";

		}
		ClipboardUtil.setContent(sql);
	}

	@Test
	public void test2() throws IOException {
		String endPoint = "obs.cn-north-1.myhwclouds.com";
		String ak = "6GBKY5WYZZLZTQZSOHR2";
		String sk = "0BRmyWXDdFv0iO7Bs4KYQjST4vrspmJJ0dm3Shr3";
		String bucketName = "dws-behaviour-data-operation";
//
//		Set<String> prefixSet = new HashSet<>();
//		for (String objectKey : ObsUtil.listAllObjectKeys("membercenter/dwsBehaviourData/2018/20181218/12-Kafka01")) {
//			prefixSet.add(StringUtils.substring(objectKey, 0, StringUtils.lastIndexOf(objectKey, "-")));
//		}
//		System.out.println(StringUtils.join(prefixSet, "\n"));

//		ObsUtil.upload(new File("G:/test.mp3"), "membercenter_test/test.mp3");
//		System.out.println(new File("G:/test.mp3").getAbsolutePath());
		System.out.println(System.getProperty("os.name"));
	}

}
