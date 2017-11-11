package com.stock.controller.user;

import com.stock.model.model.StockUserModel;
import com.stock.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by 杨蕾 on 2017/11/11.
 */
@Controller
@RequestMapping({"/user"})
public class UserMangerController
{
    private boolean isInitialize;
    private UserService userService;

    @RequestMapping({"/query"})
    public String queryUser(@RequestParam(value = "index", defaultValue = "1")int pageIndex, @RequestParam(value = "size", defaultValue = "1")int pageSize, Model model){
        if(!isInitialize){
            userService = UserService.getInstance();
        }

        List<StockUserModel> users = userService.selectUserInfo(String.valueOf(pageIndex));

        model.addAttribute("users", users);
        return "usermanager";
    }
}
