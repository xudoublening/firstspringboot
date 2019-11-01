package com.example.firstspringboot;

public class TestMain {
    public static void main(String[] args) {
        int number = 2;
        System.out.println(number + " 不变    " + Integer.toBinaryString(number));
        System.out.println(number + " 右移1位 " + Integer.toBinaryString(number >>> 1));
        number |= number >>> 1;// 10 |= (10 >>> 1 = 1)  =  10|=1 = 11
        System.out.println(number + " 右移1位 " + Integer.toBinaryString(number));
        number |= number >>> 2;// 11 >>> 2 = 0
        System.out.println(number + " 右移2位 " + Integer.toBinaryString(number));
        number |= number >>> 4;
        System.out.println(number + " 右移4位 " + Integer.toBinaryString(number));
        number |= number >>> 8;
        System.out.println(number + " 右移8位 " + Integer.toBinaryString(number));
        number |= number >>> 16;
        System.out.println(number + " 右移16位 " + Integer.toBinaryString(number));
    }
}
