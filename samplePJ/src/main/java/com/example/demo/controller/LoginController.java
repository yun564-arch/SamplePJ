package com.example.demo.controller;

import com.example.demo.entity.Login;
import com.example.demo.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final LoginService loginService;
    private final HttpSession session;

    public LoginController(LoginService loginService, HttpSession session) {
        this.loginService = loginService;
        this.session = session;
    }

    // ユーザー登録画面を表示
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String index() {
        session.invalidate();
        return "register";
    }

    // ユーザー登録処理
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute Login login, ModelAndView mv) {
        try {
            loginService.save(login);
            mv.setViewName("redirect:/login");
        } catch (IllegalArgumentException error) {
            mv.addObject("error", error.getMessage());
            mv.addObject("login", login);
            mv.setViewName("register");
        }
        return mv;
    }

    // ログイン画面を表示
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(ModelAndView mv) {
        mv.setViewName("login");
        return mv;
    }
}