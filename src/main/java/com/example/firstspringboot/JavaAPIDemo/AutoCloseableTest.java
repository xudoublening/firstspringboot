package com.example.firstspringboot.JavaAPIDemo;

/**
 * Created by Xnn on 2019/11/18 15:32
 */
public class AutoCloseableTest {
    
    public static void main(String[] args) {
        
        // AutoCloseable 还需要结合异常处理语句才可以正常调用
        try(MessageSer msg = new MessageSer("hello")){
            msg.send();
        }catch (Exception e){ }
        
    }
    
    interface IMessage extends AutoCloseable {
        void send();    // 消息发送
    }
    
    static class MessageSer implements IMessage {
        private String msg;
        public MessageSer(String msg){
            this.msg = msg;
        }
        public boolean open(){
            System.out.println("[OPEN]打开消息发送通道.");
            return true;
        }
        @Override
        public void send() {
            if (this.open()){
                System.out.println("[发送消息] - " + this.msg);
            }
        }
        public void close() throws Exception{
            System.out.println("[CLOSE]关闭消息发送.");
        }
    }
    
}
