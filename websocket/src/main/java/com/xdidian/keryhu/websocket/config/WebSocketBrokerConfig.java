package com.xdidian.keryhu.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.ExpiringSession;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;


/**
 * Created by hushuming on 2016/10/5.
 * <p>
 * pc-gateway 向pc angular2，网页发送websocket 消息。
 */

@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
public class WebSocketBrokerConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<ExpiringSession> {


    @Override
    protected void configureStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket/front")
                .addInterceptors(new CustomHandshakeInterceptor())
                .setAllowedOrigins("*")
                .withSockJS()
                .setStreamBytesLimit(512 * 1024)
                .setHttpMessageCacheSize(1000)
                .setDisconnectDelay(30 * 1000);

    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration webSocketTransportRegistration) {

        webSocketTransportRegistration.setSendTimeLimit(15 * 1000)
                .setSendBufferSizeLimit(512 * 1024);
        webSocketTransportRegistration.setMessageSizeLimit(128 * 1024);

    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue/");
        config.setApplicationDestinationPrefixes("/pcAngular2");
    }

}
