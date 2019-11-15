package com.example.firstspringboot.service.impls;

import com.example.firstspringboot.dao.IUserJoinAnnalDao;
import com.example.firstspringboot.dto.UserJoinAnnal;
import com.example.firstspringboot.service.interfaces.IStatisticsSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Xnn on 2019/11/5 17:46
 */
@Service
public class StatisticsImp implements IStatisticsSV {
    
    @Autowired
    private IUserJoinAnnalDao iUserJoinAnnalDao;
    
    @Override
    public void saveAnnal(UserJoinAnnal userJoinAnnal) {
        iUserJoinAnnalDao.save(userJoinAnnal);
    }
}
