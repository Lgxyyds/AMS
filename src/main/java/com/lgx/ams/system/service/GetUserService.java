package com.lgx.ams.system.service;

import com.lgx.ams.system.dao.UserMapper;
import com.lgx.ams.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetUserService {
    @Autowired
    private UserMapper userMapper;

    public List<User> getUser(){
        return userMapper.getUser();
    }


}
