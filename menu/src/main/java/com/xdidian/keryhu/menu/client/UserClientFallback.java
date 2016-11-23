package com.xdidian.keryhu.menu.client;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;


@Component
public class UserClientFallback implements UserClient{


    @Override
    public Boolean getIsInCompany(@RequestParam("id") String id) {
        return false;
    }
}
