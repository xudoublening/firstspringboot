package com.example.firstspringboot.xninyTest.ExtendsRunnableTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: firstspringboot
 * @description:
 * @author: XNN
 * @create: 2020-09-02 21:40
 **/
public class CheckThreadStatus implements Runnable {

    private List<BaseThread> baseThreads = new ArrayList<>();
    private String threadId;

    public CheckThreadStatus(String threadId) {
        System.out.println("构建一个校验状态线程：" + threadId);
        this.threadId = threadId;
    }

    public void addTread(BaseThread baseThread){
        this.baseThreads.add(baseThread);
    }

    @Override
    public void run() {
        boolean flag = false;
        while(!flag){
            flag = true;

            for(BaseThread t : baseThreads){
                if(!t.isEnd()){
                    flag = false;
                    break;
                }
            }
            if(flag){
                System.out.println("线程["+threadId+"] 监控的子线程全部执行完毕！");
            }else{
                System.out.println("等待执行完毕...");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
