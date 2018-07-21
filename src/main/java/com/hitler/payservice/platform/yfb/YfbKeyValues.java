package com.hitler.payservice.platform.yfb;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.hitler.core.utils.StringUtils;

/**
 * Created by thinkpad on 2015/3/28.
 */
public class YfbKeyValues {

    private List<YfbKeyValue> keyValues = new LinkedList<YfbKeyValue>();

    public List<YfbKeyValue> items()
    {
        return keyValues;
    }

    public void add(YfbKeyValue kv)
    {
        if (kv != null && !StringUtils.isEmpty(kv.getVal()))
            keyValues.add(kv);
    }

    public String sign(String key, String inputCharset)
    {
        StringBuilder sb = new StringBuilder();
        Collections.sort(keyValues, new Comparator<YfbKeyValue>(){
            public int compare(YfbKeyValue l, YfbKeyValue r) {
                return  l.compare(r);
            }
        });
        for (YfbKeyValue kv : keyValues)
        {
            YfbURLUtils.appendParam(sb, kv.getKey(), kv.getVal());
        }
        YfbURLUtils.appendParam(sb, YfbAppConstants.KEY, key);
        String s = sb.toString();
        s = s.substring(1, s.length());
        return YfbMD5Encoder.encode(s, inputCharset);
    }
}
