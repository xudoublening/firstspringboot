package com.example.firstspringboot.xninyTest.MQ.RabbitMQ.WorkQueues;

import com.rabbitmq.client.*;

public class NewTask {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 确保该队列将在RabbitMQ节点重启后继续存在。为此，需要将其声明为持久
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        StringBuffer sb = new StringBuffer();
        for (String s : argv) {
            sb.append(" ").append(s);
        }
        String message = argv.length < 1 ? "info: Hello World!" : sb.toString();
        while (true){
            // MessageProperties.PERSISTENT_TEXT_PLAIN 队列 标记为持久性
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
            Thread.sleep(3000);
        }

    }

}
