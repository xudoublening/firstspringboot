package com.example.firstspringboot.xninyTest.SFTPtest;

import java.io.File;

/**
 * @program: firstspringboot
 * @description: FTP 上传测试
 * @author: XNN
 * @create: 2020-07-22 22:09
 **/
public class UploadT {

    /*public static void main(String[] args) {

        String foldName = null;
        if (args.length > 0) {
            foldName = args[0];
        } else {
            foldName = "20200722_P";
        }

        System.out.println("upload foldName:" + foldName);

        String localDir = getTempFilePath() + foldName + File.separator;
        System.out.println("ftp233------------localPath:" + localDir + "-----------");

        uploadFile(foldName);
    }

    public final static String OPERATOR_ID = "0009";
    public final static String ROOT_DIR = "";


    public static File createFile(String fileName) {

        String filePath = getTempFilePath();
        String tmpFilePath = filePath + fileName;
        return new File(tmpFilePath);
    }

    public static void createFolder(String folderPath) {

        String filePath = getTempFilePath();
        String tmpFilePath = filePath + folderPath;
        File file = new File(tmpFilePath);
        if (file.exists()) {
            file.delete();
        }
        file.mkdirs();
    }

    public static FileOutputStream sustainableFileWriter(FileOutputStream fos, String contents) {

        try {
            byte[] bt = contents.getBytes();
            fos.write(bt, 0, bt.length);
        } catch (FileNotFoundException e) {
            System.out.println("file no fand:" + ExceptionUtil.getTrace(e));
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("file write fail:" + ExceptionUtil.getTrace(e));
            e.printStackTrace();
        }
        return fos;
    }


    public static void uploadFile(String foldName) {

        System.out.println("ftp233------------upload file start-----------");
        SFTPUtils sftpUtils = null;
        try {
            sftpUtils = new SFTPUtils("117.78.32.224", 22022, "ftp_admin", "ftp_admin##0904");

            ChannelSftp channelSftp = sftpUtils.getChannelSftp();
            // home/
            String uploadDir = channelSftp.getHome() + File.separator;
            if (!isExistDir(uploadDir, channelSftp)) {
                try {
                    System.out.println("ftp233------------create FTP fold:" + uploadDir + "-----------");
                    channelSftp.mkdir(uploadDir);
                    System.out.println("ftp233------------create FTP fold:" + uploadDir + "-----------");
                } catch (SftpException e) {
                    System.out.println("ftp233------------create FTP fold fail-----------");
                    e.printStackTrace();
                }
            } else {
                System.out.println("ftp233------------FTP alraed:" + uploadDir + "-----------");
            }

            String localDir = getTempFilePath() + foldName + File.separator;
            System.out.println("ftp233------------localPath:" + localDir + "-----------");
            File localFile = new File(localDir);
            copyFile(localFile, channelSftp, uploadDir);

        } catch (Exception e) {
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
        System.out.println("ftp233------------upload end-----------");
    }

    private static void copyFile(File dir, ChannelSftp channelSftp, String ftpPath) throws Exception {

        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    uploadToFTP(file.getPath(), ftpPath, channelSftp);
                } else if (file.isDirectory()) {
                    String photoPath = channelSftp.getHome() + File.separator + file.getName() + File.separator;
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

    public static boolean isExistDir(String path, ChannelSftp sftp) {
        boolean isExist = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(path);
            isExist = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isExist = false;
            }
        }
        return isExist;

    }

    private static void uploadToFTP(String localPath, String ftpPath, ChannelSftp channelSftp) {
        try {
            if (!isExistDir(ftpPath, channelSftp)) {
                System.out.println("ftp233------------create FTP fold:" + ftpPath + "-----------");
                channelSftp.mkdir(ftpPath);
                System.out.println("ftp233------------create FTP fold:" + ftpPath + " success-----------");
            }
            channelSftp.put(localPath, ftpPath);
        } catch (SftpException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }*/


    public static String getTempFilePath() {

        return System.getProperty("user.home") + File.separator + "tmp" + File.separator;
    }

}
