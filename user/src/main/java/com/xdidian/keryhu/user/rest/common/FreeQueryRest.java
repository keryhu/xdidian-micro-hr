package com.xdidian.keryhu.user.rest.common;

import com.xdidian.keryhu.user.domain.User;
import com.xdidian.keryhu.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.xdidian.keryhu.util.StringValidate.isEmail;
import static com.xdidian.keryhu.util.StringValidate.isPhone;


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class FreeQueryRest {


    private final UserRepository repository;
    private final MessageSource messageSource;


    /**
     * 对外提供查询email是否存在数据库的api接口，不需要增加spring security验证
     */

    @GetMapping("/users/query/isEmailExist")
    public Boolean isEmailExist(@RequestParam("email") String email) {
        return repository.findByEmail(email).isPresent();
    }


    /**
     * 对外提供查询phone是否存在与数据库，不需要增加spring security验证
     */

    @GetMapping("/users/query/isPhoneExist")
    public Boolean isPhoneExist(@RequestParam("phone") String phone) {
        return repository.findByPhone(phone).isPresent();
    }


    /**
     * rest 接口查询当前loginName所在的用户，邮箱是否已经激活，如果激活，返回ture，默认是false
     */

    @GetMapping("/users/query/emailStatus")
    public Boolean emailStatus(@RequestParam("loginName") String loginName) {
        return repository.findByEmailOrPhone(loginName,loginName)
                .map(User::isEmailStatus)
                .orElse(false);
    }

    /**
     *  查询loginName（email或phone）是否存在于数据库)
     *
     */


    @GetMapping("/users/query/isLoginNameExist")
    public Boolean isLoginNameExist(@RequestParam("loginName") String loginName) {
        return repository.findByEmailOrPhone(loginName,loginName)
                .isPresent();
    }

    /**
     * rest 接口查询当前loginName所在的用户，手机是否已经激活，如果激活，返回ture，默认是false
     */

    @GetMapping("/users/query/phoneStatus")
    public Boolean phoneStatus(@RequestParam("loginName") String loginName) {
        return repository.findByEmailOrPhone(loginName,loginName)
                .map(User::isPhoneStatus)
                .orElse(false);
    }


    /**
     * getEmailAndPhone
     * 根据提交的email 或者 phone，返回该用户的email 和phone
     * 用在 密码找回的应用中，前台输入 email或者phone，返回给前台 email和phone，
     * 方便用户根据自行选择，通过哪种方式找回密码。
     *
     *
     */


    @GetMapping("/users/query/getEmailAndPhone")
    public ResponseEntity<?> getEmailAndPhone(@RequestParam("account") String account) {
        Map<String, String> map = new HashMap<String, String>();

        Optional<User> u=repository.findByEmailOrPhone(account,account);

        boolean isAccountExist =u.isPresent();

        if (isAccountExist) {
            map.put("email", u.get().getEmail());
            map.put("phone", u.get().getPhone());
            return ResponseEntity.ok(map);
        } else {
            String err = null;
            if (isEmail(account)) {
                err =messageSource.getMessage("message.user.emailNotFound",null,
                        LocaleContextHolder.getLocale());
            } else if (isPhone(account)) {
                err =messageSource.getMessage("message.user.phoneNotFound",null,
                        LocaleContextHolder.getLocale());
            } else {
                err =messageSource.getMessage("message.user.emailOrPhoneNotFound",null,
                        LocaleContextHolder.getLocale());

            }
            map.put("result", err);
            return ResponseEntity.ok(map);
        }
    }


}
