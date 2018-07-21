package com.hitler.core.repository;

import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.HibernateException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.StringUtils;

public class DTOTransformer<T> implements ResultTransformer {
    private static final long serialVersionUID = 1197510067471880874L;
    private final Class<T> resultClass;
    private Setter[] setters;
    private PropertyAccessor propertyAccessor;

    public DTOTransformer(Class<T> resultClass) {
        if (resultClass == null)
            throw new IllegalArgumentException("resultClass cannot be null");
        this.resultClass = resultClass;
        propertyAccessor = new ChainedPropertyAccessor(new PropertyAccessor[]{
                PropertyAccessorFactory.getPropertyAccessor(resultClass, null),
                PropertyAccessorFactory.getPropertyAccessor("field")});
    }


    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Object result;
        try {
            if (setters == null) {
                setters = new Setter[aliases.length];
                for (int i = 0; i < aliases.length; i++) {
//                    String alias = convertColumnToProperty(aliases[i]);  
                    String alias = aliases[i];
                    if (alias != null) {
                        try {
                            setters[i] = propertyAccessor.getSetter(
                                    resultClass, alias);
                        } catch (PropertyNotFoundException e) {
                            continue;
                        }
                    }
                }
            }
            result = resultClass.newInstance();
            for (int i = 0; i < aliases.length; i++) {
                if (setters[i] != null && tuple[i] != null) {
                    //Setter方法只有一个参数  
                    Class<?> param = setters[i].getMethod().getParameterTypes()[0];
                    Class<?> tupleClass = tuple[i].getClass();
                    //若目标参数类型和当前参数类型不匹配 ,尝试进行转换  
                    if (!param.equals(tupleClass)) {
                        if (Number.class.isAssignableFrom((tupleClass))) {
                            Number num = (Number) tuple[i];
                            if (Long.class.equals(param)) {
                                setters[i].set(result, num.longValue(), null);
                            } else if (Integer.class.equals(param)) {
                                setters[i].set(result, num.intValue(), null);
                            } else if (Double.class.equals(param)) {
                                setters[i].set(result, num.doubleValue(), null);
                            } else if (Boolean.class.equals(param)) {
                                setters[i].set(result, num.intValue() == 1, null);
                            } else if (Float.class.equals(param)) {
                                setters[i].set(result, num.floatValue(), null);
                                //枚举类型转换
                            } else if (param.isEnum()) {
                                setters[i].set(result, param.getEnumConstants()[num.intValue()], null);
                            } else {
                                setters[i].set(result, tuple[i], null);
                            }
                            //如果tuple为参数的子类,直接设置
                            //如java.util.Date; java.sql.Date;
                        } else if (param.isAssignableFrom(tupleClass)) {
                            setters[i].set(result, tuple[i], null);
                        }
                    } else {
                        setters[i].set(result, tuple[i], null);
                    }

                }
            }
        } catch (InstantiationException e) {
            throw new HibernateException("Could not instantiate resultclass: "
                    + resultClass.getName());
        } catch (IllegalAccessException e) {
            throw new HibernateException("Could not instantiate resultclass: "
                    + resultClass.getName());
        }
        return result;
    }

    /**
     * 对于Oracle返回的列全部大写需要进行转换, 其他数据库可能不需要这么处理
     *
     * @param columnName
     * @return
     */
    public String convertColumnToProperty(String columnName) {
        columnName = columnName.toLowerCase();
        StringBuffer buff = new StringBuffer(columnName.length());
        StringTokenizer st = new StringTokenizer(columnName, "_");
        while (st.hasMoreTokens()) {
            buff.append(StringUtils.capitalize(st.nextToken()));
        }
        buff.setCharAt(0, Character.toLowerCase(buff.charAt(0)));
        return buff.toString();
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    public List transformList(List collection) {
        return (List<T>) collection;
    }

}  
