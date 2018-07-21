package com.hitler.payservice.platform.yfb;

/**
 * Created by thinkpad on 2015/3/28.
 */
public class YfbKeyValue {
    private String key;
    private String val;

    public YfbKeyValue(String k, String v)
    {
        this.key = k;
        this.val = v;
    }

    public int compare(YfbKeyValue other)
    {
        return key.compareTo(other.key);
    }

    public String getKey() {
        return key;
    }

    public String getVal() {
        return val;
    }
}
