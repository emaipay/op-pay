package com.hitler.core.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class BillNoUtils {
	
	public static final Map<String, String> SEARCH_URLS = new HashMap<String, String>();
	
	/**
	 * 投注记录
	 */
	public static final String PREFIX_TZ = "TZ";
	static { SEARCH_URLS.put(PREFIX_TZ, "/bet-record?billNo={billNo}"); }
	
	/**
	 *投注批处理单号 
	 *the batch
	 */
	public static final String PREFIX_BATCH = "BATCH";
	
	/**
	 * 追号记录
	 */
	public static final String PREFIX_ZH = "ZH";
	static { SEARCH_URLS.put(PREFIX_ZH, "/chase-record?billNo={billNo}"); }
	/**
	 * 返点记录
	 */
	public static final String PREFIX_FD = "FD";
	static { SEARCH_URLS.put(PREFIX_FD, "/rebate-record?billNo={billNo}"); }
	/**
	 * 充值记录
	 */
	public static final String PREFIX_CZ = "CZ";
	static { SEARCH_URLS.put(PREFIX_CZ, "/recharge-record?billNo={billNo}&beginCreateDate={beginTime}&endCreateDate={endTime}"); }
	/**
	 * 人工充值记录
	 */
	public static final String PREFIX_MRC = "MRC";
	static { SEARCH_URLS.put(PREFIX_MRC, "/manager-recharge-record?billNo={billNo}&beginCreateDate={beginTime}&endCreateDate={endTime}"); }
	/**
	 * 人工扣款记录
	 */
	public static final String PREFIX_MRD = "MRD";
	static { SEARCH_URLS.put(PREFIX_MRD, "/manager-reduce-record?billNo={billNo}&beginCreateDate={beginTime}&endCreateDate={endTime}"); }
	/**
	 * 提款记录
	 */
	public static final String PREFIX_TK = "TK";
	static { SEARCH_URLS.put(PREFIX_TK, "/withdraw-record?billNo={billNo}&beginCreateDate={beginTime}&endCreateDate={endTime}"); }
	/**
	 * 转账记录
	 */
	public static final String PREFIX_ZZ = "ZZ";
	static { SEARCH_URLS.put(PREFIX_ZZ, "/transfer-record?billNo={billNo}&beginCreateDate={beginTime}&endCreateDate={endTime}"); }
	/**
	 * 额度转换
	 */
	public static final String PREFIX_BE = "BE";
	static { SEARCH_URLS.put(PREFIX_BE, "/balance-exchange-record?billNo={billNo}&beginCreateDate={beginTime}&endCreateDate={endTime}"); }
	/**
	 * 额度增减
	 */
	public static final String PREFIX_BC = "BC";
	static { SEARCH_URLS.put(PREFIX_BC, "/balance-change-record?billNo={billNo}&beginCreateDate={beginTime}&endCreateDate={endTime}"); }
	/**
	 * 活动礼金
	 */
	public static final String PREFIX_LJ = "LJ";
	static { SEARCH_URLS.put(PREFIX_LJ, "/activity-prize-record?billNo={billNo}&beginCreateDate={beginTime}&endCreateDate={endTime}"); }
	/**
	 * 分红记录
	 */
	public static final String PREFIX_FH = "FH";
	static { SEARCH_URLS.put(PREFIX_FH, "/dividend-record?billNo={billNo}"); }
	/**
	 * AG投注
	 */
	public static final String PREFIX_AT = "AT";
	static { SEARCH_URLS.put(PREFIX_AT, "/ag-bet-record?billNo={billNo}"); }
	/**
	 * BB投注
	 */
	public static final String PREFIX_BT = "BT";
	static { SEARCH_URLS.put(PREFIX_BT, "/bb-bet-record?billNo={billNo}"); }
	/**
	 * 在线支付充值订单
	 */
	public static final String PREFIX_PR = "PR";
	static { SEARCH_URLS.put(PREFIX_PR, "/payment-recharge-order?billNo={billNo}&beginCreateDate={beginTime}&endCreateDate={endTime}"); }
	/**
	 * 网银转账充值订单
	 */
	public static final String PREFIX_BR = "BR";
	static { SEARCH_URLS.put(PREFIX_BR, "/bank-recharge-order?billNo={billNo}&beginCreateDate={beginTime}&endCreateDate={endTime}"); }

	/**
	 * 试玩会员自动生成账变明细的订单前缀
	 */
	public static final String PREFIX_TP= "TP";

	public static String generateBillNo(String prefix) {
		return prefix + DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(3);
	}
	public static String generateBatchNo(String prefix) {
		return prefix + DateFormatUtils.format(Calendar.getInstance(), "yyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(3);
	}
	
	public static String getSearchURL(String billNo) {
		if (billNo.length() == 24) { // 符合单号长度
			String prefix = billNo.substring(0, 2); // 单号前缀
			String no = billNo.substring(2, 24);
			String url = SEARCH_URLS.get(prefix);
			if (StringUtils.isNotBlank(url) && StringUtils.isNumeral(no)) { // 符合前缀且后缀是纯数字
				String date = no.substring(0, 4)+"-"+no.substring(4, 6)+"-"+no.substring(6, 8);
				String time = date + " " + no.substring(8, 10)+":"+no.substring(10, 12)+":"+no.substring(12, 14);
				url = url.replace("{billNo}", billNo).replace("{beginTime}", time).replace("{endTime}", time);
				return url;
			}
		}
		return null;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		for (int i=1; i<=1440; i++) {
			System.out.println("maps.put(\"20141109-"+i+"\", \""+sr.nextInt(10)+" "+sr.nextInt(10)+" "+sr.nextInt(10)+" "+sr.nextInt(10)+" "+sr.nextInt(10)+"\");");
		}
		
		System.out.println(LocalDate.now());
		Instant in = new Instant(1414508801016L);
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		formatter=formatter.withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone("GMT+8")));
		
		in = in.plus(100);
		
		System.out.println(in.get(DateTimeFieldType.millisOfSecond()));
		
		System.out.println(in.toDate());
		System.out.println(formatter.print(in));
		System.out.println(in.getMillis());
		Pattern pattern = Pattern.compile("\"phase\":\"20141018023\"(.*)\"data\":\\[\"(\\d)\",\"(\\d)\",\"(\\d)\",\"(\\d)\",\"(\\d)\"\\]\\}\\]\\}");
		Matcher matcher = pattern.matcher("{\"code\":0,\"message\":\"\",\"data\":[{\"phasetype\":200,\"phase\":\"20141018023\",\"create_at\":\"2014-01-21 14:41:05\",\"time_startsale\":\"2014-10-18 01:50:00\",\"time_endsale\":\"2014-10-18 01:55:00\",\"time_endticket\":\"2014-10-18 01:55:00\",\"time_draw\":\"2014-10-18 01:56:00\",\"status\":5,\"forsale\":0,\"is_current\":0,\"result\":{\"result\":[{\"key\":\"ball\",\"data\":[\"1\",\"5\",\"0\",\"5\",\"9\"]}]},\"result_detail\":{\"resultDetail\":[{\"key\":\"prize1\",\"bet\":\"0\",\"prize\":100000},{\"key\":\"prize2\",\"bet\":\"0\",\"prize\":20000},{\"key\":\"prize3\",\"bet\":\"0\",\"prize\":200},{\"key\":\"prize4\",\"bet\":\"0\",\"prize\":20},{\"key\":\"prize5\",\"bet\":\"0\",\"prize\":1000},{\"key\":\"prize6\",\"bet\":\"0\",\"prize\":320},{\"key\":\"prize7\",\"bet\":\"0\",\"prize\":160},{\"key\":\"prize8\",\"bet\":\"0\",\"prize\":100},{\"key\":\"prize9\",\"bet\":\"0\",\"prize\":50},{\"key\":\"prize10\",\"bet\":\"0\",\"prize\":10},{\"key\":\"prize11\",\"bet\":\"0\",\"prize\":4}]},\"pool_amount\":\"\",\"sale_amount\":\"\",\"ext\":\"\",\"fc3d_sjh\":null,\"terminal_status\":2,\"fordraw\":0,\"time_startsale_fixed\":\"2014-10-18 01:47:40\",\"time_endsale_fixed\":\"2014-10-18 01:52:40\",\"time_endsale_syndicate_fixed\":\"2014-10-18 01:55:00\",\"time_endsale_upload_fixed\":\"2014-10-18 01:55:00\",\"time_draw_fixed\":\"2014-10-18 01:56:00\",\"time_startsale_correction\":140,\"time_endsale_correction\":140,\"time_endsale_syndicate_correction\":0,\"time_endsale_upload_correction\":0,\"time_draw_correction\":0,\"time_exchange\":\"2014-12-16 01:56:00\"},{\"phasetype\":\"200\",\"phase\":\"20141018024\",\"create_at\":\"2014-01-21 14:41:05\",\"time_startsale\":\"2014-10-18 01:55:00\",\"time_endsale\":\"2014-10-18 10:00:00\",\"time_endticket\":\"2014-10-18 10:00:00\",\"time_draw\":\"2014-10-18 10:01:00\",\"status\":\"2\",\"forsale\":\"1\",\"is_current\":\"1\",\"result\":null,\"result_detail\":null,\"pool_amount\":\"\",\"sale_amount\":\"\",\"ext\":\"\",\"fc3d_sjh\":null,\"terminal_status\":\"1\",\"fordraw\":\"0\",\"time_startsale_fixed\":\"2014-10-18 01:52:40\",\"time_endsale_fixed\":\"2014-10-18 09:57:40\",\"time_endsale_syndicate_fixed\":\"2014-10-18 10:00:00\",\"time_endsale_upload_fixed\":\"2014-10-18 10:00:00\",\"time_draw_fixed\":\"2014-10-18 10:01:00\",\"time_startsale_correction\":140,\"time_endsale_correction\":140,\"time_endsale_syndicate_correction\":0,\"time_endsale_upload_correction\":0,\"time_draw_correction\":0,\"time_exchange\":\"2014-12-16 10:01:00\"}],\"redirect\":\"\",\"datetime\":\"2014-10-18 04:08:45\",\"timestamp\":1413576525}");
		
		//Pattern pattern = Pattern.compile("(.*)message(\\d\\d)(\\d)(\\d)(\\d)");
		//Matcher matcher = pattern.matcher("23fawef_message12345");
		//Pattern pattern = Pattern.compile("\"number\":\"(\\d) (\\d) (\\d) (\\d) (\\d)\",\"period\":\"20141017083");
		//Matcher matcher = pattern.matcher("{\"latestPeriods\":[{\"number\":\"6 0 2 2 1\",\"period\":\"20141017084\"},{\"number\":\"0 8 9 1 9\",\"period\":\"20141017083\"},{\"number\":\"4 0 4 4 6\",\"period\":\"20141017082\"},{\"number\":\"4 5 8 7 7\",\"period\":\"20141017081\"},{\"number\":\"7 2 8 5 3\",\"period\":\"20141017080\"},{\"number\":\"9 7 3 8 0\",\"period\":\"20141017079\"},{\"number\":\"3 7 6 0 1\",\"period\":\"20141017078\"},{\"number\":\"9 6 4 8 5\",\"period\":\"20141017077\"},{\"number\":\"6 4 1 8 1\",\"period\":\"20141017076\"},{\"number\":\"9 5 2 8 7\",\"period\":\"20141017075\"}],\"successful\":\"true\",\"statusDesc\":\"获取数据成功\"}");
		
		matcher.find();
		for (int i=1; i<=matcher.groupCount(); i++) {
			
				System.out.println(matcher.group(i));
			
		}
	}
}
