package com.example.firstspringboot.xninyTest.ExtendsRunnableTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: firstspringboot
 * @description:
 * @author: XNN
 * @create: 2020-09-02 21:46
 **/
public class TestMain {

    public static void main(String[] args) {

        ExecutorService pools = Executors.newFixedThreadPool(5);
        BaseThread baseThread = null;
        CheckThreadStatus checkThread = new CheckThreadStatus("check-1");
        for (int i=0;i<10;i++){
            if (i%2 == 1){
                baseThread = new TestAThread((i+1)*5);
            }else{
                baseThread = new TestBThread((i+1)*3);
            }
            pools.submit(baseThread);
            checkThread.addTread(baseThread);
        }
        new Thread(checkThread).start();
        pools.shutdown();
    }

}
