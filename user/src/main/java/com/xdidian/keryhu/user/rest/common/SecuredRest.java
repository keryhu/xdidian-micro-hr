package com.xdidian.keryhu.user.rest.common;

import com.xdidian.keryhu.user.domain.User;
import com.xdidian.keryhu.user.domain.UserInfoDto;
import com.xdidian.keryhu.user.exception.UserNotFoundException;
import com.xdidian.keryhu.user.repository.UserRepository;
import com.xdidian.keryhu.user.service.ConverterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hushuming on 2016/10/30.
 *
 * 需要用户登录后，
 */

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class SecuredRest {

    private final UserRepository repository;
    private final ConverterUtil converterUtil;
    private final MessageSource messageSource;

    /**
     *
     *  getUserInfo
     *  查看本人或他人的 ，保存在user数据库中的资料，
     * 也可以是用户登陆后，显示在 前台页面的  个人信息表
     * 目前前台直接   获取这个接口，以后如果要增加信息，再改)
     *  @param id    设定文件  userId
     * @return  用户登录后，显示的个人资料，目前这个前台还未实现，因为公司的资料还未做好
     * @throws
     */


    @GetMapping("/users/userInfo")
    public UserInfoDto getUserInfo(@RequestParam("id") String id){

        Object[] args={id};

        String err=messageSource.getMessage("message.user.idNotFound",
                args, LocaleContextHolder.getLocale());
        User u=repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(err));
        return converterUtil.userToInfoDto.apply(u);
    }


}
