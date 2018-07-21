package com.hitler.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hitler.core.web.view.MappingJsonView;

/**
 * Created by yang on 2017/3/20.
 * @version 1.0
 * @description java校验器：只用于web层做检验
 */

public class ValidUtil {
    private static final Logger log = LoggerFactory.getLogger(MappingJsonView.class);

    /**
     * 返回表单绑定数据异常提示：返回所有错误校验信息
     * @param object
     * @return
     */
    public static Map<String, Object> bindMsgAll(Object object){
        Map<String, Object> map = Maps.newHashMap();
        if(object instanceof BindingResult){
            BindingResult br  = (BindingResult)object;
            Set<Map<String, Object>> errorList = Sets.newHashSet();
            List<FieldError> errors = br.getFieldErrors();
            for (FieldError error : errors) {
                Map<String, Object> attr = new HashMap<String, Object>();
                attr.put("field", error.getField());
                attr.put("message", error.getDefaultMessage() != null ? error.getDefaultMessage() : "not DefaultMessage!");
                errorList.add(attr);
            }
            map.put("errors", errorList);
            log.info("返回表单数据绑定异常提示:" + errorList);
        }
        return map;
    }

    /**
     * 返回表单绑定数据异常提示：返回其中一个错误校验信息
     * @param object
     * @return
     */
    public static String bindMsgOne(Object object){
        if(object instanceof BindingResult){
            BindingResult br  = (BindingResult)object;
            List<FieldError> errors = br.getFieldErrors();
            String errorMsg=errors.get(0).getDefaultMessage() != null ? errors.get(0).getDefaultMessage() : "not DefaultMessage!";
            log.info("返回表单数据绑定异常提示:" + errorMsg);
            return errorMsg;
        }
        return null;
    }


}