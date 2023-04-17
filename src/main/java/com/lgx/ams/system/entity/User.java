package com.lgx.ams.system.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;
    private String userLoginName;
    private String userName;
    private String userPassword;
    private String userSex;
    private String userMobilePhone;
    private String userEmail;
    private String userQq;
    private Date createTime;
    private Date updateTime;

}
