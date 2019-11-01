package com.example.firstspringboot.service.interfaces;

import com.example.firstspringboot.vo.BaseResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Controller
public interface IInformationSV {
    /**
     * 保存新的新闻文件
     * @param file
     * @return
     * @throws Exception
     */
    public BaseResponse saveNewInformation(MultipartFile file,Integer type) throws Exception;

    /**
     * 获取首页显示的最新 几条资讯
     * @return
     */
    public BaseResponse getIndexShow() throws Exception ;

    /**
     * 根据传入id 删除资讯
     * @param id
     * @return
     * @throws Exception
     */
    public BaseResponse delInformationById(String id)throws Exception;

    /**
     * 初始化 资讯
     * @throws Exception
     */
    public void initialization() throws Exception;
}
