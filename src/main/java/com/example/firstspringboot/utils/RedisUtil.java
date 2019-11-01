package com.example.firstspringboot.utils;

import org.springframework.validation.BindingResult;
import redis.clients.jedis.Jedis;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisUtil {

    private static final String host = "127.0.0.1";
    private static Jedis jedis;

    public static Jedis getRedis() throws Exception{
        if (jedis == null){
            jedis = new Jedis(host);
        }
        return jedis;
    }

    /**
     * 获取某个key缓存的指定条数
     * @param key
     * @param num 全部-1
     * @return
     * @throws Exception
     */
    public static List<String> getRedisListByKey(String key, Long num) throws Exception{
        List<String> list = null;
        list = getRedis().lrange(key,0,num);
        getRedis().close();
        return  list;
    }
    public static Map<String, String> getMapByKey(String key) throws Exception{
        Map<String,String> map = new HashMap<>();
        map = getRedis().hgetAll(key);
        getRedis().close();
        return  map;
    }
}
