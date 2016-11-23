package com.xdidian.keryhu.company.client;

import com.xdidian.keryhu.company.domain.feign.EmailAndPhoneDto;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 
 * @Description : feign 连接失败的一个默认方法。
 * @date : 2016年6月18日 下午8:09:32
 * @author : keryHu keryhu@hotmail.com
 */
@Component
public class UserClientFallback implements UserClient {



  @Override
  public Boolean isIdExist(String id) {

    return false;
  }

    @Override
    public EmailAndPhoneDto getEmailAndPhoneById(@RequestParam("id") String id) {
        return new EmailAndPhoneDto();
    }



}
