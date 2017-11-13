package com.stock.controller.user;

import com.stock.dao.UserDao;
import com.stock.model.model.StockUserModel;
import com.stock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨蕾 on 2017/11/11.
 */
@Controller
@RequestMapping({"/user"})
public class UserMangerController {
//    @Autowired
//    private UserDao userDao;

    private boolean isInitialize;
    private UserService userService;

    @RequestMapping({"/query"})
    public String queryUser(@RequestParam(value = "index", defaultValue = "1") int pageIndex, @RequestParam(value = "size", defaultValue = "20") int pageSize, Model model) {
        if (!isInitialize) {
            userService = UserService.getInstance();
            isInitialize = true;
        }

//        if (pageIndex <= 1) {
//            pageIndex = 1;
//            pageSize = pageSize != 20 ? 20 : pageSize;
//            int userTotalCount = this.userDao.getUserTotalCount();
//            int pageCount = (int) Math.ceil(userTotalCount / pageSize);
//            List<Integer> pages = new ArrayList<>();
//            for (int i = 1; i <= pageCount; i++) {
//                pages.add(i);
//            }
//            model.addAttribute("pages", pages);
//            model.addAttribute("pageCount", pageCount);
//        }

        List<StockUserModel> users = userService.selectUserInfo(String.valueOf(pageIndex));
        model.addAttribute("users", users);

        return "usermanager";
    }
}
