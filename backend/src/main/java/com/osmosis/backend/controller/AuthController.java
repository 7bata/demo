package com.osmosis.backend.controller;

import com.osmosis.backend.entity.Account;
import com.osmosis.backend.entity.RestBean;
import com.osmosis.backend.mapper.UserMapper;
import com.osmosis.backend.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Resource
    AuthorizeService authorizeService;
    
    @Resource
    UserMapper userMapper;
    
    @GetMapping("/check-role")
    public RestBean<String> checkUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            // 获取用户的权限列表
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                    .orElse("unknown");
            return RestBean.success(role);
        } else {
            return RestBean.failure(401, "未登录");
        }
    }
    
    @GetMapping("/validate-role")
    public RestBean<Boolean> validateUserRole(@RequestParam String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            // 验证用户是否具有指定角色
            boolean hasRole = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
            
            if(hasRole) {
                return RestBean.success(true);
            } else {
                return RestBean.failure(403, false);
            }
        } else {
            return RestBean.failure(401, false);
        }
    }
    
    /**
     * 获取当前登录用户的信息
     */
    @GetMapping("/current-user")
    public RestBean<Map<String, Object>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Account account = userMapper.findByUsernameOrEmail(username);
            if(account != null) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", account.getId());
                userInfo.put("username", account.getUsername());
                userInfo.put("email", account.getEmail());
                userInfo.put("role", account.getRole());
                return RestBean.success(userInfo);
            }
        }
        return RestBean.error(401, "未登录或用户不存在");
    }
} 