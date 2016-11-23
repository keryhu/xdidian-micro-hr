package com.xdidian.keryhu.user.stream.company;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by hushuming on 2016/10/10.
 * <p>
 * 当新公司注册完成后，新地点的工作人员审核完材料后，将审核完的结果从company——info，发送出来，
 * 接受方包含： mail服务器，手机服务器，websocket，user-account，4个。
 * <p>
 * user-account服务器接受到消息后，根据checkType，判断是agree还是reject，
 * 如果是agree，则更新此userId对应的role权限为 company-admin，更新companyId
 * 如果是reject，不做任何处理
 */
public interface CheckNewCompanyInputChannel {

    String NAME = "checkNewCompanyInputChannel";

    @Input(NAME)
    SubscribableChannel check();
}
