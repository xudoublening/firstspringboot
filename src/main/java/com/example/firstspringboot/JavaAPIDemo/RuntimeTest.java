package com.example.firstspringboot.JavaAPIDemo;

/**
 * Created by Xnn on 2019/11/18 15:01
 */
public class RuntimeTest {
    
    public static void main(String[] args) {
        Runtime run = Runtime.getRuntime();
        System.out.println("[最大可用内存] -1- " + run.maxMemory());
        System.out.println("[可用内存空间] -1- " + run.totalMemory());
        System.out.println("[空闲内存空间] -1- " + run.freeMemory());
        String str = "";
        for (int x = 0; x < 20000; x ++){
            str += "1"+x;
        }
        System.out.println("[最大可用内存] -2- " + run.maxMemory());
        System.out.println("[可用内存空间] -2- " + run.totalMemory());
        System.out.println("[空闲内存空间] -2- " + run.freeMemory());
        run.gc();   // 手动调用垃圾回收
        System.out.println("[最大可用内存] -3- " + run.maxMemory());
        System.out.println("[可用内存空间] -3- " + run.totalMemory());
        System.out.println("[空闲内存空间] -3- " + run.freeMemory());
    }
    
}
