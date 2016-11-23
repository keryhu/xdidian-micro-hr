package com.xdidian.keryhu.account_activate.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;



/**
 * @Description : 当前台用户填写正确的email验证码后，发送这个message通知，user-account接受到通知后
 *              更改email所在的user数据库的emailStatus为truer
 * @date : 2016年6月18日 下午9:01:22
 * @author : keryHu keryhu@hotmail.com
 */
@Component
@EnableBinding(ActivatedSuccessOutputChannel.class)
@Slf4j
public class ActivatedSuccessProducer {

  @Autowired
  private ActivatedSuccessOutputChannel channel;

  /**
   * 当前台用户正确输入email验证码或phone验证码，发送这个message通知，user-account接受到通知后 
   * 更改email或phone所在的user数据库的emailStatus或phoneStatus为true
   * 
   * 当前台用户，在更改个人资料，例如修改手机号或者邮箱的时候，验证码输入成功，或者 注册时候，验证邮箱验证码成功的时候，
   * 会发送此消息出去，通知user service，更新
   * 判断的依据是 CommonTokenDto 的ApplySituation 值。
   * 如果为SIGNUP，则更新此account所在的email status为true
   * 如果是 EDIT,则更新此userId对应下的，account，并且更新status
   * 
   */
  public void send(CommonTokenDto dto) {
    boolean result = channel.success().send(MessageBuilder.withPayload(dto).build());
    
    Assert.isTrue(result, "发送激活成功的id message出去失败！");
    log.info("此服务为： "+dto.getApplySituation()+" ，更新的账号为： "+dto.getAccount());

  }

}
