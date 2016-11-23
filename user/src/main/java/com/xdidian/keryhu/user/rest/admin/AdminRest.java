package com.xdidian.keryhu.user.rest.admin;

import com.mysema.commons.lang.Assert;
import com.querydsl.core.types.Predicate;
import com.xdidian.keryhu.domain.Role;
import com.xdidian.keryhu.user.domain.QUser;
import com.xdidian.keryhu.user.domain.User;
import com.xdidian.keryhu.user.domain.admin.AddServiceForm;
import com.xdidian.keryhu.user.repository.UserRepository;
import com.xdidian.keryhu.user.service.admin.AdminConvertUtil;
import com.xdidian.keryhu.user.service.admin.AdminService;
import com.xdidian.keryhu.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


/**
 * Created by hushuming on 2016/9/29.
 * <p>
 * 当新地点管理员给 对 客服人员  增加，编辑，删除 rest 服务的时候，促发的事件rest，只有管理员权限才能操作。
 * <p>
 * 前台 已经验证过了，admin 开头的 路由，只能新地点管理人员才能访问，其他都不能。
 */

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j

public class AdminRest {

    private final AdminService adminService;
    private final UserService userService;
    private final UserRepository repository;
    private final AdminConvertUtil adminConvertUtil;
    private final MessageSource messageSource;


    @PostMapping("/admin/add-service")
    public ResponseEntity<?> addService(@RequestBody final AddServiceForm form) {

        adminService.validateAddService(form);
        Map<String, Boolean> map = new HashMap();

        User u = adminConvertUtil.addServiceFormToUser.apply(form);
        repository.save(u);
        map.put("result", true);
        return ResponseEntity.ok(map);
    }

    //根据名字，查找 当前数据库中的 新地点的客服人员。 暴露给前台的信息有： 姓名，email，手机号，注册日期，
    // 上次登录日期，id

