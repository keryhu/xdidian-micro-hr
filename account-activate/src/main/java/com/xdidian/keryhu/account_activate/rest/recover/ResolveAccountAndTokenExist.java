package com.xdidian.keryhu.account_activate.rest.recover;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xdidian.keryhu.account_activate.client.UserClient;
import com.xdidian.keryhu.account_activate.repository.CommonActivatedTokenRepository;
import com.xdidian.keryhu.account_activate.service.CommonTokenService;
import com.xdidian.keryhu.account_activate.service.TokenExpiredService;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;

import lombok.RequiredArgsConstructor;

/**
 * @author keryhu  keryhu@hotmail.com
 * CheckAccountAndTokenForRecover
 * 此服务用在，显示密码找回，需要输入新密码的页面。
 * 前台在 显示路由该页面之前，需要先查看下，该account和token是否存在，是否还在有效期，如果都在，就停留在该页面，
 * 如果已经失效，或者account不存在，那么就返回账号失效，给前台。并且删除本地的账号。)
 * @date 2016年9月10日 下午3:02:17
 */


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class ResolveAccountAndTokenExist {

    private final UserClient userClient;
    private final CommonActivatedTokenRepository repository;
    private final CommonTokenService commonTokenService;
    private final TokenExpiredService tokenExpiredService;

    @GetMapping("/accountActivate/recover/isAccountAndTokenExist")
    public ResponseEntity<?> isAccountAndTokenExist(
            @RequestParam("account") String account,
            @RequestParam("token") String token) {


        boolean eu = userClient.isLoginNameExist(account);
        boolean el = repository.findByAccountAndApplySituation(account, ApplySituation.RECOVER)
                .map(e -> e.getToken().equals(token)).orElse(false);
        if (eu && el) {
            if (commonTokenService.isCodeExired(account, ApplySituation.RECOVER)) {
                tokenExpiredService.executeExpired(account, ApplySituation.RECOVER);
            }
        }

        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("result", eu && el);
        return ResponseEntity.ok(map);
    }

}
