package com.example.firstspringboot.xninyTest.MQ.RabbitMQ.RPC;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @program: firstspringboot
 * @description: RPC 服务端
 * @author: XNN
 * @create: 2020-06-02 23:05
 **/
public class RPCServer {
    private static final String RPC_QUEUE_NAME = "rpc_queue";

    private static int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        /* 声明队列
            queue -队列名称
            durable -如果我们声明一个持久队列，则为true（该队列将在服务器重启后保留下来）
            exclusive -如果我们声明一个排他队列，则为true（仅限此连接）
            autoDelete -如果我们声明一个自动删除队列，则为true（服务器将在不再使用它时将其删除）
            arguments -队列的其他属性（构造参数）
        */
        channel.queueDeclare(RPC_QUEUE_NAME,false,false,false,null);

        channel.queuePurge(RPC_QUEUE_NAME);// 清除给定队列的内容

        channel.basicQos(1); //一次仅接受一条肯定确认的消息（请视为大约）

        System.out.println(" [x] Awaiting RPC requests");

        final Object monitor = new Object();

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
        channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
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
