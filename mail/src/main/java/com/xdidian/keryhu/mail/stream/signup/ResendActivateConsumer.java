package com.xdidian.keryhu.mail.stream.signup;


import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;

import com.xdidian.keryhu.mail.service.SignupService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;



/**
 * @author : keryHu keryhu@hotmail.com
 * @Description :
 * <p>
 * 应用此服务的情景有3个，密码重设，注册完邮箱激活，还有个人资料修改的 重新发送服务，或者 密码重设的第一次服务。
 * @date : 2016年6月18日 下午9:07:26
 */
@EnableBinding(ResendActivateInputChannel.class)
@Slf4j
public class ResendActivateConsumer {

    @Autowired
    private SignupService signupService;


    @StreamListener(ResendActivateInputChannel.NAME)
    public void receive(CommonTokenDto dto) {

        // 通过zuul 中转到 user-account，这样前台 点击 url，后台user-account使用 get 方法来处理

        signupService.sendSignupMail(dto);

    }
}
