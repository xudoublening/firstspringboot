package com.example.firstspringboot.xninyTest.MQ.RabbitMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @program: firstspringboot
 * @description: 生产者
 * @author: XNN
 * @create: 2020-06-02 15:59
 **/
public class ProducterDemo {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {

        String[] a = {"dog.love.dontSend","dog.love.dontSend 不可能"};
        test1("topic-test-1 ", a);

        // [0]话题，[1]...[n]=message
        String[] b = {"dog.sleep","dog.sleep","狗","睡"};
        test1("topic-test-2 ", b);

        String[] c = {"WeChat.log.cat","dog.log.cat","狗子","猫"};
        test1("topic-test-3 ", c);

        String[] d = {"dog.log.sleep","dog.log.sleep","狗子","睡"};
        test1("topic-test-4 ", d);

        test1("topic-test-5 ", new String[]{});

    }

    private static void test1(String n,String[] a) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 将消息发布到交换器，而不是无名的消息交换器。话题交换
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");

        String routKey = getRouting(a);   //绑定键
        String message = n + getMessage(a);

        channel.basicPublish(EXCHANGE_NAME, routKey, null, message.getBytes("UTF-8"));
        System.out.println(" [" + n + "] Sent '" + message + "'");
    }

    private static String getRouting(String[] strings) {
        if (strings.length < 1)
            return "cat.green.sleep";
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
        if (length < startIndex) return "";
        StringBuilder words = new StringBuilder(strings[startIndex]);
        for (int i = startIndex + 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
