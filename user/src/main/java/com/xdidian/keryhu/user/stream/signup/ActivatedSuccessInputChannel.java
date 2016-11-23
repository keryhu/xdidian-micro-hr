package com.xdidian.keryhu.user.stream.signup;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;


/**
 * @Description : 
 * 
 * 这个是在  前台注册完email验证或者修改个人资料的时候，如果用户修完资料，且验证码通过了验证，由account－activated发送出来的消息。
 * user－account接受到消息，根据 ApplySituation 值是SIGNUP OR EDIT,来具体执行。
 * 如果是SIGNUP 那么就更新此email 的status为true
 * 如果是EDIT，那么更新此userId所在的account，和status为true
 * 
 *  
 * @date : 2016年6月18日 下午9:24:04
 * @author : keryHu keryhu@hotmail.com
 */
public interface ActivatedSuccessInputChannel {
  
  String NAME = "activatedSuccessInputChannel";

  @Input(NAME)
  SubscribableChannel input();

}
