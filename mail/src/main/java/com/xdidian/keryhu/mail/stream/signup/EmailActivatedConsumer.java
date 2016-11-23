package com.xdidian.keryhu.mail.stream.signup;


import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;

import com.xdidian.keryhu.mail.service.SignupService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;



/**
 * @author : keryHu keryhu@hotmail.com
 * @Description : email激活所促发的应用
 * @date : 2016年6月18日 下午9:06:51
 */
@EnableBinding(EmailActivatedInputChannel.class)
@Slf4j
public class EmailActivatedConsumer {


    @Autowired
    private SignupService signupService;


    @StreamListener(EmailActivatedInputChannel.NAME)
    public void receive(CommonTokenDto dto) {

        // 通过zuul 中转到 user-account，这样前台 点击 url，后台user-account使用 get 方法来处理

       signupService.sendSignupMail(dto);


    }
}
