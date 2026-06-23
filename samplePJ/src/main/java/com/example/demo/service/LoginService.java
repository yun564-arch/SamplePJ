package com.example.demo.service;

import com.example.demo.entity.Login;

public interface LoginService {

    // ユーザーを名前で検索するメソッド
    Login findByUsername(String username);

    // ユーザーを登録するメソッド
    void save(Login login);

    // ユーザー名とパスワードで認証するメソッド
    boolean authenticate(String username, String rawPassword);

}