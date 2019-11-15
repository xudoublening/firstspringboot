package com.example.firstspringboot.controller;

import com.example.firstspringboot.dto.UserJoinAnnal;
import com.example.firstspringboot.service.interfaces.IStatisticsSV;
import com.example.firstspringboot.vo.BaseResponse;
import java.sql.Timestamp;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 功能描述: 统计相关接口
 *
 * @author: xnn
 */
@Controller
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {
    
    @Autowired
    private IStatisticsSV iStatisticsSV;
    
    /**
     * 功能描述: 计数 〈〉 appId:应用id  markId:统计位标识
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
        UserJoinAnnal userJoinAnnal = new UserJoinAnnal();
        userJoinAnnal.setUserId(0);
        userJoinAnnal.setAppId(appId);
        userJoinAnnal.setMarkId(markId);
        userJoinAnnal.setLoginIp(ip);
        userJoinAnnal.setAnnalTime(new Timestamp(System.currentTimeMillis()));
        userJoinAnnal.setCityCode(001);
        userJoinAnnal.setAnnalMonth("201911");
        userJoinAnnal.setAnnalYear("2019");
        iStatisticsSV.saveAnnal(userJoinAnnal);
    }
    
    /**
     * 功能描述: 给用户新建应用,返回一个应用id
     * 〈〉
     * @param: []
     * @return: com.example.firstspringboot.vo.BaseResponse
     * @author: xnn
     */
    @RequestMapping("/createApp")
    public BaseResponse createApp(@RequestParam("cust_id")String custId){
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
