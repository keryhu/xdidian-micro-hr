package com.xdidian.keryhu.auth_server.security.custom;


import javax.servlet.http.HttpServletRequest;

import com.xdidian.keryhu.auth_server.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.xdidian.keryhu.auth_server.domain.AuthUserDto;
import com.xdidian.keryhu.auth_server.service.LoginAttemptUserService;
import com.xdidian.keryhu.auth_server.service.UserServiceImpl;
import com.xdidian.keryhu.auth_server.service.Utils;
import com.xdidian.keryhu.auth_server.stream.LoginSuccessProducer;
import lombok.extern.slf4j.Slf4j;


/**
 * 使用自定义的CustomAuthenticationProvider，来判断用户的用户名，密码是否输入的正确，
 * 和记录登录失败 和成功的事件。
 * 处理的顺序，用户名是否存在－>email是否激活－>密码是否匹配。
 * 因为前期注册的用户，没有硬性加上手机号的验证，所以在登录这一块，手机的验证，不作限制。)
 * 2016年7月14日 下午4:28:50
 * keryHu keryhu@hotmail.com
 */
@Component("customAuthenticationProvider")
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    // 这里 不能使用 @RequiredArgsConstructor(onConstructor = @__(@Autowired)) ，
    //  也不能，变成 constructor autowired，只能目前这个样子，不要更改，否则会出错，形成依赖循环
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginSuccessProducer sendSource;
    @Autowired
    private Utils utils;
    @Autowired
    private LoginAttemptUserService loginAttemptUserService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {



        String ip = utils.getIp(request);
        // 为了优先检查这个 ，放在这里，而不是 UserDetailsService，因为这个 优先运行。
        if (loginAttemptUserService.isBlocked(ip)) {
            log.info("您的IP已经被锁定账户了");
            throw new BadCredentialsException(loginAttemptUserService.getBlockMsg());
        }

        UsernamePasswordAuthenticationToken token =
                (UsernamePasswordAuthenticationToken) authentication;
        String name = token.getName();

        UserDetails user = userDetailsService.loadUserByUsername(name);

        //用户点击 登录按钮后，如果email未激活，优先报错处理
        AuthUserDto dto = userService.findByIdentity(name).orElse(null);
        String password = user.getPassword();
        String tokenPassword = (String) token.getCredentials();
        //当email未激活的时候，发送含有email，resendtoken resignuptoken 的message出去，让account-activate
        //来处理后台接口， CustomAuthenticationFailureHandler，直接跳转到含有resendtoken resignuptoken参数的页面

        if (!dto.isEmailStatus()) {
            log.info("email not activated !");
            // 获取到从 acctount-activated 传递过来的email未激活，激活码是否过期的信息。
            String result = userService.getToken(dto.getEmail());
            log.info(result);
            // 为什么这里不使用其他自定义的exception，因为使用其他的exception，报告的是500错误，而不是4XX错误，
            // CustomAuthenticationFailureHandler，无法处理
            throw new BadCredentialsException(result);
        }
        // 用户名不存在的情况，已经在 UserDetailsService 实现了，这里无需重复
        else if (!passwordEncoder.matches(tokenPassword, password)) {
            log.info("password not right  !");
            loginAttemptUserService.loginFail(ip, name);
            throw new BadCredentialsException(userService.getLoginFailMsg(request));
        } else {
            log.info("login successfully  !");
            loginAttemptUserService.loginSuccess(ip, name);
            // 登录成功后，通过spring cloud stream rabbit 将登录成功的userId发送出去，user－account接受
            sendSource.send(name);
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {

        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}
