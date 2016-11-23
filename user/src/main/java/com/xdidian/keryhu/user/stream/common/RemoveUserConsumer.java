package com.xdidian.keryhu.user.stream.common;

import com.xdidian.keryhu.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * @author : keryHu keryhu@hotmail.com
 * @Description : 接受到需要删除User消息时，具体的实施方法,传递过来的参数id，
 * 可能是uuid，也可能是email， 还可能是phone，需要先判断。然后将他们都转为user 的
 * id
 * @date : 2016年6月18日 下午9:26:15
 */
@EnableBinding(RemoveUserInputChannel.class)
@Slf4j
public class RemoveUserConsumer {

    @Autowired
    private UserRepository repository;

    @StreamListener(RemoveUserInputChannel.NAME)
    public void removeUser(String id) {
        log.info("id is : {}", id);
        if (!(id == null || id.isEmpty())) {

            repository.findByEmailOrPhoneOrId(id, id, id)
                    .ifPresent(e -> repository.delete(e));

            log.info("user-account 成功删除了此user 的 id is ：{} ", id);
        }
    }

}
