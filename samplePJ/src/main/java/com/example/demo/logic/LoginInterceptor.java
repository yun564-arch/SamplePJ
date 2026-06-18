package com.example.demo.logic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 1. セッションを取得
    	HttpSession session = request.getSession();

        // 2. セッションからusernameを取得
    	String username = (String)session.getAttribute("username");

        // 3. usernameがnullなら/loginにリダイレクト
    	if(username == null) {
    		response.sendRedirect("/login");
    		return false;
    	 // 4. nullでなければそのまま通す（trueを返す）
    	}
        	return true;
       
    }
}
