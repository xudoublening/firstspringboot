package com.example.firstspringboot.dao;

import com.example.firstspringboot.dto.GnSysConfig;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

public interface GnSysConfigDao {

    @Select("select * from gn_sys_config")
    public List<GnSysConfig> getAll();
}
