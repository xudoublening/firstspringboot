package com.example.firstspringboot.xninyTest.WriterFile;

import com.ailk.mvno.crm.utils.util.ExceptionUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @program: firstspringboot
 * @description: 输出流写入文本
 * @author: XNN
 * @create: 2020-07-22 11:14
 **/
public class WriterFile {

    public static void main(String[] args) throws Exception {

        StringBuilder contents = new StringBuilder();
        byte[] blank = {0x01};
        contents.append("1111").append(new String(blank)).append("2222");
        File file = createFile("0009.TXT");
        FileOutputStream fos = new FileOutputStream(file);
        fos = sustainableFileWriter(fos, contents.toString());
        fos.flush();
        fos.close();
    }

    /**
     * 使用可持续写入的输出流进行写入
     *
     * @param contents 本次需要写入的内容
     * @param fos      可持续写入的输出流
     * @return
     */
    public static FileOutputStream sustainableFileWriter(FileOutputStream fos, String contents) {

        try {
            byte[] bt = contents.getBytes();
            fos.write(bt, 0, bt.length);
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在：" + ExceptionUtil.getTrace(e));
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("文件内容写入失败：" + ExceptionUtil.getTrace(e));
            e.printStackTrace();
        }
        return fos;
    }

    public static File createFile(String fileName){
        String basePath = "C:\\MyFile\\test\\";
        fileName = basePath + fileName;
        return new File(fileName);
    }

}
