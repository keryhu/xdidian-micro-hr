package com.xdidian.keryhu.user.rest.service;

import com.xdidian.keryhu.user.domain.User;
import com.xdidian.keryhu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hushuming on 2016/10/12.
 */

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ServiceRest {

    private final UserRepository repository;

    // 客人人员，前台搜索  会员的 工具， 另外一个类似的是
    // 新地点管理人员的搜索工具，不过两个路由地址不一样，搜索条件不一样
    //  搜索条件是，用户提交的 关键字,conteng,参数就代表 这个。（包含了用户提交的姓名，手机号，邮箱）
    @GetMapping("/service/queryWithPage")

    public Page<User> get(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam("content") String content) {


        return repository.findByEmailOrPhoneOrNameOrIdAllIgnoreCase(
                content, content, content,content, pageable);
    }
}
