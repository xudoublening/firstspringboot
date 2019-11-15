package com.example.firstspringboot.xninyUtils;

/**
 * 生产者  消费者
 * 多线程  同步,等待,唤醒  实现例子
 *
 * Created by Xnn on 2019/11/15 10:42
 */
public class ThreadExample {
    
    public static void main(String[] args) {
        Massage msg = new Massage();
        new Thread(new Consumer(msg)).start();
        new Thread(new Producer(msg)).start();
    }
    
}

class Consumer implements Runnable{
    
    private Massage msg;
    public Consumer(Massage msg){
        this.msg = msg;
    }
    @Override
    public void run() {
        for (int x = 0; x < 100 ; x ++){
            System.out.println(this.msg.get());
        }
    }
}

class Producer implements Runnable{
    
    private Massage msg;
    public Producer(Massage msg){
        this.msg = msg;
    }
    @Override
    public void run() {
        for (int x = 0; x < 100 ; x ++){
            if (x % 2 == 0){
                this.msg.set("张三","男");
            }else{
                this.msg.set("李四","女");
            }
            
        }
    }
}

class Massage {
    private String name;
    private String sex;
    private boolean flag = false;
    // flag = true 生产完成, 可以消费
    // flag = false 可以生产, 消费等待
    public synchronized void set(String name,String sxe){
        if (this.flag != false){
            try {
                super.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.name = name;
        this.sex = sxe;
        this.flag = true;
        super.notify();
    }
    public synchronized String get(){
        if (this.flag != true){
            try {
                super.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.flag = false;
        try{
            return this.name + " - " + this.sex;
        }finally {
            super.notify();
        }
        
    }
}
