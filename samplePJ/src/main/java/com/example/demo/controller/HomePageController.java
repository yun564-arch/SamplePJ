package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomePageController {

    private final HttpSession session;

    public HomePageController(HttpSession session) {
        this.session = session;
    }

    // ホームページを表示するメソッド
    @RequestMapping("/")
    public String index() {
        session.invalidate();
        return "homePage";
    }
}