package com.example.firstspringboot.xninyUtils;

/**
 * Created by Xnn on 2019/11/15 17:41
 */
public class DaemonThreadTest {
    
    // 守护线程是围绕着用户线程执行,当所有用户线程执行完毕后,守护线程也将消失
    
    public static void main(String[] args) {
        Thread userThreadA = new Thread(()->{
            for (int a = 0; a < 10 ; a ++){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " 执行, a = " + a);
            }
        },"A用户线程");
        Thread userThreadB = new Thread(()->{
            for (int b = 0; b < 20 ; b ++){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " 执行, b = " + b);
            }
        },"B用户线程");
        Thread daemonThread = new Thread(()->{
            for (int x = 0; x < Integer.MAX_VALUE; x ++){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " 执行, x = " + x);
            }
        },"守护线程");
        
        // 设置为守护线程
        daemonThread.setDaemon(true);
        
        userThreadA.start();
        userThreadB.start();
        daemonThread.start();
    }
    
}
