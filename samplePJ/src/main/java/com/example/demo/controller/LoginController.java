package com.example.demo.controller;

import com.example.demo.entity.Login;
import com.example.demo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private HttpSession session;

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

    // ログイン処理
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "password", required = false) String password, ModelAndView mv) {

        Login login = loginService.findByUsername(username);

        if (login == null || !login.getPassword().equals(password)) {
            mv.addObject("error", "ユーザー名またはパスワードが間違っています");
            mv.setViewName("login");
            return mv;
        }

        session.setAttribute("username", username);
        mv.setViewName("redirect:/tasks");
        return mv;
    }
    
    // ログアウト処理
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        session.invalidate();
        return "redirect:/login";
    }    
}