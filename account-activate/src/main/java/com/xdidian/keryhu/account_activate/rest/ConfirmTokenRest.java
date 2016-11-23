package com.xdidian.keryhu.account_activate.rest;

import static com.xdidian.keryhu.account_activate.domain.Constants.ACTIVATE_SUCCESS;
import static com.xdidian.keryhu.account_activate.domain.Constants.TOKEN_EXPIRED;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
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
import com.xdidian.keryhu.account_activate.service.TokenConfirmSuccessService;
import com.xdidian.keryhu.account_activate.service.TokenExpiredService;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;

import lombok.RequiredArgsConstructor;

/**
 * @author keryhu keryhu@hotmail.com
 * @ClassName: ConfirmTokenRest
 * 这个是用户注册时候，验证码email激活， 前台输入token验证码，验证token的 具体的rest)
 * @date 2016年9月9日 下午3:11:55
 */


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j

public class ConfirmTokenRest {

    private final UserClient userClient;
    private final CommonTokenService commonTokenService;
    private final TokenExpiredService tokenExpiredService;
    private final TokenConfirmSuccessService tokenConfirmSuccessService;


    /**
     * 这是一个通用的 验证码提交，验证 验证码token的 post 方法，3合一
     * 适用于  密码找回，注册完的email验证，个人资料修改。只需要按照 参数提交即可。)
     */

    @PostMapping("/accountActivate/confirmToken")
    public ResponseEntity<?> tokenConfirm(@RequestBody final CommonConfirmTokenDto dto) {
        Assert.notNull(dto.getApplySituation(),"ApplySituation 不能为空");

        Map<String, Object> map = new HashMap<String, Object>();

        // 如果是注册，需要验证这个。email,必需没有激活，否则直接返回 email已经激活

        if (dto.getApplySituation().equals(ApplySituation.SIGNUP)) {
            Assert.isTrue(!userClient.emailStatus(dto.getAccount()), "email已经激活，请直接登录！");
        }

        // 验证token和account

        commonTokenService.validateCodeAndAccount(dto, TokenType.CONFIRM);

        // 如果激活过期

        if (commonTokenService.isCodeExired(dto.getAccount(), dto.getApplySituation())) {
            map.put("result", TOKEN_EXPIRED);
            tokenExpiredService.executeExpired(dto.getAccount(), dto.getApplySituation());
            return ResponseEntity.ok(map);
        }
        // 验证成功了
        else {
            // 如果是密码恢复，也只需要返回前台成功即可，因为前台有保留的accuont 和 token，只需要在下一次提交再加上new password 即可
            tokenConfirmSuccessService.exec(dto);
            map.put("result", ACTIVATE_SUCCESS);

            return ResponseEntity.ok(map);
        }
    }
}
