package com.xdidian.keryhu.auth_server.security;


import com.xdidian.keryhu.auth_server.security.custom.CustomAuthSuccessHandler;
import com.xdidian.keryhu.auth_server.security.custom.CustomAuthenticationFailureHandler;
import com.xdidian.keryhu.auth_server.security.custom.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * @author : keryHu keryhu@hotmail.com
 * @Description : auth-server security主方法。注意不要使用 @RequiredArgsConstructor(onConstructor
 * = @__(@Autowired)))
 * @date : 2016年6月18日 下午8:05:55
 */
@Configuration
@Order(-20)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //也不能，变成 constructor autowired，只能目前这个样子，不要更改
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private CustomAuthSuccessHandler customAuthSuccessHandler;


    /**
     * 自定义的登录访问控制。
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }


    /**
     * 注意对于auth-server 里的url 路由security 控制，都加到 ResourceServerConfig class 里，不要加到此class 方法里。
     * </p>
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .formLogin().loginPage("/login")
                .failureHandler(customAuthenticationFailureHandler)
                .successHandler(customAuthSuccessHandler)
                .permitAll()
                .and()
                .csrf().disable()
                .requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access")
                .and()
                .authorizeRequests().anyRequest().authenticated()
        ;

        // @formatter:on
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }


}
