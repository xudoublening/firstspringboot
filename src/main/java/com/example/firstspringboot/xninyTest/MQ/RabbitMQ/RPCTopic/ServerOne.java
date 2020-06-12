package com.example.firstspringboot.xninyTest.MQ.RabbitMQ.RPCTopic;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @program: firstspringboot
 * @description:
 * @author: XNN
 * @create: 2020-06-03 10:20
 **/
public class ServerOne {

    private static final String RPC_TOPIC_QUEUE = "rpc_topic_queue";

    private static int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        /* 声明队列：队列名，非持久，不是排他队列，不自动删除，无其他参数 */
        channel.queueDeclare(RPC_TOPIC_QUEUE,false,false,false,null);
        channel.queuePurge(RPC_TOPIC_QUEUE);
        channel.basicQos(1);
        // 交换类型：direct（直接）, topic（话题）, headers and fanout(扇出)
        channel.exchangeDeclare(RPC_TOPIC_QUEUE, "topic");
        String queueName = channel.queueDeclare().getQueue();

        // 交换机和队列绑定 添加绑定键 severity(话题)
        channel.queueBind(queueName, RPC_TOPIC_QUEUE, "test.#");
        System.out.println("[等待消息产生。。。]");

        Object monitor = new Object();

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
                        .correlationId(properties.getCorrelationId()).build();

                String response = "";

                try {
                    String message = new String(body, "UTF-8");
                    int n = Integer.parseInt(message);

                    System.out.println(" [.] fib(" + message + ")");
                    response += fib(n);
                } catch (RuntimeException e) {
                    System.out.println(" [.] " + e.toString());
                } finally {
                    /*
                        exchange -将消息发布到的交易所
                        routingKey -路由键
                        props -消息的其他属性-路由标头等
                        body -邮件正文
                    * */
                    channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));

                    /* 确认一条或多条收到的消息
                        deliveryTag-来自收到的标签AMQP.Basic.GetOk或AMQP.Basic.Deliver
                        multiple-确认所有消息，直到并包括提供的交付标签；为true；如果仅确认提供的交付标签，则为false
                    *  */
                    channel.basicAck(envelope.getDeliveryTag(), false);
                    // RabbitMq consumer worker thread notifies the RPC server owner thread
                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            }
        };
        // autoAck-如果服务器应考虑一旦传递已确认的消息，则为true；如果服务器应该期望显式确认，则返回false
        channel.basicConsume(RPC_TOPIC_QUEUE, false, consumer);
        // Wait and be prepared to consume the message from RPC client.
        while (true) {
            synchronized (monitor) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
