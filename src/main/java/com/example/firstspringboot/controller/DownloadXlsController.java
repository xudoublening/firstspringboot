package com.example.firstspringboot.controller;

import com.example.firstspringboot.dto.GnSysConfig;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: firstspringboot
 * @description: 表格文件下载
 * @author: XNN
 * @create: 2020-10-18 21:26
 **/
@Controller
@RequestMapping("/downloadXls")
public class DownloadXlsController {
    private static String getName = "get";

    private static String dataFormat = "yyyy-MM-dd";


    public static <T> void exportExcelData(String title, String[] headers,
           String[] colums, Collection<T> dataset, HttpServletResponse response) throws Exception {
        XSSFWorkbook workBook = null;

        try {
            // 创建一个workbook 对应一个excel应用文件
            workBook = new XSSFWorkbook();
            // 在workbook中添加一个sheet,对应Excel文件中的sheet
            XSSFSheet sheet = workBook.createSheet(title);
            // 设置表格默认列宽度为15个字节
            sheet.setDefaultColumnWidth((short) 15);

            // 构建表头
            XSSFRow headRow = sheet.createRow(0);
            XSSFCell cell = null;
            for (int i = 0; i < headers.length; i++) {
                cell = headRow.createCell(i);
                XSSFRichTextString text = new XSSFRichTextString(headers[i]);
                cell.setCellValue(text);
            }


            // 构建表体数据
            Iterator<T> it = dataset.iterator();
            int index = 0;
            while (it.hasNext()) {
                index++;
                XSSFRow bodyRow = sheet.createRow(index);
                T t = (T) it.next();

                for (int i = 0; i < colums.length; i++) {
                    cell = bodyRow.createCell(i);

                    String fieldName = colums[i];
                    String getMethodName = getName + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                    Class<? extends Object> tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});

                    String textValue = null;
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
                        textValue = sdf.format(date);
                    } else if (value != null) {
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();
                    } else {
                        textValue = " ";
                    }

                    cell.setCellValue(textValue);
                }
            }

            workBook.write(response.getOutputStream());
        } finally {
            if (workBook != null) {
                workBook.close();
            }
        }
    }

    @RequestMapping("/test2")
    public void test2(HttpServletResponse response) throws Exception {
        long startTime = System.currentTimeMillis();
        String title = "test2";
        String[] headers = {"key","类型","值","状态","备注"};
        String[] colums = {"cfgKey","cfgType","cfgValue","state","remark"};
        List<GnSysConfig> list = new ArrayList<>();
        GnSysConfig config = null;
        for (int i=0; i < 1000;i++){
            config = new GnSysConfig();
            config.setCfgType("test2");
            config.setRemark("测试2");
            config.setCfgKey("key-" + i);
            config.setCfgValue(String.valueOf(i));
            config.setState(i%2==1?"1":"0");
            list.add(config);
        }
        System.out.println("数据生成耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        exportExcelData(title,headers,colums,list,response);
    }

    @RequestMapping("/test1")
    public void downloadTest1(HttpServletResponse response) throws Exception {

        String tmpFileName = "/tmp" + File.separatorChar + "download_test1.xls";
        File file1 = new File(tmpFileName);
        if (file1.exists()) {
            file1.delete();
        }

        //下载文件
        //设置响应头和客户端保存文件名
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + "HX_number_data.xls");

        OutputStream ops = response.getOutputStream();
        InputStream fis = new BufferedInputStream(new FileInputStream(tmpFileName));
        byte[] b = new byte[2048];
        int length;
        while ((length = fis.read(b)) > 0) {
            ops.write(b, 0, length);
        }
        ops.close();
        fis.close();

    }

}
