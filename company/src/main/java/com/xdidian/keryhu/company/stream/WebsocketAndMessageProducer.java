package com.xdidian.keryhu.company.stream;

import com.xdidian.keryhu.domain.message.MessageCommunicateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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

@Component
@EnableBinding(WebsocketAndMessageOutputChannel.class)
@Slf4j
public class WebsocketAndMessageProducer {

    @Autowired
    private WebsocketAndMessageOutputChannel channel;

    public void send(MessageCommunicateDto dto){
        boolean result = channel.newMessage()
                .send(MessageBuilder.withPayload(dto).build());

        Assert.isTrue(result, "新公司注册，服务器从company发送message消息失败！");

        log.info("company服务器发送公司注册成功的消息成功,接受方为：websocket和message！");
    }
}
