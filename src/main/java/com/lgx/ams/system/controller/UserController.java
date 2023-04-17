package com.lgx.ams.system.controller;

import com.lgx.ams.system.entity.File;
import com.lgx.ams.system.entity.User;
import com.lgx.ams.system.service.LoginService;
import com.lgx.ams.system.service.UserService;
import com.lgx.ams.system.util.FileUtil;
import com.lgx.ams.system.util.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/main/userList")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @GetMapping("/list")
    public String list(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("loginUser");
        String pageIndex = request.getParameter("pageIndex");

        //第一次请求肯定是走第一页，页面大小固定
        //设置页面大小
        int pageSize = 10;
        //当前页码
        int currentPageNo = 1;

        if (pageIndex != null){
            currentPageNo = Integer.parseInt(pageIndex);
        }
        //获取用户总数（分页 上一页：下一页的情况）
        //总数量
        int totalCount = userService.getUserCount();
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = pageSupport.getTotalPageCount();//总共有几页
        //控制首页和尾页
        if(currentPageNo<1){
            currentPageNo = 1;
        }else if (currentPageNo>totalPageCount){
            currentPageNo = totalPageCount;
        }

        List<User> userList = userService.getUserList((currentPageNo-1)*pageSize,pageSize);

        model.addAttribute("list",userList);
        model.addAttribute("totalRow",totalCount);
        model.addAttribute("toTalPage", totalPageCount);
        model.addAttribute("currentPageNo",currentPageNo);
        return "main/userList";
    }


    @GetMapping("/update")
    public String upload(@RequestParam("userId") String id, Model model) {
        List<User> users = userService.getById(id);
        User user = users.get(0);
        model.addAttribute("user",user);
        return "main/userUpdate";
    }

    @PostMapping("/update")
    public String update(@RequestParam("userId") String id,String userName, String userLoginName, String userPassword, String userSex, String userMobilePhone,String userQq,String userEmail) {

        Date date = new Date();//创建时间
        User user = new User();//创建Document实体类用于保存

        user.setUserName(userName);
        user.setUserLoginName(userLoginName);
        user.setUserPassword(userPassword);
        user.setUserSex(userSex);
        user.setUserMobilePhone(userMobilePhone);
        user.setUserQq(userQq);
        user.setUserEmail(userEmail);
        user.setUpdateTime(new Date());

        boolean flag = userService.updateFile(id, user);
        if (flag){
            return "redirect:/main/userList/list";
        }else {
            return "修改失败";
        }
    }

    //根据id删除文件
    @GetMapping("/delete")
    @ResponseBody
    public String delete(String id) {

        Boolean flag = userService.deleteFileById(id);
        if (flag) {
            return "1";
        }
        return "0";
    }

    //查看用户信息
    @GetMapping("/userView")
    public String userView(@RequestParam("userId") String id,Model model) {
        System.out.println(id);
        List<User> users = userService.getById(id);
        User user = users.get(0);
        model.addAttribute("user",user);
        return "main/userView";
    }

    @GetMapping("/userAdd")
    public String add() {
        return "main/userAdd";
    }

    @PostMapping("/userAdd")
    @ResponseBody
    public String userAdd( String userName, String userLoginName, String userPassword, String userSex, String userMobilePhone,String userQq,String userEmail) {

        Date date = new Date();//创建时间
        User user = new User();//创建Document实体类用于保存
        String uuid = UUID.randomUUID().toString();
        user.setUserId(uuid);
        user.setUserName(userName);
        user.setUserLoginName(userLoginName);
        user.setUserPassword(userPassword);
        user.setUserSex(userSex);
        user.setUserMobilePhone(userMobilePhone);
        user.setUserQq(userQq);
        user.setUserEmail(userEmail);
        user.setCreateTime(date);

        boolean b = userService.saveUser(user);
        if (b){
            return "添加成功";
        }else {
            return "添加成功";
        }
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

}
