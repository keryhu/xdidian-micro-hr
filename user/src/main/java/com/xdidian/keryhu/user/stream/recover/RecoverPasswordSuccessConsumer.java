package com.xdidian.keryhu.user.stream.recover;

import com.xdidian.keryhu.domain.tokenConfirm.NewPasswordDto;
import com.xdidian.keryhu.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;


import lombok.extern.slf4j.Slf4j;
import static com.xdidian.keryhu.util.StringValidate.isEmail;

/**
 * 
*  RecoverPasswordSuccessConsumer
* 当用户更新密码成功后，useraccount接受来自account-activate发送的消息
* 如果account类型为email，且emailStatur为false，那么设置为true)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月26日 下午6:43:26
 */


@EnableBinding(RecoverPasswordSuccessInputChannel.class)
@Slf4j
public class RecoverPasswordSuccessConsumer {
  
  @Autowired
  private UserRepository repository;
  
  @StreamListener(RecoverPasswordSuccessInputChannel.NAME)
  public void ReceiveRecoverPassword(NewPasswordDto dto){
    repository.findByEmailOrPhone(dto.getAccount(),dto.getAccount())
    .ifPresent(e->{
      e.setPassword(dto.getHashPassword());
      if(isEmail(dto.getAccount())&&!e.isEmailStatus()){
        e.setEmailStatus(true);
      }
      repository.save(e);
      log.info("userAccount,密码重设 {}  成功 ！ ",dto.getAccount());
    });
  }

}
