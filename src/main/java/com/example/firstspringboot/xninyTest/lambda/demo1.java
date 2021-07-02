package com.example.firstspringboot.xninyTest.lambda;

import java.util.function.Function;

/**
 * Describe：
 *
 * @author xnn
 * @createTime 2021/03/19 10:01
 */
public class demo1 {
    
    
    public void functionTest() {
        int num = fun("100",s->Integer.parseInt(s) );//字符串转成int类型
        System.out.println(num);
        //先将字符串转成int，再*10
        int result = method("5", s->Integer.parseInt(s), a->a *= 10);
        System.out.println(result);
    }
    
    //用于字符串处理
    public Integer fun(String s, Function<String,Integer> fun){
        return fun.apply(s);
    }
    
    //andThen:先执行一次apply操作,然后将第一次执行apply操作后获得的返回值作为参数再执行一次apply操作
    public int method(String s, Function<String,Integer> one,Function<Integer,Integer> two){
        return one.andThen(two).apply(s);
    }

}
