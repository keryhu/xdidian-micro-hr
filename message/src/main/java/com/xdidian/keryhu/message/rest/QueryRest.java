package com.xdidian.keryhu.message.rest;

import com.xdidian.keryhu.domain.message.ReadGroup;
import com.xdidian.keryhu.message.domain.Message;
import com.xdidian.keryhu.message.domain.SubjectMsg;
import com.xdidian.keryhu.message.repository.MessageRepository;
import com.xdidian.keryhu.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hushuming on 2016/11/10.
 */

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QueryRest {


    private final MessageRepository repository;

    //查询当前用户还有多少消息未读，返回给前台是  FrontMessageDto 对象
    @GetMapping("/query/subjectMsg")
    public ResponseEntity<?> querySubjectMsg() {

        Set<SubjectMsg> subjectMsgs = new HashSet<>();
        String userId = SecurityUtils.getCurrentLogin();

        // 查看是否是新地点的管理员或者客服
        Boolean isXidian = SecurityUtils.isXdidian();

        log.info(String.valueOf(isXidian));

        if (isXidian) {

            Set<SubjectMsg> s = repository.findByReadGroup(ReadGroup.XDIDIAN)
                    .map(Message::getSubjectMsgs)
                    .orElse(new HashSet<>());
            subjectMsgs.addAll(s);
        }

        Set<SubjectMsg> sm = repository.
                findByReadGroupAndUserId(ReadGroup.INDIVIDUAL, userId)
                .map(Message::getSubjectMsgs)
                .orElse(null);

        if (sm != null) {
            subjectMsgs.addAll(sm);
        }
        log.info(String.valueOf(subjectMsgs));
        return ResponseEntity.ok(subjectMsgs);
    }
}
