package com.xdidian.keryhu.user.service.admin;

import com.xdidian.keryhu.domain.Role;
import com.xdidian.keryhu.user.domain.User;
import com.xdidian.keryhu.user.domain.admin.AddServiceForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;

/**
 * Created by hushuming on 2016/9/29.
 * <p>
 * 新地点 admin，客服人员，使用的 类型转换的 service
 */

@Component("adminConvertUtil")
public class AdminConvertUtil {

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 将admin 录入的客服人员的信息，保存到数据库对象user
    public Function<AddServiceForm, User> addServiceFormToUser =
            x -> {
                User u = new User();
                u.setEmail(x.getEmail().toLowerCase());
                u.setPhone(x.getPhone());
                u.setName(x.getName());
                u.setPassword(passwordEncoder.encode(x.getPassword()));
                u.addRole(Role.ROLE_XDIDIAN_SERVICE);
                u.setRegisterTime(LocalDateTime.now());
                u.setEmailStatus(true);
                u.setPhoneStatus(true);
                //第一次的注册用户，默认设置 生日为 1月1日。
                u.setBirthday(LocalDate.of(1999, 1, 1));

                return u;
            };
}
