package com.xdidian.keryhu.auth_server.security.custom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.xdidian.keryhu.util.SecurityUtils.isXdidian;

/**
 * Created by hushuming on 2016/11/21.
 * 定义登录成功后，跳转页面，根据权限
 *
 * 通过判断savedRequest，是否为null，查看用户是直接登录login，还是登录了一个secured 页面，被
 * redirect login页面的，如果是第二种情况，那么用户登录后，还会返回到之前的secured页面，如果是
 * 第一种情况，那么根据用户的role权限来决定登录后的页面。
 * 此功能已经实现，angular2 前台无需定义此功能了
 */

@Component
@Slf4j
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final String host = "http://localhost";


    private final String angular2_url = new StringBuffer(host)
            .append(":4200").toString();

    // 新地点客服登录页面
    private String SERVICE_HOME_URL = new StringBuffer(angular2_url)
            .append("/service/home").toString();

    // 新地点会员登录页面
    private String PROFILE_HOME_URL = new StringBuffer(angular2_url)
            .append("/profile/home").toString();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // 判断是否含有 savedRequest。如果没有的话，就根据 role来指定 redirect 页面
        SavedRequest savedRequest =
                (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

        // 只有url中没有redirectUrl，才使用自己的根据role来 跳转页面
        if (savedRequest != null) {
            response.sendRedirect(savedRequest.getRedirectUrl());
        }
        else {
            log.info("savedRequest null ");

            if (isXdidian()) {
                log.info("service redirect ");
                response.sendRedirect(SERVICE_HOME_URL);
            } else {
                log.info("profile redirect");
                response.sendRedirect(PROFILE_HOME_URL);
            }
        }



    }
}
