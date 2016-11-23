package com.xdidian.keryhu.auth_server.security.custom;


import com.xdidian.keryhu.auth_server.service.UserService;
import com.xdidian.keryhu.auth_server.service.UtilsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * Created by hushuming on 2016/11/20.
 * <p>
 * 处理用户密码错误，页面到login？error，，
 * email未激活，且激活过期，跳转 注册页面
 * email未激活，但未过期，跳转前台激活页面
 */
@Component
@Slf4j
public class CustomAuthenticationFailureHandler extends
        SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UtilsImpl utils;
    @Autowired
    private UserService userService;

    private final String host = "http://localhost";


    private final String angular2_url = new StringBuffer(host)
            .append(":4200").toString();

    // 用在用户email没有激活，就登录的时候，如果之前的激活码未过期，则出现此标示
    private final String TOKEN_EXPIRED = "tokenExpired";

    // 用在用户email没有激活，就登录的时候，如果之前的激活码已经过期，则跳转到 signup页面
    private String SINGUP_URL = new StringBuffer(angular2_url)
            .append("/signup").toString();

    private String ACCOUNT_ACTIVATED_URL = new StringBuffer(angular2_url)
            .append("/email-activate").toString();


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {


        String loginFailMsg = userService.getLoginFailMsg(request);
        if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
            log.info(exception.getMessage());


            // 如果email激活码未过期，则跳转到含有 resendToken和resignupToken 的页面
            if (exception.getMessage().contains("resendToken")
                    && exception.getMessage().contains("resignupToken")) {

                // 将含有 resendtoken的 string 转为 map对象
                Map<String, String> object = utils.emailUnActivatedStringToMap(exception.getMessage());

                String url = new StringBuffer(ACCOUNT_ACTIVATED_URL)
                        .append("?email=")
                        .append(object.get("email"))
                        .append("&resendToken=")
                        .append(object.get("resendToken"))
                        .append("&resignupToken=")
                        .append(object.get("resignupToken"))
                        .toString();
                // 跳转到 email激活 页面，用户直接填入 token
                response.sendRedirect(url);
            }
            //如果已经过期，跳转注册页面
            else if (exception.getMessage().equals(TOKEN_EXPIRED)) {
                response.sendRedirect(SINGUP_URL);
            }
            // 最后才是 错误其他处理页面，例如密码错误，登录失败了几回
            else if (exception.getMessage().equals(loginFailMsg)) {
                log.info("password error");
                String url = "/uaa/login";
                //httpServletRequest.getSession() 使用这个记录错误信息，将他转移到 login页面
                request.getSession().setAttribute("error", true);
                request.getSession().setAttribute("excep", exception.getMessage());
                response.sendRedirect(url);
            }

        } else super.onAuthenticationFailure(request, response, exception);
    }
}
