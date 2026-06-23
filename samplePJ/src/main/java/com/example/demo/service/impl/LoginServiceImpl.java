package com.example.demo.service.impl;

import com.example.demo.entity.Login;
import com.example.demo.mapper.LoginMapper;
import com.example.demo.service.LoginService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final LoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;

    public LoginServiceImpl(LoginMapper loginMapper, PasswordEncoder passwordEncoder) {
        this.loginMapper = loginMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Login findByUsername(String username) {
        return loginMapper.findByUsername(username);
    }

    // ログイン・登録共通の入力チェック
    private void validateLogin(Login login) {
        if (login.getUsername() == null || login.getUsername().isEmpty()) {
            throw new IllegalArgumentException("ユーザー名を入力してください");
        }
        if (login.getPassword() == null || login.getPassword().isEmpty()) {
            throw new IllegalArgumentException("パスワードを入力してください");
        }
        if (!login.getUsername().matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("ユーザー名は半角英数字で入力してください");
        }
        if (!login.getPassword().matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("パスワードは半角英数字で入力してください");
        }
    }

    // 登録専用チェック(共通チェック + 重複チェック)
    private void validateRegister(Login login) {
        validateLogin(login);
        if (loginMapper.findByUsername(login.getUsername()) != null) {
            throw new IllegalArgumentException("このユーザー名は既に使用されています");
        }
    }

    public void save(Login login) {
        validateRegister(login);
        login.setPassword(passwordEncoder.encode(login.getPassword())); // ハッシュ化してから保存
        loginMapper.save(login);
    }

    // ログイン認証をServiceに集約
    public boolean authenticate(String username, String rawPassword) {
        Login login = loginMapper.findByUsername(username);
        if (login == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, login.getPassword());
    }
}