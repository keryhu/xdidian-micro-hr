package com.xdidian.keryhu.signup.service;


import com.xdidian.keryhu.domain.SignupDto;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;
import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;
import com.xdidian.keryhu.signup.domain.ActivatedProperties;
import com.xdidian.keryhu.signup.domain.SignupForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Function;

import static com.xdidian.keryhu.util.GeneratorRandomNum.get;
/**
 * @Description : 类型转换 不能使用 @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 * @date : 2016年6月18日 下午9:16:46
 * @author : keryHu keryhu@hotmail.com
 */
@Component
@EnableConfigurationProperties(ActivatedProperties.class)
public class ConverterUtil {

  @Autowired
  private ActivatedProperties activatedProperties;
  
  
  /**
   * 将web前端提交的用户注册数据，转换为 dto，因为需要远程http，所以在传输之前，就先加密密码。
   */
  public Function<SignupForm, SignupDto> propertyFormToSignupDto = x -> {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);

    //将email格式专为小写。
    return new SignupDto(x.getEmail().toLowerCase(), x.getPhone(),x.getName(),
        passwordEncoder.encode(x.getPassword()));
  };



  /**
   * 将注册完的物业公司对象PropertyForm，转为CommonTokenDto,为了email激活，这个单独是为了email的激活，
   * 手机的激活不是这个方法。
   */
  
  public Function<SignupForm, CommonTokenDto> signupFormToCommonTokenDto=x->{
    CommonTokenDto dto=new CommonTokenDto();
    if (activatedProperties != null && activatedProperties.getExpiredTime() > 0) {
      LocalDateTime expireDate =
          LocalDateTime.now().plusHours(activatedProperties.getExpiredTime());
      dto.setExpireDate(expireDate);

    }
    dto.setAccount(x.getEmail().toLowerCase());
    //只有需要手工输入的验证码，使用自建的6位随即数字组合。
    dto.setToken(get(6));
    dto.setResignupToken(UUID.randomUUID().toString());
    dto.setResendToken(UUID.randomUUID().toString());
    dto.setApplySituation(ApplySituation.SIGNUP);
    return dto;
  };
  
  
  
}
