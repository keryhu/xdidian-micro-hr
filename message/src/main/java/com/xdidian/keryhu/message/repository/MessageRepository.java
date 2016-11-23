package com.xdidian.keryhu.message.repository;

import com.xdidian.keryhu.domain.message.ReadGroup;
import com.xdidian.keryhu.message.domain.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.Set;

/**
 * Created by hushuming on 2016/11/10.
 */

public interface MessageRepository extends MongoRepository<Message,String>{

    // 这个主要用在，当前用户是新地点的客服或者新地点的管理人员
    Optional<Message> findByReadGroup(ReadGroup readGroup);

    //当前用户查询自己还有多少个人消息未读
    Optional<Message> findByReadGroupAndUserId(ReadGroup readGroup, String userId);

}
