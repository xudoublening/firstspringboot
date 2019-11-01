package com.example.firstspringboot.service.interfaces;

import com.example.firstspringboot.vo.InformationVo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public interface IRedisSV {
    /**
     * 获取 资讯集合
     * @param type 新闻 公告
     * @return
     * @throws Exception
     */
    public List<InformationVo> getInformation(String type) throws Exception;

    /**
     * 根据id 获取资讯详情
     * @param infoId
     * @return
     * @throws Exception
     */
    public InformationVo getHashMapById(String infoId)throws Exception;

    /**
     * 获取全部资讯中(不分类) 指定条数
     * @param num 数据条数
     * @return
     * @throws Exception
     */
    public List<InformationVo> getInformationAll(Integer num) throws Exception;
}
