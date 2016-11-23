package com.xdidian.keryhu.auth_server.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface Utils {

  // 获取当前的ip
   String getIp(HttpServletRequest request);

  // 当email 未激活的时候，从account-activated 发送过来的，包含resendtoken的string，转为map
  Map<String,String> emailUnActivatedStringToMap(String value);

}
