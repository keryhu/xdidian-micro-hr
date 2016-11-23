package com.xdidian.keryhu.message.stream;

import com.xdidian.keryhu.domain.message.MessageCommunicateDto;
import com.xdidian.keryhu.domain.message.Operate;
import com.xdidian.keryhu.domain.message.ReadGroup;
import com.xdidian.keryhu.message.domain.Message;
import com.xdidian.keryhu.message.domain.SubjectMsg;
import com.xdidian.keryhu.message.repository.MessageRepository;
import com.xdidian.keryhu.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    MessageRepository repository;

    @StreamListener(WebsocketAndMessageInputChannel.NAME)
    public void receiveMessage(MessageCommunicateDto dto) {

        log.info("message 组件接受到新消息： "+dto);

        if (dto.getReadGroup().equals(ReadGroup.XDIDIAN)) {
            boolean readGroupExist = repository.
                    findByReadGroup(dto.getReadGroup()).isPresent();
            Message me = repository.findByReadGroup(dto.getReadGroup()).orElse(null);

            if (readGroupExist) {
                me.setSubjectMsgs(updateSubjectMsg(me.getSubjectMsgs(), dto));
                repository.save(me);

                // readGroup not exist 新建一个
            } else {
                Message m = new Message();
                m.setReadGroup(ReadGroup.XDIDIAN);
                Set<SubjectMsg> s = new HashSet<>();
                m.setSubjectMsgs(updateSubjectMsg(s, dto));
                repository.save(m);
            }


        } else if (dto.getReadGroup().equals(ReadGroup.INDIVIDUAL)) {
            // 这个才是  读取message 的userId，而不是当前用户
            log.info(dto.getUserId());
            boolean readGroupExist = repository.
                    findByReadGroupAndUserId(ReadGroup.INDIVIDUAL,
                            dto.getUserId()).isPresent();

            Message me = repository.findByReadGroupAndUserId(ReadGroup.INDIVIDUAL,
                    dto.getUserId()).orElse(null);
            if (readGroupExist) {
                me.setSubjectMsgs(updateSubjectMsg(me.getSubjectMsgs(), dto));
                repository.save(me);
            }
            // readGroup not exist 新建一个
            else {
                Message m = new Message();
                m.setReadGroup(ReadGroup.INDIVIDUAL);
                Set<SubjectMsg> s = new HashSet<>();
                m.setUserId(dto.getUserId());
                m.setSubjectMsgs(updateSubjectMsg(s, dto));
                log.info("79");
                log.info(String.valueOf(m));
                repository.save(m);
            }
        }

    }

    /**
     * 将MessageCommunicateDto 的值，更新到 Set<SubjectMsg>里面去
     */
    private Set<SubjectMsg> updateSubjectMsg(Set<SubjectMsg> subjectMsgs,
                                             MessageCommunicateDto dto) {

        // 查看数据库中，是否存在此subject
        boolean subjectExist = subjectMsgs.stream()
                .anyMatch(e -> e.getSubject().equals(dto.getSubject()));

        if (subjectExist) {
            return subjectMsgs.stream()
                    .map(e -> {
                        // 先寻找到原来的然后替换
                        if (e.getSubject().equals(dto.getSubject())) {
                            AtomicInteger atomic =
                                    new AtomicInteger(e.getCount());
                            if (dto.getOperate().equals(Operate.ADD)) {
                                // 原子性＋1
                                e.setCount(atomic.incrementAndGet());
                            } else if (dto.getOperate().equals(Operate.MINUS)) {
                                if (e.getCount() > 0) {
                                    e.setCount(atomic.decrementAndGet());
                                }
                            }
                        }

                        return e;
                    })
                    // 过滤掉 account<=0 的数据
                    .filter(e -> e.getCount() > 0)
                    .collect(Collectors.toSet());
        }
        // 如果找不到，就新建,找不到的情况下，是没有办法减少acount的
        else {
            SubjectMsg s = new SubjectMsg();
            s.setSubject(dto.getSubject());
            if (dto.getOperate().equals(Operate.ADD)) {
                AtomicInteger atomic =
                        new AtomicInteger(1);
                s.setCount(atomic.get());
                subjectMsgs.add(s);
            }

            return subjectMsgs;
        }


    }
}
