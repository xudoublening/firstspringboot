package com.example.firstspringboot.xninyTest.ExtendsRunnableTest;

/**
 * @program: firstspringboot
 * @description:
 * @author: XNN
 * @create: 2020-09-02 21:38
 **/
public class TestBThread implements BaseThread {

    private Boolean isEnd = false;

    private int len;

    public TestBThread(int len) {
        System.out.println("构建一个TestBThread，数据量："+len);
        this.len = len;
    }

    @Override
    public Boolean isEnd() {
        return isEnd;
    }

    @Override
    public void run() {

        try {
            for (int i = 0; i < len; i++) {
                System.out.println(Thread.currentThread().getName() + " 处理B类业务数据 ... i=" + i);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            isEnd = true;
        }

    }
}
