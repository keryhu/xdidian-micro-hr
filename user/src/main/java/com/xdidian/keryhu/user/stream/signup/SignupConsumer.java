package com.xdidian.keryhu.user.stream.signup;

import com.xdidian.keryhu.domain.SignupDto;
import com.xdidian.keryhu.user.domain.User;
import com.xdidian.keryhu.user.repository.UserRepository;
import com.xdidian.keryhu.user.service.ConverterUtil;
import com.xdidian.keryhu.user.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * @Description : 物业公司用户注册后，接受具体注册信息的message的方法
 * @date : 2016年6月18日 下午9:25:40
 * @author : keryHu keryhu@hotmail.com
 */
@EnableBinding(SignupInputChannel.class)
@Slf4j
public class SignupConsumer {

  @Autowired
  private ConverterUtil converterUtil;

  @Autowired
  private UserRepository repository;

  @StreamListener(SignupInputChannel.NAME)
  public void saveProperty(SignupDto dto) {

    log.info("user-account 已经收到了用户注册信息，具体为 ： " + dto);

    User user = converterUtil.signupDtoToUser.apply(dto);
    // 保存数据库
    repository.save(user);
  }

}
