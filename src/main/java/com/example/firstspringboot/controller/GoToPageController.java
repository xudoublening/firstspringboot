package com.example.firstspringboot.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.formula.functions.Index;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pageto")
public class GoToPageController {
    private static final Log log = LogFactory.getLog(GoToPageController.class);

    @GetMapping("/toindex")
    public String goToIndex(Model model){
        log.info("去首页...");
        model.addAttribute("index",new Index());
        return "/index";
    }
}
