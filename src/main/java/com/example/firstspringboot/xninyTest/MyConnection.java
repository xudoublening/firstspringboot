package com.example.firstspringboot.xninyTest;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * @program:
 * @description:
 * @author: XNN
 * @create: 2020-06-09 14:47
 **/
public class MyConnection {

    private static Logger log = LoggerFactory.getLogger(MyConnection.class);
    private volatile static MyConnection myConnection;

    private static Connection connection = null;

    private static Map<String, MyChannel> myChannelMap = new HashMap<>();

    private static String host;
    private static int port;
    private static String username;
    private static String password;
    private static String vhost;
    private static int channelCount;
    private static int waitTime;

    private MyConnection() {
        JSONObject params = JSONObject.parseObject("{\"host\":\"47.114.49.155\",\"port\":5672,\"username\":\"hxlx\",\"password\":\"hxlx123\",\"vhost\":\"vhost_test\",\"channelCount\":\"10\",\"waitTime\":\"1000\"}");
        host = params.getString("host");
        port = params.getInteger("port");
        username = params.getString("username");
        password = params.getString("password");
        vhost = params.getString("vhost");
        channelCount = params.getInteger("channelCount");
        waitTime = params.getInteger("waitTime");
    }

    public static MyConnection getMyConnection() {
        if (myConnection == null) {
            synchronized (MyConnection.class) {
                if (myConnection == null) {
                    myConnection = new MyConnection();
                }
            }
        }
        return myConnection;
    }

    private Connection getConnection() {
        if (connection == null || !connection.isOpen()) {
            log.info("connection 连接为空或连接断开，重新创建连接===>>");
            log.info("host：" + host);
            log.info("port：" + port);
            log.info("username：" + username);
            log.info("password：" + password);
            log.info("vhost：" + vhost);
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setPort(port);
            factory.setUsername(username);
            factory.setPassword(password);
            factory.setVirtualHost(vhost);
            try {
                connection = factory.newConnection();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public synchronized MyChannel getIdleChannel() {

        MyChannel myChannel = null;

        try {

            // channel连接数未达到上限创建一个新的
            if (myChannelMap.entrySet().size() < channelCount) {

                log.info("====连接池当前数量：" + myChannelMap.entrySet().size());
                log.info("====连接池上限数量：" + channelCount);

                Channel channel = getMyConnection().getConnection().createChannel();
                String key = UUID.randomUUID().toString();
                // 这个channel创建后就要返回提供使用，不是空闲状态
                myChannel = new MyChannel(false, key, channel);

                // 添加到连接池
                log.info("添加一个连接到连接池，key=" + key);
                myChannelMap.put(key, myChannel);
                return myChannel;
            }

            // channel连接池数量达到上限，则获取一个空闲的channel
            for (Map.Entry<String, MyChannel> channelEntry : myChannelMap.entrySet()) {
                log.info("当前key=" + channelEntry.getValue().getKey() + ",是否空闲：" + channelEntry.getValue().isIdle());
                // 找到一个空闲的channel
                if (channelEntry.getValue().isIdle()) {
                    log.info("找到一个空闲的channel，key=" + channelEntry.getKey());
                    myChannel = channelEntry.getValue();
                    myChannel.setIdle(false);   //赋值为非空闲
                    myChannelMap.put(myChannel.getKey(),myChannel);
                    break;
                }
            }

            // 当前没有空闲的channel，等待继续获取
            if (myChannel == null) {
                log.info("当前没有空闲的channel，等待 "+waitTime+"ms 后继续获取");
                Thread.sleep(waitTime);
            }

        } catch (IOException io) {
            io.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return myChannel;
    }

    /***
     * @Description: 释放通道，设置为空闲状态
     *
     * @Param: [myChannel]
     * @return: void
     * @Author: XNN
     * @Date: 2020/6/11
     */
    public void releaseChannel(MyChannel myChannel) {

        String mapKey = myChannel.getKey();
        log.info("释放通道：" + mapKey);
        myChannel.setIdle(true);
        myChannelMap.put(mapKey, myChannel);

    }

    static class MyChannel {

        private boolean isIdle;
        private String key;
        private Channel channel;

        public MyChannel(boolean isIdle, String key, Channel channel) {
            this.isIdle = isIdle;
            this.key = key;
            this.channel = channel;
        }

        public boolean isIdle() {
            return isIdle;
        }

        public void setIdle(boolean idle) {
            isIdle = idle;
        }

        public Channel getChannel() {
            return channel;
        }

        public void setChannel(Channel channel) {
            this.channel = channel;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

}
