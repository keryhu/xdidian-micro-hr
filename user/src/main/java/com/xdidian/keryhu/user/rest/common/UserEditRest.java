package com.xdidian.keryhu.user.rest.common;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.xdidian.keryhu.user.domain.*;
import com.xdidian.keryhu.user.domain.edit.BirthdayModifyDto;
import com.xdidian.keryhu.user.domain.edit.ChangePasswordDto;
import com.xdidian.keryhu.user.domain.edit.NameModifyDto;
import com.xdidian.keryhu.user.domain.edit.UserEditDto;
import com.xdidian.keryhu.user.exception.UserNotFoundException;
import com.xdidian.keryhu.user.repository.UserRepository;
import com.xdidian.keryhu.user.service.ConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.xdidian.keryhu.user.service.UserService;

import lombok.RequiredArgsConstructor;



/**
 * @author keryhu keryhu@hotmail.com
 * UserPutRest
 * 用户资料修改的时候，涉及到 put rest 方法)
 * 2016年9月5日 下午5:21:05
 */

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserEditRest {

    private final UserService userService;
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final ConverterUtil converterUtil;
    private final MessageSource messageSource;

    /**
     * 用户登录平台后，修改姓名，如果提交的内容里面，含有了密码，那么就保存该名字。
     * 如果提交的信息里面，密码的内容不存在，则只负责验证，如果提交的内容里面密码存在，但是错误，则报错，密码错误) @param @param dto @param @return
     * 设定文件 @return ResponseEntity<?> 返回类型 @throws
     */


    @PostMapping("/users/edit/name")
    public ResponseEntity<?> validateNameCanModifyOrSave(
            @RequestBody final NameModifyDto dto) {
        // 先验证name 是否符合命名规范， 2 是否可以修改（超过60天），是否是当前用户。
        Map<String, Object> map = new HashMap<String, Object>();
        userService.validateNameEdit(dto.getName(), dto.getUserId());

        if (StringUtils.isEmpty(dto.getPassword()) || dto.getPassword() == null) {
            map.put("result", true);
            return ResponseEntity.ok(map);
        } else {
            String hashPassword =repository.findById(dto.getUserId()).get().getPassword();
            boolean match = encoder.matches(dto.getPassword(), hashPassword);
            if (!match) {
                String err=messageSource.getMessage("message.user.passwordNotCorrect",null,
                        LocaleContextHolder.getLocale());
                map.put("result", err);
                return ResponseEntity.ok(map);
            } else {
                repository.findById(dto.getUserId()).ifPresent(e -> {
                    e.setName(dto.getName());
                    e.setNameModifyTime(LocalDateTime.now());
                    repository.save(e);
                });
                map.put("result", true);
                return ResponseEntity.ok(map);
            }
        }
    }


    //修改生日

    @PostMapping("/users/edit/birthday")
    public ResponseEntity<?> editBirthday(@RequestBody final BirthdayModifyDto dto) {
        //验证传递来的id，是否存在，月份和天数，是否对，密码是否对。
        userService.validateBirthdayAndSave(dto);
        Map<String, Object> map = new HashMap<String, Object>();

        if (StringUtils.isEmpty(dto.getPassword()) || dto.getPassword() == null) {
            map.put("result", true);
            return ResponseEntity.ok(map);
        }
        map.put("result", true);
        return ResponseEntity.ok(map);
    }


    /**
     * 用户修改密码的时候，需要的rest
     * @param dto
     * @return
     */
    @PostMapping("/users/edit/password")
    public ResponseEntity<?> changePassword(@RequestBody final ChangePasswordDto dto) {
        //验证传递来的id，是否存在，月份和天数，是否对，密码是否对。
        Map<String, Object> map = new HashMap<String, Object>();
        userService.validateChangePassword(dto);

        repository.findById(dto.getId())
                .ifPresent(e -> {
                    e.setPassword(encoder.encode(dto.getNewPassword()));
                    repository.save(e);
                });
        map.put("result", true);
        return ResponseEntity.ok(map);
    }

    //用户修改个人资料前，首先加载页面的时候，
    // 获取所有可以被修改的信息，例如姓名，头像，email，手机号
    @GetMapping("/users/edit/info")
    public UserEditDto getEditInfo(@RequestParam("id") String id){
        Map<String, Object> map = new HashMap<String, Object>();
        Object[] args={id};
        String err=messageSource.getMessage("message.user.idNotFound",
                args,LocaleContextHolder.getLocale());
        User u=repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(err));

        return converterUtil.userToEditDto.apply(u);
    }

}
