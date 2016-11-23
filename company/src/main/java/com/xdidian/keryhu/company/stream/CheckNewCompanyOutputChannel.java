package com.xdidian.keryhu.company.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

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
public interface CheckNewCompanyOutputChannel {

    // 此channel的值和 application bindings下面的值一致
    String NAME = "checkNewCompanyOutputChannel";

    @Output(NAME)
    MessageChannel check();
}
