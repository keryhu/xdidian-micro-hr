package com.xdidian.keryhu.user.config;

import com.xdidian.keryhu.domain.Role;
import com.xdidian.keryhu.user.domain.User;
import com.xdidian.keryhu.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * @author : keryHu keryhu@hotmail.com
 * @Description : 增加一个此服务启动时，自动实现的功能。 就是增加系统管理员到 mongo 数据库中便于其它service 验证用户名和密码。
 * @date : 2016年6月18日 下午9:23:02
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MongoUserLoader {


    private final UserRepository repository;

    private final PasswordEncoder encoder;

    @PostConstruct
    public void init() {

        if (!repository.findByPhone("15900455491").isPresent()) {
            User user = new User();
            user.setEmail("keryhu@hotmail.com");
            // 增加编码
            user.setPassword(encoder.encode("skdjf7782c"));
            user.setPhone("15900455491");
            user.setRegisterTime(LocalDateTime.now());
            user.addRole(Role.ROLE_XDIDIAN_ADMIN);
            // 因为是管理员，所以默认设置email已经激活
            user.setEmailStatus(true);
            user.setPhoneStatus(true);
            user.setBirthday(LocalDate.of(1999, 1, 1));
            repository.save(user);
        }

    }


}