    @GetMapping("/admin/queryByName")
    public Page<User> findByName(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "name", required = false) final String name) {

        QUser user = new QUser("user");
        Predicate predicate = null;
        Predicate rolePredicate = user.roles.contains(Role.ROLE_XDIDIAN_SERVICE);

        log.info("name is : " + name);

        if (name == null) {
            predicate = rolePredicate;
        } else {
            predicate = user.name.eq(name)
                    .and(rolePredicate);
        }
        return repository.findAll(predicate, pageable);
    }

    //删除选择的新地点客服人员，参数是id。
    @DeleteMapping("/admin/delById")
    public ResponseEntity<?> delById(@RequestParam("ids") String[] ids) {

        Map<String, Boolean> map = new HashMap();

        Stream.of(ids)
                .map(e -> repository.findById(e).orElse(null))
                .filter(e -> e != null)
                .forEach(e -> repository.delete(e));

        map.put("result", true);
        return ResponseEntity.ok(map);
    }


    // 新地点的管理人员，根据搜索条件，搜索数据库中的会员，返回的是pagination对象。
    // 包含了用户注册日期，用户最后登录日期   requesParam 参数必需要分开，不能何在一起为一个object，

    @GetMapping("/admin/queryWithPage")
    public Page<User> get(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "registerTimeBegin", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime registerTimeBegin,
            @RequestParam(value = "registerTimeEnd", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime registerTimeEnd,
            @RequestParam(value = "lastLoginTimeBegin", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastLoginTimeBegin,
            @RequestParam(value = "lastLoginTimeEnd", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastLoginTimeEnd) {


        boolean contentNotNull = content != null;
        boolean registerTimeNotNull = registerTimeBegin != null
                && registerTimeEnd != null;

        boolean lastLoginTimeNotNull = lastLoginTimeBegin != null
                && lastLoginTimeEnd != null;

        boolean contentAndRegisterTimeAllNotNull = contentNotNull && registerTimeNotNull;

        boolean contentAndLastLoginTimeAllNotNull = contentNotNull && lastLoginTimeNotNull;

        boolean contentAndRegisterTimeAndLastLoginTimeAllNotNull = contentNotNull &&
                registerTimeNotNull && lastLoginTimeNotNull;

        String err = null;

        if (registerTimeNotNull) {
            err = messageSource.getMessage("message.adminRest.regsterTimeBeginMustBeforeEnd",
                    null, LocaleContextHolder.getLocale());

            Assert.isTrue(registerTimeBegin.isBefore(registerTimeEnd), err);
        }
        if (lastLoginTimeNotNull) {
            err = messageSource.getMessage("message.adminRest.lastLoginTimeBeginMustBeforeEnd",
                    null, LocaleContextHolder.getLocale());

            Assert.isTrue(lastLoginTimeBegin.isBefore(lastLoginTimeEnd), err);
        }


        QUser user = new QUser("user");

        Predicate contentPredicate = null;
        Predicate registerTimePredicate = null;
        Predicate lastLoginTimePredicate = null;

        // content is null, registerTime  is not null  ,last login Time not null
        Predicate contentNullButRegisterTimeAndLastLoginTimeNotNullPredicate = null;

        // content is not null, registerTime  is not null  , last login is null
        Predicate contentAndRegisterTimePredicate = null;

        // content is not null, registerTime  is not null  , last login not null
        Predicate contentAndRegisterTimeAndLastLoginTimePredicate = null;

        // content is not null, registerTime  is null  , last login is not null
        Predicate contentAndLastLoginTimePredicate = null;


        if (contentNotNull) {
            contentPredicate = user.email.eq(content)
                    .or(user.phone.eq(content)).or(user.name.eq(content))
                    .or(user.id.eq(content));
        }


        if (registerTimeNotNull) {
            registerTimePredicate = user.registerTime.after(registerTimeBegin)
                    .and(user.registerTime.before(registerTimeEnd));
            log.info(String.valueOf(registerTimePredicate));

        }
        if (lastLoginTimeNotNull) {
            lastLoginTimePredicate = user.lastLoginTime.after(lastLoginTimeBegin)
                    .and(user.lastLoginTime.before(lastLoginTimeEnd));
        }

        // content is null, registerTime  is not null  ,last login Time not null
        if (!contentNotNull && registerTimeNotNull && lastLoginTimeNotNull) {

            contentNullButRegisterTimeAndLastLoginTimeNotNullPredicate =
                    user.registerTime.after(lastLoginTimeBegin)
                            .and(user.registerTime.before(lastLoginTimeEnd))

                            .and(lastLoginTimePredicate);

        }


        // content is not null, registerTime  is not null  , last login not null

        if (contentAndRegisterTimeAndLastLoginTimeAllNotNull) {
            contentAndRegisterTimeAndLastLoginTimePredicate = user.email.eq(content)
                    .or(user.phone.eq(content)).or(user.name.eq(content))
                    .or(user.id.eq(content))
                    .and(registerTimePredicate)
                    .and(lastLoginTimePredicate);

        }

        // content is not null, registerTime  is not null  , last login is null
        if (contentAndRegisterTimeAllNotNull) {
            contentAndRegisterTimePredicate = user.email.eq(content)
                    .or(user.phone.eq(content)).or(user.name.eq(content))
                    .and(registerTimePredicate);
        }

        if (contentAndLastLoginTimeAllNotNull) {
            contentAndLastLoginTimePredicate = user.email.eq(content)
                    .or(user.phone.eq(content)).or(user.name.eq(content))
                    .or(user.id.eq(content))
                    .and(lastLoginTimePredicate);
        }


        if (!contentNotNull) {
            // content is null, registerTime  is not null
            if (registerTimeNotNull) {
                // content is null, registerTime  is not null  ,last login Time not null
                if (lastLoginTimeNotNull) {
                    return repository.findAll(
                            contentNullButRegisterTimeAndLastLoginTimeNotNullPredicate, pageable);
                }
                // content is null, registerTime  is not null  ,last login Time is  null
                else {
                    return repository.findAll(registerTimePredicate, pageable);
                }
            }

            // content is null, registerTime  is null ,last login Time not null
            else {
                if (lastLoginTimeNotNull) {
                    return repository.findAll(lastLoginTimePredicate, pageable);
                }
                // content is null, registerTime  is null ,last login Time  null
                else {
                    return repository.findAll(pageable);
                }
            }
        }
        // if content !=null
        else {
            // content is not null, registerTime  is not null
            if (registerTimeNotNull) {

                // content is not null, registerTime  is not null  , last login not null
                if (lastLoginTimeNotNull) {
                    return repository.findAll(contentAndRegisterTimeAndLastLoginTimePredicate,
                            pageable);
                }
                // content is not null, registerTime  is not null  , last login is null
                else {
                    return repository.findAll(contentAndRegisterTimePredicate, pageable);
                }
            }
            // content is not null, registerTime  is  null
            else {
                // content is not null, registerTime  is null  , last login not null
                if (lastLoginTimeNotNull) {
                    return repository.findAll(contentAndLastLoginTimePredicate, pageable);
                }
                // content is not null, registerTime  is  null  , last login is null
                else {
                    return repository.findAll(contentPredicate, pageable);
                }
            }
        }
    }


}
