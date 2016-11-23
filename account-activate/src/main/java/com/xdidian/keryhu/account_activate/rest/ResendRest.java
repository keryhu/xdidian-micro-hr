package com.xdidian.keryhu.account_activate.rest;

import static com.xdidian.keryhu.account_activate.domain.Constants.TOKEN_EXPIRED;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.xdidian.keryhu.account_activate.client.UserClient;
import com.xdidian.keryhu.account_activate.domain.CommonConfirmTokenDto;
import com.xdidian.keryhu.account_activate.domain.TokenType;
import com.xdidian.keryhu.account_activate.service.CommonTokenService;
import com.xdidian.keryhu.account_activate.service.ResendService;
import com.xdidian.keryhu.account_activate.service.TokenExpiredService;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class ResendRest {

    private final UserClient userClient;
    private final CommonTokenService commonTokenService;
    private final TokenExpiredService tokenExpiredService;
    private final ResendService resendService;

    @PostMapping("/accountActivate/resend")
    public ResponseEntity<?> resend(
            @RequestBody final CommonConfirmTokenDto dto) {
        Assert.isTrue(dto.getApplySituation() != null, "ApplySituation 不能为空");

        Map<String, Object> map = new HashMap<String, Object>();
        // 如果是注册，需要验证这个。email,必需没有激活，否则直接返回 email已经激活

        if (dto.getApplySituation().equals(ApplySituation.SIGNUP)) {
            Assert.isTrue(!userClient.emailStatus(dto.getAccount()), "email已经激活，请直接登录！");
        }

        // 验证token和account
        commonTokenService.validateCodeAndAccount(dto, TokenType.RESEND);
        // 如果激活过期

        if (commonTokenService.isCodeExired(dto.getAccount(), dto.getApplySituation())) {
            map.put("result", TOKEN_EXPIRED);
            tokenExpiredService.executeExpired(dto.getAccount(), dto.getApplySituation());
            return ResponseEntity.ok(map);
        }
        // 验证成功了
        else {
            resendService.clickResend(dto.getAccount(), dto.getApplySituation());
            // 需要重设的url 参数，并且返回到前台，让前台去更新相关数据。
            Map<String, String> r = resendService.resetUrlParams(dto.getAccount(), dto.getApplySituation());
            return ResponseEntity.ok(r);
        }
    }

}
