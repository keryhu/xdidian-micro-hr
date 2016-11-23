package com.xdidian.keryhu.websocket.config;

import com.xdidian.keryhu.websocket.domain.ActiveWebSocketUser;
import com.xdidian.keryhu.websocket.domain.ActiveWebSocketUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;
import java.util.Calendar;

/**
 * Created by hushuming on 2016/11/16.
 */

@Slf4j
public class WebSocketConnectHandler<S>
        implements ApplicationListener<SessionConnectEvent> {


    private ActiveWebSocketUserRepository repository;
    private SimpMessageSendingOperations messagingTemplate;

    public WebSocketConnectHandler(SimpMessageSendingOperations messagingTemplate,
                                   ActiveWebSocketUserRepository repository
    ) {

        super();
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {

        MessageHeaders headers = event.getMessage().getHeaders();
        log.info(String.valueOf("28: " + headers));
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        log.info("43: "+sha.getSessionId());
        Principal user = SimpMessageHeaderAccessor.getUser(headers);
        log.info(String.valueOf("33: " + user));
        if (user != null) {
            String id = SimpMessageHeaderAccessor.getSessionId(headers);
            log.info(String.valueOf("36: " + id));
            ActiveWebSocketUser a =
                    new ActiveWebSocketUser(id, user.getName(), Calendar.getInstance());
            log.info(String.valueOf("41: " + a));
            repository.save(a);
        }


    }
}
