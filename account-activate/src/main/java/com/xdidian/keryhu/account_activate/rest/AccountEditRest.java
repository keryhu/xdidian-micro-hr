package com.xdidian.keryhu.account_activate.rest;

import static com.xdidian.keryhu.account_activate.domain.Constants.TOKEN_EXPIRED;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.xdidian.keryhu.account_activate.client.UserClient;
import com.xdidian.keryhu.account_activate.domain.CommonActivatedLocalToken;
import com.xdidian.keryhu.account_activate.domain.FormAccountEditDto;
import com.xdidian.keryhu.account_activate.repository.CommonActivatedTokenRepository;
import com.xdidian.keryhu.account_activate.service.AccountEditService;
import com.xdidian.keryhu.account_activate.service.CommonTokenService;
import com.xdidian.keryhu.account_activate.service.ConverterUtil;
import com.xdidian.keryhu.account_activate.service.ResendService;
import com.xdidian.keryhu.account_activate.service.TokenExpiredService;
import com.xdidian.keryhu.account_activate.stream.ResendActivateProducer;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;
import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author keryhu keryhu@hotmail.com
 * @ClassName: EmailEditRest
 * 当前台要求，更改个人手机号的时候，首先调用此接口， 此rest 有2个post 方法 1 验证提交的
 * 用户登录密码，userId，已经原有的email是否符合要求，是否存在于数据库 2 当验证完上面的登录密码后，后台保存 此 email
 * 到本服务器的数据库，并且生成随机码，前台再次要求用户输入验证码，只有验证符合要求后 才发送新的密码，userId到user service
 * 服务器，要求此服务器更新此账号。)
 * @date 2016年9月10日 下午5:17:50
 */

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class AccountEditRest {

    private final PasswordEncoder encoder;
    private final UserClient userClient;
    private final CommonActivatedTokenRepository repository;
    private final AccountEditService accountEditService;
    private final CommonTokenService commonTokenService;
    private final TokenExpiredService tokenExpiredService;
    private final ResendService resendService;
    private final ConverterUtil converterUtil;
    private final ResendActivateProducer resendActivateProducer;


    // 修改email 或phone验证完可以修改email的后，在发送message出去之前，先要判断下，account activated 数据库是否已经存在了该account
    // ,且该token没有过期，如果存在此情况，那么直接返回前台，想要的resendtoken，让前台转动输入token的页面

    @PostMapping("/accountActivate/edit")
    public ResponseEntity<?> validateEmailCanModifyOrSave(
            @RequestBody final FormAccountEditDto dto) {
        // 验证传递来的id，是否存在，email 是否符合规范，email是否已经存在于数据库，如果存在报错。，否则返回成功，可以修改。
        accountEditService.validateAccountEdit(dto);
        Map<String, Object> map = new HashMap<String, Object>();

        if (dto.getPassword() == null || dto.getPassword().length() == 0) {
            map.put("result", true);
            return ResponseEntity.ok(map);
        } else {
            String hashPassword = userClient.getHashPassword(dto.getUserId());
            boolean match = encoder.matches(dto.getPassword(), hashPassword);
            log.info("userId is : " + dto.getUserId() + " , hashPassword is : " + hashPassword);
            if (!match) {
                map.put("result", "密码错误！");
                return ResponseEntity.ok(map);
            } else {
                map.put("result", true);
                // 发送消息出去，生成随机码，让前台输入验证码

                boolean accountAndApplySituationExist = repository
                        .findByAccountAndApplySituation(dto.getAccount(), ApplySituation.EDIT).isPresent();

                // 如果account存在于本地数据库,且是 修改个人资料模式

                if (accountAndApplySituationExist) {
                    // 如果token没有过期，那么执行类似 resend click的操作
                    if (!commonTokenService.isCodeExired(dto.getAccount(), ApplySituation.EDIT)) {
                        resendService.clickResend(dto.getAccount(), ApplySituation.EDIT);

                        // 需要重设的url 参数，并且返回到前台，让前台去更新相关数据。
                        Map<String, String> r =
                                resendService.resetUrlParams(dto.getAccount(), ApplySituation.EDIT);
                        r.put("account", dto.getAccount());
                        r.put("userId", dto.getUserId());
                        log.info("用户想要修改个人资料手机／邮箱的时候，发现此前已经提交过了，且验证码还未过期 ");
                        return ResponseEntity.ok(r);
                    } else {
                        //如果验证码已经过期，那么首先先删除之前的验证码纪录
                        tokenExpiredService.executeExpired(dto.getAccount(), ApplySituation.EDIT);
                        map.put("result", TOKEN_EXPIRED);
                        return ResponseEntity.ok(map);
                    }
                }

                // 如果account 不存在数据库

                //将前台的提交的数据，转为 数据库保存的类型，和发送消息 出去的类型
                CommonActivatedLocalToken token = converterUtil.formAccountEditDtoToCommonActivatedLocalToken.apply(dto);
                //再次转为  发送消息的 message 流转的message 对象
                CommonTokenDto commonTokenDto = converterUtil.commonActivatedLocalTokenToCommonTokenDto.apply(token);

                //发送message 消息出去，接受者为两个，mail 服务器和 手机发送平台，他们自己根据逻辑判断是否需要处理
                //使用resend 消息channel发送，效果一样，节省重新新建 channel的资源
                resendActivateProducer.send(commonTokenDto);

                repository.save(token);
                Map<String, String> response = new HashMap<String, String>();

                response.put("account", dto.getAccount());
                response.put("userId", dto.getUserId());
                response.put("resendToken", token.getResendToken());

                return ResponseEntity.ok(response);
            }
        }

    }


}
