package com.xdidian.keryhu.company.stream;

import com.xdidian.keryhu.domain.company.CheckCompanyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by hushuming on 2016/10/9.
 * <p>
 * 此接口，包含了拒绝和同意 注册公司的信息，通过 checkType来区分。
 * <p>
 * 当新的公司，审核通过后，发送出去的消息。接收方有4个
 * 1 给user——account，通知他更新 user 的权限为 ROLE_COMPANY_ADMIN，更新companyId为新的。（id或email或phone，和companyId）
 * 2 通知邮件服务器，发送审核成功的通知，(email-必需，companyId）。
 * 3 通知手机平台，发送审核成功的通知，（phone--必需，companyId）
 * 4 通知websocket，给对应的userId，发送通知（userId-必需，companyId）
 * <p>
 * 具体审核公司的post，此路由是 拒绝公司的注册资料，并将拒绝的理由保存起来。接收方有邮件服务器，手机平台，websocket。
 *
 * 审核失败了：
 * 1 通知邮件服务器，发送审核失败的通知，(email-必需，companyId）。
 * 3 通知手机平台，发送审核失败的通知，（phone--必需，companyId）
 * 4 通知websocket，给对应的userId，发送失败通知（userId-必需，companyId）
 */
@Component
@EnableBinding(CheckNewCompanyOutputChannel.class)
@Slf4j
public class CheckNewCompanyProducer {

    @Autowired
    private CheckNewCompanyOutputChannel channel;

    public void send(CheckCompanyDto dto) {

        boolean result = channel.check()
                .send(MessageBuilder.withPayload(dto).build());

        Assert.isTrue(result, "新地点工作人员审核新公司注册完，发送结果message出去。发送失败！");

        log.info("新地点工作人员审核新公司注册完，发送结果message出去消息成功！");

    }
}
