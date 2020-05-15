package io.loiter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

@Service
@Slf4j
public class MyUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("执行时用户名为： {}", username);
        return new User("foo", // 账号
//                "{bcrypt}"+new BCryptPasswordEncoder().encode("foo"), // 密码
                "{bcrypt}$2a$10$wKpi2QbN2fTQX9B5OHUBuO0rhsN8v1XmZcU1FjwEUTZ.w4GaCPkV6",
//                new Md4PasswordEncoder().encode("foo"), // spring security 版本不需要添加{MD4}前缀。
                new ArrayList<>());  /// 权限列表， 授权
    }
}
