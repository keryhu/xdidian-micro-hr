package com.xdidian.keryhu.account_activate.stream;

import com.xdidian.keryhu.domain.tokenConfirm.NewPasswordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: UpdateSuccessProducer
* @Description: 当新密码 验证成功后，后台会发出message出去，通知userAccount更新密码。)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月26日 下午5:10:42
 */



@Component
@EnableBinding(RecoverPasswordSuccessOutChannel.class)
@Slf4j
public class RecoverPasswordSuccessProducer {
  
  @Autowired
  RecoverPasswordSuccessOutChannel channel;
  
  public void send(NewPasswordDto dto){
    channel.send().send(MessageBuilder.withPayload(dto).build());
    log.info("更新密码成功，现在有password－reset服务器发送message出去。。" );
  }

}
