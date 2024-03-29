package com.waiter.waiter.auth;

import com.waiter.waiter.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/orders/reference-cook").hasAnyAuthority("COOK")
                .mvcMatchers("/orders/reference-waiter", "/tables/**").hasAnyAuthority("WAITER")
                .mvcMatchers("/menu/*", "/menu","/login", "/register", "/test","/","/drinks/**","/dishes/**","/orders/***","/orders/**","/orders/****","/", "/tablesTest", "/testCardColor").permitAll()
                .mvcMatchers("/login", "/register", "/test","/","/drinks/**","/dishes/**","/tables/**","/orders/***","/orders/**","/order/****","/", "/tablesTest","/order-dish","/order-dish/*","/order-dish/**","/order-dish/***","/order-drink","/order-drink/*","/order-drink/**","/order-drink/***","/order-drink/****","/order/add-drink-to-order").permitAll()
                .mvcMatchers("/","/drinks/**","/dishes/**", "/tables/**", "/hello").hasAnyAuthority("WAITER", "COOK")
                .anyRequest().hasAnyAuthority("ROLE_ADMIN")
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().permitAll();
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**","/js/**","/css/**","/webjars/**");
    }
}