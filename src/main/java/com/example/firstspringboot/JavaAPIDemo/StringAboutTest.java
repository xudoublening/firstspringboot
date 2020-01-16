package com.example.firstspringboot.JavaAPIDemo;

/**
 * Created by Xnn on 2019/11/18 15:37
 */
public class StringAboutTest {
    
    public static void main(String[] args) {
        testString();
        testStringBuffer();
        testCharSequence();
    }
    
    /**
     * CharSequence(jdk1.4) 是一个描述字符串的接口, 常用的子类有: String,StringBuffer,StringBuilder
     */
    public static void testCharSequence(){
        CharSequence cs = new StringBuffer("hello world !");
        CharSequence str = "Hello world !" ;        // 子类实例像父接口转型
    
        System.out.println(cs.charAt(4));           // 获取指定索引字符
        System.out.println(cs.subSequence(6,12));   // 字符串截取
        System.out.println(str.length());           // 获取字符串长度
        
    }
    
    /**
     * StringBuffer 线程安全的, 允许重复修改
     * StringBuilder 使用方法和StringBuffer一致,但没有用synchronized同步方法,是线程不安全的
     */
    public static void testStringBuffer(){
        StringBuffer str = new StringBuffer("Hello ");
        changeStringBuffer(str);
        System.out.println(str);
        System.out.println(str.reverse());  // 字符串逆转
    }
    public static void changeStringBuffer(StringBuffer str){
        str.append("world ").append("!");
    }
    
    /**
     * String 有两个常量池: 静态常量池, 运行时常量池
     * String类对象实例化建议使用直接赋值的形式实现,这样可以直接保存在常量池中,方便下次复用
     * 弊端: 内容不允许修改
     */
    public static void testString(){
        String str = "Hello ";
        changeString(str);
        System.out.println(str);
    }
    public static void changeString(String str){
        str += "world !";
    }
    
    
}
