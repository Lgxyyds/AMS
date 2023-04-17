package com.lgx.ams.system.controller;


import com.lgx.ams.system.core.JsonResult;
import com.lgx.ams.system.entity.User;
import com.lgx.ams.system.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static com.lgx.ams.system.core.JsonResult.failMessage;
import static com.lgx.ams.system.core.JsonResult.successMessage;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView login(){
        return new ModelAndView("login");
    }

    @PostMapping(value = "/login.do")
    @ResponseBody
    public JsonResult<String> login(HttpSession session, String loginName, String password) {

        List< Map> maps = loginService.getUserNameAndPassword(loginName);
        if (!maps.isEmpty()){
            String userPassword = (String) maps.get(0).get("userPassword");
            String userLoginName = (String) maps.get(0).get("userLoginName");
            String userId = maps.get(0).get("userId").toString();
            if(userLoginName.equals(loginName)&&userPassword.equals(password)){

                User user = new User();
                user.setUserPassword(userPassword);
                user.setUserLoginName(userLoginName);
                user.setUserId(userId);
                session.setAttribute("loginUser",user);
                return successMessage("登录成功");
            }else {
                return failMessage("密码错误");
            }
        }else {
            return failMessage("用户名不存在");
        }
    }

    @RequestMapping("/logout")
    @ResponseBody
    public String logout(HttpSession session){
        session.invalidate();
        return "1";
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView index()
    {
        return new ModelAndView("index");
    }

    //跳转到注册页面
    @RequestMapping(value = "/register",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView register()
    {
        return new ModelAndView("register");
    }

    //检查输入用户名是否已存在
    @GetMapping("/checkUser")
    @ResponseBody
    public String checkUser(String username) {
        String json = ""; //返回的JSON字符串
        List<Map> user = loginService.getUserNameAndPassword(username);
        if (user.isEmpty()) {
            json = "yes";
        } else {
            json = "no";
        }
        return json;
    }

    //用户注册
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public String regist(String username, String password) {
        boolean result = loginService.saveUser(username,password);
        if (result) {
            return "1";
        } else {
            return "0";
        }
    }

    //注册成功返回页面
    @RequestMapping(value = "/success",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView success()
    {
        return new ModelAndView("success");
    }

}
