package com.xdidian.keryhu.company.client;

import com.xdidian.keryhu.company.domain.feign.EmailAndPhoneDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * @author : keryHu keryhu@hotmail.com
 * @Description : 针对于user-account service remote rest 服务
 * @date : 2016年6月18日 下午8:09:14
 */
@FeignClient(name = "user", fallback = UserClientFallback.class)
public interface UserClient {


    /**
     * 根据唯一标志，email、phone，或user中的id，3个里任何一种，来查看数据库的user
     *
     * @param id
     * @return
     */

    @RequestMapping(value = "/users/query/findById", method = RequestMethod.GET)
    public Boolean isIdExist(@RequestParam("id") String id);

    /**
     * 根据userId。来获取该用户的email和phone对象。
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "users/emailAndPhone", method = RequestMethod.GET)
    public EmailAndPhoneDto getEmailAndPhoneById(@RequestParam("id") String id);


}


