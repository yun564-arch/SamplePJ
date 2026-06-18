package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomePageController {
	
	@Autowired
	HttpSession session;


    // ホームページを表示するメソッド
    @RequestMapping("/")
    public String index() {
    	session.invalidate();
    	return "homePage";

    }
}