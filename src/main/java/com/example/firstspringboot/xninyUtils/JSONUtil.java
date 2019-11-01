package com.example.firstspringboot.xninyUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 功能描述: JSON相关处理工具类
 * @author: xnn
 */
public class JSONUtil {
    
    static class JSONCompare implements Comparator<Object>{
        
        private String compareKey;
        private Boolean asc = false;
    
        public JSONCompare(String compareKey) {
        
            this.compareKey = compareKey;
        }
    
        public JSONCompare(String compareKey, Boolean asc) {
        
            this.compareKey = compareKey;
            this.asc = asc;
        }
    
        public String getCompareKey() {
        
            return compareKey;
        }
    
        public void setCompareKey(String compareKey) {
        
            this.compareKey = compareKey;
        }
    
        public Boolean getAsc() {
        
            return asc;
        }
    
        public void setAsc(Boolean asc) {
        
            this.asc = asc;
        }
    
        @Override
        public int compare(Object o1, Object o2) {
            Map<Object,Object> map1 = (Map<Object, Object>) o1;
            Map<Object,Object> map2 = (Map<Object, Object>) o2;
            if (map1.containsKey(compareKey) && map2.containsKey(compareKey)){
                String valA = map1.get(compareKey)+"";
                String valB = map2.get(compareKey)+"";
                if (asc)
                    return valA.compareTo(valB);
                return -valA.compareTo(valB);
            }
            return 0;
        }
    }
    
    public static JSONArray testOneData(){
        
        JSONObject jsonObject1 = new JSONObject(true);
        jsonObject1.put("time","201907");
        jsonObject1.put("value",35.83);
        jsonObject1.put("name","张三");
        JSONObject jsonObject2 = new JSONObject(true);
        jsonObject2.put("time","201908");
        jsonObject2.put("value",25.83);
        jsonObject2.put("name","李四");
        JSONObject jsonObject3 = new JSONObject(true);
        jsonObject3.put("time","201906");
        jsonObject3.put("value",45.83);
        jsonObject3.put("name","王五");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);
        jsonArray.add(jsonObject3);
        return jsonArray;
    }
    
    public static void testOneSortByKey() throws Exception{
    
        JSONArray jsonArray = testOneData();
        List<Object> list = jsonArray.subList(0,jsonArray.size());
        // System.out.println("list :"+JSONObject.toJSONString(list));
        // Comparator comparator = new JSONCompare("time",true);
        // System.out.println("jsonArray排序前:"+JSONObject.toJSONString(jsonArray));
        // jsonArray.sort(comparator);
        // System.out.println("jsonArray排序后:"+JSONObject.toJSONString(jsonArray));
        
        Comparator comparator2 = new JSONCompare("time",true);
        System.out.println("list排序前:"+JSONObject.toJSONString(list));
        Collections.sort(list,comparator2);
        System.out.println("list排序前:"+JSONObject.toJSONString(list));
        
        List<Object> objects = new ArrayList<>();
        System.out.println(objects.isEmpty());
        
    }
    
    public static void main(String[] args) {
        try{
            
            testOneSortByKey();
            
            
        }catch (Exception e){
        
        }
        
    }
}
