package com.example.firstspringboot.xninyTest.RedPacketsTest;

import java.math.BigDecimal;

/**
 * 功能描述: 抢红包模拟测试
 * Created by Xnn on 2020/1/16 17:46
 */
public class RedPacketsTest {
    
    static class User implements Runnable {
        
        private RedPacket redPacket;
        private boolean flag;
        private long millis;
    
        public User(RedPacket redPacket,long millis) {
            this.redPacket = redPacket;
            this.millis = millis;
            this.flag = true;
        }
    
        @Override
        public void run() {
            Integer count = 1;
            while (this.flag){
                count++;
                try{
                    BigDecimal grab = redPacket.grabRedPacket(Thread.currentThread().getName()+count);
                    System.out.println(Thread.currentThread().getName()+": "+ grab+"元!");
                    System.out.println(redPacket.selectInfoList());
                    Thread.sleep(this.millis);
                }catch (Exception e){
                    System.out.println(Thread.currentThread().getName()+": 红包没了,我不抢了");
                    this.flag = false;
                }
            }
        }
    }
    
    public static void main(String[] args) {
        RedPacket redPacket = new RedPacket(BigDecimal.valueOf(1000),7,"2");
        User userA = new User(redPacket,10);
        User userB = new User(redPacket,12);
        User userC = new User(redPacket,9);
        Thread threadA = new Thread(userA,"userA");
        Thread threadB = new Thread(userB,"userB");
        Thread threadC = new Thread(userC,"userC");
        threadA.start();
        threadB.start();
        threadC.start();
    }

    
}
