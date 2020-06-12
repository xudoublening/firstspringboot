package com.example.firstspringboot.xninyTest.MQ.RabbitMQ.RPC;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @program: firstspringboot
 * @description: RPC 客户端
 * @author: XNN
 * @create: 2020-06-02 19:01
 **/
public class RPCClient implements AutoCloseable {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";

    public RPCClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public static void main(String[] argv) {
        try (RPCClient fibonacciRpc = new RPCClient()) {
            for (int i = 0; i < 32; i++) {
                String i_str = Integer.toString(i);
                System.out.println(" [x] Requesting fib(" + i_str + ")");
                String response = fibonacciRpc.call(i_str);
                System.out.println(" [.] Got '" + response + "'");
            }
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String call(String message) throws IOException, InterruptedException {

        final String corrId = UUID.randomUUID().toString();// 生成一个唯一的relatedId 编号

        /*
        * 主动声明一个名为服务器的独占，自动删除，非持久队列
        * 获取队列名（随机）
        * */
        String replyQueueName = channel.queueDeclare().getQueue();

        // 消息的其他属性设置- 设置唯一relatedId  消息响应队列
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        // 消息发布
        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                   byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)){
                    response.offer(new String(body, "UTF-8"));
                }
            }
        };

        /*  消费
            queue -队列名称
            autoAck-如果服务器应考虑一旦传递已确认的消息，则为true；如果服务器应该期望显式确认，则返回false
            callback -消费者对象的接口
        * */
        String ctag = channel.basicConsume(replyQueueName, true, consumer);

        String result = response.take();
        /* 取消消费者
        * consumerTag -客户端或服务器生成的消费者标签以建立上下文
        *  */
        channel.basicCancel(ctag);
        return result;
    }

    public void close() throws IOException {
        connection.close();
    }
}
