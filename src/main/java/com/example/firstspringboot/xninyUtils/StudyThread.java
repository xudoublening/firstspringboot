package com.example.firstspringboot.xninyUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

// 单继承做法
class MyThread1 extends Thread{
    private String title;
    public MyThread1 (String title){
        this.title = title;
    }
    @Override
    public void run() {
        for (int i=0; i < 10 ; i++){
            System.out.println(this.title+">> i = " + i);
        }
    }
}

// 实现Runnable接口
class MyThread implements Runnable{
    private String title;
    public MyThread (String title){
        this.title = title;
    }
    @Override
    public void run() {
        
        for (int x = 0; x < 10 ; x++){
            System.out.println(this.title + "运行, x = " + x);
        }
    }
}

// 多用户抢占资源例子
class MyThread2 implements Runnable{
    private String title;
    private int number = 5;
    public MyThread2 (String title){
        this.title = title;
    }
    @Override
    public void run() {
        
        for (int x = 0; x < 10 ; x++){
            if (this.number > 0){
                System.out.println("当前资源剩余: " + this.number--);
            }
        }
    }
}

// 实现Callable中的 call()方法, get()获取返回值
class MyThread3 implements Callable<String>{
    
    @Override
    public String call() throws Exception {
        for (int i=0; i < 10 ; i++){
            System.out.println("Callable线程执行: i = "+i);
        }
        return "Callable线程执行完毕";
    }
}

/**
 * Created by Xnn on 2019/11/13 15:38
 */
public class StudyThread {
    
    public static void main(String[] args) {
        // test1();
        // test2();
        // test3();
        // test4();
        // test5();
        // test6();
        // test7();
        // test8();
        // test9();
        test10();
        
    }
    
    // 线程优先级  优先级高的有可能先执行
    public static void test10(){
        Runnable run = ()->{
          for (int i = 0; i < 10; i ++){
              try {
                  Thread.sleep(100);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              System.out.println(Thread.currentThread().getName()+" - 执行");
          }
        };
        Thread threadA = new Thread(run,"线程对象A");
        Thread threadB = new Thread(run,"线程对象B");
        Thread threadC = new Thread(run,"线程对象C");
        
        threadC.setPriority(Thread.MAX_PRIORITY);
        threadB.setPriority(Thread.MIN_PRIORITY);
    
        System.out.println("threadA : "+threadA.getPriority());
        System.out.println("threadB : "+threadB.getPriority());
        System.out.println("threadC : "+threadC.getPriority());
        
        threadA.start();
        threadB.start();
        threadC.start();
    }
    
    // 线程礼让执行时调用yield()方法,每次只礼让一次资源
    public static void test9(){
        Thread thread = new Thread(()->{
            for (int x = 0; x < 50 ; x++){
                try {
                    if (x % 3 == 0){
                        System.out.println(Thread.currentThread().getName()+" -礼让资源- ");
                        Thread.yield();
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+", 执行 x = " + x);
            }
        },"[一个弱小的线程]");
        thread.start();
        for (int i = 0; i < 50 ; i ++){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+", number = " + i);
        }
    }
    
    // 线程强制执行  取到需要强制执行的线程对象 join()开始强制执行
    public static void test8(){
        Thread mainThread = Thread.currentThread();
        Thread thread = new Thread(()->{
           for (int x = 0; x < 50 ; x++){
               try {
                   if (x == 3){
                       System.out.println("**** 开始强制执行 main Thread ");
                       mainThread.join();
                   }
                   Thread.sleep(100);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               System.out.println(Thread.currentThread().getName()+", 执行 x = " + x);
           }
        },"[一个弱小的线程]");
        thread.start();
        for (int i = 0; i < 50 ; i ++){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+", number = " + i);
        }
    }
    
    // 线程中断
    public static void test7(){
        Thread thread = new Thread(()->{
            System.out.println(Thread.currentThread().getName()+" 开始执行>>>");
            try {
                Thread.sleep(10000);
                System.out.println(Thread.currentThread().getName()+" 执行结束<<<");
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+" 执行过程被中断...");
            }
            
        },"中断测试线程-");
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!thread.isInterrupted()){   // 未中断
            System.out.println("中断执行 ");
            thread.interrupt();
        }
    }
    
    // 例: 耗时任务分配给子线程操作
    public static void test6(){
    
        try{
            System.out.println("执行操作一");
            FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    Integer temp = 0;
                    for (int i = 0; i < Integer.MAX_VALUE; i++){
                        temp +=i;
                    }
                    return temp;
                }
            });
            new Thread(task).start();
            System.out.println("执行操作二");
            System.out.println("执行操作N");
            System.out.println("子线程执行结果: "+task.get());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static void test5(){
    
        try{
            FutureTask<String> task = new FutureTask<String>(new MyThread3());
            new Thread(task).start();
            System.out.println("执行结果: "+task.get());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static void test4(){
    
        MyThread2 myThread2 = new MyThread2("资源抢占-1");
        new Thread(myThread2).start();
        new Thread(myThread2).start();
        new Thread(myThread2).start();
        
    }
    
    public static void test3(){
        for (int x=0; x < 3; x++){
            String title = "线程="+x;
            new Thread(()->{
                for (int y=0; y < 10; y ++){
                    System.out.println(title+" 运行, y = "+y);
                }
            }).start();
        }
    }
    
    public static void test2(){
        MyThread1 thread1 = new MyThread1("线程-1");
        MyThread1 thread2 = new MyThread1("线程-2");
        MyThread1 thread3 = new MyThread1("线程-3");
        thread1.start();
        thread2.start();
        thread3.start();
    }
    
    public static void test1(){
        Thread threadA = new Thread(new MyThread("线程-A"));
        Thread threadB = new Thread(new MyThread("线程-B"));
        Thread threadC = new Thread(new MyThread("线程-C"));
        threadA.start();
        threadB.start();
        threadC.start();
    }
}
