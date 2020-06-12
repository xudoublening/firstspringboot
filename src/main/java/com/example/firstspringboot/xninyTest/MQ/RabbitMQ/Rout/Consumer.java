package com.example.firstspringboot.xninyTest.MQ.RabbitMQ.Rout;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @program: firstspringboot
 * @description:
 * @author: XNN
 * @create: 2020-06-02 16:33
 **/
public class Consumer {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {

        String[] a = {};
        test1("test-1",a);
        String[] b = {"info"};
        test1("test-2",b);
        String[] c = {"info","error","waring"};
        test1("test-3",c);

    }

    public static void test1(String n,String[] a) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        if (a.length < 1) {
            System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
            return;
        }

        for (String severity : a) {
            //已经创建了一个扇出交换和一个队列。现在我们需要告诉交换机将消息发送到我们的队列。
            // 交换和队列之间的关系称为绑定
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }

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
