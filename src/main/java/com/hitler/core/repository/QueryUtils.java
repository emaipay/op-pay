package com.hitler.core.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lichengqi on 2017/4/6.
 * Desc
 */
@SuppressWarnings("all")
public class QueryUtils {

    private static final String EQUALS_CONDITION_STRING = " %s %s :%s";

   // private static final String PAGE_CONDITION_STRING = " limit :offset,:size";

    /**
     * @param sql
     * @param list
     * @return
     */
    public static String generateQueryString(String sql, List<SearchFilter> list) {
        StringBuilder sb = new StringBuilder(sql);
        sql = sql.replaceAll("\\(.*\\)\\s+as[\\s+(\\S)\\s+]", "");
        if(sql.matches(".*((W|w)(H|h)(E|e)(R|r)(E|e))+.+")) {
            sb.append(" AND ");
        } else {
            sb.append(" WHERE ");
        }
        if (list != null && !list.isEmpty()) {
            for (SearchFilter field : list) {
                sb.append(String.format(EQUALS_CONDITION_STRING, camelToUnderline(field.getFieldName()), getOptionSymbol(field.getOp()), field.getFieldName()));
                sb.append(" AND ");
            }
        }
        sb.append(" 1 = 1");
        return sb.toString();
    }


    /**
     * @param sql
     * @param list
     * @return
     */
    public static String generateQueryCountString(String sql) {
        StringBuffer sb = new StringBuffer(sql.length() + 20);
        sb.append("select count(1) as c from (").append(sql).append(") t");
        return sb.toString();
    }
    /**
     * @param list
     * @return
     */
    public static Map<String,Object> convertToSqlCommandParam(List<SearchFilter> list) {
        Map<String, Object> map = new HashMap();
        if (list == null || list.isEmpty()) return map;
        for (SearchFilter field : list) {
            Object value = field.getValue();
            if (OP.LIKE.equals(field.getOp())) {
                value = "%" + value + "%";
            } else if (OP.PLIKE.equals(field.getOp())) {
                value = "%" + value;
            } else if (OP.ALIKE.equals(field.getOp())) {
                value = value + "%";
            }
            map.put(field.getFieldName(), value);
        }
        return map;
    }

    /**
     * @param op
     * @return
     */
    private static String getOptionSymbol(OP op) {
        String symbol = "";
        switch (op) {
            case GT:
                symbol = ">";
                break;
            case EQ:
                symbol = "=";
                break;
            case LT:
                symbol = "<";
                break;
            case GTE:
                symbol = ">=";
                break;
            case LTE:
                symbol = "<=";
                break;
            case ALIKE:
                symbol = "LIKE";
                break;
            case LIKE:
                symbol = "LIKE";
                break;
            case PLIKE:
                symbol = "LIKE";
                break;
        }
        return symbol;
    }


    /**
     *
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(Character.toUpperCase(c));
            }
        }
        return sb.toString();
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。
     * @param name
     * @return
     */
    public static String underlineToCamel(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || "".equals(name)) {
            return "";
        } else if (!name.contains("_")) {
            return name;
        }
        String camels[] = name.split("_");
        for (String camel :  camels) {
            if (camel.isEmpty()) {
                continue;
            }
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String sql = "select * from (select * from table where 1=1) as t left join (select * from table where 1=1) as t2 on t1.id = t2.id";
        System.out.println(generateQueryString(sql, null));
    }
}
