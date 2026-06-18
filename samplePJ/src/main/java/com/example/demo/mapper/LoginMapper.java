package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.entity.Login;

@Mapper
public interface LoginMapper {

    // ユーザーを名前で検索するメソッド
    Login findByUsername(String username);
    // ユーザーを登録するメソッド
    void save(Login login);

}