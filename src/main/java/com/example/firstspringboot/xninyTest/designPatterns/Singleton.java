package com.example.firstspringboot.xninyTest.designPatterns;

import java.util.concurrent.Callable;

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

    /***
     * @Description: 数组最小路径求和案例 
     *
     * @Param: 
     * @return: 
     * @Author: XNN
     * @Date: 2020/5/26
     */
    static class TestThreadCallable implements Callable<Integer>{

        private int[][] map;

        public TestThreadCallable(int[][] map){
            this.map = map;
        }

        @Override
        public Integer call() throws Exception {
            int min = map[0][0];
            int row = map.length-1;
            int column = map[0].length-1;
            System.out.println("二维数据行："+map.length+"，列："+map[0].length);
            int nowColumn = 0;
            int nowRow = 0;
            do {
                System.out.println("现在坐标["+nowRow+"]["+nowColumn+"]值为："+map[nowRow][nowColumn]);
                int temp1 = map[nowRow==row?nowRow:nowRow+1][nowColumn];
                int temp2 = map[nowRow][nowColumn==column?nowColumn:nowColumn+1];
                if (nowRow==row && nowColumn<column){
                    System.out.println("现在【行】到边界了");
                    temp1 = map[nowRow][nowColumn+1];
                }
                if (nowRow<row && nowColumn==column){
                    System.out.println("现在【列】到边界了");
                    temp2 = map[nowRow+1][nowColumn];
                }

                if (temp1>temp2){
                    System.out.println(min+"+"+temp2);
                    min += temp2;
                    nowColumn += nowColumn<column?1:0;
                }
                else{
                    System.out.println(min+"+"+temp1);
                    min += temp1;
                    nowRow += nowRow<row?1:0;
                }
            }while (!(nowColumn == column && nowRow == row));

            return min;
        }
    }

    public static void main(String[] args) throws Exception{

        int[][] map = {
                {9,18,7,10},
                {8,2,16,13},
                {17,20,9,2},
                {12,10,16,8}};
        TestThreadCallable testCallable = new TestThreadCallable(map);
        int min = testCallable.call();
        System.out.println("最小路径合："+min);
//        TestThread test1 = new TestThread(10L,50);
//        TestThread test2 = new TestThread(8L,40);
//        TestThread test3 = new TestThread(9L,60);
//
//        Thread r1 = new Thread(test1,"test1");
//        Thread r2 = new Thread(test2,"test2");
//        Thread r3 = new Thread(test3,"test3");
//
//        r1.start();
//        r2.start();
//        r3.start();

    }
}
