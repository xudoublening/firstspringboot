package com.example.firstspringboot.xninyUtils;

/**
 * 双重检查锁单例模式  加volatile修饰符
 */
public class GetNumberService {
    
    private volatile static GetNumberService numberService;
    private volatile static int number = 0;
    
    private GetNumberService(){};
    
    public static GetNumberService getNumberService(){
        if (numberService == null){
            synchronized (GetNumberService.class){
                if (numberService == null){
                    numberService = new GetNumberService();
                }
            }
        }
        return numberService;
    }
    
    public synchronized int getNumber(){
        return number++;
    }
    
    public synchronized void initializationNumber(){
        this.number = 0;
    }
    
}
