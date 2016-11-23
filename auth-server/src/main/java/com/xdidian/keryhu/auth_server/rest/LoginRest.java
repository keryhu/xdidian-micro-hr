package com.xdidian.keryhu.auth_server.rest;

import com.xdidian.keryhu.auth_server.domain.LoginAttemptProperties;
import com.xdidian.keryhu.auth_server.service.LoginAttemptUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hushuming on 2016/11/20.
 */
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(LoginAttemptProperties.class)
@Slf4j
public class LoginRest {

    private final LoginAttemptUserService loginAttemptService;


    /**
     * 如果ip被冻结，则登录的时候，自动显示错误页面
     *
     */

    @GetMapping("/login")
    public String loginError(Model model, HttpServletRequest request) {

        int leftTimes = loginAttemptService.leftLoginTimes(request);

        boolean blockStatus= (leftTimes == 0);
        String msg=blockStatus?loginAttemptService.getBlockMsg():null;



        if (blockStatus) {
            model.addAttribute("blockMsg", msg);

        }
        return "login";
    }

}
