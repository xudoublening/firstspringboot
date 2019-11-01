package com.example.firstspringboot.controller;

import com.example.firstspringboot.vo.BaseResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 功能描述: 统计相关接口
 *
 * @author: xnn
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {
    
    /**
     * 功能描述: 计数 〈〉
     *
     * @param: [request, response]
     * @return: void
     * @author: xnn
     */
    @RequestMapping("/count/{appId}/{markId}")
    public void count(@PathVariable String appId, @PathVariable String markId,
            HttpServletRequest request, HttpServletResponse response) {
    
        String ip = getServletIP(request);
        log.info("appId=" + appId + ";markId=" + markId + ";ip=" +ip);
        
    }
    
    /**
     * 功能描述: 新建应用
     * 〈〉
     * @param: []
     * @return: com.example.firstspringboot.vo.BaseResponse
     * @author: xnn
     */
    @RequestMapping("/createApp")
    public BaseResponse createApp(){
        log.info("==新建应用==");
        BaseResponse response = new BaseResponse();
        
        return response;
    }
    
    /**
     * 功能描述: 创建统计标识
     * 〈〉
     * @param: []
     * @return: com.example.firstspringboot.vo.BaseResponse
     * @author: xnn
     */
    @RequestMapping("/createMark/{appId}")
    public BaseResponse createMark(@PathVariable String appId){
        log.info("==新建应用==");
        BaseResponse response = new BaseResponse();
        
        return response;
    }
}
