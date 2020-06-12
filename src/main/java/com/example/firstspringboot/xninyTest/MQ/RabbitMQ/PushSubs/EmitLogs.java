package com.example.firstspringboot.xninyTest.MQ.RabbitMQ.PushSubs;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @program: firstspringboot
 * @description:
 * @author: XNN
 * @create: 2020-06-02 11:28
 **/
public class EmitLogs {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 我们现在希望将消息发布到日志交换器，而不是无名的消息交换器。发送时我们需要提供一个routingKey，但是对于扇出交换，它的值将被忽略
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        StringBuffer sb = new StringBuffer();
        for (String s : argv) {
            sb.append(" ").append(s);
        }
        String message = argv.length < 1 ? "info: Hello World!" : sb.toString();

        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");

    }
}
