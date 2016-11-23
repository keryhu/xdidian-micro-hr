package com.xdidian.keryhu.websocket.config;

import com.xdidian.keryhu.websocket.domain.ActiveWebSocketUser;
import com.xdidian.keryhu.websocket.domain.ActiveWebSocketUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Arrays;

/**
 * Created by hushuming on 2016/11/16.
 */

@Slf4j
public class WebSocketDisconnectHandler<S>
        implements ApplicationListener<SessionDisconnectEvent> {
    private ActiveWebSocketUserRepository repository;
    private SimpMessageSendingOperations messagingTemplate;

    public WebSocketDisconnectHandler(SimpMessageSendingOperations messagingTemplate,
                                      ActiveWebSocketUserRepository repository) {
        super();
        this.messagingTemplate = messagingTemplate;
        this.repository = repository;
    }

    public void onApplicationEvent(SessionDisconnectEvent event) {

        log.info("" + event.getSessionId());

        String id = event.getSessionId();
        if (id != null) {
            ActiveWebSocketUser user = this.repository.findOne(id);
            if (user != null) {
                this.repository.delete(id);
            }
        }


    }
}