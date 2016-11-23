package com.xdidian.keryhu.mail.stream.company;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by hushuming on 2016/10/10.
 * <p>
 * 当新公司注册完成后，新地点的工作人员审核完材料后，将审核完的结果从company——info，发送出来，
 * 接受方包含： mail服务器，手机服务器，websocket，user-account，4个。
 * <p>
 * mail服务器接受到消息后，根据checkType，判断是agree还是reject，写不同的主题邮件，
 * 然后将审核通过／审核拒绝的 消息发送给 email账号。
 */
public interface CheckNewCompanyInputChannel {

    String NAME = "checkNewCompanyInputChannel";

    @Input(NAME)
    SubscribableChannel check();
}
