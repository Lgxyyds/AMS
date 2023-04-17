package com.lgx.ams.system.dao;

import com.lgx.ams.system.entity.File;
import com.lgx.ams.system.entity.User;
import org.beetl.sql.mapper.BaseMapper;
import org.beetl.sql.mapper.annotation.SqlResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@SqlResource("user")
@Repository
public interface UserMapper extends BaseMapper<User> {

    List<User> getUser();
    List<Map> getUserNameAndPassword(String userName);
    int  saveUser(String userName,String password);
    List<User> getUserList(int current, int pageSize);
    int getUserCount();
}
