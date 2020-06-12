package com.example.firstspringboot.xninyTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @program: firstspringboot
 * @description:
 * @author: XNN
 * @create: 2020-06-09 17:36
 **/
public class Test {

    private static final String EXCHANGE_NAME = "topic_logs";
    private static final String routKey = "wechat.template.*";

    public static void main(String[] args) {

        startConsumer();
        startProduct();
    }

    private static void startProduct(){
        System.out.println("===开启消息发送===");
        new Thread(new ProductThread(10)).start();
        new Thread(new ProductThread(20)).start();
        new Thread(new ProductThread(30)).start();
        new Thread(new ProductThread(40)).start();
        new Thread(new ProductThread(5)).start();
        new Thread(new ProductThread(6)).start();
        new Thread(new ProductThread(7)).start();
        new Thread(new ProductThread(8)).start();
        new Thread(new ProductThread(10)).start();
        new Thread(new ProductThread(20)).start();
        new Thread(new ProductThread(30)).start();
        new Thread(new ProductThread(40)).start();
        new Thread(new ProductThread(5)).start();
        new Thread(new ProductThread(6)).start();
        new Thread(new ProductThread(7)).start();
        new Thread(new ProductThread(8)).start();
    }

    private static void startConsumer(){
        System.out.println("===开启消息接收===");

        try {

            MyConnection.MyChannel myChannel = null;
            while (myChannel == null){
                myChannel = MyConnection.getMyConnection().getIdleChannel();
            }
            Channel channel = myChannel.getChannel();

            // 交换类型：direct（直接）, topic（话题）, headers and fanout(扇出)
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            String queueName = channel.queueDeclare().getQueue();

            // 交换机和队列绑定 添加绑定键 severity(话题)
            channel.queueBind(queueName, EXCHANGE_NAME, routKey);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("微信  交换器["+ EXCHANGE_NAME +"]-绑定键["+ routKey +"] 接收到：" + JSONObject.toJSONString(message));
                }
            };
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
