package com.osmosis.backend.config;

import com.alibaba.fastjson.JSONObject;
import com.osmosis.backend.entity.RestBean;
import com.osmosis.backend.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    AuthorizeService authorizeService;
    @Resource
    DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           PersistentTokenRepository tokenRepository) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .failureHandler(this::onAuthenticationFailure)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onAuthenticationSuccess)
                )
                .rememberMe(
                        remember -> remember
                        .rememberMeParameter("remember")
                        .tokenRepository(tokenRepository)
                        .tokenValiditySeconds(3600*24*30)
                )

                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors
                        .configurationSource(this.corsConfigurationSource())
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(this::onAuthenticationFailure)
                )
                .build();
    }
    @Bean
    public PersistentTokenRepository tokenRepository()
    {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOriginPattern("*");
        cors.setAllowCredentials(true);
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        cors.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity security) throws Exception {
        AuthenticationManagerBuilder authBuilder = security.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(authorizeService);
        return authBuilder.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        if (request.getRequestURI().endsWith("/login"))
            response.getWriter().print(JSONObject.toJSONString(RestBean.success("登录成功")));
        else if (request.getRequestURI().endsWith("/logout"))
            response.getWriter().print(JSONObject.toJSONString(RestBean.success("登出成功")));
    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
//        String message;
//        if (exception instanceof BadCredentialsException) {
//            message = "用户名或密码错误";
//        } else {
//            message = exception.getMessage();
//        }
//        response.getWriter().print(JSONObject.toJSONString(RestBean.failure(401, message)));
        //原项目这里是直接返回了一个exception.getMessage()，但是不知道为什么，登录时用户名或密码错误，总是会在返回时直接被Bad Credentials覆盖
        //2025.3.10测试的时候发现AuthorizeService的自定义异常无法被正常抛出，所以在这里直接默认返回“用户名或密码错误”
        //2025.3.11 AuthorizeService那块代码已经修复，这里可以正常返回自定义异常信息，讲原来的UsernameNotFoundException变成了BadCredentialsException
        response.getWriter().print(JSONObject.toJSONString(RestBean.failure(401, exception.getMessage())));
    }
}