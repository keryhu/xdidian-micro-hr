package com.xdidian.keryhu.message.domain;

import com.xdidian.keryhu.domain.message.ReadGroup;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

/**
 * Created by hushuming on 2016/11/10.
 *
 * 存在数据库中的对象。用来记录一个用户目前有哪些关注的消息未读，有多少
 */

@Document
@Data
public class Message {

    @Id
    private String id= UUID.randomUUID().toString();

    @Indexed
    private String userId;   // 如果此message是针对新地点的，那么userId可以为null，否则必填


    // 为了存储速度快，和没有重复，这里使用set。而不是list
    private Set<SubjectMsg> subjectMsgs;

    // 属于新地点还是个人，read
    @Indexed
    private ReadGroup readGroup;



}
