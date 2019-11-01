package com.example.firstspringboot.xninyUtils;

import com.alibaba.fastjson.JSONArray;

/**
 * Created by Xnn on 2019/9/11 10:27
 */
public class ObjectUtil {
    
    public static void testOne(){
        
        String jsonStr = "[{\"time\":\"201907\",\"value\":35.83},{\"time\":\"201908\",\"value\":25.83},{\"time\":\"201906\",\"value\":45.83}]";
    
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        
        Object o = jsonArray.get(0);
        
    }
    
}
