package com.xdidian.keryhu.account_activate.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;


/**
 * @Description : 删除user数据的channel，传递的是email，userAccount会根据类型判断是email，phone，uuid，哪种形式
 * @date : 2016年6月18日 下午9:02:15
 * @author : keryHu keryhu@hotmail.com
 */
public interface RemoveUserOutputChannel {

  // 此channel的值和 application bindings下面的值一致
  String NAME = "removeUserOutputChannel";

  @Output(NAME)
  MessageChannel removeUser();

}
