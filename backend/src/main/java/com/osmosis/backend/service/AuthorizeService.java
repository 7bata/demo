package com.osmosis.backend.service;


import com.osmosis.backend.entity.Account;
import com.osmosis.backend.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeService implements UserDetailsService {
    @Resource
    UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null)
            throw new BadCredentialsException("用户名不能为空");
        Account account = mapper.findByUsernameOrEmail(username);
        if (account == null)
            throw new BadCredentialsException("用户名或密码错误");
        
        // 获取用户角色，如果为空则默认为student
        String role = account.getRole();
        if(role == null || role.isEmpty()) {
            role = "student";
        }
        
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(role)
                .build();
    }
}
