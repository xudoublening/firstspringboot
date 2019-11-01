package com.example.firstspringboot.xninyUtils;

import java.util.Date;

/**
 * Created by Xnn on 2019/9/11 15:25
 */
public class RowNumberThread extends Thread {
    
    private String name;
    private Long frequency;//频率(时间间隔)
    private Integer times;//次数
    
    public RowNumberThread(String name, Long frequency, Integer times) {
        
        this.name = name;
        this.frequency = frequency;
        this.times = times;
    }
    
    @Override
    public void run() {
        try {
            long start = System.currentTimeMillis();
            Integer count = 0;
            GetNumberService numberService = GetNumberService.getNumberService();
            while (count<times){
                System.out.println(name+" : "+numberService.getNumber());
                count ++;
                Thread.sleep(frequency);
            }
            System.out.println(name+" 循环次数:"+times+", 用时:"+(System.currentTimeMillis()-start)+"ms");
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
