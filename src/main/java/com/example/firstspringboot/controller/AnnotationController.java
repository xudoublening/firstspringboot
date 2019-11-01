package com.example.firstspringboot.controller;

import com.example.firstspringboot.dto.ZhuJieTestDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/annotation")
public class AnnotationController {
    private static final Log log = LogFactory.getLog(AnnotationController.class);

    @RequestMapping(value = {"/test01"},method = RequestMethod.POST)
    public ZhuJieTestDto test01(@Valid @RequestBody ZhuJieTestDto zhuJieTestDto){
        log.info(">>>>>test01 ");
        zhuJieTestDto.setId(11101);
        return zhuJieTestDto;
    }
    //当验证不通过时也进入方法执行  BindingResult 将验证结果参数传递进来
    @RequestMapping(value = {"/test02"},method = RequestMethod.POST)
    public ZhuJieTestDto test02(@Valid @RequestBody ZhuJieTestDto zhuJieTestDto, BindingResult result){
        log.info(">>>>>test02 ");
        if (result.hasErrors()){
            log.info("验证不通过: "+result.toString());
        }
        zhuJieTestDto.setId(11102);
        return zhuJieTestDto;
    }
}
