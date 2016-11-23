/**
 * @Title: ConverterUtil.java
 * @Package com.xdidian.keryhu.emailActivate.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author keryhu keryhu@hotmail.com
 * @date 2016年5月11日 下午9:16:46
 * @version V1.0
 */
package com.xdidian.keryhu.account_activate.service;

import com.xdidian.keryhu.account_activate.domain.AccountMethodDto;
import com.xdidian.keryhu.account_activate.domain.ActivatedProperties;
import com.xdidian.keryhu.account_activate.domain.CommonActivatedLocalToken;
import com.xdidian.keryhu.account_activate.domain.FormAccountEditDto;
import com.xdidian.keryhu.account_activate.domain.NewPasswordFormDto;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;
import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;
import com.xdidian.keryhu.domain.tokenConfirm.NewPasswordDto;
import com.xdidian.keryhu.domain.tokenConfirm.RecoverMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.xdidian.keryhu.util.GeneratorRandomNum.get;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Function;


/**
 * 
 * @Description : 用于email激活的时候，类型的转换
 * @date : 2016年6月18日 下午8:55:13
 * @author : keryHu keryhu@hotmail.com
 */
@Component
@EnableConfigurationProperties(ActivatedProperties.class)
public class ConverterUtil {

  @Autowired
  private ActivatedProperties activatedProperties;
  
  @Autowired
  private  PasswordEncoder encoder;


  /**
   * 将 message直接流通的 CommonTokenDto 转为本地存储的 mongo对象 CommonActivatedToken
   */

  public Function<CommonTokenDto, CommonActivatedLocalToken> commonTokenDtoToCommonActivatedLocalToken =
      x -> {
        CommonActivatedLocalToken token = new CommonActivatedLocalToken();
        token.setAccount(x.getAccount());
        token.setToken(x.getToken());
        token.setExpiryDate(x.getExpireDate());
        token.setSentTimes(0);
        token.setResendToken(x.getResendToken());
        if (x.getResignupToken() != null) {
          token.setResignupToken(x.getResignupToken());
        }
        token.setApplySituation(x.getApplySituation());
        // 设置下次 点击 “重新发送激活邮件”的冷却时间。
        token.setSendExpiryDate(
            LocalDateTime.now().plusMinutes(activatedProperties.getMinutesOfSendCycle()));
        if (x.getUserId() != null) {
          token.setUserId(x.getUserId());
        }
        
        if(x.getRecoverMethod()!=null&&(x.getRecoverMethod().equals(RecoverMethod.EMAIL)
            || x.getRecoverMethod().equals(RecoverMethod.PHONE))){
          token.setRecoverMethod(x.getRecoverMethod());
        }
       
        return token;
      };

  /**
   * 将本地存储的 CommonActivatedToken，转为 CommonTokenDto，目的是为了 resend
   * 
   */


  public Function<CommonActivatedLocalToken, CommonTokenDto> commonActivatedLocalTokenToCommonTokenDto =
      x -> {
        CommonTokenDto dto = new CommonTokenDto();
        dto.setAccount(x.getAccount());
        dto.setApplySituation(x.getApplySituation());
        dto.setExpireDate(x.getExpiryDate());
        dto.setToken(x.getToken());
        dto.setResendToken(x.getResendToken());
        if (x.getApplySituation().equals(ApplySituation.SIGNUP)) {
          dto.setResignupToken(x.getResignupToken());
        }
        if (x.getApplySituation().equals(ApplySituation.EDIT)) {
          dto.setUserId(x.getUserId());
        }
        if (x.getApplySituation().equals(ApplySituation.RECOVER)) {
          dto.setRecoverMethod(x.getRecoverMethod());
        }

        return dto;
      };


  
  /**
   * 将前台提交的对象 NewPasswordFormDto 转为发送给userAccount，更新密码的 message 类型的 NewPasswordDto
   * 
   */
  
  public Function<NewPasswordFormDto,NewPasswordDto> newPasswordFormDtoToNewPasswordDto=
      x->{
        NewPasswordDto dto=new NewPasswordDto();
        dto.setAccount(x.getAccount());
        dto.setHashPassword(encoder.encode(x.getNewPassword()));
        return dto;
      };
      
      
      /**
       *  将前台提交的对象AccountMethodDto 转为本地存储数据库CommonActivatedLocalToken对象，应用在 checkMethod rest上面
       * 
       */
      
  public Function<AccountMethodDto,CommonActivatedLocalToken> accountMethodDtoToCommonActivatedLocalToken=
      x->{
        CommonActivatedLocalToken token=new CommonActivatedLocalToken();
        token.setAccount(x.getAccount());
        token.setToken(get(6));
        // 设置验证码过期时间
        if (activatedProperties != null && activatedProperties.getExpiredTime() > 0) {
          LocalDateTime expireDate =
              LocalDateTime.now().plusHours(activatedProperties.getExpiredTime());
          token.setExpiryDate(expireDate);
        }
        if(x.getMethod().equals("EMAIL")){
          token.setRecoverMethod(RecoverMethod.EMAIL);
        }
        else if(x.getMethod().equals("PHONE")){
          token.setRecoverMethod(RecoverMethod.PHONE);
        }
        // 设置重复发送的次数为0
        token.setSentTimes(0);
        // 设置6位随机码
        token.setApplySituation(ApplySituation.RECOVER);
        token.setResendToken(UUID.randomUUID().toString());
        token.setSendExpiryDate(
            LocalDateTime.now().plusMinutes(activatedProperties.getMinutesOfSendCycle()));
        return token;
      };

      /**
       * 将前台编辑的 form dto对象转为本地数据库存储对象，并设定随机码，resendtoken等数据。
       * 应用在，前台修改个人资料，邮件或者手机号的时候，验证完登录密码，提交新的account，要求验证此account的时候。
       * 
       */
      
  public Function<FormAccountEditDto,CommonActivatedLocalToken> formAccountEditDtoToCommonActivatedLocalToken=
          x->{
            CommonActivatedLocalToken t=new CommonActivatedLocalToken();
            t.setUserId(x.getUserId());
            t.setAccount(x.getAccount());
            t.setToken(get(6));
       
            // 设置验证码过期时间
            if (activatedProperties != null && activatedProperties.getExpiredTime() > 0) {
              LocalDateTime expireDate =
                  LocalDateTime.now().plusHours(activatedProperties.getExpiredTime());
              t.setExpiryDate(expireDate);
            }
            // 设置重复发送的次数为0
            t.setSentTimes(0);
            // 设置6位随机码
            t.setApplySituation(ApplySituation.EDIT);
            t.setResendToken(UUID.randomUUID().toString());
            t.setSendExpiryDate(
                LocalDateTime.now().plusMinutes(activatedProperties.getMinutesOfSendCycle()));
            return t;
          };
          
          
}
