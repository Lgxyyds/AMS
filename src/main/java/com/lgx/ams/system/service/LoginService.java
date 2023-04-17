package com.lgx.ams.system.service;

import com.lgx.ams.system.dao.UserMapper;
import com.lgx.ams.system.entity.User;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LoginService{

    @Autowired
    private UserMapper userMapper;

    @Resource
    private SQLManager sqlManager;

    public List<Map> getUserNameAndPassword(String userName){
        List<Map> map = userMapper.getUserNameAndPassword(userName);
        return map;
    }

    public boolean saveUser(String userName,String password){
        boolean flag = false;
        User user = new User();
        String uuid = UUID.randomUUID().toString();
        user.setUserId(uuid);
        user.setUserName(userName);
        user.setUserLoginName(userName);
        user.setUserPassword(password);
        user.setCreateTime(new Date());
        Query<User> query = sqlManager.query(User.class);
        int i1 = query.insert(user);
//        int i = userMapper.saveUser(userName, password);
        if (i1>0){
            flag = true;
        }
        return flag;
    }
}
