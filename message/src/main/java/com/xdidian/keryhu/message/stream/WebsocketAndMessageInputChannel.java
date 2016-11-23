package com.xdidian.keryhu.message.stream;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by hushuming on 2016/11/10.
 *  channel  接受所有其他组件，目的是发送消息提醒前台有未读消息的channel
 接受多个服务器。
 */
public interface WebsocketAndMessageInputChannel {

    String NAME = "websocketAndMessageInputChannel";

    @Input(NAME)
    SubscribableChannel input();
}
