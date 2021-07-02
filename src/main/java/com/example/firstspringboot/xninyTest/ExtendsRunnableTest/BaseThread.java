package com.example.firstspringboot.xninyTest.ExtendsRunnableTest;

public interface BaseThread extends Runnable {

    Boolean isEnd();

    public abstract void run();

}
