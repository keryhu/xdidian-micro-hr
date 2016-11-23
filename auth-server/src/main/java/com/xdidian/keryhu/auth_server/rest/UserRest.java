package com.xdidian.keryhu.auth_server.rest;

import com.xdidian.keryhu.auth_server.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.xdidian.keryhu.util.StringValidate.isEmail;
import static com.xdidian.keryhu.util.StringValidate.isPhone;

/**
 * Created by hushuming on 2016/11/20.
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRest {

    private final UserClient userClient;
    private final MessageSource messageSource;

    /**
     * 返回当前登陆用户，principal
     */
    @RequestMapping("/user")
    @ResponseBody
    public Principal user(Principal user) {
        return user;
    }


    /**
     * 前台web能否一次性处理 map 转换的json 的错误信息，显示到前台 到时候设置一致的错误返回结果，
     * 尝试下。。如果可以的话，优先使用这个rest，，这个rest只要调用一次http请求，节省资源 另外，调用结束后，在这个方法里面返回
     * emailStatus的结果，如果为true，那么就不需要调用 “/query/emailNotActived”，，如果为false，则调用此接口
     * 前台web，在检测完email格式，和phone格式正确后，调用此接口，查看的逻辑主要有： 当前账户ip是否被冻结 如果是phone，那么是否存在于数据库
     * 如果是email，那么是否存在于数据库 emailStatus的状态是什么，如果yes则通过，如果false，web前台接下来需要调用“/query/emailNotActived”方法 )
     */
    @RequestMapping(value = "/query/validateLoginName", method = RequestMethod.GET)
    public ResponseEntity<?> validateLoginName(@RequestParam("loginName") String loginName) {

        Map<String, Object> result = new HashMap<String, Object>();

        Consumer<String> errorM = x -> {

            if (isEmail(x)) {
                String err = "";
                if (!userClient.isEmailExist(x)) {
                    err = messageSource.getMessage("message.loginName.emailNotExist", null,
                            LocaleContextHolder.getLocale());

                }
                result.put("error", err);
            } else if (isPhone(x)) {
                String err = "";
                if (!userClient.isPhoneExist(x)) {
                    err = messageSource.getMessage("message.loginName.phoneNotExist", null,
                            LocaleContextHolder.getLocale());
                    result.put("error", err);
                }
                result.put("error", err);
            }

        };

        errorM.accept(loginName);

        return ResponseEntity.ok(result);
    }


}