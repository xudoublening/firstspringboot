package com.example.firstspringboot.xninyTest.MQ.RabbitMQ.RPCTopic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @program: firstspringboot
 * @description:
 * @author: XNN
 * @create: 2020-06-03 10:21
 **/
public class ClientOne implements AutoCloseable {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_topic_queue";

    public ClientOne() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public static void main(String[] args){
        try(ClientOne clientOne = new ClientOne()){
            String routKey;
            for(int i=1;i<30;i++){
                if (i%2 == 0){
                    routKey = "test.dog.sleep";
                }else{
                    routKey = "cat.red.no";
                }
                System.out.println("[request - " + routKey + "] " + i);
                String response = clientOne.call(Integer.toString(i),routKey);
                System.out.println("[response] " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String call(String message,String routKey) throws IOException,InterruptedException{

        String corrId = UUID.randomUUID().toString();// 生成唯一编号

        /* 声明一个服务器独占，自动删除，非持久队列 */
        String queueName = channel.queueDeclare().getQueue();

        /* 消息其他属性设置 */
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .correlationId(corrId).replyTo(queueName).build();
        /* 声明交换 消息以 topic 类型发布到交换器 */
        channel.exchangeDeclare(requestQueueName,"topic");
        /* 消息发布 routKey-路由绑定键 */
        channel.basicPublish(requestQueueName,routKey,properties,message.getBytes("UTF-8"));

        System.out.println("[消息发布] " + message);
        /* 队列返回结果 */
        BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                       byte[] body) throws IOException {
                /* corrId 被消费，获取结果 */
                if (properties.getCorrelationId().equals(corrId)){
                    response.offer(new String(body,"UTF-8"));
                }
            }
        };
        /* 消费 */
        String consumerTag = channel.basicConsume(queueName,true,consumer);

        String result = response.take();
        channel.basicCancel(consumerTag);   //取消消费者。。

        return result;
    }


    @Override
    public void close() throws Exception {
        channel.close();
    }
}
