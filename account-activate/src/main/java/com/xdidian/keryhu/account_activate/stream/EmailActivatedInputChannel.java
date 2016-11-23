package com.xdidian.keryhu.account_activate.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;



/**
 * @Description : email激活接受消息的channel
 * @date : 2016年6月18日 下午9:01:59
 * @author : keryHu keryhu@hotmail.com
 */
public interface EmailActivatedInputChannel {

    String NAME = "emailActivatedInputChannel";

    @Input(NAME)
    SubscribableChannel input();

}
