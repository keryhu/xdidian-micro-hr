package com.xdidian.keryhu.user.rest.common;

import com.xdidian.keryhu.user.domain.User;
import com.xdidian.keryhu.user.domain.feign.AuthUserDto;
import com.xdidian.keryhu.user.domain.feign.EmailAndPhoneDto;
import com.xdidian.keryhu.user.domain.feign.LoggedWithMenuDto;
import com.xdidian.keryhu.user.exception.UserNotFoundException;
import com.xdidian.keryhu.user.repository.UserRepository;
import com.xdidian.keryhu.user.service.ConverterUtil;
import com.xdidian.keryhu.user.service.UserService;
import com.xdidian.keryhu.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by hushuming on 2016/10/30.
 * <p>
 * 为了其他的service 组件，feign 调用user 服务器 ，创建的rest
 */

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class FeignRest {

    private final UserRepository repository;
    private final UserService userService;
    private final ConverterUtil converterUtil;
    private final MessageSource messageSource;

    private String err=null;

    /**
     * 根据唯一标志，email、phone，或user中的id，3个里任何一种，来查看数据库的user
     * 用在 auth-server登录的时候 feign
     */

    @GetMapping("/users/query/findByIdentity")
    public ResponseEntity<?> findByIdentity(
            @RequestParam("identity") String identity) {

        Object[] args = {identity};
        err=messageSource.getMessage("message.user.idNotFound",args,
                LocaleContextHolder.getLocale());

        // 如果用户不存在，则抛出错误,返回json {"code":404,"message":"您查询的用户不存在！！"}
        User user = repository
                .findByEmailOrPhoneOrId(identity, identity, identity)
                .orElseThrow(() -> new UserNotFoundException(err));
        // 将User 转为 AuthUser对象
        AuthUserDto au = converterUtil.userToAuthUser.apply(user);
        return ResponseEntity.ok(au);
    }

    /**
     * findById 用在account－activated 激活的时候，feign 查看Userid是否存在)
     */

    @GetMapping("/users/query/findById")
    public Boolean isIdExist(@RequestParam("id") String id) {
        return repository.findByEmailOrPhoneOrId(id, id, id).isPresent();
    }


    /**
     * isInComopany
     * 查询当前用户是否有公司组织，还是单身一人，根据这个结果，前台判断在用户登录后，
     * 显示什么样子的菜单。
     * 目前调用这个接口的feign是 personal－core)
     * <p>
     * //使用在  personal——core 服务器里面，用户获取自身 菜单的时候，
     * 同时获取，用户的姓名，用户的companyId。
     *
     * @param id 特指 userId，不是email，也不会是phone，
     *           因为用户获取菜单的时候，直接上传的userId。
     *           <p>
     *           为什么这需要从前台传递userId，而不是自己获取，
     *           因为menu服务器，如果通过 @AuthenticationPrincipal ，
     *           那么就必须要传递 user，而这个目前没有。
     */
    @GetMapping("/users/getIsInCompanyAndName")

    public LoggedWithMenuDto getIsInCompanyAndName(
            @RequestParam("id") String id) {

        return userService.getIsInCompanyAndName(id);
    }


    /**
     * 用于 个人资料修改的时候，需要用户输入密码，
     * account-activate service spring feign 此接口。
     * 以确保是当前用户，验证成功了才能修改个人资料
     * 所以这个是其他服务器 验证码密码的一个类。 注意这个密码是加密过的
     * 提供的参数，是 email或phone
     * 这个方法必需要加密才能获取。
     */

    @GetMapping("/users/getHashPassword")
    public String getHashPassword(@RequestParam("id") String id) {

        return repository.findById(id)
                .filter(User::isEmailStatus)
                .map(User::getPassword)
                .orElse("");
    }

    /**
     *
     * 用在company 服务 组件，用来传递spring feign。对方使用userId，
     * 来同时获取email和phone
     * @param id  特指userId
     */

    @GetMapping("users/emailAndPhone")
    public EmailAndPhoneDto getEmailAndPhoneById(
            @RequestParam("id") String id) {
        Object[] args = {id};
        err=messageSource.getMessage("message.user.idNotFound",args,
                LocaleContextHolder.getLocale());
        EmailAndPhoneDto ep = new EmailAndPhoneDto();
        User u = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(err));
        ep.setEmail(u.getEmail());
        ep.setPhone(u.getPhone());
        return ep;
    }


    /**
     * pc gateway 使用，登录后，返回给前台当前用户的姓名
     * @return
     */
    @GetMapping("users/findCurrentName")
    public String findCurrentName(){

        String id= SecurityUtils.getCurrentLogin();
        return repository.findById(id)
                .map(User::getName)
                .orElse("");
    }


}
