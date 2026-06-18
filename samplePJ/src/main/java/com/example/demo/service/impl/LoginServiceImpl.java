package com.example.demo.service.impl;

import com.example.demo.entity.Login;
import com.example.demo.mapper.LoginMapper;
import com.example.demo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;

    public Login findByUsername(String username) {
        return loginMapper.findByUsername(username);
    }

    private void validate(Login login) {

        if (login.getUsername() == null || login.getUsername().isEmpty()) {
            throw new IllegalArgumentException("ユーザー名を入力してください");
        }

        if (login.getPassword() == null || login.getPassword().isEmpty()) {
            throw new IllegalArgumentException("パスワードを入力してください");
        }

        if (loginMapper.findByUsername(login.getUsername()) != null) {
            throw new IllegalArgumentException("このユーザー名は既に使用されています");
        }
    }

    public void save(Login login) {
        validate(login);
        loginMapper.save(login);
    }
}