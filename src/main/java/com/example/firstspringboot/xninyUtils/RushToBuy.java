package com.example.firstspringboot.xninyUtils;

/**
 * Created by Xnn on 2019/11/15 17:07
 */
public class RushToBuy {
    
    public static void main(String[] args) {
        Service service = new Service();
        new Thread(new Buyer(service),"Buyer - A ").start();
        new Thread(new Buyer(service),"Buyer - B ").start();
        new Thread(new Buyer(service),"Buyer - C ").start();
    }
    
}

class Buyer implements Runnable{
    
    private Service service;
    public Buyer(Service service){
        this.service = service;
    }
    @Override
    public void run() {
        for (int x = 0; x < 5; x ++){
            this.service.buy();
        }
    }
}

// 一个服务类
class Service {
    
    private boolean flag = false;
    // 购买方法
    public synchronized void buy(){
        if (this.flag){
            try {
                super.wait();   // 有一个对象正在购买,等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.flag = true;
        try {
            Thread.sleep(100);
            System.out.println(Thread.currentThread().getName() + " - 开始购买");
            this.flag = false;
            System.out.println(Thread.currentThread().getName() + " - 购买结束 <<<");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            super.notify();
        }
    }
}
