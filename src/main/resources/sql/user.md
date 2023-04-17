getUser
===
    select * from user;

getUserNameAndPassword
===
    select user_id,user_login_name,user_name,user_password from user where user_login_name = #{userName}

saveUser
===
    INSERT INTO `ams`.`user` (`user_login_name`, `user_name`, `user_password`) VALUES (#{userName}, #{userName}, #{password}); 

getUserList
===
    select * from `user` order by create_time limit #{current},#{pageSize}

getUserCount
===
    select count(1) as count from `user`
