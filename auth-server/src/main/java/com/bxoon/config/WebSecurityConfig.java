package com.bxoon.config;

import com.bxoon.util.Md5Util;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * WebSecurityConfig
 *
 * @author fengzheng
 * @date 2019/10/10
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 密码的加密方式
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 也可以自定义密码的加密方式
     */
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new PasswordEncoder() {
//
//            @Override
//            public boolean matches(CharSequence rawPassword, String encodedPassword) {
//                return encodedPassword.equals(Md5Util.str2Base32MD5((String)rawPassword));
//            }
//
//            @Override
//            public String encode(CharSequence rawPassword) {
//                return Md5Util.str2Base32MD5((String)rawPassword);
//            }
//        };
////       return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

    /**
     * 授权码模式需要用此对象
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 允许匿名访问所有接口 主要是 oauth 接口
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll();
    }
}
