package com.example.firstspringboot.service;

import com.example.firstspringboot.dao.GnSysConfigDao;
import com.example.firstspringboot.dto.GnSysConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GnSysConfigService {

    @Autowired
    private GnSysConfigDao gnSysConfigDao;

    public List<GnSysConfig> getAll(){
        return gnSysConfigDao.getAll();
    }
}
