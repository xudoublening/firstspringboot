package com.example.firstspringboot.xninyTest.MQ.RabbitMQ.Topic;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @program: firstspringboot
 * @description:
 * @author: XNN
 * @create: 2020-06-02 17:17
 **/
public class Consumer {

    private static final String EXCHANGE_NAME = "topic_logs";

    static {
        try {
//            test1("topic-xxxx *.log.*","*.log.*");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] argv) throws Exception {

        test1("topic-1 ",null);
        test1("topic-2 *.*.sleep","*.*.sleep");
        test1("topic-3 dog.#","dog.#");
        test1("topic-4 *.log.*","*.log.*");

    }

    public static void test1(final String n, String topicStr) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        /*factory.setHost("47.114.49.155");
//        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("hxlx");
        factory.setPassword("hxlx123");
        factory.setVirtualHost("vhost_test");*/

        factory.setHost("117.78.48.183");
        factory.setPort(5672);
        factory.setUsername("hxlx");
        factory.setPassword("hxlx123");
        factory.setVirtualHost("hx_vhost");

        /*factory.setHost("117.78.39.115");
        factory.setPort(5672);
        factory.setUsername("huaxiang10036");
        factory.setPassword("HX_rabbitmq619");
        factory.setVirtualHost("huaxiang10036");*/

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 交换类型：direct（直接）, topic（话题）, headers and fanout(扇出)
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();
//        String queueName = "TEST_QUEUE"+n;


        if (topicStr == null || topicStr.isEmpty()) {
            System.err.println("Usage: 没有添加主题（话题） 结束");
            if (channel.isOpen()){
                channel.close();
            }
            if (connection.isOpen()){
                connection.close();
            }
            return;
        }

        // 交换机和队列绑定 添加绑定键 severity(话题)
        channel.queueBind(queueName, EXCHANGE_NAME, topicStr);
        channel.queueDeclare(queueName,true,false,false,null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [" + n + "] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName,true,consumer);
    }
}
