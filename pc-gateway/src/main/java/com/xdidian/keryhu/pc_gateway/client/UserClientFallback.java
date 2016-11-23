package com.xdidian.keryhu.pc_gateway.client;

import org.springframework.stereotype.Component;


/**
 * 
 * @Description : feign 连接失败的一个默认方法。
 * @date : 2016年6月18日 下午8:09:32
 * @author : keryHu keryhu@hotmail.com
 */
@Component
public class UserClientFallback implements UserClient {


  /**
   * 登录后，返回给前台当前用户的姓名
   */

  @Override
  public String findCurrentName() {
    return null;
  }
}
