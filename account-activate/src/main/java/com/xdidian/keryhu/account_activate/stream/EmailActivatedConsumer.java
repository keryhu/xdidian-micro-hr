package com.xdidian.keryhu.account_activate.stream;


import com.xdidian.keryhu.account_activate.repository.CommonActivatedTokenRepository;
import com.xdidian.keryhu.account_activate.service.ConverterUtil;
import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;



/**
 * @Description : email激活service 接收到传递过来的，需要被激活的数据后的处理办法， 先进行类型转换，
 * 此消息，有signup服务器发送出来，接受方，有2个，一个是此服务器，负责存储 相应的token，另外一个就是mail 服务器，
 * 他接受到后，就直接发送邮件出去了
 * 然后设置默认的发送次数为0，设置resend的冷却时间，再保存数据库
 * @date : 2016年6月18日 下午9:01:42
 * @author : keryHu keryhu@hotmail.com
 */
@EnableBinding(EmailActivatedInputChannel.class)
@Slf4j
public class EmailActivatedConsumer {

  @Autowired
  private ConverterUtil converterUtil;
  
  @Autowired
  private CommonActivatedTokenRepository repository;

 
  @StreamListener(EmailActivatedInputChannel.NAME)
  public void receive(CommonTokenDto dto) {
    
    repository.save(converterUtil.commonTokenDtoToCommonActivatedLocalToken.apply(dto));
    log.info("email激活服务器，接受到刚刚注册的 dto is ： {}", dto);

  }
}
