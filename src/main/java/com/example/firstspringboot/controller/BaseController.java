package com.example.firstspringboot.controller;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by Xnn on 2019/9/23 16:13
 */
public class BaseController {
    
    private static final String ERROR_IP = "ERROR IP";
    
    public static final Log log = LogFactory.getLog(BaseController.class);
    
    public String getServletIP(HttpServletRequest request){
        String ip = request.getRemoteAddr();
        // 优先取 X-Real-IP
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("0:0:0:0:0:0:0:1".equals(ip)) {
                ip = ERROR_IP;
                return ip;
            }
        }
        if ("unknown".equalsIgnoreCase(ip)) {
            ip = ERROR_IP;
            return ip;
        }
        int index = ip.indexOf(',');
        if (index >= 0) {
            ip = ip.substring(0, index);
        }
        return ip;
    }
}
