package com.xdidian.keryhu.websocket.stream;

import com.xdidian.keryhu.domain.message.MessageCommunicateDto;
import com.xdidian.keryhu.domain.message.ReadGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;


/**
 * Created by hushuming on 2016/11/10.
 * channel  接受所有其他组件，目的是发送消息提醒前台有未读消息的channel
 * 接受多个服务器。
 * <p>
 * 分两中情况,如果是发送给新地点的人员， 那么发送的时候，
 */

@EnableBinding(WebsocketAndMessageInputChannel.class)
@Slf4j
public class WebsocketAndMessageConsumer {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @StreamListener(WebsocketAndMessageInputChannel.NAME)
    public void receiveMessage(MessageCommunicateDto dto) {

        log.info(String.valueOf(dto));

        if (dto.getReadGroup().equals(ReadGroup.XDIDIAN)) {
            //这是一个群发消息，接收方为新地点的管理员或客服
            log.info("websocket 接受到来自其他组件的的消息，现在准备发送websocket出去！");
            simpMessagingTemplate.convertAndSend("/topic/websocketAndMessage", dto);

        } else if (dto.getReadGroup().equals(ReadGroup.INDIVIDUAL)) {
            //这是一个单独发送消息，接收方为需要userId。
            //客户端的接受 主题是：
            // stompClient.subscribe('/userId/' + '/topic/checkNewCompany,...)
            log.info("websocket 接受到来自其他组件的的消息，现在准备发送websocket出去！");
            simpMessagingTemplate.convertAndSendToUser(dto.getUserId(),
                    "/queue/websocketAndMessage", dto);
        }


    }
}
