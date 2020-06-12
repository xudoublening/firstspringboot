package com.example.firstspringboot.xninyTest.MQ.RabbitMQ.PushSubs;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @program: firstspringboot
 * @description:
 * @author: XNN
 * @create: 2020-06-02 13:57
 **/
public class ReceiveLogs {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();

        //已经创建了一个扇出交换和一个队列。现在我们需要告诉交换机将消息发送到我们的队列。
        // 交换和队列之间的关系称为绑定
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                   byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName,true,consumer);
    }
}
