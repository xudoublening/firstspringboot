package com.example.firstspringboot.xninyTest;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @program: firstspringboot
 * @description:
 * @author: XNN
 * @create: 2020-06-11 14:00
 **/
public class ProductThread implements Runnable {

    private long sleep;

    public ProductThread(long sleep) {
        this.sleep = sleep;
    }

    @Override
    public void run() {

        System.out.println("发送线程开始》》》》》》");
//        String[] d = {"wechat.template.directSend","{\"touser\":\"oU8Ki0o6x2U6qczi0kf7wcR18rJ8\",\"template_id\":\"SuZxA4GfVYZQFT6mFq3ackiYIC6xGxlwn4QtIIohSjU\",\"url\":\"http://weixin.qq.com\",\"data\":{\"first\":{\"value\": \"小主，您的套餐情况出炉啦！\",\"color\":\"#173177\"},\"keyword1\":{\"value\":\"${PHONE}\",\"color\":\"#173177\"},\"keyword2\":{\"value\":\"${NOW_TIME}\",\"color\":\"#173177\"},\"keyword3\":{\"value\": \"${PRODUCT_NAME}\",\"color\":\"#173177\"},\"remark\":{\"value\": \"感谢您的使用！\",\"color\": \"#173177\"}}}"};

        for (int i=0;i<200;i++){
            try {
                String wy = Thread.currentThread().getName()+"-data:"+i;
                String[] d = {"wechat.template.directSend",wy};
                test1("topic-test-4 ", d,i*sleep*10);
                // 不同的休眠时间 测试通道占用
                Thread.sleep(sleep);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void test1(String n,String[] a,long sleep) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.114.49.155");
//        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("hxlx");
        factory.setPassword("hxlx123");
        factory.setVirtualHost("vhost_test");

        MyConnection.MyChannel myChannel = null;
        while (myChannel == null){
            myChannel = MyConnection.getMyConnection().getIdleChannel();
        }
        System.out.println("使用通道："+myChannel.getKey());
        Channel channel = myChannel.getChannel();

        // 将消息发布到交换器，而不是无名的消息交换器。话题交换
        channel.exchangeDeclare("topic_logs","topic");

        String routKey = getRouting(a);   //绑定键
        String message = getMessage(a);

        channel.basicPublish("topic_logs", routKey, null, message.getBytes("UTF-8"));
        System.out.println(" [" + n + "] Sent '" + message + "'");

        System.out.println("等待 "+sleep+"ms后释放通道");
        // 释放通道
        MyConnection.getMyConnection().releaseChannel(myChannel);
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
