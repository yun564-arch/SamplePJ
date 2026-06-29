package com.example.demo.config;

import com.example.demo.entity.Login;
import com.example.demo.mapper.LoginMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final LoginMapper loginMapper;

    public UserDetailsServiceImpl(LoginMapper loginMapper) {
        this.loginMapper = loginMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Login login = loginMapper.findByUsername(username);
        if (login == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません");
        }
        return User.builder()
                .username(login.getUsername())
                .password(login.getPassword()) // すでにBCryptでハッシュ化済みの値
                .roles("USER")
                .build();
    }
}
