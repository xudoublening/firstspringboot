package com.example.firstspringboot.controller;

import com.example.firstspringboot.dto.GnSysConfig;
import com.example.firstspringboot.dto.Student;
import com.example.firstspringboot.service.GnSysConfigService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/test")
public class HelloController {

    private static final Log log = LogFactory.getLog(HelloController.class);

    @Autowired private GnSysConfigService gnSysConfigService;

    @RequestMapping(value = {"/testParam"}, method = RequestMethod.GET)
    public String testParam(@RequestParam(value = "giveFee",required = false)String giveFee,
            @RequestParam(value = "giveValue")String giveValue) {
        return "giveFee: "+giveFee+"; giveValue: "+giveValue;
    }
    
    @RequestMapping(value = {"/hello"}, method = RequestMethod.GET)
    public String hello() {
        return "hello, you !";
    }

    @RequestMapping(value = {"/getstudents"}, method = RequestMethod.GET)
    public List<Student> getStudent() {
        log.debug("查询所有学生>>>>>>>>>>>>>debug");

        List<Student> stu = new ArrayList<>();
        stu.add(getStu101());
        stu.add(getStu102());
        stu.add(getStu103());
        return stu;
    }
    @RequestMapping(value = {"/getAllConfig"}, method = RequestMethod.GET)
    public List<GnSysConfig> getAllConfig() {
        log.debug("查询所有配置>>>>>>>>>>>>>debug");


        return gnSysConfigService.getAll();
    }

    @GetMapping("/{id}")
    public Student getOne(@PathVariable int id){

        log.debug("getOne()"+id);

        switch (id){
            case 101:
                return getStu101();
            case 102:
                return getStu102();
            case 103:
                return getStu103();
            default:
                log.info("没找到"+id);
                return null;
        }
    }

    @PostMapping
    public Student insertStu(@RequestBody Student student){
        log.debug("insertStu() 传入参数为:"+student);

        //设置新增数据id
        student.setNumber(110);
        return student;
    }

    @PostMapping(value = {"/{id}/photos"},consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFile(@PathVariable int id, @RequestParam("photo_file")MultipartFile photo)throws Exception{
        log.debug("接收文件的Stu id:"+id+",文件:"+photo.getOriginalFilename());

        FileOutputStream ops = new FileOutputStream("target/"+photo.getOriginalFilename());
        IOUtils.copy(photo.getInputStream(),ops);
        ops.close();
    }

    @GetMapping(value = {"/{id}/icon"},produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] downloadIcon(@PathVariable int id)throws Exception{
        log.debug("downLoadIcon() "+id);

        String icon = "src/test/resource/IMG1.jpg";
        InputStream is = new FileInputStream(icon);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len = 0;
        byte[] b = new byte[1024];
        while ((len = is.read(b)) != -1){
            out.write(b,0,len);
        }
        return out.toByteArray();
    }

    @PutMapping("/{id}")
    public Student updateStu(@PathVariable int id,@RequestBody Student student)throws Exception{
        log.debug("updateStu() "+id+">>>>"+student);

        //根据数据库 进行更新
        if (id == 101 || id == 102 || id == 103){
            return getStu101();
        }else{
            throw new RuntimeException("无效的id:"+id);
        }
    }

    @DeleteMapping("/{id}")
    public Map<String,String> deleteStu(@PathVariable int id, HttpServletRequest request,
                         @RequestParam(value = "delete_reason",required = false)String reason)throws Exception{
        log.debug("deleteStu() "+id+" ; deleteReason:"+reason);
        Map<String,String> result = new HashMap<>();
        if (id == 101){
            result.put("message","删除id:"+id+"  删除原因:"+reason);
        }
        else if (id == 102 || id == 103){
            throw new RuntimeException("id: 102 || 103 , 不可删除");
        }
        else{
            throw new NotFound();
        }
        return result;
    }

    private Student getStu101(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2014,Calendar.SEPTEMBER,1,0,0);
        return new Student(101,"LiNa", 18, "女",calendar.getTime());
    }
    private Student getStu102(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2012,Calendar.SEPTEMBER,1,0,0);
        return new Student(102,"Xnn",20,"男",calendar.getTime());
    }
    private Student getStu103(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013,Calendar.SEPTEMBER,1,0,0);
        return new Student(103,"WJ",19,"男",calendar.getTime());
    }
}
