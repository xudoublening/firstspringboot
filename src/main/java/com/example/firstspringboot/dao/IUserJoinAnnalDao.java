package com.example.firstspringboot.dao;

import com.example.firstspringboot.dto.UserJoinAnnal;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * 功能描述: 用户连接记录表
 * 〈〉
 * @author: xnn
 * on 2019/11/5 17:05
 */
public interface IUserJoinAnnalDao {
    @Insert("INSERT INTO user_join_annal "
            + "(`user_id`, `app_id`, `mark_id`, `login_ip`, `city_code`, `annal_time`, `annal_month`, `annal_year`) "
            + "VALUES (#{userId},#{appId},#{markId},#{loginIp},#{cityCode},#{annalTime},#{annalMonth},#{annalYear})")
    void save(UserJoinAnnal userJoinAnnal);
}
