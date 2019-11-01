package com.example.firstspringboot;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRedis {

    public static void pushKeyValue(String key, Object value){

    }

    public static void main(String[] args) {

        Jedis jedis = new Jedis("127.0.0.1");
//        jedis.lpush("XNN.TEST.LPUSH","这是一个LPUSH的测试");
//        jedis.lpush("XNN.TEST.LPUSH","这又是一个LPUSH的测试");//头部插入  左
//        jedis.lpush("XNN.TEST.LPUSH","这双是一个LPUSH的测试");
//        jedis.rpush("XNN.TEST.LPUSH","这叒是一个LPUSH的测试");//尾部插入  右
//        List<String> ss = jedis.lrange("XNN.TEST.LPUSH",0,-1);
//        jedis.lpush("INFORMATION.TYPE.NEWS","190429001");
//        jedis.lpush("INFORMATION.TYPE.NEWS","190429002");
//        jedis.lpush("INFORMATION.TYPE.NEWS","190429003");
//
//        Map<String,String> m1 = new HashMap<>();
//        m1.put("title","小米现货开卖拉");
//        m1.put("date","2019年4月20日");
//        jedis.hmset("190429001",m1);
//
//        Map<String,String> m2 = new HashMap<>();
//        m2.put("title","特朗普更新推特");
//        m2.put("date","2019年4月21日");
//        jedis.hmset("190429002",m2);
//
//        Map<String,String> m3 = new HashMap<>();
//        m3.put("title","北京博览会开幕");
//        m3.put("date","2019年4月23日");
//        jedis.hmset("190429003",m3);
    }
}
