package com.example.firstspringboot.xninyTest.designPatterns;

import com.example.firstspringboot.xninyTest.designPatterns.Prototype.PrototypeClass;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * @program: firstspringboot
 * @description: 原型模式
 * @author: XNN
 * @create: 2020-09-24 19:34
 **/
public class Prototype {
    
    public static void main(String[] args) {

        /* 1基本原型克隆 */
        PrototypeClass prototypeClass = new PrototypeClass();
        PrototypeClass cloneClass = (PrototypeClass)prototypeClass.clone();
        System.out.println("prototypeClass==cloneClass ："+(prototypeClass==cloneClass));

        /* 2克隆原型大部分属性 */
        prototypeClass = new PrototypeClass("张三","2020年9月1日","福州大学");
        cloneClass = (PrototypeClass)prototypeClass.clone();
        cloneClass.setName("李四");
        System.out.println(prototypeClass.toString());
        System.out.println(cloneClass.toString());

        /* 3原型工厂 从原型工厂取（克隆）原型操作 */
        PrototypeManager prototypeManager = PrototypeManager.getPrototypeManager();
        BasicPrototype circlePrototype = prototypeManager.getProto("Circle");
        circlePrototype.countArea();
        BasicPrototype squarePrototype = prototypeManager.getProto("Square");
        squarePrototype.countArea();
    }

    /* 原型工厂类 */
    static class PrototypeManager{

        private volatile static PrototypeManager prototypeManager = null;
        private HashMap<String,BasicPrototype> protoPools = new HashMap<>();

        private PrototypeManager() {
            this.addProto("Circle",new Circle());
            this.addProto("Square",new Square());
        }

        public static PrototypeManager getPrototypeManager(){
            if (prototypeManager == null){
                synchronized (PrototypeManager.class){
                    if (prototypeManager == null){
                        prototypeManager = new PrototypeManager();
                    }
                }
            }
            return prototypeManager;
        }

        public void addProto(String key,BasicPrototype value){
            protoPools.put(key,value);
        }

        public BasicPrototype getProto(String key){
            BasicPrototype prototype = protoPools.get(key);
            return (BasicPrototype)prototype.clone();
        }

    }

    /***
     * @Description:  图形计算基本原型
     *
     * @Author: XNN
     * @Date: 2020/9/24
     */
    interface BasicPrototype extends Cloneable{
        // 克隆
        public Object clone();
        // 计算面积
        public void countArea();
    }

    /***
     * @Description: 圆形
     */
    static class Circle implements BasicPrototype{
        @Override
        public Object clone() {
            Circle circle = null;
            try {
                circle = (Circle)super.clone();
                System.out.println("克隆【圆】成功！");
            } catch (CloneNotSupportedException e) {
                System.out.println("克隆【圆】失败：" + e.getMessage());
            }
            return circle;
        }

        @Override
        public void countArea() {
            int r = 0;
            System.out.println("这是一个圆形，请输入它的半径：");
            Scanner input = new Scanner(System.in);
            r = input.nextInt();
            System.out.println("圆的面积为：" + 3.1415926*r*r + "\n");
        }
    }
    /** 正方形 */
    static class Square implements BasicPrototype {
        @Override
        public Object clone() {
            Square square = null;
            try {
                square = (Square)super.clone();
                System.out.println("克隆【正方形】成功！");
            } catch (CloneNotSupportedException e) {
                System.out.println("克隆【正方形】失败：" + e.getMessage());
            }
            return square;
        }

        @Override
        public void countArea() {
            int r = 0;
            System.out.println("这是一个正方形，请输入它的边长：");
            Scanner input = new Scanner(System.in);
            r = input.nextInt();
            System.out.println("正方形的面积为：" + r*r + "\n");
        }
    }

    /***
     * @Description: 原型类
     *
     * @Author: XNN
     * @Date: 2020/9/24
     */
    static class PrototypeClass implements Cloneable{

        private String name;
        private String date;
        private String school;

        public PrototypeClass() {
            System.out.println("创建了一个无参原型！");
        }

        public PrototypeClass(String name, String date, String school) {
            this.name = name;
            this.date = date;
            this.school = school;
            System.out.println("创建了一个有参原型！");
        }

        @Override
        protected Object clone() {
            PrototypeClass cloneObject = null;
            try {
                cloneObject = (PrototypeClass)super.clone();
                System.out.println("复制原型成功！");
            }catch (CloneNotSupportedException e){
                System.out.println("复制原型失败：" + e.getMessage());
            }

            return cloneObject;
        }

        @Override
        public String toString() {
            return name + " 于 " + date + " 入学【" + school + "】";
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getDate() {
            return date;
        }
        public void setDate(String date) {
            this.date = date;
        }
        public String getSchool() {
            return school;
        }
        public void setSchool(String school) {
            this.school = school;
        }
    }

}
