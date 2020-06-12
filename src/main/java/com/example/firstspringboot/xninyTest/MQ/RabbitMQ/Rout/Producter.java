package com.example.firstspringboot.xninyTest.MQ.RabbitMQ.Rout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @program: firstspringboot
 * @description:
 * @author: XNN
 * @create: 2020-06-02 16:33
 **/
public class Producter {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {

        String[] a = {};
        test1("test-1 ", a);
        String[] b = {"error","info","waring"};
        test1("test-2 ", b);

    }

    private static void test1(String n,String[] a) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 我们现在希望将消息发布到日志交换器，而不是无名的消息交换器。发送时我们需要提供一个routingKey，但是对于扇出交换，它的值将被忽略
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");

        String severity = getSeverity(a);   //绑定键
        String message = n + getMessage(a);

        channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("UTF-8"));
        System.out.println(" [" + n + "] Sent '" + message + "'");
    }

    private static String getSeverity(String[] strings) {
        if (strings.length < 1)
            return "info";
        return strings[0];
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 2)
            return "Hello World!";
        return joinStrings(strings, " ", 1);
    }

    private static String joinStrings(String[] strings, String delimiter, int startIndex) {
        int length = strings.length;
        if (length == 0) return "";
        if (length <= startIndex) return "";
        StringBuilder words = new StringBuilder(strings[startIndex]);
        for (int i = startIndex + 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
