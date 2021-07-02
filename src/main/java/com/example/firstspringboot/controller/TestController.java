package com.example.firstspringboot.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Describe：测试接口
 *
 * @author xnn
 * @createTime 2021/03/19 11:02
 */
@RestController
@RequestMapping("/test")
public class TestController {
    
    @ResponseBody
    @PostMapping("/timeout")
    public Object timeOutTest(@RequestBody Object o){
    
        System.out.println("接收到请求,30秒后返回原数据:" + JSONObject.toJSONString(o));
        try {
            Thread.sleep(30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return o;
    }

}
