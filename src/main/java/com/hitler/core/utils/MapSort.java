package com.hitler.core.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MapSort {
	public static void main(String[] args) {

        Map<String, String> map = new TreeMap<String, String>();
       // 签名串:mch_id=1834126509&money=0.10&order_number=0915165206390616&order_time=1505465526&ret_code=200&transaction_id=661505465526182679&key=f07d6df99d870ba2b2d5d9f2a2fc3373
       //签名后:6114A994096166BF4E6094C672617D37
      //云聚付签名:005CFF334F725A7743F42346F883EB29
      //  mch_id=1834126509&money=0.10&order_number=0915182521066596&order_time=1505471121&other_order_number=101709156651734&ret_code=200&ret_msg=失败&transaction_id=661505471121043189&key=f07d6df99d870ba2b2d5d9f2a2fc3373


        map.put("mch_id", "1834126509");
        map.put("money", "0.10");
        map.put("order_number", "0915182521066596");
        map.put("order_time", "1505471121");
        map.put("other_order_number", "101709156651734");
        map.put("ret_code", "200");
        map.put("transaction_id", "661505471121043189");
        map.put("ret_msg", "成功");

        map = sortMapByKey(map);    //按Key进行排序

        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        StringBuilder sb = new StringBuilder();
		for (String key : map.keySet()) {
			sb.append(key+"="+map.get(key)+"&");
		}
		sb.append("key=f07d6df99d870ba2b2d5d9f2a2fc3373");
		String sign = MD5Utils.getMD5Str(sb.toString());
		sign = sign.toUpperCase();
		System.out.println("我方签名串："+sb.toString());
		System.out.println("我方签名："+sign);
		//云聚付签名:C3E5E22F151FA496F07D5DC564A113A7
		System.out.println("云聚付签名：C3E5E22F151FA496F07D5DC564A113A7");
		
		//[sign] => 0C149DA2174FBE97D139FD5444B35C07
    }
    
    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }
}

class MapKeyComparator implements Comparator<String>{

    @Override
    public int compare(String str1, String str2) {
        
        return str1.compareTo(str2);
    }
}
