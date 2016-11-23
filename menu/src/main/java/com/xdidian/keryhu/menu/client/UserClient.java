package com.xdidian.keryhu.menu.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "user", fallback = UserClientFallback.class)
public interface UserClient {

    // 这里获取 ，用户是否 已经注册公司，和用户的姓名，一起返回给前台。,id is userId

    @RequestMapping(value = "/users/getIsInCompany", method = RequestMethod.GET)
    Boolean getIsInCompany(@RequestParam("id") String id);

}
