package com.example.firstspringboot.xninyTest.MQ.RabbitMQ.Topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @program: firstspringboot
 * @description:
 * @author: XNN
 * @create: 2020-06-02 17:17
 **/
public class Producter {

    private static final String EXCHANGE_NAME = "topic_logs";
//    private static final String EXCHANGE_NAME = "MESSAGE_PUSH";

    public static void main(String[] argv) throws Exception {

        String[] a = {"dog.love.cat","dog.love.cat 不可能"};
        test1("topic-test-1 ", a);

        // [0]话题，[1]...[n]=message
        String[] b = {"dog.sleep","dog.sleep","狗","睡"};
        test1("topic-test-2 ", b);

        String[] c = {"dog.log.cat","dog.log.cat","狗子","猫"};
        test1("topic-test-3 ", c);

        String[] d = {"dog.log.sleep","dog.log.sleep","狗子","睡"};
        test1("topic-test-4 ", d);

        test1("topic-test-5 ", new String[]{});

        /*String[] d = {"WECHAT.template.directSend","{\"touser\":\"oU8Ki0o6x2U6qczi0kf7wcR18rJ8\",\"template_id\":\"SuZxA4GfVYZQFT6mFq3ackiYIC6xGxlwn4QtIIohSjU\",\"url\":\"http://weixin.qq.com\",\"data\":{\"first\":{\"value\": \"小主，您的套餐情况出炉啦！\",\"color\":\"#173177\"},\"keyword1\":{\"value\":\"${PHONE}\",\"color\":\"#173177\"},\"keyword2\":{\"value\":\"${NOW_TIME}\",\"color\":\"#173177\"},\"keyword3\":{\"value\": \"${PRODUCT_NAME}\",\"color\":\"#173177\"},\"remark\":{\"value\": \"感谢您的使用！\",\"color\": \"#173177\"}}}"};
        test1("topic-test-4 ", d);*/

    }

    private static void test1(String n,String[] a) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.114.49.155");
//        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("hxlx");
        factory.setPassword("hxlx123");
        factory.setVirtualHost("vhost_test");

        /*factory.setHost("117.78.48.183");
        factory.setPort(5672);
        factory.setUsername("hxlx");
        factory.setPassword("hxlx123");
        factory.setVirtualHost("hx_vhost");*/

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 将消息发布到交换器，而不是无名的消息交换器。话题交换
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");

        String routKey = getRouting(a);   //绑定键
        String message = getMessage(a);

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
