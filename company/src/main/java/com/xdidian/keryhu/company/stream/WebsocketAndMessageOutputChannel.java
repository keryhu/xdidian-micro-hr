package com.xdidian.keryhu.company.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Created by hushuming on 2016/11/10.
 *
 * 此channel，用在专门发送消息给websocket和message组件的。
 * 作用是，通知他们更新组件。
 * 用在company里面，具体的应用是：
 *
 * 当新公司创建完成时，发送消息通知，主题是 待审核的公司，数量是+1
 * 当新公司创建后，审核完成时，如果审核通过，发送通知出去，主题是：公司创建成功 +1
 * 当新公司创建后，审核完成时，如果审核失败，发送通知出去，主题是，公司创建失败，接受人是，申请人
 */
public interface WebsocketAndMessageOutputChannel {

    // 此channel的值和 application bindings下面的值一致
    String NAME = "websocketAndMessageOutputChannel";

    @Output(NAME)
    MessageChannel newMessage();
}
