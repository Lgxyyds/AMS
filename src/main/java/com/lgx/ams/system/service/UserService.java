package com.lgx.ams.system.service;

import com.lgx.ams.system.dao.UserMapper;
import com.lgx.ams.system.entity.File;
import com.lgx.ams.system.entity.User;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.query.Query;
import org.beetl.sql.mapper.annotation.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    private SQLManager sqlManager;
    @Autowired
    private UserMapper userMapper;

    public List<User> getUserList(int current, int pageSize){
        return userMapper.getUserList(current,pageSize);
    }

    public int getUserCount(){
        return userMapper.getUserCount();
    }

    public List<User> getById(String id){
        Query<User> query = sqlManager.query(User.class);
        List<User> userList = query.andEq("user_id", id).select();
        return userList;
    }

    //修改用户信息
    public Boolean updateFile(String id,User user){
        boolean flag = false;
        Query<User> query = sqlManager.query(User.class);
        int count = query.andEq("user_id", id).updateSelective(user);
        if (count>0){
            flag = true;
        }
        return flag;
    }

    //根据id删除
    public Boolean deleteFileById(String id){
        boolean flag = false;
        Query<User> query = sqlManager.query(User.class);
        int id1 = query.andEq("user_id", id).delete();
        if (id1>0){
            flag = true;
        }
        return flag;
    }

    //保存用户
    public boolean saveUser(User user){
        boolean flag = false;
        Query<User> query = sqlManager.query(User.class);
        int i = query.insert(user);
        if (i>0){
            flag = true;
        }
        return flag;
    }

}
