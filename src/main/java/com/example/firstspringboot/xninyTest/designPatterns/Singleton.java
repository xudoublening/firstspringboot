package com.example.firstspringboot.xninyTest.designPatterns;

/**
 * 单例模式
 */
public class Singleton {

    private volatile static Singleton singleton;
    private Singleton(){}

    public static Singleton getSingleton(){
        if (singleton == null){
            synchronized (Singleton.class){
                if (singleton == null){
                    System.out.println(Thread.currentThread().getName()+"=====");
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }

    /**
     * 测试线程
     */
    static class TestThread implements Runnable{

        private long sleepTime;
        private int runTimes;

        public TestThread(long sleepTime,int runTimes){
            this.sleepTime = sleepTime;
            this.runTimes = runTimes;
        }

        @Override
        public void run() {
            while (runTimes>0){
                System.out.println(Thread.currentThread().getName()+": "+Singleton.getSingleton());
                runTimes--;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception{
        TestThread test1 = new TestThread(10L,50);
        TestThread test2 = new TestThread(8L,40);
        TestThread test3 = new TestThread(9L,60);

        Thread r1 = new Thread(test1,"test1");
        Thread r2 = new Thread(test2,"test2");
        Thread r3 = new Thread(test3,"test3");

        r1.start();
        r2.start();
        r3.start();

    }
}
