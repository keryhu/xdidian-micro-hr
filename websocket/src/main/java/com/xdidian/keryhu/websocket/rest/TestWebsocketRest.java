package com.xdidian.keryhu.websocket.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.concurrent.TimeUnit;

/**
 * Created by hushuming on 2016/11/9.
 */

@RestController
@Slf4j
public class TestWebsocketRest {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")
    @SendTo("/topic/testHello")
    public String greet(String hello) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return "你好："+hello;
    }

    @GetMapping("/front/22")
    public ResponseEntity g(Principal principal){

        log.info(principal.getName());

        simpMessagingTemplate.convertAndSendToUser(principal.getName(),
                "/queue/websocketAndMessage", "这是个人websocket信息");

        return ResponseEntity.ok("ok");
    }
}
