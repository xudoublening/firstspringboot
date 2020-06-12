package com.example.firstspringboot.xninyTest.MQ.RabbitMQ;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

/**
 * @program: firstspringboot
 * @description: 消费者
 * @author: XNN
 * @create: 2020-06-02 16:01
 **/
public class ConsumerDemo {

    private static final String EXCHANGE_NAME = "topic_logs";
    private static final String templateSendUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    private static String accessToken = "33_FlcnMbduzAJPEhqLyaaQN7L-IaApBm-zoKHv75HAgBxGEruPUq6XiW-i17ljvkLqKDuZ0ghfusnRg5Cep-DRZtYqC9Nbn783n2BxfTuGAPzjYJdR-za0xvw4Ey47PhLa443c4sjdoofHObXWMULgABAUCT";

    public static void main(String[] argv) throws Exception {

        if (argv.length>0){
            accessToken = argv[0];
        }
        test1("topic-2 *.*.dontSend","*.*.dontSend");
        test1("topic-3 WeChat.#","WeChat.#");

    }

    public static void test1(final String n, String topicStr) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 交换类型：direct（直接）, topic（话题）, headers and fanout(扇出)
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();

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

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [" + n + "] Received '" + message + "'");
                sendMessage(message);
            }
        };
        channel.basicConsume(queueName,true,consumer);
    }

    public static String sendMessage(String title){
        JSONObject body = new JSONObject();
        body.put("touser","oU8Ki0o6x2U6qczi0kf7wcR18rJ8");
        body.put("template_id","OdLIzT0zn7MNwbvdPe1S8J21N4agnXLvIBhcZ7Mmx5w");
        body.put("url","http://weixin.qq.com");

        JSONObject data = new JSONObject();
        JSONObject first = new JSONObject();
        first.put("value",title);

        JSONObject keyword1 = new JSONObject();
        keyword1.put("value","17809092345");

        JSONObject keyword2 = new JSONObject();
        keyword2.put("value","2014年9月22日");

        JSONObject keyword3 = new JSONObject();
        keyword3.put("value","39.8元");

        JSONObject remark = new JSONObject();
        remark.put("value","请及时充值！");

        data.put("first",first);
        data.put("keyword1",keyword1);
        data.put("keyword2",keyword2);
        data.put("keyword3",keyword3);
        data.put("remark",remark);

        body.put("data",data);
        String response = null;
        try {
            response = sendPost(templateSendUrl+accessToken,body.toJSONString(),null);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String sendPost(String url, String param, Map<String, String> header)
            throws IOException, URISyntaxException {

        System.out.println("url >>> " + url);
        System.out.println("param >>> " + param);
        String charset = "utf-8";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(new URL(url).toURI());
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
                if ("charset".equals(entry.getKey())) {
                    charset = entry.getValue();
                }
            }
        }
        StringEntity dataEntity = new StringEntity(param, ContentType.APPLICATION_JSON);
        httpPost.setEntity(dataEntity);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            // 请求成功且有返回体
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, charset);
            } else {
                System.out.println(
                        "远程调用失败" + response.getStatusLine().getStatusCode() + response.getStatusLine().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
