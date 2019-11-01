package com.example.firstspringboot.xninyUtils;

/**
 * Created by Xnn on 2019/9/11 15:43
 */
public class ThreadTest {
    
    public static void main(String[] args) {
        Thread t1 = new RowNumberThread("<A线程>",20l,800);
        Thread t2 = new RowNumberThread("<B线程>",10l,1000);
        Thread t3 = new RowNumberThread("<C线程>",30l,500);
        Thread t4 = new RowNumberThread("<D线程>",50l,300);
        Thread t5 = new RowNumberThread("<E线程>",80l,200);
        Thread t6 = new RowNumberThread("<F线程>",100l,200);
        Thread t7 = new RowNumberThread("<G线程>",5l,2000);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        
    }
}
