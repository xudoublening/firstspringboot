package com.example.firstspringboot.service.impls;

import com.example.firstspringboot.commons.Constants;
import com.example.firstspringboot.service.interfaces.IRedisSV;
import com.example.firstspringboot.utils.RedisUtil;
import com.example.firstspringboot.vo.InformationVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RedisSVimpl implements IRedisSV {
    private static final Log log = LogFactory.getLog(RedisSVimpl.class);

    @Override
    public List<InformationVo> getInformationAll(Integer num) throws Exception {
        List<String> newsKey = RedisUtil.getRedisListByKey(Constants.InformationType.ALL_INFORMATION,Long.parseLong(num+""));
        RedisUtil.getRedis().close();
        return readToInformationVo(newsKey);
    }

    @Override
    public List<InformationVo> getInformation(String type) throws Exception {
        List<String> newsKey = RedisUtil.getRedisListByKey(type,-1L);
        RedisUtil.getRedis().close();
        return readToInformationVo(newsKey);
    }

    @Override
    public InformationVo getHashMapById(String infoId) throws Exception {

        InformationVo informationVo = new InformationVo();
        Map<String,String> map = RedisUtil.getMapByKey(infoId);
        if (map != null) {
            informationVo.setNewsId(infoId);
            informationVo.setDate(map.get(InformationVo.DATE));
            informationVo.setTitle(map.get(InformationVo.TITLE));
            informationVo.setDivInfo(map.get(InformationVo.DIVINFO));
        }
        RedisUtil.getRedis().close();
        return informationVo;
    }
    private List<InformationVo> readToInformationVo(List<String> newsKey) throws Exception{
        List<InformationVo> list = new ArrayList<>();
        if (newsKey != null && newsKey.size() > 0){
            for (String key : newsKey) {
                InformationVo informationVo = new InformationVo();
                informationVo.setNewsId(key);
                informationVo.setTitle(RedisUtil.getRedis().hget(key,InformationVo.TITLE));
                informationVo.setDate(RedisUtil.getRedis().hget(key,InformationVo.DATE));
                list.add(informationVo);
            }
        }
        RedisUtil.getRedis().close();
        return list;
    }
}
