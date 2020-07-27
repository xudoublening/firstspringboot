package com.example.firstspringboot.xninyTest;

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
    private static final String WECHAT_MESSAGE_QUEUE = "WECHAT_MESSAGE_QUEUE";

    public static void main(String[] args) {

        startConsumer("000000");
        startConsumer("111111");
        startProduct();
    }

    private static void startProduct(){
        System.out.println("===开启消息发送===");
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
        new Thread(new ProductThread(1)).start();
    }

    private static void startConsumer(String name){
        System.out.println("===开启消息接收===");

        try {

            MyConnection.MyChannel myChannel = null;
            while (myChannel == null){
                myChannel = MyConnection.getMyConnection().getIdleChannel();
            }
            Channel channel = myChannel.getChannel();

//            // 交换类型：direct（直接）, topic（话题）, headers and fanout(扇出)
////            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
////            String queueName = channel.queueDeclare().getQueue();
////
////            // 交换机和队列绑定 添加绑定键 severity(话题)
////            channel.queueBind(queueName, EXCHANGE_NAME, routKey);

            // 交换类型：direct（直接）, topic（话题）, headers and fanout(扇出)
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            // 声明"WECHAT_MESSAGE_QUEUE"队列, 声明为持久序列（队列将在服务器重启后保留下来）
            channel.queueDeclare(WECHAT_MESSAGE_QUEUE,true,false,false,null);
            // 一次仅接受一条肯定确认的消息（请视为大约）
            channel.basicQos(1);

            // 交换机和队列绑定 添加绑定键 severity(话题)
            channel.queueBind(WECHAT_MESSAGE_QUEUE, EXCHANGE_NAME, routKey);

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("消费者【"+name+"】处理消息===>"+message);
                }
            };
            channel.basicConsume(WECHAT_MESSAGE_QUEUE, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
