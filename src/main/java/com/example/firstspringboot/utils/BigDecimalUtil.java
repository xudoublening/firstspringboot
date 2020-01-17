package com.example.firstspringboot.utils;

import java.math.BigDecimal;

/**
 * Created by Xnn on 2020/1/17 16:15
 */
public class BigDecimalUtil {
    
    /**
     * 功能描述: 保留指两位数小数(四舍五入)
     * 〈〉
     * @param: [value, newScale]
     * @return: java.lang.Double
     * @author: xnn
     */
    public static Double setScale(Double value){
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    public static void main(String[] args) {
    
        BigDecimal bigDecimal = new BigDecimal(Math.random());
        System.out.println(bigDecimal);
    }
    
}
