package com.xdidian.keryhu.mail.service;

import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;
import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;
import com.xdidian.keryhu.mail.mail.EmailHtmlSender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import static com.xdidian.keryhu.util.StringValidate.isEmail;

/**
 * Created by hushuming on 2016/10/30.
 * <p>
 * signup stream 用到的service，
 */
@Component
@Slf4j
public class SignupService {

    private final EmailHtmlSender mailSender;

    @Autowired
    public SignupService(EmailHtmlSender mailSender) {
        this.mailSender = mailSender;
    }

    // 用户email激活或者重发的时候，根据 ApplySituation 不同，得到不同的 主题
    private String getTypeAccordingApplySituation(ApplySituation applySituation) {

        String result = "";
        switch (applySituation) {

            case EDIT:
                result="修改邮箱";
                break;
            case SIGNUP:
                result="注册验证";
                break;
            case RECOVER:
                result="密码找回";
                break;
            default:
                break;
        }
        return result;
    }

    // 当emailActivated 和 resendAcitvated 的时候，发送邮件的服务
    public void sendSignupMail(CommonTokenDto dto){
        if (isEmail(dto.getAccount())) {
            final Context ctx = new Context();
            String type = getTypeAccordingApplySituation(dto.getApplySituation());
            String subject = "新地点－" + type;
            ctx.setVariable("dto", dto);    //必须保留，前台需要读取过期时间
            ctx.setVariable("token", dto.getToken());
            ctx.setVariable("type", type);
            ctx.setVariable("email", dto.getAccount());
            mailSender.send(dto.getAccount(), subject, "emailActivated", ctx);

            log.info("因为用户点击' 再次发送邮件 '，再次给 {} 发送验证码邮件 ");
        } else {
            log.info("接受到的需要被激活的对象 不是email激活。");
        }
    }

}
