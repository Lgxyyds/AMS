package com.lgx.ams.system.controller;

import com.lgx.ams.system.entity.User;
import com.lgx.ams.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @RequestMapping("/main/welcome")
    public String home(Model model) {
//        int count = userService.count();
//        model.addAttribute("count", count);
        model.addAttribute("time", new Date());
        return "main/welcome";
    }

    @RequestMapping("/main/information")
    public String UserInformation(@RequestParam("userId") String id,Model model) {
        List<User> users = userService.getById(id);
        User user = users.get(0);
        model.addAttribute("user",user);
        return "main/information";
    }

}
