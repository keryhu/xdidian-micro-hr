package com.xdidian.keryhu.account_activate.rest.recover;

import static com.xdidian.keryhu.account_activate.domain.Constants.UPDATE_SUCCESS;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.xdidian.keryhu.account_activate.domain.NewPasswordFormDto;
import com.xdidian.keryhu.account_activate.repository.CommonActivatedTokenRepository;
import com.xdidian.keryhu.account_activate.service.ConverterUtil;
import com.xdidian.keryhu.account_activate.service.RecoverService;
import com.xdidian.keryhu.account_activate.stream.RecoverPasswordSuccessProducer;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;

import lombok.RequiredArgsConstructor;

/**
 * 
 * NewPasswordRest
 * 在密码验证过程中，有一个比较特殊，就是密码找回，除了相同的不是，需要验证提交的token
 * 
 *               但是，此服务还多出了一步，前台在验证完token后，还需要提交新的 password，以便后台user service 更新新的密码。 )
 * @author keryhu keryhu@hotmail.com
 *  2016年9月10日 上午9:44:46
 */

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class NewPasswordRest {

  private final RecoverService recoverService;
  private final CommonActivatedTokenRepository repository;
  private final ConverterUtil converterUtil;
  private final RecoverPasswordSuccessProducer recoverPasswordSuccessProducer;


  /**
   * 
   *  submitNewPassword   1 account，验证码必需符合要求。 2
   *         验证码不能过期，如果已经过期，直接删除原来的数据，页面导航到login页面 3 新的密码，不能和原来的一致，必需是不同的密码。 如果一样，出现错误提示：必需密码不同 3
   *         保存新的密码，account，发出消息出去，让user账号得知。如果user账号得知，那么保存新的密码，如果account是
   *         email格式，且如果emailstatus如果为false的话，那么更新为true，还有删除本地的 account纪录 ) @param @param
   *         dto @param @return 设定文件 @return ResponseEntity<?> 返回类型 @throws
   */



  @PostMapping("/accountActivate/recover/newpassword")
  public ResponseEntity<?> submitNewPassword(
          @RequestBody final NewPasswordFormDto dto) {

    recoverService.validateNewPassword(dto);

    // 发送新密码的通知出去
    recoverPasswordSuccessProducer
        .send(converterUtil.newPasswordFormDtoToNewPasswordDto.apply(dto));
    // 删除此account的,recover本地数据库。
    repository.findByAccountAndApplySituation(dto.getAccount(), ApplySituation.RECOVER)
        .ifPresent(repository::delete);
    
    // 找出 recover 和signup 所有满足条件的删除
    repository.findByAccount(dto.getAccount()).stream()
        .filter(e -> e.getApplySituation() != ApplySituation.EDIT)
        .forEach(repository::delete);


    Map<String, String> map = new HashMap<String, String>();
    map.put("result", UPDATE_SUCCESS);
    return ResponseEntity.ok(map);

  }

}
