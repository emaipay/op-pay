package com.hitler.payservice.platform.mf;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.hitler.core.utils.StringUtils;

/**
 * Created by thinkpad on 2015/3/28.
 */
public class KeyValues {

    private List<KeyValue> keyValues = new LinkedList<KeyValue>();

    public List<KeyValue> items()
    {
        return keyValues;
    }

    public void add(KeyValue kv)
    {
        if (kv != null && !StringUtils.isEmpty(kv.getVal()))
            keyValues.add(kv);
    }

    public String sign(String key, String inputCharset)
    {
        StringBuilder sb = new StringBuilder();
        Collections.sort(keyValues, new Comparator<KeyValue>(){
            public int compare(KeyValue l, KeyValue r) {
                return  l.compare(r);
            }
        });
        for (KeyValue kv : keyValues)
        {
            URLUtils.appendParam(sb, kv.getKey(), kv.getVal());
        }
        URLUtils.appendParam(sb, "key", key);
        String s = sb.toString();
        s = s.substring(1, s.length());
        return MD5Encoder.encode(s, inputCharset);
    }
}
