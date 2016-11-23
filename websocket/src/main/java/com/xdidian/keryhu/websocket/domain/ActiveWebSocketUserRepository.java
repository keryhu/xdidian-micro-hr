package com.xdidian.keryhu.websocket.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Created by hushuming on 2016/11/16.
 */
public interface ActiveWebSocketUserRepository
        extends MongoRepository<ActiveWebSocketUser, String> {

   Optional<ActiveWebSocketUser> findByUserId(String userId);
}
