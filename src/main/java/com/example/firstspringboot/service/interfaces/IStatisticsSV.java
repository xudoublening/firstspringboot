package com.example.firstspringboot.service.interfaces;

import com.example.firstspringboot.dto.UserJoinAnnal;
import org.springframework.stereotype.Controller;

/**
 * 功能描述: 统计服务
 * 〈〉
 * @param:
 * @return:
 * @author: xnn
 * on 2019/11/1 11:22
 */
@Controller
public interface IStatisticsSV {
    
    public void saveAnnal(UserJoinAnnal userJoinAnnal);
    
}
