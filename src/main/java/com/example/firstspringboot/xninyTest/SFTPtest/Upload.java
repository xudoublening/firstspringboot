package com.example.firstspringboot.xninyTest.SFTPtest;

import java.io.File;

/**
 * @program: firstspringboot
 * @description: 上传文件到SFTP
 * @author: XNN
 * @create: 2020-07-17 10:20
 **/
public class Upload {

    /*public static void main(String[] args) {

        String foldName = null;
        if (args.length > 0){
            foldName = args[0];
        } else{
            foldName = "20200716_P";
        }

        System.out.println("开始上传文件夹："+foldName);
        uploadFile(foldName);
    }

    public final static String OPERATOR_ID = "0009"; // 运营商标识

    *//**
     * 1.数据文件
     * F代表全量文件,P代表增量文件,文件序列号最大为9999,不分割时固定为0001
     * 0001_2017021600_F_0001.TXT, 表示某虚商截止2017年2月16日0时之前的全量用户数据
     * 0001_2017021500_P_0001.TXT, 表示某虚商2017年2月14日的增量数据文件
     * 文件的内容2017年2月14日0时2017年2月14日24时之间的用户数数据
     * <p>
     * 2.校验文件
     * 数据文件名为NNNN_20170215_F_0001.TXT的数据文件,行数为10000行,文件大小为343284字节
     * 其对于的校验文件内容为：NNNN_20170215_F_0001.TXT 10000 343284
     *//*
    public static File createFile(String fileName) {
        // 缓存路径
        String filePath = getTempFilePath();
        // 文件名称
        String tmpFilePath = filePath + fileName;
        return new File(tmpFilePath);
    }

    public static void createFolder(String folderPath) {
        // 缓存路径
        String filePath = getTempFilePath();
        // 文件名称
        String tmpFilePath = filePath + folderPath;
        File file = new File(tmpFilePath);
        if (file.exists()) {
            file.delete();
        }
        file.mkdirs();
    }

    *//**
     * 使用可持续写入的输出流进行写入
     *
     * @param contents 本次需要写入的内容
     * @param fos      可持续写入的输出流
     * @return
     *//*
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

    *//**
     * 本地文件上传
     *
     * @param foldName 文件夹
     *//*
    public static void uploadFile(String foldName) {

        System.out.println("ftp233------------文件上传开始-----------");
        SFTPUtils sftpUtils = null;
        try {
            sftpUtils = new SFTPUtils("192.168.20.10",8360,"0009","cxjc-49sn-wgeb");

            ChannelSftp channelSftp = sftpUtils.getChannelSftp();
            // 20191222_F/
            String uploadDir = foldName + File.separator;
            if (dirExistFTP(uploadDir, channelSftp)) {
                try {
                    channelSftp.mkdir(uploadDir);
                    System.out.println("ftp233------------创建FTP目录:"+uploadDir+"-----------");
                } catch (SftpException e) {
                    System.out.println("ftp233------------创建FTP目录失败-----------");
                    e.printStackTrace();
                }
            } else {
                System.out.println("ftp233------------FTP目录已存在:"+uploadDir+"-----------");
            }

            String localDir = getTempFilePath() + foldName + File.separator;
            System.out.println("ftp233------------本地文件缓存路径:"+localDir+"-----------");
            File localFile = new File(localDir);
            copyFile(localFile, channelSftp, uploadDir);

        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sftpUtils != null) {
                    sftpUtils.closeChannel();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("ftp233------------文件上传结束-----------");
    }

    *//**
     * 拷贝本地文件到FTP
     *
     * @param foldName
     * @param channelSftp
     *//*
    private static void copyFile(File dir, ChannelSftp channelSftp, String ftpPath) {

        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    uploadToFTP(file.getPath(), ftpPath, channelSftp);
                } else if (file.isDirectory()) {
                    String photoPath = ftpPath + file.getName() + File.separator;
                    File[] photos = file.listFiles();
                    if (photos != null && photos.length > 0) {
                        for (File img : photos) {
                            uploadToFTP(img.getPath(), photoPath, channelSftp);
                        }
                    }
                }
            }
        }
    }

    *//**
     * 判断FTP服务器上的路径是否存在
     *
     * @param path        ftp路径
     * @param channelSftp
     * @return
     *//*
    private static boolean dirExistFTP(String path, ChannelSftp channelSftp) {
        try {
            SftpATTRS attrs = channelSftp.stat(path);
            return !attrs.isDir();
        } catch (SftpException e) {
            return true;
        }
    }

    *//**
     * 上传到FTP
     *
     * @param localPath   本地文件路径
     * @param ftpPath     FTP保存路径
     * @param channelSftp
     *//*
    private static void uploadToFTP(String localPath, String ftpPath, ChannelSftp channelSftp) {
        try {
            if (dirExistFTP(ftpPath, channelSftp)) {
                channelSftp.mkdir(ftpPath);
                System.out.println("ftp233------------创建FTP目录:"+ftpPath+"-----------");
            }
            channelSftp.put(localPath, ftpPath);
        } catch (SftpException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 缓存路径
     *
     * @return
     */
    public static String getTempFilePath() {

        return System.getProperty("user.home") + File.separator + "tmp" + File.separator;
    }

}
