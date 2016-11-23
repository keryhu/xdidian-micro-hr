package com.xdidian.keryhu.websocket.config;

import com.xdidian.keryhu.websocket.domain.ActiveWebSocketUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.session.ExpiringSession;

/**
 * Created by hushuming on 2016/11/16.
 */

@Configuration
public class WebSocketHandlersConfig<S extends ExpiringSession> {

    @Bean
    public WebSocketConnectHandler<S> webSocketConnectHandler(
            SimpMessageSendingOperations messagingTemplate,
            ActiveWebSocketUserRepository repository) {
        return new WebSocketConnectHandler<S>(messagingTemplate, repository);
    }

    @Bean
    public WebSocketDisconnectHandler<S> webSocketDisconnectHandler(
            SimpMessageSendingOperations messagingTemplate,
            ActiveWebSocketUserRepository repository) {
        return new WebSocketDisconnectHandler<S>(messagingTemplate, repository);
    }
}
