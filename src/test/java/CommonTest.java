import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multiset;
import com.google.common.collect.MutableClassToInstanceMap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Table;
import com.google.gson.JsonObject;
import com.sun.istack.internal.NotNull;
import glodon.gcj.member.center.utils.pojo.CstmSyncBehaviourDayLog;
import glodon.gcj.member.center.utils.pojo.CstmSyncBehaviourLog;
import glodon.gcj.member.center.utils.pojo.HttpResultVO;
import glodon.gcj.member.center.utils.util.HttpSendWrapperUtil;
import glodon.gcj.member.center.utils.util.OperateLogUtils;
import glodon.gcj.member.center.utils.util.OperateSyncLogUtils;
import glodon.gcj.member.center.utils.util.commons.lang3.EnhancedStringUtils;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import glodon.gcj.member.center.utils.util.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.apache.commons.lang3.StringUtils.substring;

/**
 * @author yangnx
 * @date 2018/2/24
 */
public class CommonTest {

	@Test
	public void test1() {
		String table = "DWS_HW_TABLE";
		String key = "test";
		int time = 10;

		RedisUtil.setValue(table, key, "something");

//		RedisUtil.sAdd(table, key, "111", 10);
//		RedisUtil.sAdd(table, key, "222", 10);
//		System.out.println(RedisUtil.initPool().getResource().srem(table + ":" + key, "111"));

		boolean success = true;
		JedisPool pool = RedisUtil.initPool();
		Jedis jedis = null;

		byte result;
		try {
			jedis = pool.getResource();
			System.out.println(jedis.llen(table));
			System.out.println(jedis.llen(table + ":" + key));
//
//
//			if (jedis.expire(table + ":" + key, time) != -1L) {
//				result = 1;
//			} else {
//				result = 0;
//			}
		} catch (JedisException var11) {
			success = false;
			if (jedis != null) {
				pool.returnBrokenResource(jedis);
			}

			throw var11;
		} finally {
			if (success && jedis != null) {
				pool.returnResource(jedis);
			}

		}

	}

	@Test
	public void test3() {
		CstmSyncBehaviourLog log = new CstmSyncBehaviourLog();
		log.setType(1111);
		log.setRemark("1111");
		OperateLogUtils.updateSubLog(OperateLogUtils.insertSubLog(log), ((byte) 2));

//		CstmSyncBehaviourDayLog dayLog = new CstmSyncBehaviourDayLog(1111, 20180101, ((byte) 1));
//		OperateLogUtils.updateDayLog(OperateLogUtils.insertDayLog(dayLog), ((byte) 3));
//		OperateLogUtils.updateDayLog(333741, ((byte) 1));

//		System.out.println(System.getProperty("user.name"));
	}

	@Test
	public void test5() {
		Calendar cl = Calendar.getInstance();
		int day = cl.get(Calendar.DATE);
		cl.set(Calendar.DATE, day - 1);
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy/MM/dd");
		String ctime = formatter.format(cl.getTime());
		System.out.println(ctime);
	}

	@Test
	public void test2() throws Exception {
		FileInputStream fileInputStream = FileUtils.openInputStream(new File("E:\\test\\gczs\\behaviorlog\\20180417_Kafka01_38843.log"));
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

		File newFile = new File("E:\\test\\gczs\\behaviorlog\\20180417_Kafka01_3test3.log");

		int count = 0;
		String line;
		while ((line = bufferedReader.readLine()) != null && count < 100) {
			FileUtils.write(newFile, line + System.getProperty("line.separator"), "utf-8", true);
			count++;
		}

	}


}
