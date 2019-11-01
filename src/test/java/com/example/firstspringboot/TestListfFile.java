package com.example.firstspringboot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TestListfFile {
    public static void main(String[] args) {
        String path = "C:\\MyFile\\需求说明等\\公告";
        System.out.println("遍历文件夹["+path+"]下文件");
        File file = new File(path);
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (!file1.isDirectory()){
                System.out.println(file1);
                System.out.println(file1.getName());
                try {
                    FileInputStream inp = new FileInputStream(file1);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
